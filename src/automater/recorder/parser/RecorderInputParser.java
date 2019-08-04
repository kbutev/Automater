/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.parser;

import automater.recorder.model.RecorderUserInput;
import automater.recorder.model.RecorderUserInputKey;
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
 * This is a standart implementation of BaseRecorderInputParser.
 * 
 * @author Bytevi
 */
public class RecorderInputParser implements BaseRecorderInputParser {
    public final ArrayList<RecorderParserFlag> settings;
    
    private final RecorderInputParserDelegate _delegate;
    private final RecorderSystemKeyboardTranslator _keyboardTranslator = new RecorderSystemKeyboardTranslator();
    private final RecorderSystemMouseTranslator _mouseTranslator = new RecorderSystemMouseTranslator(_keyboardTranslator);
    private final EventLogger _eventLogger = new EventLogger();
    
    public RecorderInputParser(ArrayList<RecorderParserFlag> settings, RecorderInputParserDelegate delegate)
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
        if (!settings.contains(RecorderParserFlag.RECORD_KEYBOARD_EVENTS))
        {
            return;
        }
        
        Date timestamp = getCurrentTime();
        RecorderUserInputKey translatedKey = _keyboardTranslator.translate(keyboardEvent, press);
        
        if (translatedKey == null)
        {
            return;
        }
        
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
        
        if (userInput != null)
        {
            _delegate.onParseResult(userInput);
        }
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
        if (!settings.contains(RecorderParserFlag.RECORD_MOUSE_CLICKS))
        {
            return;
        }
        
        Date timestamp = getCurrentTime();
        RecorderUserInputKey mouseKey = _mouseTranslator.translate(mouseEvent);
        
        if (mouseKey == null)
        {
            return;
        }
        
        _eventLogger.logMouseEvent(mouseEvent, mouseKey);
        
        RecorderUserInput userInput;
        
        // Press
        if (press)
        {
            userInput = RecorderUserInput.createMousePress(timestamp, mouseKey);
        }
        // Release
        else
        {
            userInput = RecorderUserInput.createMouseRelease(timestamp, mouseKey);
        }
        
        if (userInput != null)
        {
            _delegate.onParseResult(userInput);
        }
    }
    
    @Override
    public void evaluate(NativeMouseEvent mouseMoveEvent)
    {
        
    }
    
    @Override
    public void evaluate(NativeMouseWheelEvent mouseWheelEvent)
    {
        if (!settings.contains(RecorderParserFlag.RECORD_MOUSE_WHEEL))
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
        if (!settings.contains(RecorderParserFlag.RECORD_WINDOW_EVENTS))
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
            if (settings.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: keyboard event '" + translatedKey.toString() + "'");
            }
        }
        
        void logMouseEvent(NativeMouseEvent keyboardEvent, RecorderUserInputKey translatedKey)
        {
            if (settings.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: mouse event '" + translatedKey.toString() + "'");
            }
        }
        
        void logMouseWheelEvent(NativeMouseWheelEvent keyboardEvent)
        {
            if (settings.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: mouse wheel event");
            }
        }
        
        void logWindowEvent(WindowEvent keyboardEvent)
        {
            if (settings.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: window event");
            }
        }
    }
}
