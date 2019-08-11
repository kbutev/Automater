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
     
    public static RecorderUserInput createMouseMotion(Date timestamp)
    {
        return new RecorderUserInputMouseMotion(timestamp);
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

class RecorderUserInputKeyboardPress extends RecorderUserInput
{
    public final RecorderUserInputKey key;
    
    RecorderUserInputKeyboardPress(Date timestamp, RecorderUserInputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    @Override
    public String getStandart() {
        return "Keyboard Press" + " '" + key.toString() + "'";
    }
}

class RecorderUserInputKeyboardRelease extends RecorderUserInput
{
    public final RecorderUserInputKey key;
    
    RecorderUserInputKeyboardRelease(Date timestamp, RecorderUserInputKey key)
    {
        super(timestamp);
        this.key = key;
    }
    
    @Override
    public String getStandart() {
        return "Keyboard Release" + " '" + key.toString() + "'";
    }
}

class RecorderUserInputMousePress extends RecorderUserInput
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
    
    @Override
    public String getStandart() {
        return "Mouse Press" + " '" + key.toString() + "'";
    }
}

class RecorderUserInputMouseRelease extends RecorderUserInput
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
    
    @Override
    public String getStandart() {
        return "Mouse Release" + " '" + key.toString() + "'";
    }
}

class RecorderUserInputMouseMotion extends RecorderUserInput
{
    RecorderUserInputMouseMotion(Date timestamp)
    {
        super(timestamp);
    }
    
    @Override
    public String getStandart() {
        return "MouseMotion";
    }
}

class RecorderUserInputMouseWheel extends RecorderUserInput
{
    RecorderUserInputMouseWheel(Date timestamp)
    {
        super(timestamp);
    }
    
    @Override
    public String getStandart() {
        return "MouseWheel";
    }
}

class RecorderUserInputWindow extends RecorderUserInput
{
    RecorderUserInputWindow(Date timestamp)
    {
        super(timestamp);
    }
    
    @Override
    public String getStandart() {
        return "InputWindow";
    }
}

