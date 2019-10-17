/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.parser;

import automater.recorder.model.RecorderUserInput;
import automater.recorder.model.RecorderUserInputMouseMotion;
import java.awt.event.WindowEvent;
import java.util.List;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import automater.input.InputMouseMove;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Optimizes mouse click inputs, by grouping them in a single record event.
 * 
 * This parsers requires to work with a BaseRecorderModel whose
 * allowsDuplicateInputs() returns false.
 * 
 * @author Bytevi
 */
public class RecorderNativeParserSmart extends RecorderNativeParser {
    @Nullable private RecorderUserInputMouseMotion _currentMouseMotion;
    
    public RecorderNativeParserSmart(@NotNull List<RecorderParserFlag> flags)
    {
        super(flags);
    }
    
    @Override
    public @Nullable RecorderUserInput evaluatePress(@NotNull NativeKeyEvent keyboardEvent)
    {
        clearCurrentMouseMotion();
        return super.evaluatePress(keyboardEvent);
    }
    
    @Override
    public @Nullable RecorderUserInput evaluateRelease(@NotNull NativeKeyEvent keyboardEvent)
    {
        clearCurrentMouseMotion();
        return super.evaluateRelease(keyboardEvent);
    }
    
    @Override
    public @Nullable RecorderUserInput evaluatePress(@NotNull NativeMouseEvent mouseEvent)
    {
        clearCurrentMouseMotion();
        return super.evaluatePress(mouseEvent);
    }
    
    @Override
    public @Nullable RecorderUserInput evaluateRelease(@NotNull NativeMouseEvent mouseEvent)
    {
        clearCurrentMouseMotion();
        return super.evaluateRelease(mouseEvent);
    }
    
    @Override
    public @Nullable RecorderUserInput evaluateMouseMove(@NotNull NativeMouseEvent mouseMoveEvent)
    {
        RecorderUserInput result = super.evaluateMouseMove(mouseMoveEvent);
        
        // Not a mouse motion input, clear current mouse motion input
        if (!(result instanceof InputMouseMove))
        {
            clearCurrentMouseMotion();
            
            return result;
        }
        
        InputMouseMove mouseInput = (InputMouseMove)result;
        
        // Not currently parsing a mouse motion input, create a new one
        // from scratch
        if (_currentMouseMotion == null)
        {
            _currentMouseMotion = RecorderUserInputMouseMotion.create(mouseInput);
            return _currentMouseMotion;
        }
        // Else, just mutate the current one and return null
        _currentMouseMotion.addMovementPoint(mouseInput);
        return null;
    }
    
    @Override
    public @Nullable RecorderUserInput evaluateMouseWheel(@NotNull NativeMouseWheelEvent mouseWheelEvent)
    {
        clearCurrentMouseMotion();
        return super.evaluateMouseWheel(mouseWheelEvent);
    }
    
    @Override
    public @Nullable RecorderUserInput evaluateWindowEvent(@NotNull WindowEvent windowEvent)
    {
        clearCurrentMouseMotion();
        return super.evaluateWindowEvent(windowEvent);
    }
    
    private void clearCurrentMouseMotion()
    {
        _currentMouseMotion = null;
    }
}
