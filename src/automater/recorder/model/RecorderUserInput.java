/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

import automater.input.InputKeyModifiers;
import automater.input.InputKey;
import static automater.input.InputKeyValue.*;
import automater.utilities.Description;
import automater.utilities.Logger;
import java.io.Serializable;
import automater.input.Input;
import automater.input.InputDescriptions;
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
    
    private final long _timestamp;
    
    public static RecorderUserInput createKeyboardPress(long timestamp, InputKey key)
    {
        return new RecorderUserInputKeyboardPress(timestamp, key);
    }
    
    public static RecorderUserInput createKeyboardRelease(long timestamp, InputKey key)
    {
        return new RecorderUserInputKeyboardRelease(timestamp, key);
    }
    
    public static RecorderUserInput createMousePress(long timestamp, InputKey mouseKey)
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
    
    public static RecorderUserInput createMouseRelease(long timestamp, InputKey mouseKey)
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
     
    public static RecorderUserInput createMouseMove(long timestamp, int x, int y)
    {
        return new RecorderUserInputMouseMove(timestamp, x, y);
    }
    
    public static RecorderUserInput createMouseWheel(long timestamp)
    {
        return new RecorderUserInputMouseWheel(timestamp);
    }
    
    public static RecorderUserInput createWindow(long timestamp)
    {
        return new RecorderUserInputWindow(timestamp);
    }
    
    private RecorderUserInput()
    {
        _timestamp = 0;
    }
    
    protected RecorderUserInput(long timestamp)
    {
        this._timestamp = timestamp;
    }
    
    // # UserInput
    
    @Override
    public long getTimestamp() {
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
        return "";
    }

    @Override
    public String getDebug() {
        return getVerbose();
    }
}

class RecorderUserInputKeyboardPress extends RecorderUserInput implements InputKeyClick
{
    public final InputKey key;
    
    RecorderUserInputKeyboardPress(long timestamp, InputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return InputDescriptions.getKeyboardInputDescription(true, key).getStandart();
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
    
    RecorderUserInputKeyboardRelease(long timestamp, InputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return InputDescriptions.getKeyboardInputDescription(false, key).getStandart();
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
    
    static RecorderUserInput createLeftMouse(long timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_LEFT_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static RecorderUserInput createRightMouse(long timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_RIGHT_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static RecorderUserInput createMiddleMouse(long timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_MIDDLE_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static RecorderUserInput create4Mouse(long timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_4_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static RecorderUserInput create5Mouse(long timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_5_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    RecorderUserInputMousePress(long timestamp, InputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return InputDescriptions.getMouseInputDescription(true, key).getStandart();
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
    
    static RecorderUserInput createLeftMouse(long timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_LEFT_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static RecorderUserInput createRightMouse(long timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_RIGHT_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static RecorderUserInput createMiddleMouse(long timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_MIDDLE_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static RecorderUserInput create4Mouse(long timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_4_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static RecorderUserInput create5Mouse(long timestamp, InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_5_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    RecorderUserInputMouseRelease(long timestamp, InputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return InputDescriptions.getMouseInputDescription(false, key).getStandart();
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
    
    RecorderUserInputMouseMove(long timestamp, int x, int y)
    {
        super(timestamp);
        this.x = x;
        this.y = y;
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return InputDescriptions.getMouseMoveDescription(x, y).getStandart();
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
    RecorderUserInputMouseWheel(long timestamp)
    {
        super(timestamp);
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return InputDescriptions.getMouseWheelDescription(0).getStandart();
    }
    
    // # UserInputMouseWheel

    @Override
    public int getScrollValue() {
        return 0;
    }
}

class RecorderUserInputWindow extends RecorderUserInput implements InputSpecialAction
{
    RecorderUserInputWindow(long timestamp)
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

