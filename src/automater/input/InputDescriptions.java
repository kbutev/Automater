/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

import automater.utilities.Description;
import java.io.Serializable;

/**
 *
 * @author Bytevi
 */
public class InputDescriptions {
    public static Description getDoNothingDescription(long timestamp)
    {
        String name = "DoNothing";
        String time = String.valueOf(timestamp);
        String description = name;
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getWaitDescription(long timestamp, long wait)
    {
        String name = "Wait";
        String time = String.valueOf(timestamp);
        String description = name + " for " + String.valueOf(wait) + "ms";
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getKeyboardInputDescription(long timestamp, boolean press, InputKey key)
    {
        String time = String.valueOf(timestamp);
        
        if (press)
        {
            String name = "KeyboardPress";
            String description = name + " " + "'" + key.toString() + "'";
            String verbose = time + " " + description;
            String tooltip = description;
            
            return new InputDescription(name, description, verbose, tooltip);
        }
        
        String name = "KeyboardRelease";
        String description = name + " " + "'" + key.toString() + "'";
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getMouseInputDescription(long timestamp, boolean press, InputKey key)
    {
        String time = String.valueOf(timestamp);
        
        if (press)
        {
            String name = "MousePress";
            String description = name + " " + "'" + key.toString() + "'";
            String verbose = timestamp + " " + description;
            String tooltip = description;
            
            return new InputDescription(name, description, verbose, tooltip);
        }
        
        String name = "MouseRelease";
        String description = name + " " + "'" + key.toString() + "'";
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getMouseMoveDescription(long timestamp, int x, int y)
    {
        String name = "MouseMove";
        String time = String.valueOf(timestamp);
        String description = name + " " + "(" + x + "," + y + ")";
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getMouseMotionDescription(long timestamp, InputMouseMove first, InputMouseMove last, int numberOfMovements)
    {
        String name = "MouseMotion";
        String time = String.valueOf(timestamp);
        String description = name + " (" + String.valueOf(numberOfMovements) + "x) ends at (" + last.getX() + ", " + last.getY() + ")";
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getMouseWheelDescription(long timestamp, int value)
    {
        String name = "MouseWheel";
        String time = String.valueOf(timestamp);
        String description = name + " " + "(" + value + ")";
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
}

class InputDescription implements Description, Serializable {
    String name;
    String value;
    String verbose;
    String tooltip;
    
    InputDescription(String name, String value, String verbose, String tooltip)
    {
        this.name = name;
        this.value = value;
        this.verbose = verbose;
        this.tooltip = tooltip;
    }

    @Override
    public String getStandart() {
        return value;
    }

    @Override
    public String getVerbose() {
        return verbose;
    }

    @Override
    public String getStandartTooltip() {
        return tooltip;
    }

    @Override
    public String getVerboseTooltip() {
        return tooltip;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDebug() {
        return getVerbose();
    }
}
