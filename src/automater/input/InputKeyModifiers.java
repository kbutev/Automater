/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Combination of modifier values.
 * 
 * @author Bytevi
 */
public class InputKeyModifiers implements Serializable {
    private final HashSet<InputKeyModifierValue> _values = new HashSet<>();
    
    public static InputKeyModifiers none()
    {
        return new InputKeyModifiers();
    }
    
    public InputKeyModifiers()
    {
        
    }
    
    public InputKeyModifiers(InputKeyModifierValue value)
    {
        _values.add(value);
    }
    
    public InputKeyModifiers(HashSet<InputKeyModifierValue> values)
    {
        for (InputKeyModifierValue value : values)
        {
            _values.add(value);
        }
    }
    
    public InputKeyModifiers(String string)
    {
        String suffix = InputKeyModifierValue.getSeparatorSymbol();
        
        String[] strings = suffix.split(string);
        
        for (int e = 0; e < string.length(); e++)
        {
            _values.add(InputKeyModifierValue.valueOf(strings[e]));
        }
    }
    
    @Override
    public String toString()
    {
        String value = "";
        
        for (InputKeyModifierValue flag : _values)
        {
            value = value.concat(flag.toString());
        }
        
        return value;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof InputKeyModifiers)
        {
            InputKeyModifiers other = (InputKeyModifiers)o;
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
    
    public Set<InputKeyModifierValue> getValues()
    {
        return _values;
    }
    
    public InputKeyModifiers copy()
    {
        return new InputKeyModifiers(this._values);
    }
    
    public InputKeyModifiers createWithNewAddedModifier(InputKeyModifierValue value)
    {
        InputKeyModifiers modifiers = copy();
        
        if (value == InputKeyModifierValue.NONE)
        {
            return modifiers;
        }
        
        modifiers._values.add(value);
        
        return modifiers;
    }
    
    public InputKeyModifiers createWithRemovedModifier(InputKeyModifierValue value)
    {
        InputKeyModifiers modifiers = copy();
        
        if (value == InputKeyModifierValue.NONE)
        {
            return modifiers;
        }
        
        modifiers._values.remove(value);
        
        return modifiers;
    }
}
