/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

/**
 * Represents a modifier for a system key value.
 * 
 * @author Bytevi
 */
public enum RecorderUserInputKeyModifierValue {
    NONE,
    CTRL,
    ALT,
    SHIFT,
    WINDOWS_OR_COMMAND,
    FUNCTION;
    
    public String getSuffix()
    {
        return "+";
    }
    
    public boolean isOnWindowsPlatform()
    {
        return true;
    }
    
    @Override
    public String toString()
    {
        String value;
        
        switch(this)
        {
            case NONE:
                value = "";
                break;
            case CTRL:
                value = "CTRL";
                break;
            case ALT:
                value = "ALT";
                break;
            case SHIFT:
                value = "SHIFT";
                break;
            case WINDOWS_OR_COMMAND:
                value = isOnWindowsPlatform() ? "WIN" : "CMD";
                break;
            case FUNCTION:
                value = "FN";
                break;
            default:
                value = "";
                break;
        }
        
        if (value.length() > 0)
        {
            value = value.concat(getSuffix());
        }
        
        return value;
    }
}
