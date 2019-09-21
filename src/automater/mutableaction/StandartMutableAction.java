/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.mutableaction;

import automater.TextValue;
import automater.input.InputDescriptions;
import automater.input.InputDoNothing;
import automater.input.InputKey;
import automater.input.InputKeyClick;
import automater.input.InputKeyValue;
import automater.input.InputMouse;
import automater.input.InputMouseMotion;
import automater.input.InputMouseMove;
import automater.utilities.CollectionUtilities;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.StringFormatting;
import automater.work.Action;
import automater.work.BaseAction;
import java.util.ArrayList;
import java.util.List;

/**
 * Standart implementation for BaseMutableAction.
 *
 * @author Bytevi
 */
public class StandartMutableAction implements BaseMutableAction {
    MutableActionType type;
    long timestamp;
    List<String> specificValues = new ArrayList<>();
    
    String firstName;
    String firstValue;
    String secondName;
    String secondValue;
    
    public static BaseMutableAction createDoNothing(long timestamp)
    {
        return new StandartMutableActionDoNothing(timestamp);
    }
    
    public static BaseMutableAction create(BaseAction action)
    {
        MutableActionType type = StandartMutableActionConstants.getTypeFromAction(action);
        long timestamp = action.getPerformTime();
        
        StandartMutableAction a = null;
        
        boolean isInputKeyClick = action instanceof InputKeyClick;
        boolean isInputKeyboardClick = !(action instanceof InputMouse);
        
        // DoNothing
        if (action instanceof InputDoNothing)
        {
            return createDoNothing(timestamp);
        }
        
        // KeyboardClick
        if (isInputKeyClick && isInputKeyboardClick)
        {
            InputKeyClick keyboardClick = (InputKeyClick)action;
            a = new StandartMutableActionKeyboardClick(type, timestamp);
            a.firstName = TextValue.getText(TextValue.EditAction_Key);
            a.firstValue = keyboardClick.getKeyValue().toString();
            a.secondName = TextValue.getText(TextValue.EditAction_Press);
            a.secondValue = String.valueOf(keyboardClick.isPress());
        }
        
        // MouseClick
        if (isInputKeyClick && !isInputKeyboardClick)
        {
            InputKeyClick mouseClick = (InputKeyClick)action;
            InputKeyValue keyValue = mouseClick.getKeyValue().value;
            a = new StandartMutableActionMouseClick(type, timestamp);
            a.firstName = TextValue.getText(TextValue.EditAction_Key);
            a.firstValue = StandartMutableActionConstants.getMouseClickSpecificValueForKeyValue(keyValue);
            a.secondName = TextValue.getText(TextValue.EditAction_Press);
            a.secondValue = String.valueOf(mouseClick.isPress());
            a.specificValues = StandartMutableActionConstants.getMouseClickSpecificValues();
        }
        
        // MouseMove
        if (action instanceof InputMouseMove)
        {
            InputMouseMove move = (InputMouseMove)action;
            
            a = new StandartMutableActionMouseMove(type, timestamp);
            a.firstName = TextValue.getText(TextValue.EditAction_X);
            a.firstValue = String.valueOf(move.getX());
            a.secondName = TextValue.getText(TextValue.EditAction_Y);
            a.secondValue = String.valueOf(move.getY());
        }
        
        // MouseMotion
        if (action instanceof InputMouseMotion)
        {
            InputMouseMotion inputMotion = (InputMouseMotion)action;
            StandartMutableActionMouseMotion motion;
            motion = new StandartMutableActionMouseMotion(type, timestamp, inputMotion.getMoves());
            a = motion;
            
            InputMouseMove lastMove = inputMotion.getLastMove();
            a.firstName = TextValue.getText(TextValue.EditAction_FinalX);
            a.firstValue = String.valueOf(lastMove.getX());
            a.secondName = TextValue.getText(TextValue.EditAction_FinalY);
            a.secondValue = String.valueOf(lastMove.getY());
        }
        
        if (a == null)
        {
            return null;
        }
        
        return a;
    }
    
    public StandartMutableAction(MutableActionType type, long timestamp)
    {
        this.type = type;
        this.timestamp = timestamp;
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        return null;
    }
    
    @Override
    public MutableActionType getType() {
        return type;
    }

    @Override
    public void setType(MutableActionType type) {
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
        return Description.createFromString("unknown");
    }
    
