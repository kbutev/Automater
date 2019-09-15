/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.TextValue;
import automater.input.InputKey;
import automater.input.InputKeyClick;
import automater.input.InputKeyValue;
import automater.input.InputMouse;
import automater.input.InputMouseMotion;
import automater.input.InputMouseMove;
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
    public static String MOUSE_KEY_REPLACEMENT_STRING = "MOUSE_";
    
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
        EditableActionType type = EditableActionType.MouseKey;
        
        if (action instanceof InputKeyClick)
        {
            if (action instanceof InputMouse)
            {
                return EditableActionType.MouseKey;
            }
            
            return EditableActionType.KeyboardKey;
        }
        
        if (action instanceof InputMouseMove)
        {
            type = EditableActionType.MouseMove;
        }
        
        if (action instanceof InputMouseMotion)
        {
            type = EditableActionType.MouseMotion;
        }
        
        return type;
    }
    
    public static List<String> getMouseClickSpecificValues()
    {
        ArrayList<String> values = new ArrayList<>();
        
        values.add(getMouseClickSpecificValueForKeyValue(InputKeyValue._MOUSE_LEFT_CLICK));
        values.add(getMouseClickSpecificValueForKeyValue(InputKeyValue._MOUSE_RIGHT_CLICK));
        values.add(getMouseClickSpecificValueForKeyValue(InputKeyValue._MOUSE_MIDDLE_CLICK));
        
        return values;
    }
    
    public static String getMouseClickSpecificValueForKeyValue(InputKeyValue value)
    {
        String str;
        String prefix = MOUSE_KEY_REPLACEMENT_STRING;
        str = value.toString();
        str = str.replaceFirst(prefix, "");
        
        return str;
    }
    
    public static InputKey getMouseKeyForTextValue(String string)
    {
        string = MOUSE_KEY_REPLACEMENT_STRING + string;
        
        InputKeyValue key;
        
        try {
            key = InputKeyValue.fromString(string);
        } catch (Exception e) {
            return null;
        }
        
        return new InputKey(key);
    }
}
