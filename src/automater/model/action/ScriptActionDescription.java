/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.action;

import org.jetbrains.annotations.NotNull;

/**
 * String representation of an action.
 * @author Kristiyan Butev
 */
public class ScriptActionDescription {
    private final @NotNull String timestamp;
    private final @NotNull String type;
    private final @NotNull String value;
    
    public ScriptActionDescription() {
        timestamp = "0";
        type = "unknown";
        value = "";
    }
    
    public ScriptActionDescription(@NotNull String timestamp, @NotNull String type, @NotNull String value) {
        this.timestamp = timestamp;
        this.type = type;
        this.value = value;
    }
}
