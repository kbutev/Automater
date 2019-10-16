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
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
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
    @NotNull public final List<RecorderParserFlag> flags;
    
    @NotNull private final RecorderSystemKeyboardTranslator _keyboardTranslator = new RecorderSystemKeyboardTranslator();
    @NotNull private final RecorderSystemMouseTranslator _mouseTranslator = new RecorderSystemMouseTranslator(_keyboardTranslator);
    @NotNull private final EventLogger _eventLogger = new EventLogger();
    
    private Date _firstDate;
    
    public RecorderNativeParser(@NotNull List<RecorderParserFlag> flags)
    {
        this.flags = CollectionUtilities.copyAsImmutable(flags);
    }
    
    @Override
    public @Nullable RecorderUserInput evaluatePress(@NotNull NativeKeyEvent keyboardEvent)
    {
        return evaluateKeyboard(keyboardEvent, true);
    }
    
    @Override
    public @Nullable RecorderUserInput evaluateRelease(@NotNull NativeKeyEvent keyboardEvent)
    {
        return evaluateKeyboard(keyboardEvent, false);
    }
    
    private @Nullable RecorderUserInput evaluateKeyboard(@NotNull NativeKeyEvent keyboardEvent, boolean press)
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
    public @Nullable RecorderUserInput evaluatePress(@NotNull NativeMouseEvent mouseEvent)
    {
        return evaluateMouse(mouseEvent, true);
    }
    
    @Override
    public @Nullable RecorderUserInput evaluateRelease(@NotNull NativeMouseEvent mouseEvent)
    {
        return evaluateMouse(mouseEvent, false);
    }
    
    private @Nullable RecorderUserInput evaluateMouse(@NotNull NativeMouseEvent mouseEvent, boolean press)
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
    public @Nullable RecorderUserInput evaluateMouseMove(@NotNull NativeMouseEvent mouseMoveEvent)
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
    public @Nullable RecorderUserInput evaluateMouseWheel(@NotNull NativeMouseWheelEvent mouseWheelEvent)
    {
        if (!flags.contains(RecorderParserFlag.RECORD_MOUSE_WHEEL))
        {
            return null;
        }
        
        long timestamp = evaluteTimeForNextEvent();
        int value = mouseWheelEvent.getScrollAmount();
        
        if (mouseWheelEvent.getWheelRotation() == 1)
        {
            value *= -1;
        }
        
        _eventLogger.logMouseWheelEvent(mouseWheelEvent);
        
        RecorderUserInput userInput;
        userInput = RecorderUserInput.createMouseWheel(timestamp, value);
        
        return userInput;
    }
    
    @Override
    public @Nullable RecorderUserInput evaluateWindowEvent(@NotNull WindowEvent windowEvent)
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
        void logKeyboardClickEvent(@NotNull NativeKeyEvent keyboardEvent, @NotNull InputKey translatedKey)
        {
            if (flags.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: keyboard click event '" + translatedKey.toString() + "'");
            }
        }
        
        void logMouseClickEvent(@NotNull NativeMouseEvent keyboardEvent, @NotNull InputKey translatedKey)
        {
            if (flags.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: mouse click event '" + translatedKey.toString() + "'");
            }
        }
        
        void logMouseMovementEvent(@NotNull NativeMouseEvent keyboardEvent, int x, int y)
        {
            if (flags.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: mouse movement event (" + x + "," + y + ")");
            }
        }
        
        void logMouseWheelEvent(@NotNull NativeMouseWheelEvent keyboardEvent)
        {
            if (flags.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: mouse wheel event");
            }
        }
        
        void logWindowEvent(@NotNull WindowEvent keyboardEvent)
        {
            if (flags.contains(RecorderParserFlag.LOG_EVENTS))
            {
                Logger.messageEvent(this, "RecorderInputParser: window event");
            }
        }
    }
}
