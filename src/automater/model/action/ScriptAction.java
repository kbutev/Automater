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
public interface ScriptAction {
    
    @NotNull String getActionType();
    double getTimestamp();
    
    @NotNull ScriptAction copy();
}
