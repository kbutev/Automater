/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.event;

import org.jetbrains.annotations.NotNull;

/**
 * String representation of an event.
 *
 * @author Kristiyan Butev
 */
public class EventDescription {

    private final @NotNull String type;
    private final @NotNull String value;
    private final @NotNull String description;

    public EventDescription() {
        type = "unknown";
        value = "";
        description = type + "|" + value;
    }

    public EventDescription(@NotNull String type, @NotNull String value) {
        this.type = type;
        this.value = value;
        description = type + "|" + value;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
