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
    
    interface WithParameter<Input> {
        public void perform(Input argument);
    }

    public static @NotNull Blank buildBlank() {
        return new Blank() {
            @Override
            public void perform() {
                // Do nothing
            }
        };
    }

    public static <Input> @NotNull WithParameter buildBlankWithParameter() {
        return new WithParameter<Input>() {
            @Override
            public void perform(Input argument) {
                // Do nothing
            }
        };
    }
}
