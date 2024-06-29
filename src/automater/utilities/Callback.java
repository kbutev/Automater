/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Bytevi
 */
public interface Callback {
    
    interface Blank {
        public void perform();
    }
    
    interface Return <Output> {
        public Output perform();
    }
    
    interface Param <Input> {
        public void perform(Input argument);
    }

    public static @NotNull Blank buildBlank() {
        return () -> {
            // Do nothing
        };
    }

    public static <Input> @NotNull Param buildBlankWithParameter() {
        return (Param<Input>) (Input argument) -> {
            // Do nothing
        };
    }
}
