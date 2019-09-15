/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.utilities.Description;
import automater.work.BaseAction;
import java.util.List;

/**
 * Use buildAction() to retrieve the result.
 *
 * @author Bytevi
 */
public interface BaseEditableAction {
    public BaseAction buildAction() throws Exception;
    
    public EditableActionType getType();
    public void setType(EditableActionType type);
    
    public long getTimestamp();
    public void setTimestamp(long timestamp);
    
    public Description getDescription();
    public String getStateDescription();
    
    public String getFirstValueName();
    public String getFirstValue();
    public void setFirstValue(String value);
    public String getSecondValueName();
    public String getSecondValue();
    public void setSecondValue(String value);
    
    public List<String> getPossibleSpecificValues();
}
