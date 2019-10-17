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
import automater.input.InputScreenshot;
import automater.input.InputSystemCommand;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.StringFormatting;
import automater.work.Action;
import automater.work.BaseAction;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard implementation for BaseMutableAction.
 * 
 * Use buildAction() to retrieve a BaseAction object.
 *
 * @author Bytevi
 */
public class StandardMutableAction implements BaseMutableAction {
    public static final int MAX_WAIT_VALUE = 999999;
    public static final int MAX_MOVE_X_VALUE = 9999;
    public static final int MAX_MOVE_Y_VALUE = 9999;
    
    protected MutableActionType type;
    protected long timestamp;
    @NotNull protected ArrayList<BaseMutableActionProperty> properties = new ArrayList<>();
    
    public static StandardMutableAction createDoNothing(long timestamp)
    {
        return new StandartMutableActionDoNothing(timestamp);
    }
    
    public static StandardMutableAction createFromAction(@NotNull BaseAction action)
    {
        MutableActionType type = StandardMutableActionConstants.getTypeFromAction(action);
        long timestamp = action.getPerformTime();
        
        StandardMutableAction a;
        BaseMutableActionProperty property;
        
        boolean isInputKeyClick = action instanceof InputKeyClick;
        boolean isInputKeyboardClick = !(action instanceof InputMouse);
        
        // Do Nothing & Wait
        if (action instanceof InputDoNothing)
        {
            InputDoNothing input = (InputDoNothing)action;
            
            if (input.getDuration() == 0)
            {
                return createDoNothing(timestamp);
            }
            else
            {
                int duration = (int)input.getDuration();
                a = new StandartMutableActionWait(timestamp, duration);
                return a;
            }
            
        }
        
        // KeyboardClick
        if (isInputKeyClick && isInputKeyboardClick)
        {
            InputKeyClick keyboardClick = (InputKeyClick)action;
            a = new StandartMutableActionKeyboardClick(type, timestamp, keyboardClick);
            return a;
        }
        
        // MouseClick
        if (isInputKeyClick && !isInputKeyboardClick)
        {
            InputKeyClick mouseClick = (InputKeyClick)action;
            a = new StandartMutableActionMouseClick(type, timestamp, mouseClick);
            return a;
        }
        
        // MouseMove
        if (action instanceof InputMouseMove)
        {
            InputMouseMove move = (InputMouseMove)action;
            
            a = new StandartMutableActionMouseMove(type, timestamp, move);
            return a;
        }
        
        // MouseMotion
        if (action instanceof InputMouseMotion)
        {
            InputMouseMotion inputMotion = (InputMouseMotion)action;
            InputMouseMove lastMove = inputMotion.getLastMove();
            StandartMutableActionMouseMotion motion;
            motion = new StandartMutableActionMouseMotion(type, timestamp, inputMotion.getMoves(), lastMove);
            a = motion;
            return a;
        }
        
        // System command
        if (action instanceof InputSystemCommand)
        {
            InputSystemCommand inputCommand = (InputSystemCommand)action;
            
            a = new StandartMutableActionSystemCommand(type, timestamp, inputCommand.getValue(), inputCommand.reportsErrors());
            return a;
        }
        
        // Screenshot
        if (action instanceof InputScreenshot)
        {
            InputScreenshot inputScreenshot = (InputScreenshot)action;
            
            a = new StandartMutableActionScreenshot(type, timestamp, inputScreenshot.getPath());
            return a;
        }
        
        return null;
    }
    
    public StandardMutableAction(@NotNull MutableActionType type, long timestamp)
    {
        this.type = type;
        this.timestamp = timestamp;
    }
    
    @Override
    public @NotNull MutableActionType getType() {
        return type;
    }

    @Override
    public @NotNull List<BaseMutableActionProperty> getProperties() {
        return properties;
    }
    
    @Override
    public @NotNull BaseMutableActionProperty getFirstProperty() {
        return getProperties().get(0);
    }
    
    @Override
    public @NotNull BaseMutableActionProperty getSecondProperty() {
        return getProperties().get(1);
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
    public @NotNull Description getDescription() {
        return Description.createFromString("unknown");
    }
    
    @Override
    public @NotNull String getStateDescription() {
        return "unknown";
    }
    
    public BaseAction buildAction() throws Exception {
        return null;
    }
}

class StandartMutableActionDoNothing extends StandardMutableAction implements InputDoNothing {
    public StandartMutableActionDoNothing(long timestamp)
    {
        super(MutableActionType.DoNothing, timestamp);
    }
    
