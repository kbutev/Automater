/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.input.InputDescriptions;
import automater.input.InputKey;
import automater.input.InputKeyClick;
import automater.input.InputMouse;
import automater.input.InputMouseMove;
import automater.utilities.CollectionUtilities;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.work.Action;
import automater.work.BaseAction;
import java.util.ArrayList;
import java.util.List;

/**
 * Standart EditableAction implementation.
 *
 * @author Bytevi
 */
public class StandartEditableAction implements BaseEditableAction {
    EditableActionType type;
    long timestamp;
    List<String> specificValues = new ArrayList<>();
    
    String firstName;
    String firstValue;
    String secondName;
    String secondValue;
    
    public static BaseEditableAction create(BaseAction action)
    {
        EditableActionType type = StandartEditableActionConstants.getTypeFromAction(action);
        long timestamp = action.getPerformTime();
        
        StandartEditableAction a = null;
        
        boolean isInputKeyClick = action instanceof InputKeyClick;
        boolean isInputKeyboardClick = !(action instanceof InputMouse);
        
        // KeyboardClick
        if (isInputKeyClick && isInputKeyboardClick)
        {
            InputKeyClick keyboardClick = (InputKeyClick)action;
            a = new StandartEditableActionKeyboard(type, timestamp);
            a.firstName = StandartEditableActionConstants.KEYBOARD_CLICK_FIRST_NAME;
            a.firstValue = keyboardClick.getKeyValue().toString();
            a.secondName = StandartEditableActionConstants.KEYBOARD_CLICK_SECOND_NAME;
            a.secondValue = String.valueOf(keyboardClick.isPress());
        }
        
        // MouseClick
        if (isInputKeyClick && !isInputKeyboardClick)
        {
            InputKeyClick mouseClick = (InputKeyClick)action;
            a = new StandartEditableActionMouse(type, timestamp);
            a.firstName = StandartEditableActionConstants.MOUSE_CLICK_FIRST_NAME;
            a.firstValue = mouseClick.getKeyValue().toString();
            a.secondName = StandartEditableActionConstants.MOUSE_CLICK_SECOND_NAME;
            a.secondValue = String.valueOf(mouseClick.isPress());
            a.specificValues = StandartEditableActionConstants.getMouseClickSpecificValues();
        }
        
        // MouseMove
        if (action instanceof InputMouseMove)
        {
            a = new StandartEditableAction(type, timestamp);
            a.firstName = StandartEditableActionConstants.MOUSE_MOVE_FIRST_NAME;
            a.secondName = StandartEditableActionConstants.MOUSE_MOVE_SECOND_NAME;
        }
        
        if (a == null)
        {
            return null;
        }
        
        return a;
    }
    
    public StandartEditableAction(EditableActionType type, long timestamp)
    {
        this.type = type;
        this.timestamp = timestamp;
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        return null;
    }
    
    @Override
    public EditableActionType getType() {
        return type;
    }

    @Override
    public void setType(EditableActionType type) {
        this.type = type;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public Description getDescription() {
        return null;
    }
    
    @Override
    public String getFirstValueName() {
        return firstName;
    }

    @Override
    public String getFirstValue() {
        return firstValue;
    }

    @Override
    public void setFirstValue(String value) {
        firstValue = value;
    }
    
    @Override
    public String getSecondValueName() {
        return secondName;
    }

    @Override
    public String getSecondValue() {
        return secondValue;
    }

    @Override
    public void setSecondValue(String value) {
        secondValue = value;
    }

    @Override
    public List<String> getPossibleSpecificValues() {
        return CollectionUtilities.copyAsImmutable(specificValues);
    }
}

class StandartEditableActionKeyboard extends StandartEditableAction {
    public StandartEditableActionKeyboard(EditableActionType type, long timestamp)
    {
        super(type, timestamp);
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        InputKey inputKey = new InputKey(firstValue);
        boolean isPress = Boolean.valueOf(secondValue);
        
        InputKeyClick keyClick = new InputKeyClick() {
            @Override
            public InputKey getKeyValue() {
                return inputKey;
            }

            @Override
            public boolean isPress() {
                return isPress;
            }

            @Override
            public long getTimestamp() {
                return timestamp;
            }
        };
        
        return Action.createKeyClick(timestamp, keyClick, getDescription());
    }
    
    @Override
    public Description getDescription() {
        InputKey inputKey = new InputKey(firstValue);
        boolean isPress = Boolean.valueOf(secondValue);
        
        return InputDescriptions.getKeyboardInputDescription(isPress, inputKey);
    }
}

class StandartEditableActionMouse extends StandartEditableAction {
    public StandartEditableActionMouse(EditableActionType type, long timestamp)
    {
        super(type, timestamp);
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        int x = Integer.parseInt(firstValue);
        int y = Integer.parseInt(secondValue);
        
        if (x < 0 || y < 0)
        {
            Errors.throwInvalidArgument("Enter non-negative x,y values");
        }
        
        return null;
    }
    
    @Override
    public Description getDescription() {
        InputKey inputKey = new InputKey(firstValue);
        boolean isPress = Boolean.valueOf(secondValue);
        
        return InputDescriptions.getKeyboardInputDescription(isPress, inputKey);
    }
}