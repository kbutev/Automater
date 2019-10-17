/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.recorder.model.RecorderResult;
import automater.recorder.parser.RecorderJHookListenerDelegate;
import automater.recorder.parser.RecorderJHookListener;
import automater.recorder.model.RecorderUserInput;
import automater.input.InputKey;
import automater.recorder.parser.BaseRecorderNativeParser;
import automater.recorder.parser.RecorderParserFlag;
import automater.recorder.parser.RecorderSystemKeyboardTranslator;
import automater.settings.Hotkey;
import automater.utilities.CollectionUtilities;
import automater.utilities.DeviceScreen;
import automater.utilities.Errors;
import automater.utilities.Logger;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelEvent;

/**
 * A service that listens to system user input and records it as RecorderResult.
 * 
 * @author Bytevi
 */
public class Recorder implements RecorderJHookListenerDelegate {
    private static Recorder singleton;
    @NotNull private final Object _lock = new Object();
    
    @NotNull public final Defaults defaults = new Defaults();
    
    @NotNull private final RecorderMasterNativeParser _masterParser;
    @Nullable private RecorderJHookListener _nativeListener;
    
    private boolean _recording = false;
    @Nullable private BaseRecorderListener _listener;
    @Nullable private BaseRecorderNativeParser _inputParser;
    @Nullable private BaseRecorderModel _recorderModel;
    
    private Recorder()
    {
        _masterParser = new RecorderMasterNativeParser();
    }
    
    // # Public
    
    synchronized public static @NotNull Recorder getDefault()
    {
        if (singleton == null)
        {
            singleton = new Recorder();
        }
        
        return singleton;
    }
    
    public void preload()
    {
        // Setup the Recorder, to make sure its up and running so when starting
        // the recording, no delay will be experienced
        startIfNotStarted();
    }
    
    public void start(@NotNull BaseRecorderNativeParser parser, @NotNull BaseRecorderModel model, @NotNull BaseRecorderListener listener) throws Exception
    {
        start(parser, model, listener, null);
    }
    
    public void start(@NotNull BaseRecorderNativeParser parser, @NotNull BaseRecorderModel model, @NotNull BaseRecorderListener listener, @Nullable Hotkey stopHotkey) throws Exception
    {
        startIfNotStarted();
        
        Dimension size = DeviceScreen.getPrimaryScreenSize();
        
        Logger.messageEvent(this, "Start recording on screen size " + size.width + "x" + size.height + "...");
        
        synchronized (_lock)
        {
            if (_recording)
            {
                Errors.throwIllegalStateError("Cannot start Recorder, already started");
            }
        }
        
        synchronized (_lock)
        {
            _recording = true;
            _inputParser = parser;
            _recorderModel = model;
            _listener = listener;
            
            _masterParser.setSubparser(_inputParser);
        }
    }
    
    public void stop() throws Exception
    {
        Logger.messageEvent(this, "Stop!");
        
        boolean isRecording;
        
        synchronized (_lock)
        {
            if (!_recording)
            {
                Errors.throwIllegalStateError("Cannot stop Recorder, must be recording first");
            }
            
            isRecording = _recording;
        }
        
        if (isRecording)
        {
            cancel(false, null);
        }
    }
    
    public void registerHotkeyListener(@NotNull RecorderHotkeyListener listener)
    {
        startIfNotStarted();
        
        _masterParser.registerHotkeyListener(listener);
    }
    
    public void unregisterHotkeyListener(@NotNull RecorderHotkeyListener listener)
    {
        _masterParser.unregisterHotkeyListener(listener);
    }
    
    public void registerPlayStopHotkeyListener(@NotNull RecorderHotkeyListener listener)
    {
        startIfNotStarted();
        
        Logger.messageEvent(this, "Registering a play/stop hotkey listener for client " + listener.toString());
        _masterParser.setPlayStopHotkeyListener(listener);
    }
    
    public void unregisterPlayStopHotkeyListener()
    {
        RecorderHotkeyListener l = _masterParser.getPlayStopHotkeyListener();
        
        if (l != null)
        {
            Logger.messageEvent(this, "Unregistering a play/stop hotkey listener for client " + l.toString());
        }
        
        _masterParser.setPlayStopHotkeyListener(null);
    }
    
