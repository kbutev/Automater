/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.settings;

import automater.input.InputKey;
import automater.input.InputKeyModifiers;
import automater.input.InputKeyValue;
import com.sun.istack.internal.NotNull;

/**
 * A keystroke.
 *
 * @author Bytevi
 */
public class Hotkey {
    @NotNull public final InputKey key;
    
    public Hotkey(@NotNull InputKey key)
    {
        this.key = key;
    }
    
    public Hotkey(@NotNull InputKeyValue keyValue)
    {
        this.key = new InputKey(keyValue);
    }
    
    public boolean isEqualTo(@NotNull InputKey key)
    {
        if (!key.modifiers.equals(InputKeyModifiers.none()))
        {
            return false;
        }
        
        return this.key.equals(key);
    }
}
