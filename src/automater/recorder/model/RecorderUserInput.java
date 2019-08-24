/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

import static automater.recorder.model.RecorderUserInputKeyValue.*;
import automater.utilities.DateUtilities;
import automater.utilities.Description;
import automater.utilities.Logger;
import java.io.Serializable;
import java.util.Date;

/**
 * Encapsulates user input information.
 * 
 * @author Bytevi
 */
public class RecorderUserInput implements Serializable, Description {
    private static final RecorderUserInput _logInstance = new RecorderUserInput();
    
    public final Date timestamp;
    
    private RecorderUserInput()
    {
        timestamp = null;
    }
    
    public static RecorderUserInput createKeyboardPress(Date timestamp, RecorderUserInputKey key)
    {
        return new RecorderUserInputKeyboardPress(timestamp, key);
    }
    
    public static RecorderUserInput createKeyboardRelease(Date timestamp, RecorderUserInputKey key)
    {
        return new RecorderUserInputKeyboardRelease(timestamp, key);
    }
    
    public static RecorderUserInput createMousePress(Date timestamp, RecorderUserInputKey mouseKey)
    {
        switch (mouseKey.value)
        {
            case _MOUSE_LEFT_CLICK:
                return RecorderUserInputMousePress.createLeftMouse(timestamp, mouseKey.mask);
            case _MOUSE_RIGHT_CLICK:
                return RecorderUserInputMousePress.createRightMouse(timestamp, mouseKey.mask);
            case _MOUSE_MIDDLE_CLICK:
                return RecorderUserInputMousePress.createMiddleMouse(timestamp, mouseKey.mask);
            case _MOUSE_4_CLICK:
                return RecorderUserInputMousePress.create4Mouse(timestamp, mouseKey.mask);
            case _MOUSE_5_CLICK:
                return RecorderUserInputMousePress.create5Mouse(timestamp, mouseKey.mask);
        }
        
        Logger.warning(_logInstance, "Cannot create mouse press input from given mouse key " + mouseKey.toString());
        
        return null;
    }
    
    public static RecorderUserInput createMouseRelease(Date timestamp, RecorderUserInputKey mouseKey)
    {
        switch (mouseKey.value)
        {
            case _MOUSE_LEFT_CLICK:
                return RecorderUserInputMouseRelease.createLeftMouse(timestamp, mouseKey.mask);
            case _MOUSE_RIGHT_CLICK:
                return RecorderUserInputMouseRelease.createRightMouse(timestamp, mouseKey.mask);
            case _MOUSE_MIDDLE_CLICK:
                return RecorderUserInputMouseRelease.createMiddleMouse(timestamp, mouseKey.mask);
            case _MOUSE_4_CLICK:
                return RecorderUserInputMouseRelease.create4Mouse(timestamp, mouseKey.mask);
            case _MOUSE_5_CLICK:
                return RecorderUserInputMouseRelease.create5Mouse(timestamp, mouseKey.mask);
        }
        
        Logger.warning(_logInstance, "Cannot create mouse release input from given mouse key " + mouseKey.toString());
        
        return null;
    }
     
    public static RecorderUserInput createMouseMotion(Date timestamp, int x, int y)
    {
        return new RecorderUserInputMouseMotion(timestamp, x, y);
    }
    
    public static RecorderUserInput createMouseWheel(Date timestamp)
    {
        return new RecorderUserInputMouseWheel(timestamp);
    }
    
    public static RecorderUserInput createWindow(Date timestamp)
    {
        return new RecorderUserInputWindow(timestamp);
    }
    
    public RecorderUserInput(Date timestamp)
    {
        this.timestamp = timestamp;
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return "unknown";
    }

    @Override
    public String getVerbose() {
        return getStandart();
    }

    @Override
    public String getStandartTooltip() {
        return "";
    }

    @Override
    public String getVerboseTooltip() {
        return "";
    }

    @Override
    public String getName() {
        return DateUtilities.asHourMinuteSecondMilisecond(timestamp);
    }

    @Override
    public String getDebug() {
        return getVerbose();
    }
}

class RecorderUserInputKeyboardPress extends RecorderUserInput implements UserInputKeyClick
{
    public final RecorderUserInputKey key;
    
    RecorderUserInputKeyboardPress(Date timestamp, RecorderUserInputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return "KeyboardPress " + "'" + key.toString() + "'";
    }
    
    // # UserInputKeyClick

    @Override
    public RecorderUserInputKey getKeyValue() {
        return this.key;
    }

    @Override
    public boolean isPress() {
        return true;
    }
}

class RecorderUserInputKeyboardRelease extends RecorderUserInput implements UserInputKeyClick
{
    public final RecorderUserInputKey key;
    
