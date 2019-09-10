/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

/**
 * Represents a keyboard or mouse click input.
 *
 * @author Bytevi
 */
public interface InputKeyClick extends Input {
    public InputKey getKeyValue();
    public boolean isPress();
}
