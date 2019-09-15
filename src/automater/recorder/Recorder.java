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
import automater.utilities.Logger;
import java.awt.Dimension;
import java.awt.Rectangle;
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
 * Must be started in order to function, stopped when no longer needed and in
 * order to retrieve the recorded input.
 * 
 * @author Bytevi
 */
public class Recorder implements RecorderJHookListenerDelegate {
    private static Recorder singleton;
    private final Object _lock = new Object();
    
    public final Defaults defaults = new Defaults();
    
    private RecorderJHookListener _nativeListener;
    private RecorderMasterNativeParser _masterParser;
    
    private BaseRecorderListener _listener;
    private BaseRecorderNativeParser _inputParser;
    private BaseRecorderModel _recorderModel;
    private Hotkey _stopHotkey;
    private Dimension _recordingSize;
    
    private Recorder()
    {
        startIfNotStarted();
    }
    
    // # Public
    
    synchronized public static Recorder getDefault()
    {
        if (singleton == null)
        {
            singleton = new Recorder();
        }
        
        return singleton;
    }
    
    public void start(BaseRecorderNativeParser parser, BaseRecorderModel model, BaseRecorderListener listener) throws Exception
    {
        start(parser, model, listener, null);
    }
    
    public void start(BaseRecorderNativeParser parser, BaseRecorderModel model, BaseRecorderListener listener, Hotkey stopHotkey) throws Exception
    {
        Dimension size = DeviceScreen.getPrimaryScreenSize();
        
        Logger.messageEvent(this, "Start recording on screen size " + size.width + "x" + size.height + "...");
        
        synchronized (_lock)
        {
            _inputParser = parser;
            _recorderModel = model;
            _listener = listener;
            _stopHotkey = stopHotkey;
            _recordingSize = size;
            
            _masterParser.setSubparser(_inputParser);
        }
    }
    
    public void stop() throws Exception
    {
        Logger.messageEvent(this, "Stop!");
        
        cancel(false, null);
    }
    
    public void registerHotkeyListener(RecorderHotkeyListener listener)
    {
        _masterParser.registerHotkeyListener(listener);
    }
    
    public void unregisterHotkeyListener(RecorderHotkeyListener listener)
    {
        _masterParser.unregisterHotkeyListener(listener);
    }
    
    public void registerPlayStopHotkeyListener(RecorderHotkeyListener listener)
    {
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
    
    // # RecorderNativeListenerDelegate
    
    @Override
    public void onParseInput(RecorderUserInput input)
    {
        // If not recording, do nothing
        if (_recorderModel == null)
        {
            return;
        }
        
        boolean success;
        
        // Process the input that native hook listener has translated for us
        try {
            success = _recorderModel.addInput(input);
        } catch (Exception e) {
            try {
                Logger.messageEvent(this, "Cancel! Exception encountered: " + e.toString());
                e.printStackTrace(System.out);
                cancel(false, e);
            } catch (Exception e2) {
                
            }
            
            return;
        }
        
        // Alert listener
        if (_listener == null)
        {
            return;
        }
        
        if (success)
        {
            _listener.onRecordedUserInput(input);
        }
        else
        {
            _listener.onFailedRecordedUserInput(input);
        }
    }
    
    // # Private
    
    private void startIfNotStarted()
    {
        if (_masterParser != null)
        {
            return;
        }
        
        _masterParser = new RecorderMasterNativeParser();
        _nativeListener = new RecorderJHookListener(_masterParser, this);
        
        try {
            _nativeListener.start();
        } catch (Exception e) {
            
        }
    }
    
    private void cancel(boolean success, Exception exception) throws Exception
    {
        synchronized (_lock)
        {
            _masterParser.setSubparser(null);
            
            BaseRecorderListener listener = _listener;
            
            _inputParser = null;
            _listener = null;
            _stopHotkey = null;
            
            RecorderResult result = _recorderModel.retrieveRecordedData();
            
            listener.onFinishedRecording(result, success, exception);
        }
    }
    
    // Default values
    public class Defaults
    {
        public ArrayList<RecorderParserFlag> getDefaultRecordFlags()
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
        
        public ArrayList<RecorderParserFlag> getRecordOnlyKeyClicksAndMouseMotionFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_MOTION);
            settings.add(RecorderParserFlag.LOG_EVENTS);
            return settings;
        }
        
        public ArrayList<RecorderParserFlag> getRecordOnlyKeyClicksFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            settings.add(RecorderParserFlag.LOG_EVENTS);
            return settings;
        }
        
        public ArrayList<RecorderParserFlag> getRecordOnlyKeyClicksAndMouseMotionSilentlyFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_MOTION);
            return settings;
        }
        
        public ArrayList<RecorderParserFlag> getRecordOnlyKeyClicksSilentlyFlags()
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
    private final Object _lock = new Object();
    
    private BaseRecorderNativeParser _subParser;
    
    private final RecorderSystemKeyboardTranslator _keyboardTranslator = new RecorderSystemKeyboardTranslator();
    private HashSet<RecorderHotkeyListener> _hotkeyListeners = new HashSet<>();
    private RecorderHotkeyListener _playStopHotkeyListener;
    
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
    
    public void setSubparser(BaseRecorderNativeParser parser)
    {
        if (parser != null)
        {
            Logger.message(this, "set parser to " + parser.toString());
        }
        else
        {
            Logger.message(this, "set parser to null");
        }
        
        synchronized (_lock)
        {
            _subParser = parser;
        }
    }
    
    public void registerHotkeyListener(RecorderHotkeyListener listener)
    {
        synchronized (_lock)
        {
            _hotkeyListeners.add(listener);
        }
    }
    
    public void unregisterHotkeyListener(RecorderHotkeyListener listener)
    {
        synchronized (_lock)
        {
            _hotkeyListeners.remove(listener);
        }
    }
    
    public RecorderHotkeyListener getPlayStopHotkeyListener()
    {
        synchronized (_lock)
        {
            return _playStopHotkeyListener;
        }
    }
    
    public void setPlayStopHotkeyListener(RecorderHotkeyListener listener)
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
    public RecorderUserInput evaluatePress(NativeKeyEvent keyboardEvent) {
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
    public RecorderUserInput evaluateRelease(NativeKeyEvent keyboardEvent) {
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
    public RecorderUserInput evaluatePress(NativeMouseEvent mouseEvent) {
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluatePress(mouseEvent);
    }

    @Override
    public RecorderUserInput evaluateRelease(NativeMouseEvent mouseEvent) {
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluateRelease(mouseEvent);
    }

    @Override
    public RecorderUserInput evaluateMouseMove(NativeMouseEvent mouseMoveEvent) {
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluateMouseMove(mouseMoveEvent);
    }

    @Override
    public RecorderUserInput evaluateMouseWheel(NativeMouseWheelEvent mouseWheelEvent) {
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluateMouseWheel(mouseWheelEvent);
    }

    @Override
    public RecorderUserInput evaluateWindowEvent(WindowEvent windowEvent) {
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluateWindowEvent(windowEvent);
    }
    
    private boolean updateHotkeyListeners(InputKey translatedKey, boolean performDelegateCall)
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
    
    public boolean hotkeyListenerIsEligibleForKeystrokeEvent(RecorderHotkeyListener l, InputKey translatedKey)
    {
        return l.isListeningForAnyHotkey() || isHotkeyEvent(l.getHotkey(), translatedKey);
    }
    
    public boolean isHotkeyEvent(Hotkey hotkey, InputKey translatedKey)
    {
        if (translatedKey == null)
        {
            return false;
        }
        
        return hotkey.isEqualTo(translatedKey);
    }
}

