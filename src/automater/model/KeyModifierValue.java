/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a modifier for a system key value.
 * 
 * @author Bytevi
 */
public enum KeyModifierValue {
    NONE,
    CTRL,
    ALT,
    SHIFT,
    WINDOWS_OR_COMMAND,
    FUNCTION;
    
    public static @NotNull String getSeparatorSymbol()
    {
        return "+";
    }
    
    public static boolean isOnWindowsPlatform()
    {
        return true;
    }
    
    @Override
    public String toString()
    {
        String value;
        
        value = switch (this) {
            case NONE -> "";
            case CTRL -> "CTRL";
            case ALT -> "ALT";
            case SHIFT -> "SHIFT";
            case WINDOWS_OR_COMMAND -> isOnWindowsPlatform() ? "WIN" : "CMD";
            case FUNCTION -> "FN";
            default -> "";
        };
        
        if (value.length() > 0)
        {
            value = value.concat(getSeparatorSymbol());
        }
        
        return value;
    }
}