    // # RecorderJHookListenerDelegate
    
    @Override
    public void onParseInput(@NotNull RecorderUserInput input)
    {
        // If not recording, do nothing
        if (!_recording)
        {
            return;
        }
        
        // Add the parsed input to the model
        try {
            _recorderModel.addInput(input);
        } catch (Exception e) {
            try {
                Logger.messageEvent(this, "Cancel! Exception encountered: " + e.toString());
                e.printStackTrace(System.out);
                _listener.onFailedRecordedUserInput(input);
                cancel(false, e);
            } catch (Exception e2) {
                
            }
            
            return;
        }
        
        // Alert listener
        if (_listener != null)
        {
            _listener.onRecordedUserInput(input);
        }
    }
    
    @Override
    public void onInputDataChange()
    {
        // If not recording, do nothing
        if (!_recording)
        {
            return;
        }
        
        // Alert listener
        if (_listener != null)
        {
            _listener.onRecordedUserInputChanged();
        }
    }
    
    // # Private
    
    private void startIfNotStarted()
    {
        if (_nativeListener != null)
        {
            return;
        }
        
        _nativeListener = new RecorderJHookListener(_masterParser, this);
        
        try {
            _nativeListener.start();
        } catch (Exception e) {
            
        }
    }
    
    private void cancel(boolean success, @NotNull Exception exception) throws Exception
    {
        synchronized (_lock)
        {
            if (!_recording)
            {
                return;
            }
            
            _recording = false;
            
            _masterParser.setSubparser(null);
            
            BaseRecorderListener listener = _listener;
            
            _inputParser = null;
            _listener = null;
            
            RecorderResult result = _recorderModel.retrieveRecordedData();
            
            listener.onFinishedRecording(result, success, exception);
        }
    }
    
    // Default values
    public class Defaults
    {
        public @NotNull ArrayList<RecorderParserFlag> getDefaultRecordFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_MOTION);
            settings.add(RecorderParserFlag.RECORD_MOUSE_WHEEL);
            settings.add(RecorderParserFlag.RECORD_WINDOW_EVENTS);
            settings.add(RecorderParserFlag.LOG_EVENTS);
            return settings;
        }
        
        public @NotNull ArrayList<RecorderParserFlag> getRecordOnlyKeyClicksAndMouseMotionFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_MOTION);
            settings.add(RecorderParserFlag.LOG_EVENTS);
            return settings;
        }
        
        public @NotNull ArrayList<RecorderParserFlag> getRecordOnlyKeyClicksFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            settings.add(RecorderParserFlag.LOG_EVENTS);
            return settings;
        }
        
        public @NotNull ArrayList<RecorderParserFlag> getRecordOnlyKeyClicksAndMouseMotionSilentlyFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_MOTION);
            return settings;
        }
        
        public @NotNull ArrayList<RecorderParserFlag> getRecordOnlyKeyClicksSilentlyFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            return settings;
        }
    }
}

class RecorderMasterNativeParser implements BaseRecorderNativeParser 
{
    @NotNull private final Object _lock = new Object();
    
    @Nullable private BaseRecorderNativeParser _subParser;
    
    @NotNull private final RecorderSystemKeyboardTranslator _keyboardTranslator = new RecorderSystemKeyboardTranslator();
    @NotNull private final HashSet<RecorderHotkeyListener> _hotkeyListeners = new HashSet<>();
    @Nullable private RecorderHotkeyListener _playStopHotkeyListener;
    
    public RecorderMasterNativeParser()
    {
        
    }
    
    public BaseRecorderNativeParser getSubparser()
    {
        synchronized (_lock)
        {
            return _subParser;
        }
    }
    
    public void setSubparser(@Nullable BaseRecorderNativeParser parser)
    {
        synchronized (_lock)
        {
            _subParser = parser;
        }
    }
    
    public void registerHotkeyListener(@NotNull RecorderHotkeyListener listener)
    {
        synchronized (_lock)
        {
            _hotkeyListeners.add(listener);
        }
    }
    
