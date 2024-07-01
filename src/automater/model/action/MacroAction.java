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
public interface MacroAction {
    
    @NotNull String getActionType();
    double getTimestamp();
    @NotNull String getName();
    
    @NotNull MacroAction copy();
    @NotNull MacroAction copyWithTimestamp(double timestamp);
}
