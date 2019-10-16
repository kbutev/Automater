/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

import com.sun.istack.internal.NotNull;
import java.util.List;

/**
 * Represents a collection of mouse movements.
 * 
 * All movements are sorted by their timestamp value (ascending order).
 * 
 * @author Bytevi
 */
public interface InputMouseMotion extends InputMouse {
    public int numberOfMovements();
    
    public @NotNull List<InputMouseMove> getMoves();
    public @NotNull InputMouseMove getFirstMove();
    public @NotNull InputMouseMove getLastMove();
    public @NotNull InputMouseMove getMoveAt(int index);
}
