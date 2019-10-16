/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.mutableaction;

import automater.utilities.Description;
import com.sun.istack.internal.NotNull;
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
