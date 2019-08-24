/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.recorder.model.UserInputKeyClick;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.work.model.ActionSystemKey;
import automater.work.model.BaseActionContext;
import automater.work.parser.ActionKeyTranslator;
import java.awt.Robot;
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
    
    String standartDescription;
    String verboseDescription;
    
    ActionKeyPress(long time, UserInputKeyClick keyClick, Description description) throws Exception
    {
        this.time = time;
        this.key = ActionKeyTranslator.translate(keyClick.getKeyValue());
        
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
        
        if (key.isKeyboardKey())
        {
            robot.keyPress(key.getValue());
        }
        
        if (key.isMouseKey())
        {
            robot.mousePress(key.getValue());
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
}

class ActionKeyRelease extends Action {
    long time;
    ActionSystemKey key;
    
    String standartDescription;
    String verboseDescription;
    
    ActionKeyRelease(long time, UserInputKeyClick keyClick, Description description) throws Exception
    {
        this.time = time;
        this.key = ActionKeyTranslator.translate(keyClick.getKeyValue());
        
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
        
        if (key.isKeyboardKey())
        {
            robot.keyRelease(key.getValue());
        }
        
        if (key.isMouseKey())
        {
            robot.mouseRelease(key.getValue());
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
}

class ActionMouseMovement extends Action {
    long time;
    int x;
    int y;
    
    String standartDescription;
    String verboseDescription;
    
    ActionMouseMovement(long time, int x, int y, Description description) throws Exception
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
    public long getPerformTime()
    {
        return time;
    }
    
    @Override
    public void perform(BaseActionContext context)
    {
        Robot robot = context.getRobot();
        
        robot.mouseMove(x, y);
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
