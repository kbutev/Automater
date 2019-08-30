/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

/**
 * Represents mouse movement input.
 *
 * @author Bytevi
 */
public interface UserInputMouseMove extends UserInputMouse {
    public int getX();
    public int getY();
}
