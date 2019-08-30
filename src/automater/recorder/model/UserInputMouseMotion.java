/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

/**
 *
 * @author Bytevi
 */
public interface UserInputMouseMotion extends UserInputMouse {
    public int numberOfMovements();
    
    public UserInputMouseMove getFirstMove();
    public UserInputMouseMove getLastMove();
    public UserInputMouseMove getMoveAt(int index);
}
