/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.work.model.MacroParameters;
import java.util.List;

/**
 * Keeps track of how much time has passed.
 * 
 * Use getNextAction(List) to either:
 * get an action to execute if the time has come right
 * or get null, meaning caller should wait
 * 
 * @author Bytevi
 */
public interface BaseExecutorTimer {
    public void setup(List<BaseAction> actions, MacroParameters parameters) throws Exception;
    
    public void reset();
    
    public long getCurrentTimeValue();
    public long getFirstTimeValue();
    public long getFinalTimeValue();
    
    public double getTimeScale();
    public void setTimeScale(double scale);
    
    public boolean canPerformNextAction(BaseAction action);
    
    public long updateCurrentTime(long dt);
}
