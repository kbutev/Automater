/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

/**
 * A function callback that takes no parameters and return no value.
 *
 * @author Bytevi
 */
public abstract class SimpleCallback {

    abstract public void perform();

    public static SimpleCallback createDoNothing() {
        return new SimpleCallback() {
            @Override
            public void perform() {
                // Do nothing
            }
        };
    }
}
