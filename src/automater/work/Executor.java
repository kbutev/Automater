/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.utilities.Logger;
import automater.work.model.ExecutorState;
import automater.work.model.Macro;
import automater.work.model.MacroParameters;
import java.awt.Robot;

/**
 * Executes macros. Can execute multiple macros simultaneously.
 *
 * @author Bytevi
 */
public class Executor {
    private static Executor singleton;
    private final Object _lock = new Object();
    
    private final ExecutorState _state = new ExecutorState();
    
    private Robot _robot;
    
    private Executor()
    {
        
    }
    
    synchronized public static Executor getDefault()
    {
        if (singleton == null)
        {
            singleton = new Executor();
        }
        
        return singleton;
    }
    
    public boolean isPerforming()
    {
        synchronized (_lock)
        {
            return !_state.isIdle();
        }
    }
    
    public BaseExecutorProcess performMacro(Macro macro, ExecutorListener listener) throws Exception
    {
        return performMacro(macro, MacroParameters.defaultValues(), listener);
    }
    
    public BaseExecutorProcess performMacro(Macro macro, MacroParameters parameters, ExecutorListener listener) throws Exception
    {
        Logger.messageEvent(this, "Perform macro '" + macro.getName() + "' with parameters " + parameters.toString());
        
        synchronized (_lock)
        {
            if (_robot == null)
            {
                initRobot();
            }
            
            BaseExecutorProcess process = new ExecutorProcess(_robot, macro.actions);
            process.setListener(listener);
            
            _state.startProcess(process, macro, parameters);
            
            return process;
        }
    }
    
    public void stopAll()
    {
        Logger.messageEvent(this, "Stop all processes");
        
        synchronized (_lock)
        {
            _state.stopAll();
        }
    }
    
    // # Private
    
    private void initRobot() throws Exception
    {
        if (_robot == null)
        {
            _robot = new Robot();
        }
    }
}
