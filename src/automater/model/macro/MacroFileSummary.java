/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.macro;

import automater.utilities.Path;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class MacroFileSummary {
    
    public final @NotNull MacroSummary summary;
    public final @NotNull Path path;
    
    public MacroFileSummary(@NotNull MacroSummary summary, @NotNull Path path) {
        this.summary = summary;
        this.path = path;
    }
}
