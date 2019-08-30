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
        return new ActionMouseMovement(translateDateToActionTime(timestamp), x, y, description);
    }
    
    public static Action createMouseMovement(Date timestamp, List<UserInputMouseMove> mouseMovements, Description description) throws Exception
    {
        return new ActionMouseMovement(translateDateToActionTime(timestamp), mouseMovements, description);
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

class ActionMouseMovement extends Action {
    long time;
    List<UserInputMouseMove> movements;
    
    String standartDescription;
    String verboseDescription;
    
    ActionMouseMovement(long time, List<UserInputMouseMove> movements, Description description) throws Exception
    {
        this.time = time;
        this.movements = CollectionUtilities.copyAsImmutable(movements);
        
        if (description != null)
        {
            this.standartDescription = description.getStandart();
            this.verboseDescription = description.getVerbose();
        }
        
        for (UserInputMouseMove m : this.movements)
        {
            Logger.message(this, "UserInputMouseMove with " + m.getTimestamp().getTime());
        }
    }
    
    ActionMouseMovement(long time, int x, int y, Description description) throws Exception
    {
        this.time = time;
        
        List<UserInputMouseMove> move = new ArrayList<>();
        move.add(new UserInputMouseMove() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }

            @Override
            public Date getTimestamp() {
                return new Date(time);
            }
        });
        this.movements = move;
        
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
    public void perform(BaseActionContext context)
    {
        Logger.messageEvent(this, "Performing mouse motion action with " + String.valueOf(movements.size()) + " movements...");
        
        Robot robot = context.getRobot();
        
        ArrayList<UserInputMouseMove> movementsCopy = new ArrayList<>(movements);
        BaseExecutorTimer timer = context.getTimer();
        
        while (movementsCopy.size() > 0)
        {
            UserInputMouseMove next = movementsCopy.get(0);
            final long nextTime = translateDateToActionTime(next.getTimestamp());
            
            BaseAction action = new BaseAction() {
                @Override
                public boolean isComplex() {
                    return false;
                }

                @Override
                public long getPerformTime() {
                    return nextTime;
                }

                @Override
                public Description getDescription() {
                    return null;
                }

                @Override
                public void perform(BaseActionContext context) {
                    robot.mouseMove(next.getX(), next.getY());
                }
            };
            
            if (timer.canPerformNextAction(action))
            {
                Logger.messageEvent(this, "Perform subaction mouse move " + nextTime);
                
                action.perform(context);
                movementsCopy.remove(0);
            }
            else
            {
                try {
                    wait(10);
                } catch (Exception e) {
                    
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
}