    public void unregisterHotkeyListener(@NotNull RecorderHotkeyListener listener)
    {
        synchronized (_lock)
        {
            _hotkeyListeners.remove(listener);
        }
    }
    
    public @Nullable RecorderHotkeyListener getPlayStopHotkeyListener()
    {
        synchronized (_lock)
        {
            return _playStopHotkeyListener;
        }
    }
    
    public void setPlayStopHotkeyListener(@Nullable RecorderHotkeyListener listener)
    {
        synchronized (_lock)
        {
            if (listener != null)
            {
                _playStopHotkeyListener = listener;
                _hotkeyListeners.add(listener);
            }
            else if (_playStopHotkeyListener != null)
            {
                _hotkeyListeners.remove(_playStopHotkeyListener);
                _playStopHotkeyListener = null;
            }
        }
    }
    
    @Override
    public @Nullable RecorderUserInput evaluatePress(@NotNull NativeKeyEvent keyboardEvent) {
        // Hotkey listeners update & alert
        InputKey translatedKey = _keyboardTranslator.translate(true, keyboardEvent, true);
        
        if (translatedKey != null)
        {
            boolean continueParsing = updateHotkeyListeners(translatedKey, true);
            
            if (!continueParsing)
            {
                return null;
            }
        }
        
        // Subparser delegation
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluatePress(keyboardEvent);
    }

    @Override
    public @Nullable RecorderUserInput evaluateRelease(@NotNull NativeKeyEvent keyboardEvent) {
        // Hotkey listeners update
        InputKey translatedKey = _keyboardTranslator.translate(true, keyboardEvent, false);
        boolean continueParsing = updateHotkeyListeners(translatedKey, false);
        
        if (!continueParsing)
        {
            return null;
        }
        
        // Subparser delegation
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluateRelease(keyboardEvent);
    }

    @Override
    public @Nullable RecorderUserInput evaluatePress(@NotNull NativeMouseEvent mouseEvent) {
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluatePress(mouseEvent);
    }

    @Override
    public @Nullable RecorderUserInput evaluateRelease(@NotNull NativeMouseEvent mouseEvent) {
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluateRelease(mouseEvent);
    }

    @Override
    public @Nullable RecorderUserInput evaluateMouseMove(@NotNull NativeMouseEvent mouseMoveEvent) {
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluateMouseMove(mouseMoveEvent);
    }

    @Override
    public @Nullable RecorderUserInput evaluateMouseWheel(@NotNull NativeMouseWheelEvent mouseWheelEvent) {
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluateMouseWheel(mouseWheelEvent);
    }

    @Override
    public @Nullable RecorderUserInput evaluateWindowEvent(@NotNull WindowEvent windowEvent) {
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluateWindowEvent(windowEvent);
    }
    
    private boolean updateHotkeyListeners(@NotNull InputKey translatedKey, boolean performDelegateCall)
    {
        boolean continueWithParsing = true;
        
        Collection<RecorderHotkeyListener> listeners;
        
        synchronized (_lock)
        {
            listeners = CollectionUtilities.copyAsImmutable(_hotkeyListeners);
        }
        
        for (RecorderHotkeyListener l : listeners)
        {
            if (hotkeyListenerIsEligibleForKeystrokeEvent(l, translatedKey))
            {
                if (performDelegateCall)
                {
                    Hotkey hotkey = new Hotkey(translatedKey);
                    l.onHotkeyPressed(hotkey);
                }
                
                // Play/stop hotkey is never recorded
                if (l == _playStopHotkeyListener)
                {
                    continueWithParsing = false;
                }
            }
        }
        
        return continueWithParsing;
    }
    
    public boolean hotkeyListenerIsEligibleForKeystrokeEvent(@NotNull RecorderHotkeyListener l, @NotNull InputKey translatedKey)
    {
        return l.isListeningForAnyHotkey() || isHotkeyEvent(l.getHotkey(), translatedKey);
    }
    
    public boolean isHotkeyEvent(@Nullable Hotkey hotkey, @NotNull InputKey translatedKey)
    {
        if (hotkey == null)
        {
            return false;
        }
        
        return hotkey.isEqualTo(translatedKey);
    }
}

