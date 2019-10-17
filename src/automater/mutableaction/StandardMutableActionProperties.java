/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.mutableaction;

import automater.utilities.CollectionUtilities;
import automater.utilities.StringFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * Standard implementation for mutable action properties.
 * 
 * @author Byti
 */
public class StandardMutableActionProperties {
    public static BaseMutableActionProperty createBoolean(@NotNull String name, boolean value)
    {
        return new StandartMutableActionPropertyBoolean(name, value);
    }
    
    public static BaseMutableActionProperty createNonNegativeInt(@NotNull String name, int value, int max)
    {
        return new StandartMutableActionPropertyNonNegativeInt(name, value, max);
    }
    
    public static BaseMutableActionProperty createNonNegativeDouble(@NotNull String name, double value, double max)
    {
        return new StandartMutableActionPropertyNonNegativeDouble(name, value, max);
    }
    
    public static BaseMutableActionProperty createString(@NotNull String name, @NotNull String value, int max)
    {
        return new StandartMutableActionPropertyString(name, value, 0, max);
    }
    
    public static BaseMutableActionProperty createString(@NotNull String name, @NotNull String value, int min, int max)
    {
        return new StandartMutableActionPropertyString(name, value, min, max);
    }
    
    public static BaseMutableActionProperty createHotkey(@NotNull String name, @NotNull String value, boolean modifiersAllowed)
    {
        return new StandartMutableActionPropertyHotkey(name, value, modifiersAllowed);
    }
    
    public static BaseMutableActionProperty createList(@NotNull String name, @NotNull List<String> values, @NotNull String defaultValue)
    {
        return new StandartMutableActionPropertyList(name, values, defaultValue);
    }
}

class StandartMutableActionPropertyGeneric implements BaseMutableActionProperty {
    @NotNull String name;
    @NotNull String value;
    
    StandartMutableActionPropertyGeneric(@NotNull String name, @NotNull String value)
    {
        this.name = name;
        this.value = value;
    }
    
    @Override
    public boolean isValid() {
        return getInvalidError() == null;
    }

    @Override
    public @Nullable String getInvalidError() {
        return null;
    }

    @Override
    public @NotNull String getValue() {
        return value;
    }

    @Override
    public void setValue(@NotNull String value) {
        this.value = value;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }
}

class StandartMutableActionPropertyBoolean extends StandartMutableActionPropertyGeneric implements MutableActionPropertyBoolean {
    StandartMutableActionPropertyBoolean(@NotNull String name, boolean value)
    {
        super(name, String.valueOf(value));
    }
    
    @Override
    public @Nullable String getInvalidError() {
        try {
            Boolean.parseBoolean(getValue());
        } catch (Exception e) {
            return "Invalid boolean value for " + getName();
        }
        
        return null;
    }
    
    @Override
    public boolean isTrue() {
        return Boolean.parseBoolean(getValue());
    }
}

class StandartMutableActionPropertyNonNegativeInt extends StandartMutableActionPropertyGeneric implements MutableActionPropertyNumber {
    final int max;
    
    StandartMutableActionPropertyNonNegativeInt(@NotNull String name, int value, int max)
    {
        super(name, String.valueOf(value));
        
        this.max = max;
    }
    
    @Override
    public @Nullable String getInvalidError() {
        if (!StringFormatting.isStringANonNegativeInt(getValue()))
        {
            return "Enter a non-negative integer for " + getName();
        }
        
        return null;
    }
    
    @Override
    public double getMaxValue() {
        return max;
    }
    
    @Override
    public double getMinValue() {
        return 0;
    }
}

class StandartMutableActionPropertyNonNegativeDouble extends StandartMutableActionPropertyGeneric implements MutableActionPropertyNumber {
    final double max;
    
    StandartMutableActionPropertyNonNegativeDouble(@NotNull String name, double value, double max)
    {
        super(name, String.valueOf(value));
        
        this.max = max;
    }
    
    @Override
    public @Nullable String getInvalidError() {
        if (!StringFormatting.isStringANonNegativeDouble(getValue()))
        {
            return "Enter a non-negative number for " + getName();
        }
        
        return null;
    }
    
    @Override
    public double getMaxValue() {
        return max;
    }
    
    @Override
    public double getMinValue() {
        return 0;
    }
}

class StandartMutableActionPropertyString extends StandartMutableActionPropertyGeneric implements MutableActionPropertyString {
    final int min;
    final int max;
    
    StandartMutableActionPropertyString(@NotNull String name, @NotNull String value, int min, int max)
    {
        super(name, value);
        
        this.min = min;
        this.max = max;
    }
    
    @Override
    public @Nullable String getInvalidError() {
        String string = getValue();
        
        if (string.length() < min)
        {
            return getName() + " must be at least " + String.valueOf(min) + " chars";
        }
        
        if (string.length() > max)
        {
            return getName() + " cannot be longer than " + String.valueOf(max) + " chars";
        }
        
        return null;
    }

    @Override
    public int getMaxLength() {
        return max;
    }

    @Override
    public int getMinLength() {
        return min;
    }
}

class StandartMutableActionPropertyHotkey extends StandartMutableActionPropertyGeneric implements MutableActionPropertyHotkey {
    boolean modifiersAllowed;
    
    StandartMutableActionPropertyHotkey(@NotNull String name, @NotNull String value, boolean modifiersAllowed)
    {
        super(name, value);
        this.modifiersAllowed = modifiersAllowed;
    }
    
    @Override
    public @Nullable String getInvalidError() {
        if (getValue().isEmpty())
        {
            return "Enter a value for " + getName();
        }
        
        return null;
    }
    
    @Override
    public boolean isModifiersAllowed() {
        return modifiersAllowed;
    }
}

class StandartMutableActionPropertyList extends StandartMutableActionPropertyGeneric implements MutableActionPropertyList {
    @NotNull List<String> values;
    
    StandartMutableActionPropertyList(@NotNull String name, @NotNull List<String> values, @NotNull String defaultValue)
    {
        super(name, defaultValue);
        
        this.values = CollectionUtilities.copyAsImmutable(values);
    }
    
    @Override
    public @Nullable String getInvalidError() {
        if (!values.contains(getValue()))
        {
            return "Select a value from " + getName();
        }
        
        return null;
    }

    @Override
    public @NotNull List<String> getValues() {
        return values;
    }
}