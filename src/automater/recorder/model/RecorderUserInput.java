/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
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
import automater.input.InputDoNothing;
import automater.input.InputKeyClick;
import automater.input.InputMouse;
import automater.input.InputMouseMove;
import automater.input.InputMouseWheel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Encapsulates user input information.
 * 
 * @author Bytevi
 */
public class RecorderUserInput implements Input, Serializable, Description {
    @NotNull private static final RecorderUserInput _logInstance = new RecorderUserInput();
    
    private final long _timestamp;
    
    public static @Nullable RecorderUserInput createDoNothing(long timestamp)
    {
        return new RecorderUserInputDoNothing(timestamp);
    }
    
    public static @Nullable RecorderUserInput createKeyboardPress(long timestamp, @NotNull InputKey key)
    {
        return new RecorderUserInputKeyboardPress(timestamp, key);
    }
    
    public static @Nullable RecorderUserInput createKeyboardRelease(long timestamp, @NotNull InputKey key)
    {
        return new RecorderUserInputKeyboardRelease(timestamp, key);
    }
    
    public static @Nullable RecorderUserInput createMousePress(long timestamp, @NotNull InputKey mouseKey)
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
    
    public static @Nullable RecorderUserInput createMouseRelease(long timestamp, @NotNull InputKey mouseKey)
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
     
    public static @Nullable RecorderUserInput createMouseMove(long timestamp, int x, int y)
    {
        return new RecorderUserInputMouseMove(timestamp, x, y);
    }
    
    public static @Nullable RecorderUserInput createMouseWheel(long timestamp, int scrollValue)
    {
        return new RecorderUserInputMouseWheel(timestamp, scrollValue);
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
    public @Nullable String getStandart() {
        return "unknown";
    }

    @Override
    public @Nullable String getVerbose() {
        return getStandart();
    }

    @Override
    public @Nullable String getStandartTooltip() {
        return "";
    }

    @Override
    public @Nullable String getVerboseTooltip() {
        return "";
    }

    @Override
    public @Nullable String getName() {
        return this.getClass().getCanonicalName();
    }

    @Override
    public @Nullable String getDebug() {
        return getVerbose();
    }
}

class RecorderUserInputDoNothing extends RecorderUserInput implements InputDoNothing {
    public RecorderUserInputDoNothing(long timestamp)
    {
        super(timestamp);
    }
    
    @Override
    public @Nullable String getStandart() {
        return InputDescriptions.getDoNothingDescription(getTimestamp()).getStandart();
    }

    @Override
    public long getDuration() {
        return 0;
    }
}

class RecorderUserInputKeyboardPress extends RecorderUserInput implements InputKeyClick
{
    @NotNull public final InputKey key;
    
    RecorderUserInputKeyboardPress(long timestamp, @NotNull InputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    // # Description
    
    @Override
    public @Nullable String getStandart() {
        return InputDescriptions.getKeyboardInputDescription(getTimestamp(), true, key).getStandart();
    }
    
    @Override
    public @Nullable String getVerbose() {
        return InputDescriptions.getKeyboardInputDescription(getTimestamp(), true, key).getVerbose();
    }
    
    // # UserInputKeyClick

    @Override
    public @NotNull InputKey getKeyValue() {
        return this.key;
    }

    @Override
    public boolean isPress() {
        return true;
    }
}

class RecorderUserInputKeyboardRelease extends RecorderUserInput implements InputKeyClick
{
    @NotNull public final InputKey key;
    
    RecorderUserInputKeyboardRelease(long timestamp, @NotNull InputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    // # Description
    
    @Override
    public @Nullable String getStandart() {
        return InputDescriptions.getKeyboardInputDescription(getTimestamp(), false, key).getStandart();
    }
    
    @Override
    public @Nullable String getVerbose() {
        return InputDescriptions.getKeyboardInputDescription(getTimestamp(), false, key).getVerbose();
    }

    // # UserInputKeyClick
    
    @Override
    public @NotNull InputKey getKeyValue() {
        return this.key;
    }

    @Override
    public boolean isPress() {
        return false;
    }
}

class RecorderUserInputMousePress extends RecorderUserInput implements InputKeyClick, InputMouse
{
    @NotNull public final InputKey key;
    
