/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

import automater.TextValue;
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
        String description = TextValue.getText(TextValue.Input_DoNothing);
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getWaitDescription(long timestamp, long wait)
    {
        String name = "Wait";
        String time = String.valueOf(timestamp);
        String description = TextValue.getText(TextValue.Input_Wait, String.valueOf(wait));
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
            String description = TextValue.getText(TextValue.Input_KeyboardPress, key.toString());
            String verbose = time + " " + description;
            String tooltip = description;
            
            return new InputDescription(name, description, verbose, tooltip);
        }
        
        String name = "KeyboardRelease";
        String description = TextValue.getText(TextValue.Input_KeyboardRelease, key.toString());
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
            String description = TextValue.getText(TextValue.Input_MousePress, key.toString());
            String verbose = timestamp + " " + description;
            String tooltip = description;
            
            return new InputDescription(name, description, verbose, tooltip);
        }
        
        String name = "MouseRelease";
        String description = TextValue.getText(TextValue.Input_MouseRelease, key.toString());
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getMouseMoveDescription(long timestamp, int x, int y)
    {
        String name = "MouseMove";
        String time = String.valueOf(timestamp);
        String description = TextValue.getText(TextValue.Input_MouseMove, String.valueOf(x), String.valueOf(y));
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getMouseMotionDescription(long timestamp, InputMouseMove first, InputMouseMove last, int numberOfMovements)
    {
        int x = last.getX();
        int y = last.getY();
        
        String name = "MouseMotion";
        String time = String.valueOf(timestamp);
        String description = TextValue.getText(TextValue.Input_MouseMotion, String.valueOf(numberOfMovements), String.valueOf(x), String.valueOf(y));
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getMouseWheelDescription(long timestamp, int value)
    {
        String name = "MouseWheel";
        String time = String.valueOf(timestamp);
        String description = TextValue.getText(TextValue.Input_MouseWheel, String.valueOf(value));
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getSystemCommand(long timestamp, String value)
    {
        String name = "SystemCommand";
        String time = String.valueOf(timestamp);
        String description = TextValue.getText(TextValue.Input_SystemCommand, value);
        String verbose = time + " " + description;
        String tooltip = description;
        
        System.out.println("description: " + description + " value: " + value);
        
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
