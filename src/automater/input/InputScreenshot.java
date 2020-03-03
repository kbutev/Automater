/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.input;

import org.jetbrains.annotations.NotNull;
import java.awt.Rectangle;

/**
 * Represents an action, that takes a snapshot of the user screen.
 * 
 * @author Byti
 */
public interface InputScreenshot {
    public @NotNull String getPath();
    
    public boolean isFullScreen();
    
    public @NotNull Rectangle getArea();
}
