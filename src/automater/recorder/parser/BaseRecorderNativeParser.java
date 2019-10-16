/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.parser;

import automater.recorder.model.RecorderUserInput;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.awt.event.WindowEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelEvent;

/**
 * Translates native key event objects to RecorderUserInput objects.
 * 
 * @author Bytevi
 */
public interface BaseRecorderNativeParser {
    public @Nullable RecorderUserInput evaluatePress(@NotNull NativeKeyEvent keyboardEvent);
    public @Nullable RecorderUserInput evaluateRelease(@NotNull NativeKeyEvent keyboardEvent);
    
    public @Nullable RecorderUserInput evaluatePress(@NotNull NativeMouseEvent mouseEvent);
    public @Nullable RecorderUserInput evaluateRelease(@NotNull NativeMouseEvent mouseEvent);
    
    public @Nullable RecorderUserInput evaluateMouseMove(@NotNull NativeMouseEvent mouseMoveEvent);
    public @Nullable RecorderUserInput evaluateMouseWheel(@NotNull NativeMouseWheelEvent mouseWheelEvent);
    
    public @Nullable RecorderUserInput evaluateWindowEvent(@NotNull WindowEvent windowEvent);
}
