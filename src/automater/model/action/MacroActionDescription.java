/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.action;

import org.jetbrains.annotations.NotNull;

/**
 * String representation of an action.
 *
 * @author Kristiyan Butev
 */
public class MacroActionDescription {

    public final @NotNull String timestamp;
    public final double timestampAsDouble;
    public final @NotNull String type;
    public final @NotNull String value;
    public final @NotNull String description;

    public MacroActionDescription() {
        timestamp = "0";
        timestampAsDouble = 0;
        type = "unknown";
        value = "";
        description = timestamp + "|" + type + "|" + value;
    }

    public MacroActionDescription(double timestamp, @NotNull String type, @NotNull String value) {
        this.timestamp = String.format("%.1f", timestamp);
        this.timestampAsDouble = timestamp;
        this.type = type;
        this.value = value;
        description = timestamp + "|" + type + "|" + value;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
