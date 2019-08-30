/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.parser;

import automater.recorder.model.RecorderUserInput;
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
    public RecorderUserInput evaluatePress(NativeKeyEvent keyboardEvent);
    public RecorderUserInput evaluateRelease(NativeKeyEvent keyboardEvent);
    
    public RecorderUserInput evaluatePress(NativeMouseEvent mouseEvent);
    public RecorderUserInput evaluateRelease(NativeMouseEvent mouseEvent);
    
    public RecorderUserInput evaluateMouseMove(NativeMouseEvent mouseMoveEvent);
    public RecorderUserInput evaluateMouseWheel(NativeMouseWheelEvent mouseWheelEvent);
    
    public RecorderUserInput evaluateWindowEvent(WindowEvent windowEvent);
}
