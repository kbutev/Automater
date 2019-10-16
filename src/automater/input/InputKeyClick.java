/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

import com.sun.istack.internal.NotNull;

/**
 * Represents a keyboard or mouse click input.
 *
 * @author Bytevi
 */
public interface InputKeyClick extends Input {
    public @NotNull InputKey getKeyValue();
    public boolean isPress();
}
