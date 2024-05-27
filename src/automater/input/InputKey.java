/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.input;

import org.jetbrains.annotations.NotNull;
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
    
    public InputKey(@NotNull InputKeyValue value)
    {
        this.value = value;
        this.modifiers = InputKeyModifiers.none();
    }
    
    public InputKey(@NotNull InputKeyValue value, @NotNull InputKeyModifiers modifiers)
    {
        this.value = value;
        this.modifiers = modifiers.copy();
    }
    
    public InputKey(@NotNull String string)
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
    
    public static String extractKeyValueFromKeyString(@NotNull String value)
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
    
    public static String extractKeyModifiersFromKeyString(@NotNull String value)
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