    @Override
    public @NotNull Description getDescription() {
        Description d = InputDescriptions.getDoNothingDescription(timestamp);
        
        return d;
    }
    
    @Override
    public @NotNull String getStateDescription() {
        return TextValue.getText(TextValue.EditAction_DescriptionDoNothing);
    }
    
    @Override
    public @NotNull BaseAction buildAction() throws Exception {
        return Action.createDoNothing(timestamp);
    }
    
    @Override
    public long getDuration() {
        return 0;
    }
}

class StandartMutableActionWait extends StandardMutableAction implements InputDoNothing {
    public StandartMutableActionWait(long timestamp, int wait)
    {
        super(MutableActionType.Wait, timestamp);
        
        String name = TextValue.getText(TextValue.EditAction_Wait);
        
        properties.add(StandardMutableActionProperties.createNonNegativeInt(name, wait, MAX_WAIT_VALUE));
    }
    
    @Override
    public @NotNull Description getDescription() {
        Description d = InputDescriptions.getWaitDescription(timestamp, getDuration());
        
        return d;
    }
    
    @Override
    public @NotNull String getStateDescription() {
        return TextValue.getText(TextValue.EditAction_DescriptionWait);
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        String value = getFirstProperty().getValue();
        
        if (!StringFormatting.isStringANonNegativeInt(value))
        {
            Errors.throwInvalidArgument("Enter a non-negative wait int");
        }
        
        long wait = Long.parseLong(value);
        return Action.createWait(timestamp, wait);
    }

    @Override
    public long getDuration() {
        String value = getFirstProperty().getValue();
        
        if (!StringFormatting.isStringANonNegativeInt(value))
        {
            return 0;
        }
        
        return Long.parseLong(value);
    }
}

class StandartMutableActionKeyboardClick extends StandardMutableAction {
    public StandartMutableActionKeyboardClick(@NotNull MutableActionType type, long timestamp, @NotNull InputKeyClick keyboardClick)
    {
        super(type, timestamp);
        
        String hotkeyName = TextValue.getText(TextValue.EditAction_Key);
        String hotkeyValue = keyboardClick.getKeyValue().toString();
        String isPressName = TextValue.getText(TextValue.EditAction_Press);
        boolean isPress = keyboardClick.isPress();
        
        properties.add(StandardMutableActionProperties.createHotkey(hotkeyName, hotkeyValue, isPress));
        properties.add(StandardMutableActionProperties.createBoolean(isPressName, isPress));
    }
    
    @Override
    public @NotNull Description getDescription() {
        InputKey inputKey = new InputKey(getFirstProperty().getValue());
        boolean isPress = Boolean.valueOf(getSecondProperty().getValue());
        
        Description d = InputDescriptions.getKeyboardInputDescription(getTimestamp(), isPress, inputKey);
        
        return d;
    }
    
    @Override
    public @NotNull String getStateDescription() {
        return TextValue.getText(TextValue.EditAction_DescriptionKeyboardClick);
    }
    
