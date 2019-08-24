/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Combination of mask values.
 * 
 * @author Bytevi
 */
public class RecorderUserInputKeyMask implements Serializable {
    private final HashSet<RecorderUserInputKeyMaskValue> _flags = new HashSet<>();
    
    public static RecorderUserInputKeyMask none()
    {
        return new RecorderUserInputKeyMask();
    }
    
    public RecorderUserInputKeyMask()
    {
        
    }
    
    public RecorderUserInputKeyMask(RecorderUserInputKeyMaskValue flag)
    {
        _flags.add(flag);
    }
    
    public RecorderUserInputKeyMask(HashSet<RecorderUserInputKeyMaskValue> flags)
    {
        for (RecorderUserInputKeyMaskValue flag : flags)
        {
            _flags.add(flag);
        }
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof RecorderUserInputKeyMask)
        {
            RecorderUserInputKeyMask other = (RecorderUserInputKeyMask)o;
            return _flags.equals(other._flags);
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this._flags);
        return hash;
    }
    
    public Set<RecorderUserInputKeyMaskValue> getFlags()
    {
        return _flags;
    }
    
    public RecorderUserInputKeyMask copy()
    {
        return new RecorderUserInputKeyMask(this._flags);
    }
    
    public RecorderUserInputKeyMask createWithNewAddedFlag(RecorderUserInputKeyMaskValue flag)
    {
        RecorderUserInputKeyMask mask = copy();
        
        if (flag == RecorderUserInputKeyMaskValue.NONE)
        {
            return mask;
        }
        
        mask._flags.add(flag);
        
        return mask;
    }
    
    public RecorderUserInputKeyMask createWithRemovedFlag(RecorderUserInputKeyMaskValue flag)
    {
        RecorderUserInputKeyMask mask = copy();
        
        if (flag == RecorderUserInputKeyMaskValue.NONE)
        {
            return mask;
        }
        
        mask._flags.remove(flag);
        
        return mask;
    }
    
    @Override
    public String toString()
    {
        String value = "";
        
        for (RecorderUserInputKeyMaskValue flag : _flags)
        {
            value = value.concat(flag.toString());
        }
        
        return value;
    }
}
