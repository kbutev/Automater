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
public abstract class Callback<T> {

    abstract public void perform(T argument);

    public static <C> @Nullable Callback createDoNothing() {
        return new Callback<C>() {
            @Override
            public void perform(C argument) {
                // Do nothing
            }
        };
    }
}
