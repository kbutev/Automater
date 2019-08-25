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
 * Combination of modifier values.
 * 
 * @author Bytevi
 */
public class RecorderUserInputKeyModifiers implements Serializable {
    private final HashSet<RecorderUserInputKeyModifierValue> _values = new HashSet<>();
    
    public static RecorderUserInputKeyModifiers none()
    {
        return new RecorderUserInputKeyModifiers();
    }
    
    public RecorderUserInputKeyModifiers()
    {
        
    }
    
    public RecorderUserInputKeyModifiers(RecorderUserInputKeyModifierValue value)
    {
        _values.add(value);
    }
    
    public RecorderUserInputKeyModifiers(HashSet<RecorderUserInputKeyModifierValue> values)
    {
        for (RecorderUserInputKeyModifierValue value : values)
        {
            _values.add(value);
        }
    }
    
    @Override
    public String toString()
    {
        String value = "";
        
        for (RecorderUserInputKeyModifierValue flag : _values)
        {
            value = value.concat(flag.toString());
        }
        
        return value;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof RecorderUserInputKeyModifiers)
        {
            RecorderUserInputKeyModifiers other = (RecorderUserInputKeyModifiers)o;
            return _values.equals(other._values);
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this._values);
        return hash;
    }
    
    public Set<RecorderUserInputKeyModifierValue> getValues()
    {
        return _values;
    }
    
    public RecorderUserInputKeyModifiers copy()
    {
        return new RecorderUserInputKeyModifiers(this._values);
    }
    
    public RecorderUserInputKeyModifiers createWithNewAddedModifier(RecorderUserInputKeyModifierValue value)
    {
        RecorderUserInputKeyModifiers modifiers = copy();
        
        if (value == RecorderUserInputKeyModifierValue.NONE)
        {
            return modifiers;
        }
        
        modifiers._values.add(value);
        
        return modifiers;
    }
    
    public RecorderUserInputKeyModifiers createWithRemovedModifier(RecorderUserInputKeyModifierValue value)
    {
        RecorderUserInputKeyModifiers modifiers = copy();
        
        if (value == RecorderUserInputKeyModifierValue.NONE)
        {
            return modifiers;
        }
        
        modifiers._values.remove(value);
        
        return modifiers;
    }
}