    @Override
    public String getStateDescription() {
        return "unknown";
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

class StandartMutableActionDoNothing extends StandartMutableAction implements InputDoNothing {
    public StandartMutableActionDoNothing(long timestamp)
    {
        super(MutableActionType.DoNothing, timestamp);
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        return Action.createDoNothing(timestamp);
    }
    
    @Override
    public Description getDescription() {
        Description d = InputDescriptions.getDoNothingDescription(timestamp);
        
        return d;
    }
    
   @Override
    public String getStateDescription() {
        return TextValue.getText(TextValue.EditAction_DescriptionDoNothing);
    }
}

class StandartMutableActionKeyboardClick extends StandartMutableAction {
    public StandartMutableActionKeyboardClick(MutableActionType type, long timestamp)
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
        
        Description d = InputDescriptions.getKeyboardInputDescription(getTimestamp(), isPress, inputKey);
        
        return d;
    }
    
   @Override
    public String getStateDescription() {
        try {
            buildAction();
        } catch (Exception e) {
            return TextValue.getText(TextValue.EditAction_StatusError, e.getMessage());
        }
        
        return TextValue.getText(TextValue.EditAction_DescriptionKeyboardClick);
    }
}

class StandartMutableActionMouseClick extends StandartMutableAction {
    public StandartMutableActionMouseClick(MutableActionType type, long timestamp)
    {
        super(type, timestamp);
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        InputKey inputKey = getMouseKey();
        
        if (inputKey == null)
        {
            return null;
        }
        
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
        InputKey inputKey = getMouseKey();
        
        if (inputKey == null)
        {
            return super.getDescription();
        }
        
        boolean isPress = Boolean.valueOf(secondValue);
        
        Description d = InputDescriptions.getMouseInputDescription(getTimestamp(), isPress, inputKey);
        
        return d;
    }
    
    @Override
    public String getStateDescription() {
        try {
            buildAction();
        } catch (Exception e) {
            return TextValue.getText(TextValue.EditAction_StatusError, e.getMessage());
        }
        
        return TextValue.getText(TextValue.EditAction_DescriptionMouseClick);
    }
    
    public InputKey getMouseKey()
    {
        return StandartMutableActionConstants.getMouseKeyForTextValue(firstValue);
    }
}

class StandartMutableActionMouseMove extends StandartMutableAction {
    public StandartMutableActionMouseMove(MutableActionType type, long timestamp)
    {
        super(type, timestamp);
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        if (!StringFormatting.isStringAnInt(firstValue) || !StringFormatting.isStringAnInt(secondValue))
        {
            Errors.throwInvalidArgument("Enter non-negative x,y values");
        }
        
        int x = Integer.parseInt(firstValue);
        int y = Integer.parseInt(secondValue);
        
        if (x < 0 || y < 0)
        {
            Errors.throwInvalidArgument("Enter non-negative x,y values");
        }
        
        return Action.createMouseMovement(timestamp, x, y, getDescription());
    }
    
    @Override
    public Description getDescription() {
        int x = Integer.parseInt(firstValue);
        int y = Integer.parseInt(secondValue);
        
        Description d = InputDescriptions.getMouseMoveDescription(timestamp, x, y);
        
        return d;
    }
    
    @Override
    public String getStateDescription() {
        try {
            buildAction();
        } catch (Exception e) {
            return TextValue.getText(TextValue.EditAction_StatusError, e.getMessage());
        }
        
        return TextValue.getText(TextValue.EditAction_DescriptionMouseMove);
    }
}

class StandartMutableActionMouseMotion extends StandartMutableActionMouseMove {
    List<InputMouseMove> moves;
    
    public StandartMutableActionMouseMotion(MutableActionType type, long timestamp, List<InputMouseMove> moves)
    {
        super(type, timestamp);
        this.moves = moves;
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        if (!StringFormatting.isStringAnInt(firstValue) || !StringFormatting.isStringAnInt(secondValue))
        {
            Errors.throwInvalidArgument("Enter non-negative x,y values");
        }
        
        int x = Integer.parseInt(firstValue);
        int y = Integer.parseInt(secondValue);
        
        if (x < 0 || y < 0)
        {
            Errors.throwInvalidArgument("Enter non-negative x,y values");
        }
        
        return Action.createMouseMovement(timestamp, moves, getDescription());
    }
    
    @Override
    public Description getDescription() {
        InputMouseMove first = moves.get(0);
        InputMouseMove last = moves.get(moves.size()-1);
        
        Description d = InputDescriptions.getMouseMotionDescription(timestamp, first, last, moves.size());
        
        return d;
    }
    
    @Override
    public String getStateDescription() {
        try {
            buildAction();
        } catch (Exception e) {
            return TextValue.getText(TextValue.EditAction_StatusError, e.getMessage());
        }
        
        return TextValue.getText(TextValue.EditAction_DescriptionMouseMotion, String.valueOf(moves.size()));
    }
}