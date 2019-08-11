/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

import java.io.Serializable;

/**
 * A key value and a key mask.
 * 
 * @author Bytevi
 */
public class RecorderUserInputKey implements Serializable {
    public final RecorderUserInputKeyValue value;
    public final RecorderUserInputKeyMask mask;
    
    public RecorderUserInputKey(RecorderUserInputKeyValue value, RecorderUserInputKeyMask mask)
    {
        this.value = value;
        this.mask = mask.copy();
    }
    
    @Override
    public String toString()
    {
        return mask.toString() + value.toString();
    }
}
