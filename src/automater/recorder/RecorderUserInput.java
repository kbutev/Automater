/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import java.util.Date;

/**
 *
 * @author Bytevi
 */
public class RecorderUserInput {
    public final Date timestamp;
    
    public static RecorderUserInput createKeyboardPress(Date timestamp, RecorderUserInputKey key)
    {
        return new RecorderUserInputKeyboardPress(timestamp, key);
    }
    
    public static RecorderUserInput createKeyboardRelease(Date timestamp, RecorderUserInputKey key)
    {
        return new RecorderUserInputKeyboardRelease(timestamp, key);
    }
    
    public static RecorderUserInput createMousePress(Date timestamp)
    {
        return new RecorderUserInputMousePress(timestamp);
    }
    
    public static RecorderUserInput createMouseRelease(Date timestamp)
    {
        return new RecorderUserInputMouseRelease(timestamp);
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
    
    protected RecorderUserInput(Date timestamp)
    {
        this.timestamp = timestamp;
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
}

class RecorderUserInputKeyboardRelease extends RecorderUserInput
{
    public final RecorderUserInputKey key;
    
    RecorderUserInputKeyboardRelease(Date timestamp, RecorderUserInputKey key)
    {
        super(timestamp);
        this.key = key;
    }
}

class RecorderUserInputMousePress extends RecorderUserInput
{
    RecorderUserInputMousePress(Date timestamp)
    {
        super(timestamp);
    }
}

class RecorderUserInputMouseRelease extends RecorderUserInput
{
    RecorderUserInputMouseRelease(Date timestamp)
    {
        super(timestamp);
    }
}

class RecorderUserInputMouseMotion extends RecorderUserInput
{
    RecorderUserInputMouseMotion(Date timestamp)
    {
        super(timestamp);
    }
}

class RecorderUserInputMouseWheel extends RecorderUserInput
{
    RecorderUserInputMouseWheel(Date timestamp)
    {
        super(timestamp);
    }
}

class RecorderUserInputWindow extends RecorderUserInput
{
    RecorderUserInputWindow(Date timestamp)
    {
        super(timestamp);
    }
}

