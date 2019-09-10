/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.parser;

import automater.input.InputKey;
import automater.input.InputKeyValue;
import org.jnativehook.mouse.NativeMouseEvent;

/**
 * Translates native mouse key events.
 * 
 * @author Bytevi
 */
public class RecorderSystemMouseTranslator {
    private final RecorderSystemKeyboardTranslator keyboardTranslator;
    
    public RecorderSystemMouseTranslator(RecorderSystemKeyboardTranslator keyboardTranslator)
    {
        this.keyboardTranslator = keyboardTranslator;
    }
    
    public InputKey translate(NativeMouseEvent keyEvent)
    {
        int mouseKeyValue = keyEvent.getButton();
        
        InputKeyValue key = InputKeyValue.UNKNOWN;
        
        switch(mouseKeyValue)
        {
            case 1:
                key = InputKeyValue._MOUSE_LEFT_CLICK;
                break;
            case 2:
                key = InputKeyValue._MOUSE_RIGHT_CLICK;
                break;
            case 3:
                key = InputKeyValue._MOUSE_MIDDLE_CLICK;
                break;
            case 4:
                key = InputKeyValue._MOUSE_4_CLICK;
                break;
            case 5:
                key = InputKeyValue._MOUSE_5_CLICK;
                break;
        }
        
        return new InputKey(key, keyboardTranslator.getCurrentMask());
    }
}
