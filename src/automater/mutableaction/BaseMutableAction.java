/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.mutableaction;

import automater.utilities.Description;
import java.util.List;

/**
 * Represents an action that can be modified.
 * 
 * Use the action properties to modify the action values.
 *
 * @author Bytevi
 */
public interface BaseMutableAction {
    public MutableActionType getType();
    
    public List<BaseMutableActionProperty> getProperties();
    public BaseMutableActionProperty getFirstProperty();
    public BaseMutableActionProperty getSecondProperty();
    
    public long getTimestamp();
    public void setTimestamp(long timestamp);
    
    public Description getDescription();
    public String getStateDescription();
}