    @Override
    public @NotNull BaseAction buildAction() throws Exception {
        InputKey inputKey = new InputKey(getFirstProperty().getValue());
        boolean isPress = Boolean.valueOf(getSecondProperty().getValue());
        
        InputKeyClick keyClick = new InputKeyClick() {
            @Override
            public @NotNull InputKey getKeyValue() {
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
}

class StandartMutableActionMouseClick extends StandardMutableAction {
    public StandartMutableActionMouseClick(@NotNull MutableActionType type, long timestamp, @NotNull InputKeyClick mouseClick)
    {
        super(type, timestamp);
        
        InputKeyValue keyValue = mouseClick.getKeyValue().value;
        
        String keyName = TextValue.getText(TextValue.EditAction_Key);
        String keyDefaultValue = StandardMutableActionConstants.getMouseClickSpecificValueForKeyValue(keyValue);
        List<String> keyValues = StandardMutableActionConstants.getMouseClickSpecificValues();
        String isPressName = TextValue.getText(TextValue.EditAction_Press);
        boolean isPress = mouseClick.isPress();
        
        properties.add(StandardMutableActionProperties.createList(keyName, keyValues, keyDefaultValue));
        properties.add(StandardMutableActionProperties.createBoolean(isPressName, isPress));
    }
    
    @Override
    public @NotNull Description getDescription() {
        InputKey inputKey = getMouseKey();
        
        if (inputKey == null)
        {
            return super.getDescription();
        }
        
        boolean isPress = Boolean.valueOf(getSecondProperty().getValue());
        
        Description d = InputDescriptions.getMouseInputDescription(getTimestamp(), isPress, inputKey);
        
        return d;
    }
    
    @Override
    public @NotNull String getStateDescription() {
        return TextValue.getText(TextValue.EditAction_DescriptionMouseClick);
    }
    
    public @Nullable InputKey getMouseKey()
    {
        String selectedValue = getFirstProperty().getValue();
        return StandardMutableActionConstants.getMouseKeyForTextValue(selectedValue);
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        InputKey inputKey = getMouseKey();
        
        if (inputKey == null)
        {
            return null;
        }
        
        boolean isPress = Boolean.valueOf(getSecondProperty().getValue());
        
        InputKeyClick keyClick = new InputKeyClick() {
            @Override
            public @NotNull InputKey getKeyValue() {
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
}

class StandartMutableActionMouseMove extends StandardMutableAction {
    public StandartMutableActionMouseMove(@NotNull MutableActionType type, long timestamp, @NotNull InputMouseMove move)
    {
        super(type, timestamp);
        
        String xName = TextValue.getText(TextValue.EditAction_X);
        int xValue = move.getX();
        String yName = TextValue.getText(TextValue.EditAction_Y);
        int yValue = move.getY();
        
        properties.add(StandardMutableActionProperties.createNonNegativeInt(xName, xValue, MAX_MOVE_X_VALUE));
        properties.add(StandardMutableActionProperties.createNonNegativeInt(yName, yValue, MAX_MOVE_Y_VALUE));
    }
    
    @Override
    public @NotNull Description getDescription() {
        String sX = getFirstProperty().getValue();
        String sY = getSecondProperty().getValue();
        
        int x = Integer.parseInt(sX);
        int y = Integer.parseInt(sY);
        
        Description d = InputDescriptions.getMouseMoveDescription(timestamp, x, y);
        
        return d;
    }
    
    @Override
    public @NotNull String getStateDescription() {
        return TextValue.getText(TextValue.EditAction_DescriptionMouseMove);
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        String sX = getFirstProperty().getValue();
        String sY = getSecondProperty().getValue();
        
        if (!StringFormatting.isStringAnInt(sX) || !StringFormatting.isStringAnInt(sY))
        {
            Errors.throwInvalidArgument("Enter non-negative x,y values");
        }
        
        int x = Integer.parseInt(sX);
        int y = Integer.parseInt(sY);
        
        if (x < 0 || y < 0)
        {
            Errors.throwInvalidArgument("Enter non-negative x,y values");
        }
        
        return Action.createMouseMovement(timestamp, x, y, getDescription());
    }
}

class StandartMutableActionMouseMotion extends StandardMutableAction {
    long originalTimestamp;
    long originalTimestampForLastMove;
    @NotNull List<InputMouseMove> moves;
    
    public StandartMutableActionMouseMotion(@NotNull MutableActionType type, long timestamp, @NotNull List<InputMouseMove> moves, @NotNull InputMouseMove lastMove)
    {
        super(type, timestamp);
        
        this.originalTimestamp = timestamp;
        this.originalTimestampForLastMove = moves.get(moves.size()-1).getTimestamp();
        this.moves = moves;
        
        String xName = TextValue.getText(TextValue.EditAction_FinalX);
        int xValue = lastMove.getX();
        String yName = TextValue.getText(TextValue.EditAction_FinalY);
        int yValue = lastMove.getY();
        
        properties.add(StandardMutableActionProperties.createNonNegativeInt(xName, xValue, MAX_MOVE_X_VALUE));
        properties.add(StandardMutableActionProperties.createNonNegativeInt(yName, yValue, MAX_MOVE_Y_VALUE));
    }
    
    @Override
    public @NotNull Description getDescription() {
        InputMouseMove first = moves.get(0);
        InputMouseMove last = getEnteredLastMove(0);
        
        if (last == null)
        {
            return InputDescriptions.getMouseMoveDescription(timestamp, first.getX(), first.getY());
        }
        
        Description d = InputDescriptions.getMouseMotionDescription(timestamp, first, last, moves.size());
        
        return d;
    }
    
    @Override
    public @NotNull String getStateDescription() {
        return TextValue.getText(TextValue.EditAction_DescriptionMouseMotion, String.valueOf(moves.size()));
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        String sX = getFirstProperty().getValue();
        String sY = getSecondProperty().getValue();
        
        if (!StringFormatting.isStringAnInt(sX) || !StringFormatting.isStringAnInt(sY))
        {
            Errors.throwInvalidArgument("Enter non-negative x,y values");
        }
        
        int x = Integer.parseInt(sX);
        int y = Integer.parseInt(sY);
        
        if (x < 0 || y < 0)
        {
            Errors.throwInvalidArgument("Enter non-negative x,y values");
        }
        
        // Create new moves based on the difference if the new timestamp (may be 0)
        // Create new last move input and remove old last move input
        long timestampOffset = timestamp - originalTimestamp;
        
        InputMouseMove last = getEnteredLastMove(timestampOffset);
        
        if (last == null)
        {
            Errors.throwInvalidArgument("Enter valid x,y values");
        }
        
        ArrayList<InputMouseMove> newMoves = getNewMovesWithTimestampOffset(timestampOffset);
        newMoves.remove(newMoves.size()-1);
        newMoves.add(last);
        
        return Action.createMouseMovement(timestamp, newMoves, getDescription());
    }
    
    private @Nullable InputMouseMove getEnteredLastMove(long timestampOffset)
    {
        String sX = getFirstProperty().getValue();
        String sY = getSecondProperty().getValue();
        
        int x;
        int y;
        final long lastTimestamp = originalTimestampForLastMove + timestampOffset;
        
        if (StringFormatting.isStringAnInt(sX) && StringFormatting.isStringAnInt(sY))
        {
            x = Integer.parseInt(sX);
            y = Integer.parseInt(sY);
        }
        else
        {
            x = 0;
            y = 0;
        }
        
        Description description = InputDescriptions.getMouseMoveDescription(lastTimestamp, x, y);
        
        try {
            return (InputMouseMove)Action.createMouseMovement(lastTimestamp, x, y, description);
        } catch (Exception e) {
            
        }
        
        return null;
    }
    
    private @NotNull ArrayList<InputMouseMove> getNewMovesWithTimestampOffset(long timestampOffset) throws Exception
    {
        ArrayList<InputMouseMove> newMoves = new ArrayList<>();
        
        for (InputMouseMove oldMove : this.moves)
        {
            long updatedTimestamp = oldMove.getTimestamp() + timestampOffset;
            int x = oldMove.getX();
            int y = oldMove.getY();
            
            Description description = InputDescriptions.getMouseMoveDescription(updatedTimestamp, x, y);
            
            InputMouseMove newMove;
            newMove = (InputMouseMove)Action.createMouseMovement(updatedTimestamp, x, y, description);
            
            newMoves.add(newMove);
        }
        
        return newMoves;
    }
}

class StandartMutableActionSystemCommand extends StandardMutableAction {
    public StandartMutableActionSystemCommand(@NotNull MutableActionType type, long timestamp, @NotNull String value, boolean reportsErrors)
    {
        super(type, timestamp);
        
        String firstName = TextValue.getText(TextValue.EditAction_Command);
        String secondName = TextValue.getText(TextValue.EditAction_ReportsErrors);
        
        properties.add(StandardMutableActionProperties.createString(firstName, value, 255));
        properties.add(StandardMutableActionProperties.createBoolean(secondName, reportsErrors));
    }
    
    @Override
    public @NotNull Description getDescription() {
        Description d = InputDescriptions.getSystemCommand(timestamp, getFirstProperty().getValue());
        
        return d;
    }
    
    @Override
    public @NotNull String getStateDescription() {
        return TextValue.getText(TextValue.EditAction_DescriptionSystemCommand);
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        String commandValue = getFirstProperty().getValue();
        boolean reportsErrors = Boolean.parseBoolean(getSecondProperty().getValue());
        
        Description d = InputDescriptions.getSystemCommand(timestamp, commandValue);
        
        return Action.createSystemCommand(timestamp, commandValue, reportsErrors, d);
    }
}

class StandartMutableActionScreenshot extends StandardMutableAction {
    public StandartMutableActionScreenshot(@NotNull MutableActionType type, long timestamp, @NotNull String path)
    {
        super(type, timestamp);
        
        String firstName = TextValue.getText(TextValue.EditAction_Path);
        
        properties.add(StandardMutableActionProperties.createString(firstName, path, 255));
    }
    
    @Override
    public @NotNull Description getDescription() {
        Description d = InputDescriptions.getScreenshot(timestamp, getFirstProperty().getValue());
        
        return d;
    }
    
    @Override
    public @NotNull String getStateDescription() {
        return TextValue.getText(TextValue.EditAction_DescriptionScreenshot);
    }
    
    @Override
    public BaseAction buildAction() throws Exception {
        String path = getFirstProperty().getValue();
        
        Description d = InputDescriptions.getScreenshot(timestamp, path);
        
        return Action.createScreenshot(timestamp, path, d);
    }
}
