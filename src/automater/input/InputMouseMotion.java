/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.input;

import org.jetbrains.annotations.NotNull;
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
