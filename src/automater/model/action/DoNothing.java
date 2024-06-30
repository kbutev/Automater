/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.action;

import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class DoNothing implements MacroAction {
    
    public static final String TYPE = "noop";
    
    public final double timestamp;
    
    public DoNothing(double timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public @NotNull String getActionType() {
        return TYPE;
    }
    
    @Override
    public double getTimestamp() {
        return timestamp;
    }
    
    @Override
    public @NotNull String getName() {
        return "Do Nothing";
    }
    
    @Override
    public @NotNull MacroAction copy() {
        return this;
    }
}
