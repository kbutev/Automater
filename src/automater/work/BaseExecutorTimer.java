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
 * canPerformNextAction() is used to check if the next action can be performed.
 * 
 * willPerformNextAction() MUST be used when an action is about to be performed,
 * in order to notify the timer so it can keep track of execution state.
 * 
 * @author Bytevi
 */
public interface BaseExecutorTimer {
    public void setup(List<BaseAction> actions, MacroParameters parameters) throws Exception;
    
    public void reset();
    
    public long getTotalDuration();
    public long getDurationPassed();
    
    public long getCurrentTimeValue();
    
    public double getTimeScale();
    public void setTimeScale(double scale);
    
    public boolean canPerformNextAction(BaseAction action);
    public void willPerformNextAction(BaseAction action);
    
    public long updateCurrentTime(long dt);
}
