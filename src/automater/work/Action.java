/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.recorder.model.UserInputKeyClick;
import automater.recorder.model.UserInputMouseMove;
import automater.utilities.CollectionUtilities;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.Logger;
import static automater.work.Action.translateDateToActionTime;
import automater.work.model.ActionSystemKey;
import automater.work.model.ActionSystemKeyModifierValue;
import automater.work.model.ActionSystemKeyModifiers;
import automater.work.model.BaseActionContext;
import automater.work.parser.ActionKeyTranslator;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Simulates user actions such as keyboard and mouse clicks.
 *
 * @author Bytevi
 */
public class Action implements BaseAction, Description {
    public static Action createKeyClick(Date timestamp, UserInputKeyClick keyClick, Description description) throws Exception
    {
        if (keyClick.isPress())
        {
            return new ActionKeyPress(translateDateToActionTime(timestamp), keyClick, description);
        }
        
        return new ActionKeyRelease(translateDateToActionTime(timestamp), keyClick, description);
    }
    
    public static Action createMouseMovement(Date timestamp, int x, int y, Description description) throws Exception
    {
        return new ActionMouseMove(translateDateToActionTime(timestamp), x, y, description);
    }
    
    public static Action createMouseMovement(Date timestamp, List<UserInputMouseMove> mouseMovements, Description description) throws Exception
    {
        int maxNumberOfSubmovements = ActionSettingsManager.getDefault().getMaxNumberOfSubmovements();
        
        return new ActionMouseMovement(translateDateToActionTime(timestamp), mouseMovements, maxNumberOfSubmovements, description);
    }
    
    public static long translateDateToActionTime(Date timestamp)
    {
        return timestamp.getTime();
    }
    
    @Override
    public boolean isComplex()
    {
        return false;
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

class ActionKeyPress extends Action {
    long time;
    ActionSystemKey key;
    ActionSystemKeyModifiers modifiers;
    
    String standartDescription;
    String verboseDescription;
    
    ActionKeyPress(long time, UserInputKeyClick keyClick, Description description) throws Exception
    {
        this.time = time;
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
}

class ActionKeyRelease extends Action {
    long time;
    ActionSystemKey key;
    ActionSystemKeyModifiers modifiers;
    
    String standartDescription;
    String verboseDescription;
    
    ActionKeyRelease(long time, UserInputKeyClick keyClick, Description description) throws Exception
    {
        this.time = time;
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
}

class ActionMouseMove extends Action {
    final long time;
    final int x;
    final int y;
    
    String standartDescription;
    String verboseDescription;
    
    ActionMouseMove(long time, int x, int y, Description description)
    {
        this.time = time;
        this.x = x;
        this.y = y;
        
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
        context.getRobot().mouseMove(x, y);
    }
    
    @Override
    public String getStandart() {
        return standartDescription;
    }
    
    @Override
    public String getVerbose() {
        return verboseDescription;
    }
}

class ActionMouseMovement extends Action {
    final long time;
    final long duration;
    final List<BaseAction> actions;
    final int maxNumberOfSubmovements;
    
    String standartDescription;
    String verboseDescription;
    
    ActionMouseMovement(long time, List<UserInputMouseMove> movements, int maxNumberOfSubmovements, Description description)
    {
        this.time = time;
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
    
    private ArrayList<BaseAction> parseMouseMoveActions(List<UserInputMouseMove> movements)
    {
        final int size = movements.size();
        
        ArrayList<BaseAction> result = new ArrayList<>();
        BaseAction previousAction = null;
        
        for (int e = 0; e < size; e++)
        {
            UserInputMouseMove current = movements.get(e);
            UserInputMouseMove next = null;
            
            if (e + 1 < size)
            {
                next = movements.get(e+1);
            }
            
            ArrayList<BaseAction> subActions = parseMouseMoveAction(current, next);
            result.addAll(subActions);
        }
        
        return result;
    }
    
    private ArrayList<BaseAction> parseMouseMoveAction(UserInputMouseMove current, UserInputMouseMove next)
    {
        ArrayList<BaseAction> result = new ArrayList<>();
        
        final long currentStartTime = translateDateToActionTime(current.getTimestamp());
        final int currentX = current.getX();
        final int currentY = current.getY();
        
        // When next is null, create only one subaction
        if (next == null)
        {
            result.add(new ActionMouseMove(currentStartTime, currentX, currentY, null));
            return result;
        }
        
        final long nextStartTime = translateDateToActionTime(next.getTimestamp());
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
