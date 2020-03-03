/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.mutableaction;

import automater.utilities.Description;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Represents an action that can be modified.
 * 
 * Use the action properties to modify the action values.
 *
 * @author Bytevi
 */
public interface BaseMutableAction {
    public @NotNull MutableActionType getType();
    
    public @NotNull List<BaseMutableActionProperty> getProperties();
    public @NotNull BaseMutableActionProperty getFirstProperty();
    public @NotNull BaseMutableActionProperty getSecondProperty();
    
    public long getTimestamp();
    public void setTimestamp(long timestamp);
    
    public @NotNull Description getDescription();
    public @NotNull String getStateDescription();
}
