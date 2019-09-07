/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.utilities.Description;
import java.io.Serializable;

/**
 *
 * @author Bytevi
 */
public class ActionSystemKey implements Serializable, Description {
    public static ActionSystemKey createKeyboardKey(int value)
    {
        return new ActionSystemKeyKeyboard(value);
    }
    
    public static ActionSystemKey createMouseKey(int value)
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
    public String getStandart() {
        return "unknown";
    }

    @Override
    public String getVerbose() {
        return getStandart();
    }

    @Override
    public String getStandartTooltip() {
        return getStandart();
    }

    @Override
    public String getVerboseTooltip() {
        return getStandart();
    }

    @Override
    public String getName() {
        return getStandart();
    }

    @Override
    public String getDebug() {
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
    public String getStandart() {
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
    public String getStandart() {
        return String.valueOf(value);
    }
}
