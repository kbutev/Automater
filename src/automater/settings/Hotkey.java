/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.settings;

import automater.recorder.model.RecorderUserInputKey;
import automater.recorder.model.RecorderUserInputKeyValue;

/**
 *
 * @author Bytevi
 */
public class Hotkey {
    public final RecorderUserInputKeyValue key;
    
    public Hotkey(RecorderUserInputKeyValue key)
    {
        this.key = key;
    }
    
    public boolean isEqualTo(RecorderUserInputKey key)
    {
        if (key == null)
        {
            return false;
        }
        
        return this.key == key.value;
    }
}
