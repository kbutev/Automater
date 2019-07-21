/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.utilities.Logger;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelEvent;

/**
 * Maps jnativehook events to RecorderUserInput objects.
 * 
 * @author Bytevi
 */
public class RecorderInputParserStandart implements RecorderInputParser {
    enum RecordSettings 
    {
        RECORD_WINDOW_EVENTS,
        RECORD_KEYBOARD_EVENTS,
        RECORD_MOUSE_MOTION,
        RECORD_MOUSE_WHEEL,
        RECORD_MOUSE_CLICKS,
        
        LOG_EVENTS
    }
    
    public final ArrayList<RecordSettings> settings;
    
    private final RecorderInputParserDelegate _delegate;
    private final RecorderSystemKeyboardTranslator _keyboardTranslator = new RecorderSystemKeyboardTranslator();
    private final EventLogger _eventLogger = new EventLogger();
    
    public RecorderInputParserStandart(ArrayList<RecordSettings> settings, RecorderInputParserDelegate delegate)
    {
        this.settings = settings;
        this._delegate = delegate;
    }
    
    public Date getCurrentTime()
    {
        return new Date();
    }
    
    @Override
    public void evaluatePress(NativeKeyEvent keyboardEvent)
    {
        evaluate(keyboardEvent, true);
    }
    
    @Override
    public void evaluateRelease(NativeKeyEvent keyboardEvent)
    {
        evaluate(keyboardEvent, false);
    }
    
    private void evaluate(NativeKeyEvent keyboardEvent, boolean press)
    {
        if (!settings.contains(RecordSettings.RECORD_KEYBOARD_EVENTS))
        {
            return;
        }
        
        Date timestamp = getCurrentTime();
        RecorderUserInputKey translatedKey = _keyboardTranslator.translate(keyboardEvent);
        
        _eventLogger.logKeyboardEvent(keyboardEvent, translatedKey);
        
        RecorderUserInput userInput;
        
        // Press
        if (press)
        {
            userInput = RecorderUserInput.createKeyboardPress(timestamp, translatedKey);
        }
        // Release
        else
        {
            userInput = RecorderUserInput.createKeyboardRelease(timestamp, translatedKey);
        }
        
        _delegate.onParseResult(userInput);
    }
    
    @Override
    public void evaluatePress(NativeMouseEvent mouseEvent)
    {
        evaluate(mouseEvent, true);
    }
    
    @Override
    public void evaluateRelease(NativeMouseEvent mouseEvent)
    {
        evaluate(mouseEvent, false);
    }
    
    private void evaluate(NativeMouseEvent mouseEvent, boolean press)
    {
        if (!settings.contains(RecordSettings.RECORD_MOUSE_CLICKS))
        {
            return;
        }
        
        Date timestamp = getCurrentTime();
        _eventLogger.logMouseEvent(mouseEvent);
        
        RecorderUserInput userInput;
        
        // Press
        if (press)
        {
            userInput = RecorderUserInput.createMousePress(timestamp);
        }
        // Release
        else
        {
            userInput = RecorderUserInput.createMouseRelease(timestamp);
        }
        
        _delegate.onParseResult(userInput);
    }
    
    @Override
    public void evaluate(NativeMouseEvent mouseMoveEvent)
    {
        
    }
    
    @Override
    public void evaluate(NativeMouseWheelEvent mouseWheelEvent)
    {
        if (!settings.contains(RecordSettings.RECORD_MOUSE_WHEEL))
        {
            return;
        }
        
        Date timestamp = getCurrentTime();
        _eventLogger.logMouseWheelEvent(mouseWheelEvent);
        
        RecorderUserInput userInput = new RecorderUserInput(timestamp);
        
        _delegate.onParseResult(userInput);
    }
    
    @Override
    public void evaluate(WindowEvent windowEvent)
    {
        if (!settings.contains(RecordSettings.RECORD_WINDOW_EVENTS))
        {
            return;
        }
        
        Date timestamp = getCurrentTime();
        _eventLogger.logWindowEvent(windowEvent);
        
        RecorderUserInput userInput = new RecorderUserInput(timestamp);
        
        _delegate.onParseResult(userInput);
    }
    
    // Private logger
    private class EventLogger
    {
        void logKeyboardEvent(NativeKeyEvent keyboardEvent, RecorderUserInputKey translatedKey)
        {
            if (settings.contains(RecordSettings.LOG_EVENTS))
            {
                Logger.messageEvent("RecorderInputParser: keyboard event '" + translatedKey.value.name() + "'");
            }
        }
        
        void logMouseEvent(NativeMouseEvent keyboardEvent)
        {
            if (settings.contains(RecordSettings.LOG_EVENTS))
            {
                Logger.messageEvent("RecorderInputParser: mouse event");
            }
        }
        
        void logMouseWheelEvent(NativeMouseWheelEvent keyboardEvent)
        {
            if (settings.contains(RecordSettings.LOG_EVENTS))
            {
                Logger.messageEvent("RecorderInputParser: mouse wheel event");
            }
        }
        
        void logWindowEvent(WindowEvent keyboardEvent)
        {
            if (settings.contains(RecordSettings.LOG_EVENTS))
            {
                Logger.messageEvent("RecorderInputParser: window event");
            }
        }
    }
}
