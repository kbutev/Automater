/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.action;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class DoNothing implements MacroAction {
    
    public static final String TYPE = "noop";
    
    @SerializedName("a")
    @NotNull public final String actionType = TYPE;

    @SerializedName("t")
    public final double timestamp;
    
    public DoNothing(double timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "Do Nothing";
    }

    @Override
    public @NotNull String getActionType() {
        return actionType;
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
    
    @Override
    public @NotNull MacroAction copyWithTimestamp(double timestamp) {
        return new DoNothing(timestamp);
    }
}
