/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.mutableaction;

import automater.TextValue;
import automater.input.InputDoNothing;
import automater.input.InputKey;
import automater.input.InputKeyClick;
import automater.input.InputKeyValue;
import automater.input.InputMouse;
import automater.input.InputMouseMotion;
import automater.input.InputMouseMove;
import automater.input.InputScreenshot;
import automater.input.InputSystemCommand;
import automater.utilities.Description;
import automater.work.BaseAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * List of mandatory and commonly used values for the standard BaseMutableAction implementation.
 *
 * @author Bytevi
 */
public class StandardMutableActionConstants {
    public static String MOUSE_KEY_REPLACEMENT_STRING = "MOUSE_";
    
    public static @NotNull List<Description> getActionTypes()
    {
        ArrayList<Description> types = new ArrayList<>();
        types.add(Description.createFromString(TextValue.getText(TextValue.EditAction_TypeDoNothing)));
        types.add(Description.createFromString(TextValue.getText(TextValue.EditAction_TypeWait)));
        types.add(Description.createFromString(TextValue.getText(TextValue.EditAction_TypeKeyboardClick)));
        types.add(Description.createFromString(TextValue.getText(TextValue.EditAction_TypeMouseClick)));
        types.add(Description.createFromString(TextValue.getText(TextValue.EditAction_TypeMouseMove)));
        types.add(Description.createFromString(TextValue.getText(TextValue.EditAction_TypeSystemCommand)));
        types.add(Description.createFromString(TextValue.getText(TextValue.EditAction_TypeScreenshot)));
        return types;
    }
    
    public static int getActionTypeSelectedIndex(@NotNull BaseAction action)
    {
        if (action instanceof InputKeyClick)
        {
            if (action instanceof InputMouse)
            {
                return 3;
            }
            
            return 2;
        }
        
        if (action instanceof InputMouseMotion)
        {
            return 4;
        }
        
        if (action instanceof InputDoNothing)
        {
            if (((InputDoNothing)action).getDuration() == 0)
            {
                return 0;
            }
            
            return 1;
        }
        
        if (action instanceof InputSystemCommand)
        {
            return 5;
        }
        
        if (action instanceof InputScreenshot)
        {
            return 6;
        }
        
        return 0;
    }
    
    public static MutableActionType getTypeFromAction(@NotNull BaseAction action)
    {
        MutableActionType type = MutableActionType.DoNothing;
        
        if (action instanceof InputKeyClick)
        {
            if (action instanceof InputMouse)
            {
                return MutableActionType.MouseKey;
            }
            
            return MutableActionType.KeyboardKey;
        }
        
        if (action instanceof InputMouseMove)
        {
            type = MutableActionType.MouseMove;
        }
        
        if (action instanceof InputMouseMotion)
        {
            type = MutableActionType.MouseMotion;
        }
        
        if (action instanceof InputDoNothing)
        {
            if (((InputDoNothing)action).getDuration() == 0)
            {
                return MutableActionType.DoNothing;
            }
            
            return MutableActionType.Wait;
        }
        
        if (action instanceof InputSystemCommand)
        {
            type = MutableActionType.SystemCommand;
        }
        
        if (action instanceof InputScreenshot)
        {
            type = MutableActionType.Screenshot;
        }
        
        return type;
    }
    
    public static @NotNull List<String> getMouseClickSpecificValues()
    {
        ArrayList<String> values = new ArrayList<>();
        
        values.add(getMouseClickSpecificValueForKeyValue(InputKeyValue._MOUSE_LEFT_CLICK));
        values.add(getMouseClickSpecificValueForKeyValue(InputKeyValue._MOUSE_RIGHT_CLICK));
        values.add(getMouseClickSpecificValueForKeyValue(InputKeyValue._MOUSE_MIDDLE_CLICK));
        
        return values;
    }
    
    public static @NotNull String getMouseClickSpecificValueForKeyValue(@NotNull InputKeyValue value)
    {
        String str;
        String prefix = MOUSE_KEY_REPLACEMENT_STRING;
        str = value.toString();
        str = str.replaceFirst(prefix, "");
        
        return str;
    }
    
    public static @Nullable InputKey getMouseKeyForTextValue(@NotNull String string)
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
