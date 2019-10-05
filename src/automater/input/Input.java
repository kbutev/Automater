/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

/**
 * Represents a generic command.
 * 
 * May be a user input, like a mouse click or keyboard click,
 * or something more fancy like a system command or a system screenshot take.
 * 
 * Described only by a timestamp value.
 * 
 * @author Bytevi
 */
public interface Input {
    public long getTimestamp();
}
