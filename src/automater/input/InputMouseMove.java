/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.input;

/**
 * Represents a single mouse movement.
 *
 * @author Bytevi
 */
public interface InputMouseMove extends InputMouse {
    public int getX();
    public int getY();
}
