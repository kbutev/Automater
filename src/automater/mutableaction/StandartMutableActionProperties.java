/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.mutableaction;

import automater.utilities.CollectionUtilities;
import automater.utilities.StringFormatting;
import java.util.List;

/**
 *
 * @author Byti
 */
public class StandartMutableActionProperties {
    public static BaseMutableActionProperty createBoolean(String name, boolean value)
    {
        return new StandartMutableActionPropertyBoolean(name, value);
    }
    
    public static BaseMutableActionProperty createNonNegativeInt(String name, int value, int max)
    {
        return new StandartMutableActionPropertyNonNegativeInt(name, value, max);
    }
    
    public static BaseMutableActionProperty createNonNegativeDouble(String name, double value, double max)
    {
        return new StandartMutableActionPropertyNonNegativeDouble(name, value, max);
    }
    
    public static BaseMutableActionProperty createString(String name, String value, byte max)
    {
        return new StandartMutableActionPropertyString(name, value, (byte)0, max);
    }
    
    public static BaseMutableActionProperty createString(String name, String value, byte min, byte max)
    {
        return new StandartMutableActionPropertyString(name, value, min, max);
    }
    
    public static BaseMutableActionProperty createHotkey(String name, String value, boolean modifiersAllowed)
    {
        return new StandartMutableActionPropertyHotkey(name, value, modifiersAllowed);
    }
    
    public static BaseMutableActionProperty createList(String name, List<String> values, String defaultValue)
    {
        return new StandartMutableActionPropertyList(name, values, defaultValue);
    }
}

class StandartMutableActionPropertyGeneric implements BaseMutableActionProperty {
    String name;
    String value;
    
    StandartMutableActionPropertyGeneric(String name, String value)
    {
        this.name = name;
        this.value = value;
    }
    
    @Override
    public boolean isValid() {
        return getInvalidError() == null;
    }

    @Override
    public String getInvalidError() {
        return null;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}

class StandartMutableActionPropertyBoolean extends StandartMutableActionPropertyGeneric implements MutableActionPropertyBoolean {
    StandartMutableActionPropertyBoolean(String name, boolean value)
    {
        super(name, String.valueOf(value));
    }
    
    @Override
    public String getInvalidError() {
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
    
    StandartMutableActionPropertyNonNegativeInt(String name, int value, int max)
    {
        super(name, String.valueOf(value));
        
        this.max = max;
    }
    
    @Override
    public String getInvalidError() {
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
    
    StandartMutableActionPropertyNonNegativeDouble(String name, double value, double max)
    {
        super(name, String.valueOf(value));
        
        this.max = max;
    }
    
    @Override
    public String getInvalidError() {
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
    final byte min;
    final byte max;
    
    StandartMutableActionPropertyString(String name, String value, byte min, byte max)
    {
        super(name, value);
        
        this.min = min;
        this.max = max;
    }
    
    @Override
    public String getInvalidError() {
        String value = getValue();
        
        if (value.length() < min)
        {
            return getName() + " must be at least " + String.valueOf(min) + " chars";
        }
        
        if (value.length() > max)
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
    
    StandartMutableActionPropertyHotkey(String name, String value, boolean modifiersAllowed)
    {
        super(name, value);
        this.modifiersAllowed = modifiersAllowed;
    }
    
    @Override
    public String getInvalidError() {
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
    List<String> values;
    
    StandartMutableActionPropertyList(String name, List<String> values, String defaultValue)
    {
        super(name, defaultValue);
        
        this.values = CollectionUtilities.copyAsImmutable(values);
    }
    
    @Override
    public String getInvalidError() {
        if (!values.contains(getValue()))
        {
            return "Select a value from " + getName();
        }
        
        return null;
    }

    @Override
    public List<String> getValues() {
        return values;
    }
}