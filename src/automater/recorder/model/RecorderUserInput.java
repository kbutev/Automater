/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

import automater.input.InputKeyModifiers;
import automater.input.InputKey;
import static automater.input.InputKeyValue.*;
import automater.utilities.DateUtilities;
import automater.utilities.Description;
import automater.utilities.Logger;
import java.io.Serializable;
import java.util.Date;
import automater.input.Input;
import automater.input.InputKeyClick;
import automater.input.InputMouse;
import automater.input.InputMouseMove;
import automater.input.InputMouseWheel;
import automater.input.InputSpecialAction;

/**
 * Encapsulates user input information.
 * 
 * @author Bytevi
 */
public class RecorderUserInput implements Input, Serializable, Description {
    private static final RecorderUserInput _logInstance = new RecorderUserInput();
    
    private final Date _timestamp;
    
    public static RecorderUserInput createKeyboardPress(Date timestamp, InputKey key)
    {
        return new RecorderUserInputKeyboardPress(timestamp, key);
    }
    
    public static RecorderUserInput createKeyboardRelease(Date timestamp, InputKey key)
    {
        return new RecorderUserInputKeyboardRelease(timestamp, key);
    }
    
    public static RecorderUserInput createMousePress(Date timestamp, InputKey mouseKey)
    {
        switch (mouseKey.value)
        {
            case _MOUSE_LEFT_CLICK:
                return RecorderUserInputMousePress.createLeftMouse(timestamp, mouseKey.modifiers);
            case _MOUSE_RIGHT_CLICK:
                return RecorderUserInputMousePress.createRightMouse(timestamp, mouseKey.modifiers);
            case _MOUSE_MIDDLE_CLICK:
                return RecorderUserInputMousePress.createMiddleMouse(timestamp, mouseKey.modifiers);
            case _MOUSE_4_CLICK:
                return RecorderUserInputMousePress.create4Mouse(timestamp, mouseKey.modifiers);
            case _MOUSE_5_CLICK:
                return RecorderUserInputMousePress.create5Mouse(timestamp, mouseKey.modifiers);
        }
        
        Logger.warning(_logInstance, "Cannot create mouse press input from given mouse key " + mouseKey.toString());
        
        return null;
    }
    
    public static RecorderUserInput createMouseRelease(Date timestamp, InputKey mouseKey)
    {
        switch (mouseKey.value)
        {
            case _MOUSE_LEFT_CLICK:
                return RecorderUserInputMouseRelease.createLeftMouse(timestamp, mouseKey.modifiers);
            case _MOUSE_RIGHT_CLICK:
                return RecorderUserInputMouseRelease.createRightMouse(timestamp, mouseKey.modifiers);
            case _MOUSE_MIDDLE_CLICK:
                return RecorderUserInputMouseRelease.createMiddleMouse(timestamp, mouseKey.modifiers);
            case _MOUSE_4_CLICK:
                return RecorderUserInputMouseRelease.create4Mouse(timestamp, mouseKey.modifiers);
            case _MOUSE_5_CLICK:
                return RecorderUserInputMouseRelease.create5Mouse(timestamp, mouseKey.modifiers);
        }
        
        Logger.warning(_logInstance, "Cannot create mouse release input from given mouse key " + mouseKey.toString());
        
        return null;
    }
     
    public static RecorderUserInput createMouseMove(Date timestamp, int x, int y)
    {
        return new RecorderUserInputMouseMove(timestamp, x, y);
    }
    
    public static RecorderUserInput createMouseWheel(Date timestamp)
    {
        return new RecorderUserInputMouseWheel(timestamp);
    }
    
    public static RecorderUserInput createWindow(Date timestamp)
    {
        return new RecorderUserInputWindow(timestamp);
    }
    
    private RecorderUserInput()
    {
        _timestamp = null;
    }
    
    protected RecorderUserInput(Date timestamp)
    {
        this._timestamp = timestamp;
    }
    
    // # UserInput
    
    @Override
    public Date getTimestamp() {
        return _timestamp;
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
        return DateUtilities.asHourMinuteSecondMilisecond(_timestamp);
    }

    @Override
    public String getDebug() {
        return getVerbose();
    }
}

class RecorderUserInputKeyboardPress extends RecorderUserInput implements InputKeyClick
{
    public final InputKey key;
    
    RecorderUserInputKeyboardPress(Date timestamp, InputKey key)
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
    public InputKey getKeyValue() {
        return this.key;
    }

    @Override
    public boolean isPress() {
        return true;
    }
}

class RecorderUserInputKeyboardRelease extends RecorderUserInput implements InputKeyClick
{
    public final InputKey key;
    
    RecorderUserInputKeyboardRelease(Date timestamp, InputKey key)
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
    public InputKey getKeyValue() {
        return this.key;
    }

    @Override
    public boolean isPress() {
        return false;
    }
}

class RecorderUserInputMousePress extends RecorderUserInput implements InputKeyClick, InputMouse
{
    public final InputKey key;
    
    static RecorderUserInput createLeftMouse(Date timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_LEFT_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static RecorderUserInput createRightMouse(Date timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_RIGHT_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static RecorderUserInput createMiddleMouse(Date timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_MIDDLE_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static RecorderUserInput create4Mouse(Date timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_4_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static RecorderUserInput create5Mouse(Date timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_5_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    RecorderUserInputMousePress(Date timestamp, InputKey key)
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
    public InputKey getKeyValue() {
        return this.key;
    }

    @Override
    public boolean isPress() {
        return true;
    }
}

class RecorderUserInputMouseRelease extends RecorderUserInput implements InputKeyClick, InputMouse
{
    public final InputKey key;
    
    static RecorderUserInput createLeftMouse(Date timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_LEFT_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static RecorderUserInput createRightMouse(Date timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_RIGHT_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static RecorderUserInput createMiddleMouse(Date timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_MIDDLE_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static RecorderUserInput create4Mouse(Date timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_4_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static RecorderUserInput create5Mouse(Date timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_5_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    RecorderUserInputMouseRelease(Date timestamp, InputKey key)
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
    public InputKey getKeyValue() {
        return this.key;
    }

    @Override
    public boolean isPress() {
        return false;
    }
}

class RecorderUserInputMouseMove extends RecorderUserInput implements InputMouseMove, InputMouse
{
    final int x;
    final int y;
    
    RecorderUserInputMouseMove(Date timestamp, int x, int y)
    {
        super(timestamp);
        this.x = x;
        this.y = y;
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return "MouseMove " + "(" + x + "," + y + ")";
    }

    // # UserInputMouseMotion
    
    @Override
    public int getX()
    {
        return x;
    }
    
    @Override
    public int getY()
    {
        return y;
    }
}

class RecorderUserInputMouseWheel extends RecorderUserInput implements InputMouseWheel, InputMouse
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

class RecorderUserInputWindow extends RecorderUserInput implements InputSpecialAction
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

