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

    private final @NotNull String timestamp;
    private final @NotNull String type;
    private final @NotNull String value;

    public EventDescription() {
        timestamp = "0";
        type = "unknown";
        value = "";
    }

    public EventDescription(@NotNull String timestamp, @NotNull String type, @NotNull String value) {
        this.timestamp = timestamp;
        this.type = type;
        this.value = value;
    }
}
