/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import automater.utilities.Size;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class Screen {
    
    public final @NotNull Size size;
    
    public Screen(@NotNull Size size) {
        this.size = size;
    }
}
