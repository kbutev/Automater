/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

import automater.utilities.Description;

/**
 *
 * @author Bytevi
 */
public class InputDescriptions {
    public static Description getKeyboardInputDescription(boolean press, InputKey key)
    {
        if (press)
        {
            return new InputDescription("KeyboardPress " + "'" + key.toString() + "'");
        }
        
        return new InputDescription("KeyboardRelease " + "'" + key.toString() + "'");
    }
    
    public static Description getMouseInputDescription(boolean press, InputKey key)
    {
        if (press)
        {
            return new InputDescription("MousePress " + "'" + key.toString() + "'");
        }
        
        return new InputDescription("MouseRelease " + "'" + key.toString() + "'");
    }
    
    public static Description getMouseMoveDescription(int x, int y)
    {
        return new InputDescription("MouseMove " + "(" + x + "," + y + ")");
    }
    
    public static Description getMouseMotionDescription(InputMouseMove first, InputMouseMove last, int numberOfMovements)
    {
        String str = "MouseMotion (" + String.valueOf(numberOfMovements) + "x) ends at (" + last.getX() + ", " + last.getY() + ")";
        return new InputDescription(str);
    }
    
    public static Description getMouseWheelDescription(int value)
    {
        return new InputDescription("MouseWheel " + "(" + value + ")");
    }
}

class InputDescription implements Description {
    String value;
    
    InputDescription(String value)
    {
        this.value = value;
    }

    @Override
    public String getStandart() {
        return value;
    }

    @Override
    public String getVerbose() {
        return value;
    }

    @Override
    public String getStandartTooltip() {
        return value;
    }

    @Override
    public String getVerboseTooltip() {
        return value;
    }

    @Override
    public String getName() {
        return value;
    }

    @Override
    public String getDebug() {
        return value;
    }
    
    
}
