/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.utilities.CollectionUtilities;
import automater.utilities.Logger;
import automater.utilities.Looper;
import automater.utilities.LooperClient;
import automater.work.BaseExecutorProcess;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds executor processes, starts them and maintains them.
 *
 * @author Bytevi
 */
public class ExecutorState implements LooperClient {
    private final Object _lock = new Object();
    
    private ArrayList<BaseExecutorProcess> _processes = new ArrayList<>();
    
    public boolean isIdle()
    {
        synchronized (_lock)
        {
            return _processes.isEmpty();
        }
    }
    
    public void startProcess(BaseExecutorProcess process) throws Exception
    {
        boolean isIdle = isIdle();
        
        synchronized (_lock)
        {
            Logger.messageEvent(this, "Starting executor process " + process.toString());
            
            // Start looping if idle
            if (isIdle)
            {
                Looper.getShared().subscribe(this);
            }
            
            _processes.add(process);
            
            process.start();
        }
    }
    
    // # LooperClient
    
    @Override
    public void loop()
    {
        update();
        
        // Stop looping, no need to if idle
        if (isIdle())
        {
            Looper.getShared().unsubscribe(this);
        }
    }
    
    // # Private
    
    private void update()
    {
        ArrayList<BaseExecutorProcess> processesToRemove = new ArrayList<>();
        
        List<BaseExecutorProcess> processes;
        
        synchronized (_lock)
        {
            processes = CollectionUtilities.copyAsImmutable(_processes);
        }
        
        for (BaseExecutorProcess p : processes)
        {
            if (p.isFinished())
            {
                processesToRemove.add(p);
            }
        }
        
        synchronized (_lock)
        {
            for (BaseExecutorProcess p : processesToRemove)
            {
                _processes.remove(p);
            }
        }
    }
}
