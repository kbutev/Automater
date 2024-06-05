/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.input;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a keyboard or mouse click input.
 *
 * @author Bytevi
 */
public interface InputKeyClick extends Input {

    public @NotNull InputKey getKeyValue();

    public boolean isPress();
}
