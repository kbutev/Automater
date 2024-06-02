/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import automater.json.JSONDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public class KeyModifier {
    @JSONDecoder.Optional
    private @Nullable List<KeyModifierValue> values;
    
    public static KeyModifier none()
    {
        return new KeyModifier();
    }
    
    public @NotNull KeyModifier createWithNewAddedModifier(@NotNull KeyModifierValue value)
    {
        KeyModifier modifiers = copy();
        
        if (value == KeyModifierValue.NONE)
        {
            return modifiers;
        }
        
        modifiers.getRawValues().add(value);
        
        return modifiers;
    }
    
    public @NotNull KeyModifier createWithRemovedModifier(@NotNull KeyModifierValue value)
    {
        KeyModifier modifiers = copy();
        
        if (value == KeyModifierValue.NONE)
        {
            return modifiers;
        }
        
        modifiers.getRawValues().remove(value);
        
        return modifiers;
    }
    
    public KeyModifier()
    {
        
    }
    
    public KeyModifier(@NotNull KeyModifierValue value)
    {
        getRawValues().add(value);
    }
    
    public KeyModifier(@NotNull List<KeyModifierValue> values)
    {
        if (values.isEmpty()) {
            return;
        }
        
        for (KeyModifierValue value : values)
        {
            getRawValues().add(value);
        }
    }
    
    public KeyModifier(@NotNull String string)
    {
        String suffix = KeyModifierValue.getSeparatorSymbol();
        
        String[] strings = string.split(Pattern.quote(suffix));
        
        for (int e = 0; e < string.length(); e++)
        {
            try {
                KeyModifierValue modifier = KeyModifierValue.valueOf(strings[e]);
                values.add(modifier);
            } catch (Exception exc) {
                
            }
        }
    }
    
    @Override
    public String toString()
    {
        String value = "";
        
        for (KeyModifierValue flag : getRawValues())
        {
            value = value.concat(flag.toString());
        }
        
        return value;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof KeyModifier other)
        {
            return getRawValues().equals(other.getRawValues());
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.getRawValues());
        return hash;
    }
    
    public boolean isEmpty() {
        return values != null && values.isEmpty();
    }
    
    private List<KeyModifierValue> getRawValues()
    {
        if (values == null) {
            values = new ArrayList<>();
        }
        
        return values;
    }
    
    public Set<KeyModifierValue> getValues()
    {
        return new HashSet(getRawValues());
    }
    
    public KeyModifier copy()
    {
        return new KeyModifier(getRawValues());
    }
    
    public boolean contains(@NotNull KeyModifierValue value) {
        return values != null ? getRawValues().contains(value) : false;
    }
}