    RecorderUserInputKeyboardRelease(Date timestamp, RecorderUserInputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return "KeyboardRelease " + "'" + key.toString() + "'";
    }

    // # UserInputKeyClick
    
    @Override
    public RecorderUserInputKey getKeyValue() {
        return this.key;
    }

    @Override
    public boolean isPress() {
        return false;
    }
}

class RecorderUserInputMousePress extends RecorderUserInput implements UserInputKeyClick, UserInputMouse
{
    public final RecorderUserInputKey key;
    
    static RecorderUserInput createLeftMouse(Date timestamp, RecorderUserInputKeyMask mask)
    {
        RecorderUserInputKey key = new RecorderUserInputKey(_MOUSE_LEFT_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static RecorderUserInput createRightMouse(Date timestamp, RecorderUserInputKeyMask mask)
    {
        RecorderUserInputKey key = new RecorderUserInputKey(_MOUSE_RIGHT_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static RecorderUserInput createMiddleMouse(Date timestamp, RecorderUserInputKeyMask mask)
    {
        RecorderUserInputKey key = new RecorderUserInputKey(_MOUSE_MIDDLE_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static RecorderUserInput create4Mouse(Date timestamp, RecorderUserInputKeyMask mask)
    {
        RecorderUserInputKey key = new RecorderUserInputKey(_MOUSE_4_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static RecorderUserInput create5Mouse(Date timestamp, RecorderUserInputKeyMask mask)
    {
        RecorderUserInputKey key = new RecorderUserInputKey(_MOUSE_5_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    RecorderUserInputMousePress(Date timestamp, RecorderUserInputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return "MousePress " + "'" + key.toString() + "'";
    }
    
    // # UserInputKeyClick

    @Override
    public RecorderUserInputKey getKeyValue() {
        return this.key;
    }

    @Override
    public boolean isPress() {
        return true;
    }
}

class RecorderUserInputMouseRelease extends RecorderUserInput implements UserInputKeyClick, UserInputMouse
{
    public final RecorderUserInputKey key;
    
    static RecorderUserInput createLeftMouse(Date timestamp, RecorderUserInputKeyMask mask)
    {
        RecorderUserInputKey key = new RecorderUserInputKey(_MOUSE_LEFT_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static RecorderUserInput createRightMouse(Date timestamp, RecorderUserInputKeyMask mask)
    {
        RecorderUserInputKey key = new RecorderUserInputKey(_MOUSE_RIGHT_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static RecorderUserInput createMiddleMouse(Date timestamp, RecorderUserInputKeyMask mask)
    {
        RecorderUserInputKey key = new RecorderUserInputKey(_MOUSE_MIDDLE_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static RecorderUserInput create4Mouse(Date timestamp, RecorderUserInputKeyMask mask)
    {
        RecorderUserInputKey key = new RecorderUserInputKey(_MOUSE_4_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static RecorderUserInput create5Mouse(Date timestamp, RecorderUserInputKeyMask mask)
    {
        RecorderUserInputKey key = new RecorderUserInputKey(_MOUSE_5_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    RecorderUserInputMouseRelease(Date timestamp, RecorderUserInputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return "MouseRelease " + "'" + key.toString() + "'";
    }
    
    // # UserInputKeyClick

    @Override
    public RecorderUserInputKey getKeyValue() {
        return this.key;
    }

    @Override
    public boolean isPress() {
        return false;
    }
}

class RecorderUserInputMouseMotion extends RecorderUserInput implements UserInputMouseMotion, UserInputMouse
{
    final int x;
    final int y;
    
    RecorderUserInputMouseMotion(Date timestamp, int x, int y)
    {
        super(timestamp);
        this.x = x;
        this.y = y;
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return "MouseMotion " + "(" + x + "," + y + ")";
    }

    // # UserInputMouseMotion
    
    @Override
    public int getMoveX() {
        return x;
    }

    @Override
    public int getMoveY() {
        return y;
    }
}

class RecorderUserInputMouseWheel extends RecorderUserInput implements UserInputMouseWheel, UserInputMouse
{
    RecorderUserInputMouseWheel(Date timestamp)
    {
        super(timestamp);
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return "MouseWheel";
    }
    
    // # UserInputMouseWheel

    @Override
    public int getScrollValue() {
        return 0;
    }
}

class RecorderUserInputWindow extends RecorderUserInput implements UserInputSpecialAction
{
    RecorderUserInputWindow(Date timestamp)
    {
        super(timestamp);
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return "InputWindow";
    }
    
    @Override
    public boolean isCloseWindow() {
        return false;
    }
}

