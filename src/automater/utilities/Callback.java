/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.Nullable;

/**
 * A function callback that takes 1 parameter and returns no value.
 *
 * @author Bytevi
 */
@FunctionalInterface
public interface Callback<Input> {

    public void perform(Input argument);

    public static <Input> @Nullable Callback createDoNothing() {
        return new Callback<Input>() {
            @Override
            public void perform(Input argument) {
                // Do nothing
            }
        };
    }
}
