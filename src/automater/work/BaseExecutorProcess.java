/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.work.model.ExecutorProgress;
import automater.work.model.MacroParameters;

/**
 * Represents an object that performs actions.
 * 
 * Call start() to start the process. When finished, process cannot start again.
 * 
 * Has complex state:
 * idle: is currently performing an action
 * waiting: is waiting for next action to become eligible for execution
 * finished: has performed all actions
 * 
 * @author Bytevi
 */
public interface BaseExecutorProcess {
    public boolean isIdle();
    public boolean isWaiting();
    public boolean isFinished();
    
    public BaseActionProcess getCurrentActionProcess();
    public BaseActionProcess getPreviousActionProcess();
    
    public ExecutorProgress getProgress();
    
    public void setExecutorTimer(BaseExecutorTimer timer);
    public void setListener(ExecutorListener listener);
    
    public void start(MacroParameters parameters) throws Exception;
    public void stop() throws Exception;
}