/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.recorder.model.RecorderUserInput;
import automater.recorder.model.RecorderUserInputKey;
import automater.recorder.parser.BaseRecorderNativeParser;
import automater.recorder.parser.RecorderParserFlag;
import automater.recorder.parser.RecorderSystemKeyboardTranslator;
import automater.settings.Hotkey;
import automater.utilities.CollectionUtilities;
import automater.utilities.Logger;
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
public class Recorder implements RecorderNativeListenerDelegate {
    private static Recorder singleton;
    private final Object _lock = new Object();
    
    public final Defaults defaults = new Defaults();
    
    private RecorderNativeListener _nativeListener;
    private RecorderMasterNativeParser _masterParser;
    
    private BaseRecorderListener _listener;
    private BaseRecorderNativeParser _inputParser;
    private BaseRecorderInputModel _recorderModel;
    private Hotkey _stopHotkey;
    
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
    
    public void start(BaseRecorderNativeParser parser, BaseRecorderInputModel model, BaseRecorderListener listener) throws Exception
    {
        start(parser, model, listener, null);
    }
    
    public void start(BaseRecorderNativeParser parser, BaseRecorderInputModel model, BaseRecorderListener listener, Hotkey stopHotkey) throws Exception
    {
        Logger.messageEvent(this, "Start...");
        
        synchronized (_lock)
        {
            _inputParser = parser;
            _recorderModel = model;
            _listener = listener;
            _stopHotkey = stopHotkey;
            
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
        _masterParser.setPlayStopHotkeyListener(listener);
    }
    
    public void unregisterPlayStopHotkeyListener()
    {
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
        
        // Process the input that native hook listener has translated for us
        try {
            _recorderModel.addInput(input);
        } catch (Exception e) {
            try {
                Logger.messageEvent(this, "Cancel! Exception encountered: " + e.toString());
                e.printStackTrace(System.out);
                cancel(false, e);
            } catch (Exception e2) {
                
            }
            
            return;
        }
        
        // Successfully parsed the input object, alert the listener
        if (input != null)
        {
            _listener.onRecordedUserInput(input);
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
        _nativeListener = new RecorderNativeListener(_masterParser, this);
        
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
        // Hotkey listeners alert
        boolean continueParsing = alertHotkeyListeners(keyboardEvent);
        
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
        
        return subparser.evaluatePress(keyboardEvent);
    }

    @Override
    public RecorderUserInput evaluateRelease(NativeKeyEvent keyboardEvent) {
        // For release keyboard events, hotkey listeners are never alerted
        // However, we still need to update the keyboard translator
        _keyboardTranslator.translate(true, keyboardEvent, false);
        
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
    public RecorderUserInput evaluateOther(WindowEvent windowEvent) {
        BaseRecorderNativeParser subparser = getSubparser();
        
        if (subparser == null)
        {
            return null;
        }
        
        return subparser.evaluateOther(windowEvent);
    }
    
    private boolean alertHotkeyListeners(NativeKeyEvent keyboardEvent)
    {
        boolean continueWithParsing = true;
        
        Collection<RecorderHotkeyListener> listeners;
        
        synchronized (_lock)
        {
            listeners = CollectionUtilities.copyAsImmutable(_hotkeyListeners);
        }
        
        for (RecorderHotkeyListener l : listeners)
        {
            if (isHotkeyEvent(l.getHotkey(), keyboardEvent))
            {
                l.onHotkeyPressed();
                
                // Play stop hotkey listener is never recorded
                if (l == _playStopHotkeyListener)
                {
                    continueWithParsing = false;
                }
            }
        }
        
        return continueWithParsing;
    }
    
    public boolean isHotkeyEvent(Hotkey hotkey, NativeKeyEvent keyboardEvent)
    {
        RecorderUserInputKey translatedKey = _keyboardTranslator.translate(true, keyboardEvent, true);
        
        if (translatedKey == null)
        {
            return false;
        }
        
        return hotkey.isEqualTo(translatedKey);
    }
}

