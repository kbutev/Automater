/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.TextValue;
import automater.input.InputDescriptions;
import automater.input.InputDoNothing;
import automater.utilities.CollectionUtilities;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.work.model.ActionSystemKey;
import automater.work.model.ActionSystemKeyModifierValue;
import automater.work.model.ActionSystemKeyModifiers;
import automater.work.model.BaseActionContext;
import automater.work.parser.ActionKeyTranslator;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;
import automater.input.InputKeyClick;
import automater.input.InputMouseMotion;
import automater.input.InputMouseMove;
import automater.input.InputKey;
import automater.input.InputKeyValue;
import automater.input.InputMouse;
import automater.input.InputMouseWheel;
import automater.input.InputSystemCommand;
import automater.utilities.DeviceNotifications;

/**
 * Simulates user actions such as keyboard and mouse clicks.
 *
 * @author Bytevi
 */
public class Action extends BaseAction {
    public static Action createDoNothing(long timestamp)
    {
        return new ActionDoNothing(timestamp);
    }
    
    public static Action createWait(long timestamp, long milliseconds)
    {
        return new ActionWait(timestamp, milliseconds);
    }
    
    public static Action createKeyClick(long timestamp, InputKeyClick keyClick, Description description) throws Exception
    {
        boolean isMouseClick = false;
        InputKeyValue key = keyClick.getKeyValue().value;
        
        if (key == InputKeyValue._MOUSE_LEFT_CLICK || 
                key == InputKeyValue._MOUSE_RIGHT_CLICK || 
                key == InputKeyValue._MOUSE_MIDDLE_CLICK)
        {
            isMouseClick = true;
        }
        
        if (isMouseClick)
        {
            if (keyClick.isPress())
            {
                return new ActionMouseKeyPress(timestamp, keyClick, description);
            }
            
            return new ActionMouseKeyRelease(timestamp, keyClick, description);
        }
        
        if (keyClick.isPress())
        {
            return new ActionKeyPress(timestamp, keyClick, description);
        }
        
        return new ActionKeyRelease(timestamp, keyClick, description);
    }
    
    public static Action createMouseMovement(long timestamp, int x, int y, Description description) throws Exception
    {
        return new ActionMouseMove(timestamp, x, y, description);
    }
    
    public static Action createMouseMovement(long timestamp, List<InputMouseMove> mouseMovements, Description description) throws Exception
    {
        int maxNumberOfSubmovements = ActionSettingsManager.getDefault().getMaxNumberOfSubmovements();
        
        return new ActionMouseMovement(timestamp, mouseMovements, maxNumberOfSubmovements, description);
    }
    
    public static Action createMouseWheel(long timestamp, int scrollValue, Description description) throws Exception
    {
        return new ActionMouseWheel(timestamp, scrollValue, description);
    }
    
    public static Action createSystemCommand(long timestamp, String value, boolean reportsErrors, Description description) throws Exception
    {
        return new ActionSystemCommand(timestamp, value, reportsErrors, description);
    }
    
    @Override
    public boolean isComplex()
    {
        return getWaitTime() > 0;
    }
    
    @Override
    public long getWaitTime()
    {
        return 0;
    }
    
    @Override
    public long getPerformTime()
    {
        Errors.throwNotImplemented("Action getPerformTime method has not been implemented!");
        return 0;
    }
    
    @Override
    public long getPerformDuration()
    {
        return 0;
    }
    
    @Override
    public long getPerformEndTime()
    {
        return getPerformTime();
    }
    
    @Override
    public Description getDescription()
    {
        return this;
    }
    
    @Override
    public void perform(BaseActionContext context)
    {
        Errors.throwNotImplemented("Action perform method has not been implemented!");
    }

    @Override
    public String getStandart() {
        return toString();
    }

    @Override
    public String getVerbose() {
        return getStandart();
    }

    @Override
    public String getStandartTooltip() {
        return getStandart();
    }

    @Override
    public String getVerboseTooltip() {
        return getStandart();
    }

    @Override
    public String getName() {
        return getStandart();
    }

    @Override
    public String getDebug() {
        return getVerbose();
    }
}

class ActionDoNothing extends Action implements InputDoNothing {
    long time;
    Description description;
    
