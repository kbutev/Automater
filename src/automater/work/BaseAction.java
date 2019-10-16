/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.utilities.Description;
import automater.work.model.BaseActionContext;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.io.Serializable;

/**
 * A stateless, program operation.
 * 
 * The method isComplex() is to check if operation will take a long time to
 * perform - these actions should be performed on a background thread, to prevent
 * blocking the main thread.
 * 
 * @author Bytevi
 */
public abstract class BaseAction implements Serializable, Description {
    public abstract boolean isComplex();
    
    public abstract long getWaitTime();
    
    public abstract long getPerformTime();
    public abstract long getPerformDuration();
    public abstract long getPerformEndTime();
    
    public abstract @Nullable Description getDescription();
    
    public abstract void perform(@NotNull BaseActionContext context);
}
