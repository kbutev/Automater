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
public class MacroFileSummary {
    
    public final @NotNull MacroSummary summary;
    public final @NotNull String filePath;
    
    public MacroFileSummary(@NotNull MacroSummary summary, @NotNull String filePath) {
        this.summary = summary;
        this.filePath = filePath;
    }
}
