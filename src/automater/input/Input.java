/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
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
