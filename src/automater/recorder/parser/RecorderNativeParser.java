/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.parser;

import automater.recorder.model.RecorderUserInput;
import automater.input.InputKey;
import automater.utilities.CollectionUtilities;
import automater.utilities.Logger;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.List;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelEvent;

/**
 * Translates jnativehook events to RecorderUserInput objects.
 * 
 * @author Bytevi
 */
public class RecorderNativeParser implements BaseRecorderNativeParser {
    public final List<RecorderParserFlag> flags;
    
    private final RecorderSystemKeyboardTranslator _keyboardTranslator = new RecorderSystemKeyboardTranslator();
    private final RecorderSystemMouseTranslator _mouseTranslator = new RecorderSystemMouseTranslator(_keyboardTranslator);
    private final EventLogger _eventLogger = new EventLogger();
    
    private Date _firstDate;
    
    public RecorderNativeParser(List<RecorderParserFlag> flags)
    {
        this.flags = CollectionUtilities.copyAsImmutable(flags);
    }
    
    @Override
    public RecorderUserInput evaluatePress(NativeKeyEvent keyboardEvent)
    {
        return evaluate(keyboardEvent, true);
    }
    
    @Override
    public RecorderUserInput evaluateRelease(NativeKeyEvent keyboardEvent)
    {
        return evaluate(keyboardEvent, false);
    }
    
    private RecorderUserInput evaluate(NativeKeyEvent keyboardEvent, boolean press)
    {
        if (!flags.contains(RecorderParserFlag.RECORD_KEYBOARD_EVENTS))
        {
            return null;
        }
        
        long timestamp = evaluteTimeForNextEvent();
        InputKey translatedKey = _keyboardTranslator.translate(true, keyboardEvent, press);
        
        if (translatedKey == null)
        {
            return null;
        }
        
        _eventLogger.logKeyboardClickEvent(keyboardEvent, translatedKey);
        
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
        
        return userInput;
    }
    
    @Override
    public RecorderUserInput evaluatePress(NativeMouseEvent mouseEvent)
    {
        return evaluate(mouseEvent, true);
    }
    
    @Override
    public RecorderUserInput evaluateRelease(NativeMouseEvent mouseEvent)
    {
        return evaluate(mouseEvent, false);
    }
    
    private RecorderUserInput evaluate(NativeMouseEvent mouseEvent, boolean press)
    {
        if (!flags.contains(RecorderParserFlag.RECORD_MOUSE_CLICKS))
        {
            return null;
        }
        
        long timestamp = evaluteTimeForNextEvent();
        InputKey mouseKey = _mouseTranslator.translate(mouseEvent);
        
        if (mouseKey == null)
        {
            return null;
        }
        
        _eventLogger.logMouseClickEvent(mouseEvent, mouseKey);
        
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
        
        return userInput;
    }
    
    @Override
    public RecorderUserInput evaluateMouseMove(NativeMouseEvent mouseMoveEvent)
    {
        if (!flags.contains(RecorderParserFlag.RECORD_MOUSE_MOTION))
        {
            return null;
        }
        
        long timestamp = evaluteTimeForNextEvent();
        int x, y;
        
        x = mouseMoveEvent.getX();
        y = mouseMoveEvent.getY();
        
        _eventLogger.logMouseMovementEvent(mouseMoveEvent, x, y);
        
        RecorderUserInput userInput;
        userInput = RecorderUserInput.createMouseMove(timestamp, x, y);
        
        return userInput;
    }
    
    @Override
    public RecorderUserInput evaluateMouseWheel(NativeMouseWheelEvent mouseWheelEvent)
    {
        if (!flags.contains(RecorderParserFlag.RECORD_MOUSE_WHEEL))
        {
            return null;
        }
        
        return null;
    }
    
    @Override
    public RecorderUserInput evaluateWindowEvent(WindowEvent windowEvent)
    {
        if (!flags.contains(RecorderParserFlag.RECORD_WINDOW_EVENTS))
        {
            return null;
        }
        
        return null;
    }
    
    // # Private
    
    public long evaluteTimeForNextEvent()
    {
        if (_firstDate == null)
        {
            _firstDate = new Date();
        }
        
        Date now = new Date();
        
        return now.getTime() - _firstDate.getTime();
    }
    
    // Private logger
    private class EventLogger
    {
        void logKeyboardClickEvent(NativeKeyEvent keyboardEvent, InputKey translatedKey)
        {
            if (flags.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: keyboard click event '" + translatedKey.toString() + "'");
            }
        }
        
        void logMouseClickEvent(NativeMouseEvent keyboardEvent, InputKey translatedKey)
        {
            if (flags.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: mouse click event '" + translatedKey.toString() + "'");
            }
        }
        
        void logMouseMovementEvent(NativeMouseEvent keyboardEvent, int x, int y)
        {
            if (flags.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: mouse movement event (" + x + "," + y + ")");
            }
        }
        
        void logMouseWheelEvent(NativeMouseWheelEvent keyboardEvent)
        {
            if (flags.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: mouse wheel event");
            }
        }
        
        void logWindowEvent(WindowEvent keyboardEvent)
        {
            if (flags.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: window event");
            }
        }
    }
}
