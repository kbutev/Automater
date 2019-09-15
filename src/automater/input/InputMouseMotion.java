/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

import java.util.List;

/**
 *
 * @author Bytevi
 */
public interface InputMouseMotion extends InputMouse {
    public int numberOfMovements();
    
    public List<InputMouseMove> getMoves();
    public InputMouseMove getFirstMove();
    public InputMouseMove getLastMove();
    public InputMouseMove getMoveAt(int index);
}