    static @NotNull RecorderUserInput createLeftMouse(long timestamp, @NotNull InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_LEFT_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static @NotNull RecorderUserInput createRightMouse(long timestamp, @NotNull InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_RIGHT_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static @NotNull RecorderUserInput createMiddleMouse(long timestamp, @NotNull InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_MIDDLE_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static @NotNull RecorderUserInput create4Mouse(long timestamp, @NotNull InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_4_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    static @NotNull RecorderUserInput create5Mouse(long timestamp, @NotNull InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_5_CLICK, mask);
        return new RecorderUserInputMousePress(timestamp, key);
    }
    
    RecorderUserInputMousePress(long timestamp, @NotNull InputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    // # Description
    
    @Override
    public @Nullable String getStandart() {
        return InputDescriptions.getMouseInputDescription(getTimestamp(), true, key).getStandart();
    }
    
    @Override
    public @Nullable String getVerbose() {
        return InputDescriptions.getMouseInputDescription(getTimestamp(), true, key).getVerbose();
    }
    
    // # UserInputKeyClick

    @Override
    public @NotNull InputKey getKeyValue() {
        return this.key;
    }

    @Override
    public boolean isPress() {
        return true;
    }
}

class RecorderUserInputMouseRelease extends RecorderUserInput implements InputKeyClick, InputMouse
{
    @NotNull public final InputKey key;
    
    static @NotNull RecorderUserInput createLeftMouse(long timestamp, @NotNull InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_LEFT_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static @NotNull RecorderUserInput createRightMouse(long timestamp, @NotNull InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_RIGHT_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static @NotNull RecorderUserInput createMiddleMouse(long timestamp, @NotNull InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_MIDDLE_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static @NotNull RecorderUserInput create4Mouse(long timestamp, @NotNull InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_4_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    static @NotNull RecorderUserInput create5Mouse(long timestamp, @NotNull InputKeyModifiers mask)
    {
        InputKey key = new InputKey(_MOUSE_5_CLICK, mask);
        return new RecorderUserInputMouseRelease(timestamp, key);
    }
    
    RecorderUserInputMouseRelease(long timestamp, @NotNull InputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    // # Description
    
    @Override
    public @Nullable String getStandart() {
        return InputDescriptions.getMouseInputDescription(getTimestamp(), false, key).getStandart();
    }
    
    @Override
    public @Nullable String getVerbose() {
        return InputDescriptions.getMouseInputDescription(getTimestamp(), false, key).getVerbose();
    }
    
    // # UserInputKeyClick

    @Override
    public @NotNull InputKey getKeyValue() {
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
        this.x = x > 0 ? x : 0;
        this.y = y > 0 ? y : 0;
    }
    
    // # Description
    
    @Override
    public @Nullable String getStandart() {
        return InputDescriptions.getMouseMoveDescription(getTimestamp(), x, y).getStandart();
    }
    
    @Override
    public @Nullable String getVerbose() {
        return InputDescriptions.getMouseMoveDescription(getTimestamp(), x, y).getVerbose();
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
    int scrollValue;
    
    RecorderUserInputMouseWheel(long timestamp, int scrollValue)
    {
        super(timestamp);
        this.scrollValue = scrollValue;
    }
    
    // # Description
    
    @Override
    public @Nullable String getStandart() {
        return InputDescriptions.getMouseWheelDescription(getTimestamp(), scrollValue).getStandart();
    }
    
    @Override
    public @Nullable String getVerbose() {
        return InputDescriptions.getMouseWheelDescription(getTimestamp(), scrollValue).getVerbose();
    }
    
    // # UserInputMouseWheel

    @Override
    public int getScrollValue() {
        return scrollValue;
    }
}
