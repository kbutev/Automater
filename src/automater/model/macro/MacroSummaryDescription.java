/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.macro;

import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class MacroSummaryDescription {
    
    private final @NotNull String name;
    private final @NotNull String count;
    private final @NotNull String description;

    public MacroSummaryDescription() {
        name = "";
        count = "";
        description = "no info";
    }

    public MacroSummaryDescription(@NotNull String name, @NotNull String count) {
        this.name = name;
        this.count = count;
        this.description = name + "|" + count + " actions";
    }
    
    @Override
    public String toString() {
        return description;
    }
}
