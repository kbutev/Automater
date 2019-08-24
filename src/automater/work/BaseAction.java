/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.utilities.Description;
import automater.work.model.BaseActionContext;

/**
 * A stateless, program operation.
 * 
 * The method isComplex() is to check if operation will take a long time to
 * perform - these actions should be performed on a background thread, to prevent
 * blocking the main thread.
 * 
 * @author Bytevi
 */
public interface BaseAction {
    public boolean isComplex();
    
    public long getPerformTime();
    
    public Description getDescription();
    
    public void perform(BaseActionContext context);
}
