/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.input;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a system command, like a cmd in Windows OS.
 * 
 * @author Byti
 */
public interface InputSystemCommand {
    public @NotNull String getValue();
    
    public boolean reportsErrors();
}
