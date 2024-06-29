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
    
    interface WithParameter <Input> {
        public void perform(Input argument);
    }

    public static @NotNull Blank buildBlank() {
        return () -> {
            // Do nothing
        };
    }

    public static <Input> @NotNull WithParameter buildBlankWithParameter() {
        return (WithParameter<Input>) (Input argument) -> {
            // Do nothing
        };
    }
}