    ActionDoNothing(long time)
    {
        this.time = time;
        this.description = InputDescriptions.getDoNothingDescription(time);
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(BaseActionContext context)
    {
        
    }
    
    @Override
    public String getStandart() {
        return description.getStandart();
    }
    
    @Override
    public String getVerbose() {
        return description.getVerbose();
    }
    
    @Override
    public long getTimestamp() {
        return time;
    }

    @Override
    public long getDuration() {
        return 0;
    }
}

class ActionWait extends Action implements InputDoNothing {
    long time;
    long wait;
    Description description;
    
    ActionWait(long time, long wait)
    {
        this.time = time;
        this.wait = wait;
        this.description = InputDescriptions.getWaitDescription(time, wait);
    }
    
    @Override
    public long getWaitTime()
    {
        return wait;
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(BaseActionContext context)
    {
        double waitTime = wait;
        waitTime /= context.getTimer().getTimeScale();
        
        long actualWaitTime = (long)waitTime;
        
        try {
            Thread.sleep(actualWaitTime);
        } catch (Exception e) {
            
        }
    }
    
    @Override
    public String getStandart() {
        return description.getStandart();
    }
    
    @Override
    public String getVerbose() {
        return description.getVerbose();
    }
    
    @Override
    public long getTimestamp() {
        return time;
    }
    
    @Override
    public long getDuration() {
        return wait;
    }
}

class ActionKeyPress extends Action implements InputKeyClick {
    long time;
    InputKey inputKey;
    ActionSystemKey key;
    ActionSystemKeyModifiers modifiers;
    
    String standartDescription;
    String verboseDescription;
    
    ActionKeyPress(long time, InputKeyClick keyClick, Description description) throws Exception
    {
        this.time = time;
        this.inputKey = keyClick.getKeyValue();
        this.key = ActionKeyTranslator.translateKeystroke(keyClick.getKeyValue());
        this.modifiers = ActionKeyTranslator.translateModifiers(keyClick.getKeyValue());
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(BaseActionContext context)
    {
        Robot robot = context.getRobot();
        
        // First, simulate press modifier keys
        for (ActionSystemKeyModifierValue value : modifiers.modifiers)
        {
            if (!context.isModifierPressed(value))
            {
                robot.keyPress(value.getValue());
            }
        }
        
        // Second, simulate press keystroke
        if (!context.isKeyPressed(key))
        {
            if (key.isKeyboardKey())
            {
                robot.keyPress(key.getValue());
            }
            
            if (key.isMouseKey())
            {
                robot.mousePress(key.getValue());
            }
        }
        
        // Finally, alert context
        context.onPressKey(key, modifiers);
    }
    
    @Override
    public String getStandart() {
        return standartDescription;
    }
    
    @Override
    public String getVerbose() {
        return verboseDescription;
    }

    @Override
    public InputKey getKeyValue() {
        return inputKey;
    }

    @Override
    public boolean isPress() {
        return true;
    }

    @Override
    public long getTimestamp() {
        return time;
    }
}

class ActionKeyRelease extends Action implements InputKeyClick {
    long time;
    InputKey inputKey;
    ActionSystemKey key;
    ActionSystemKeyModifiers modifiers;
    
    String standartDescription;
    String verboseDescription;
    
    ActionKeyRelease(long time, InputKeyClick keyClick, Description description) throws Exception
    {
        this.time = time;
        this.inputKey = keyClick.getKeyValue();
        this.key = ActionKeyTranslator.translateKeystroke(keyClick.getKeyValue());
        this.modifiers = ActionKeyTranslator.translateModifiers(keyClick.getKeyValue());
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(BaseActionContext context)
    {
        Robot robot = context.getRobot();
        
        // First, simulate release modifier keys
        for (ActionSystemKeyModifierValue value : modifiers.modifiers)
        {
            if (context.isModifierPressed(value))
            {
                robot.keyRelease(value.getValue());
            }
        }
        
        // Second, simulate release keystroke
        if (context.isKeyPressed(key))
        {
            if (key.isKeyboardKey())
            {
                robot.keyRelease(key.getValue());
            }
            
            if (key.isMouseKey())
            {
                robot.mouseRelease(key.getValue());
            }
        }
        
        // Finally, alert context
        context.onReleaseKey(key, modifiers);
    }
    
    @Override
    public String getStandart() {
        return standartDescription;
    }
    
    @Override
    public String getVerbose() {
        return verboseDescription;
    }

    @Override
    public InputKey getKeyValue() {
        return inputKey;
    }

    @Override
    public boolean isPress() {
        return false;
    }

    @Override
    public long getTimestamp() {
        return time;
    }
}

class ActionMouseKeyPress extends ActionKeyPress implements InputMouse {
    ActionMouseKeyPress(long time, InputKeyClick keyClick, Description description) throws Exception
    {
        super(time, keyClick, description);
    }
}

class ActionMouseKeyRelease extends ActionKeyRelease implements InputMouse {
    ActionMouseKeyRelease(long time, InputKeyClick keyClick, Description description) throws Exception
    {
        super(time, keyClick, description);
    }
}

class ActionMouseMove extends Action implements InputMouseMove {
    final long time;
    final int x;
    final int y;
    
