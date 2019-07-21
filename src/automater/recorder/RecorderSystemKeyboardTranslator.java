/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Translates jnativehook key events to RecorderUserInputKey.
 * 
 * @author Bytevi
 */
public class RecorderSystemKeyboardTranslator {
    private RecorderUserInputKeyMask _mask = RecorderUserInputKeyMask.NONE;
    
    public RecorderSystemKeyboardTranslator()
    {
        
    }
    
    public RecorderUserInputKey translate(NativeKeyEvent keyEvent)
    {
        handlePossibleMaskKey(keyEvent);
        
        return new RecorderUserInputKey(keyValueForJNativeHookKey(0) , _mask);
    }
    
    private void handlePossibleMaskKey(NativeKeyEvent keyEvent)
    {
        
    }
    
    public static RecorderUserInputKeyValue keyValueForJNativeHookKey(int keyCode)
    {
        return RecorderUserInputKeyValue._A_;
    }
}
