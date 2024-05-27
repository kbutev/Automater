/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work.model;

import automater.utilities.Description;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.Serializable;

/**
 * An action's keyboard or mouse key.
 * 
 * @author Bytevi
 */
public class ActionSystemKey implements Serializable, Description {
    public static @NotNull ActionSystemKey createKeyboardKey(int value)
    {
        return new ActionSystemKeyKeyboard(value);
    }
    
    public static @NotNull ActionSystemKey createMouseKey(int value)
    {
        return new ActionSystemKeyMouse(value);
    }
    
    public int getValue()
    {
        return 0;
    }
    
    public boolean isKeyboardKey()
    {
        return false;
    }
    
    public boolean isMouseKey()
    {
        return false;
    }
    
    @Override
    public boolean equals(Object o) 
    {
        if (o instanceof ActionSystemKey)
        {
            ActionSystemKey other = (ActionSystemKey)o;
            return getValue() == other.getValue();
        }
        
        return false;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 7;
        return hash;
    }

    @Override
    public @Nullable String getStandart() {
        return "unknown";
    }

    @Override
    public @Nullable String getVerbose() {
        return getStandart();
    }

    @Override
    public @Nullable String getStandartTooltip() {
        return getStandart();
    }

    @Override
    public @Nullable String getVerboseTooltip() {
        return getStandart();
    }

    @Override
    public @Nullable String getName() {
        return getStandart();
    }

    @Override
    public @Nullable String getDebug() {
        return getStandart();
    }
}

class ActionSystemKeyKeyboard extends ActionSystemKey {
    public final int value;
    
    ActionSystemKeyKeyboard(int value)
    {
        this.value = value;
    }
    
    @Override
    public int getValue()
    {
        return value;
    }
    
    @Override
    public boolean isKeyboardKey()
    {
        return true;
    }
    
    @Override
    public @Nullable String getStandart() {
        return String.valueOf(value);
    }
}

class ActionSystemKeyMouse extends ActionSystemKey {
    public final int value;
    
    ActionSystemKeyMouse(int value)
    {
        this.value = value;
    }
    
    @Override
    public int getValue()
    {
        return value;
    }
    
    @Override
    public boolean isMouseKey()
    {
        return true;
    }
    
    @Override
    public @Nullable String getStandart() {
        return String.valueOf(value);
    }
}
