/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

import java.io.Serializable;
import java.util.Objects;

/**
 * A recorded keystroke with its modifier values.
 * 
 * @author Bytevi
 */
public class InputKey implements Serializable {
    public final InputKeyValue value;
    public final InputKeyModifiers modifiers;
    
    public InputKey(InputKeyValue value)
    {
        this.value = value;
        this.modifiers = InputKeyModifiers.none();
    }
    
    public InputKey(InputKeyValue value, InputKeyModifiers modifiers)
    {
        this.value = value;
        this.modifiers = modifiers.copy();
    }
    
    public InputKey(String string)
    {
        this.value = InputKeyValue.fromString(extractKeyValueFromKeyString(string));
        this.modifiers = new InputKeyModifiers(extractKeyModifiersFromKeyString(string));
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof InputKey)
        {
            InputKey other = (InputKey)o;
            return value == other.value && modifiers.equals(other.modifiers);
        }
        
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.value);
        hash = 59 * hash + Objects.hashCode(this.modifiers);
        return hash;
    }
    
    @Override
    public String toString()
    {
        return modifiers.toString() + value.toString();
    }
    
    public boolean isKeyboardKey()
    {
        return !isMouseKey();
    }
    
    public boolean isMouseKey()
    {
        return value == InputKeyValue._MOUSE_LEFT_CLICK ||
                value == InputKeyValue._MOUSE_RIGHT_CLICK ||
                value == InputKeyValue._MOUSE_MIDDLE_CLICK ||
                value == InputKeyValue._MOUSE_4_CLICK ||
                value == InputKeyValue._MOUSE_5_CLICK;
    }
    
    public static String extractKeyValueFromKeyString(String value)
    {
        int lastIndexOfModifierSeparator = value.lastIndexOf(InputKeyModifierValue.getSeparatorSymbol());
        
        if (lastIndexOfModifierSeparator == -1)
        {
            return value;
        }
        
        if (lastIndexOfModifierSeparator+1 >= value.length())
        {
            return "";
        }
        
        String result = value.substring(lastIndexOfModifierSeparator+1);
        return result;
    }
    
    public static String extractKeyModifiersFromKeyString(String value)
    {
        int lastIndexOfModifierSeparator = value.lastIndexOf(InputKeyModifierValue.getSeparatorSymbol());
        
        if (lastIndexOfModifierSeparator == -1)
        {
            return "";
        }
        
        String result = value.substring(0, lastIndexOfModifierSeparator+1);
        return result;
    }
}