    String standartDescription;
    String verboseDescription;
    
    ActionMouseMove(long time, int x, int y, Description description)
    {
        this.time = time;
        this.x = x > 0 ? x : 0;
        this.y = y > 0 ? y : 0;
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public boolean isComplex()
    {
        return false;
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(BaseActionContext context)
    {
        float screenScaleX = context.getCurrentScreenSize().width;
        screenScaleX /= context.getRecordedScreenSize().width;
        float screenScaleY = context.getCurrentScreenSize().height;
        screenScaleY /= context.getRecordedScreenSize().height;
        
        int resultX = (int)(x * screenScaleX);
        int resultY = (int)(y * screenScaleY);
        
        context.getRobot().mouseMove(resultX, resultY);
    }
    
    @Override
    public String getStandart() {
        return standartDescription;
    }
    
    @Override
    public String getVerbose() {
        return verboseDescription;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public long getTimestamp() {
        return time;
    }
}

class ActionMouseMovement extends Action implements InputMouseMotion {
    final long time;
    final long duration;
    final List<InputMouseMove> movements;
    final List<BaseAction> actions;
    final int maxNumberOfSubmovements;
    
    String standartDescription;
    String verboseDescription;
    
    ActionMouseMovement(long time, List<InputMouseMove> movements, int maxNumberOfSubmovements, Description description)
    {
        this.time = time;
        this.movements = movements;
        this.actions = parseMouseMoveActions(CollectionUtilities.copyAsImmutable(movements));
        long lastPerformTime = this.actions.get(this.actions.size()-1).getPerformTime();
        this.duration = lastPerformTime - time;
        this.maxNumberOfSubmovements = maxNumberOfSubmovements;
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public boolean isComplex()
    {
        return true;
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public long getPerformDuration()
    {
        return duration;
    }
    
    @Override
    public long getPerformEndTime()
    {
        return time + duration;
    }
    
    @Override
    public void perform(BaseActionContext context)
    {
        Logger.messageEvent(this, "Performing mouse motion action with " + String.valueOf(actions.size()) + " movements...");
        
        BaseExecutorTimer timer = context.getTimer();
        
        for (int e = 0; e < actions.size(); )
        {
            BaseAction action = actions.get(e);
            
            if (timer.canPerformNextAction(action))
            {
                action.perform(context);
                e++;
            }
            else
            {
                try {
                    wait(2);
                } catch (Exception exc) {
                    
                }
            }
        }
        
        Logger.messageEvent(this, "Finished mouse motion action");
    }
    
    @Override
    public String getStandart() {
        return standartDescription;
    }
    
    @Override
    public String getVerbose() {
        return verboseDescription;
    }
    
    @Override
    public int numberOfMovements() {
        return movements.size();
    }
    
    @Override
    public List<InputMouseMove> getMoves() {
        return CollectionUtilities.copyAsImmutable(movements);
    }

    @Override
    public InputMouseMove getFirstMove() {
        return movements.get(0);
    }

    @Override
    public InputMouseMove getLastMove() {
        return movements.get(movements.size()-1);
    }

    @Override
    public InputMouseMove getMoveAt(int index) {
        return movements.get(index);
    }

    @Override
    public long getTimestamp() {
        return time;
    }
    
    private ArrayList<BaseAction> parseMouseMoveActions(List<InputMouseMove> movements)
    {
        final int size = movements.size();
        
        ArrayList<BaseAction> result = new ArrayList<>();
        BaseAction previousAction = null;
        
        for (int e = 0; e < size; e++)
        {
            InputMouseMove current = movements.get(e);
            InputMouseMove next = null;
            
            if (e + 1 < size)
            {
                next = movements.get(e+1);
            }
            
            ArrayList<BaseAction> subActions = parseMouseMoveAction(current, next);
            result.addAll(subActions);
        }
        
        return result;
    }
    
    private ArrayList<BaseAction> parseMouseMoveAction(InputMouseMove current, InputMouseMove next)
    {
        ArrayList<BaseAction> result = new ArrayList<>();
        
        final long currentStartTime = current.getTimestamp();
        final int currentX = current.getX();
        final int currentY = current.getY();
        
        // When next is null, create only one subaction
        if (next == null)
        {
            result.add(new ActionMouseMove(currentStartTime, currentX, currentY, null));
            return result;
        }
        
        final long nextStartTime = next.getTimestamp();
        final int nextX = next.getX();
        final int nextY = next.getY();
        
        // Break down the move action in multiple actions, so we can achieve smooth movement
        final long timeDiff = diff(currentStartTime, nextStartTime);
        final int xDiff = diff(currentX, nextX);
        final int yDiff = diff(currentY, nextY);
        
        result.add(new ActionMouseMove(currentStartTime, currentX, currentY, null));
        
        for (int e = maxNumberOfSubmovements; e > 0; e--)
        {
            final long startTime = currentStartTime + (timeDiff / maxNumberOfSubmovements);
            final int x = currentX + (xDiff / maxNumberOfSubmovements);
            final int y = currentY + (yDiff / maxNumberOfSubmovements);
            actions.add(new ActionMouseMove(startTime, x, y, null));
        }
        
        return result;
    }
    
    private int diff(int a, int b)
    {
        int result = a - b;
        
        if (result < 0)
        {
            result *= -1;
        }
        
        return result;
    }
    
    private long diff(long a, long b)
    {
        long result = a - b;
        
        if (result < 0)
        {
            result *= -1;
        }
        
        return result;
    }
}

class ActionMouseWheel extends Action implements InputMouseWheel {
    final long time;
    final int scrollValue;
    
    String standartDescription;
    String verboseDescription;
    
    ActionMouseWheel(long time, int scrollValue, Description description)
    {
        this.time = time;
        this.scrollValue = scrollValue;
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public boolean isComplex()
    {
        return false;
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(BaseActionContext context)
    {
        int amount = convertScrollWheelValueToRobotWheelValue(scrollValue);
        context.getRobot().mouseWheel(amount);
    }
    
    @Override
    public String getStandart() {
        return standartDescription;
    }
    
    @Override
    public String getVerbose() {
        return verboseDescription;
    }

    @Override
    public int getScrollValue() {
        return scrollValue;
    }
    
    @Override
    public long getTimestamp() {
        return time;
    }
    
    private int convertScrollWheelValueToRobotWheelValue(int value)
    {
        return (value / -2);
    }
}

class ActionSystemCommand extends Action implements InputSystemCommand {
    final long time;
    
    final String value;
    final boolean reportsErrors;
    
    String standartDescription;
    String verboseDescription;
    
    ActionSystemCommand(long time, String value, boolean reportsErrors, Description description)
    {
        this.time = time;
        this.value = value;
        this.reportsErrors = reportsErrors;
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
    }
    
    @Override
    public boolean isComplex()
    {
        return false;
    }
    
    @Override
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(BaseActionContext context)
    {
        try {
            Runtime.getRuntime().exec(value);
        } catch (Exception e) {
            Logger.warning(this, "Failed to perform command '" + value + "' because " + e.getMessage());
            
            if (reportsErrors)
            {
                String title = TextValue.getText(TextValue.Commands_NotificationErrorTitle, value);
                String message = TextValue.getText(TextValue.Commands_NotificationErrorMessage, e.getMessage());
                
                DeviceNotifications.getShared().displayOSNotification(title, message, message);
            }
        }
    }
    
    @Override
    public String getStandart() {
        return standartDescription;
    }
    
    @Override
    public String getVerbose() {
        return verboseDescription;
    }
    
    @Override
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean reportsErrors() {
        return reportsErrors;
    }
}
