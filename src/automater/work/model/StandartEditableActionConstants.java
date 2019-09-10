/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.TextValue;
import automater.input.InputKeyClick;
import automater.input.InputMouse;
import automater.input.InputMouseMotion;
import automater.utilities.Description;
import automater.work.BaseAction;
import java.util.ArrayList;
import java.util.List;

/**
 * List of mandatory and commonly used values for the standart EditableAction implementation.
 *
 * @author Bytevi
 */
public class StandartEditableActionConstants {
    public static final String KEYBOARD_CLICK_FIRST_NAME = "Key";
    public static final String KEYBOARD_CLICK_SECOND_NAME = "Press";
    public static final String MOUSE_CLICK_FIRST_NAME = "Mouse Key";
    public static final String MOUSE_CLICK_SECOND_NAME = "Press";
    public static final String MOUSE_MOVE_FIRST_NAME = "x";
    public static final String MOUSE_MOVE_SECOND_NAME = "y";
    
    public static List<Description> getActionTypes()
    {
        ArrayList<Description> types = new ArrayList<>();
        types.add(Description.createFromString(TextValue.getText(TextValue.EditAction_TypeKeyboardClick)));
        types.add(Description.createFromString(TextValue.getText(TextValue.EditAction_TypeMouseClick)));
        types.add(Description.createFromString(TextValue.getText(TextValue.EditAction_TypeMouseMove)));
        return types;
    }
    
    public static int getActionTypeSelectedIndex(BaseAction action)
    {
        if (action instanceof InputKeyClick)
        {
            if (action instanceof InputMouse)
            {
                return 1;
            }
            
            return 0;
        }
        
        if (action instanceof InputMouseMotion)
        {
            return 2;
        }
        
        return 0;
    }
    
    public static EditableActionType getTypeFromAction(BaseAction action)
    {
        EditableActionType type = EditableActionType.SingleString;
        
        if (action instanceof InputKeyClick)
        {
            if (action instanceof InputMouse)
            {
                return EditableActionType.SpecificValues;
            }
            
            return EditableActionType.Hotkey;
        }
        
        if (action instanceof InputMouseMotion)
        {
            type = EditableActionType.DoubleString;
        }
        
        return type;
    }
    
    public static List<String> getMouseClickSpecificValues()
    {
        ArrayList<String> values = new ArrayList<>();
        values.add(TextValue.getText(TextValue.EditAction_TypeLeftMouseClick));
        values.add(TextValue.getText(TextValue.EditAction_TypeRightMouseClick));
        values.add(TextValue.getText(TextValue.EditAction_TypeMiddleMouseClick));
        return values;
    }
}
