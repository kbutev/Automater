/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * A key value and a key mask.
 * 
 * @author Bytevi
 */
public class RecorderUserInputKey implements Serializable {
    public final RecorderUserInputKeyValue value;
    public final RecorderUserInputKeyMask mask;
    
    public RecorderUserInputKey(RecorderUserInputKeyValue value)
    {
        this.value = value;
        this.mask = RecorderUserInputKeyMask.none();
    }
    
    public RecorderUserInputKey(RecorderUserInputKeyValue value, RecorderUserInputKeyMask mask)
    {
        this.value = value;
        this.mask = mask.copy();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof RecorderUserInputKey)
        {
            RecorderUserInputKey other = (RecorderUserInputKey)o;
            return value == other.value && mask.equals(other.mask);
        }
        
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.value);
        hash = 59 * hash + Objects.hashCode(this.mask);
        return hash;
    }
    
    @Override
    public String toString()
    {
        return mask.toString() + value.toString();
    }
    
    public boolean isKeyboardKey()
    {
        return !isMouseKey();
    }
    
    public boolean isMouseKey()
    {
        return value == RecorderUserInputKeyValue._MOUSE_LEFT_CLICK ||
                value == RecorderUserInputKeyValue._MOUSE_RIGHT_CLICK ||
                value == RecorderUserInputKeyValue._MOUSE_MIDDLE_CLICK ||
                value == RecorderUserInputKeyValue._MOUSE_4_CLICK ||
                value == RecorderUserInputKeyValue._MOUSE_5_CLICK;
    }
}
