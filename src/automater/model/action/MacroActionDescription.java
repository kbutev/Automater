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

    private final @NotNull String timestamp;
    private final @NotNull String type;
    private final @NotNull String value;
    private final @NotNull String description;

    public MacroActionDescription() {
        timestamp = "0";
        type = "unknown";
        value = "";
        description = timestamp + "|" + type + "|" + value;
    }

    public MacroActionDescription(@NotNull String timestamp, @NotNull String type, @NotNull String value) {
        this.timestamp = timestamp;
        this.type = type;
        this.value = value;
        description = timestamp + "|" + type + "|" + value;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
