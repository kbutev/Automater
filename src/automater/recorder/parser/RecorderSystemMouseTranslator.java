/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.parser;

import automater.recorder.model.RecorderUserInputKey;
import automater.recorder.model.RecorderUserInputKeyValue;
import static automater.recorder.parser.RecorderSystemKeyboardTranslator.keyValueForJNativeHookKey;
import automater.utilities.Logger;
import com.sun.glass.events.KeyEvent;
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
    
    public RecorderUserInputKey translate(NativeMouseEvent keyEvent)
    {
        int mouseKeyValue = keyEvent.getButton();
        
        RecorderUserInputKeyValue key = RecorderUserInputKeyValue.UNKNOWN;
        
        switch(mouseKeyValue)
        {
            case 1:
                key = RecorderUserInputKeyValue._MOUSE_LEFT_CLICK;
                break;
            case 2:
                key = RecorderUserInputKeyValue._MOUSE_RIGHT_CLICK;
                break;
            case 3:
                key = RecorderUserInputKeyValue._MOUSE_MIDDLE_CLICK;
                break;
            case 4:
                key = RecorderUserInputKeyValue._MOUSE_4_CLICK;
                break;
            case 5:
                key = RecorderUserInputKeyValue._MOUSE_5_CLICK;
                break;
        }
        
        return new RecorderUserInputKey(key, keyboardTranslator.getCurrentMask());
    }
}
