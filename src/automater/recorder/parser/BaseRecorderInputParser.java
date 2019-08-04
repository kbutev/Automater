/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.parser;

import java.awt.event.WindowEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelEvent;

/**
 * Recorder parse interface.
 * 
 * @author Bytevi
 */
public interface BaseRecorderInputParser {
     public void evaluatePress(NativeKeyEvent keyboardEvent);
     public void evaluateRelease(NativeKeyEvent keyboardEvent);
     
     public void evaluatePress(NativeMouseEvent mouseEvent);
     public void evaluateRelease(NativeMouseEvent mouseEvent);
     
     public void evaluate(NativeMouseEvent mouseMoveEvent);
     public void evaluate(NativeMouseWheelEvent mouseWheelEvent);
     
     public void evaluate(WindowEvent windowEvent);
}
