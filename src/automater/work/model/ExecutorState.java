/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work.model;

import automater.utilities.CollectionUtilities;
import automater.utilities.Logger;
import automater.utilities.Looper;
import automater.utilities.LooperClient;
import automater.work.BaseExecutorProcess;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds executor processes, starts them and maintains them.
 *
 * @author Bytevi
 */
public class ExecutorState implements LooperClient {
    @NotNull private final Object _lock = new Object();
    
    @NotNull private ArrayList<BaseExecutorProcess> _processes = new ArrayList<>();
    
    public boolean isIdle()
    {
        synchronized (_lock)
        {
            return _processes.isEmpty();
        }
    }
    
    public void startProcess(@NotNull BaseExecutorProcess process, @NotNull Macro macro, @NotNull MacroParameters parameters) throws Exception
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
            
            process.start(macro, parameters);
        }
    }
    
    public void stopAll()
    {
        synchronized (_lock)
        {
            Logger.messageEvent(this, "Stopping all processes");
            
            for (BaseExecutorProcess process : _processes)
            {
                try {
                    process.stop();
                } catch (Exception e) {
                    
                }
            }
            
            _processes.clear();
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
    
    private boolean isProcessEligibleForRemoval(@NotNull BaseExecutorProcess process)
    {
        return process.isFinished();
    }
    
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
            if (isProcessEligibleForRemoval(p))
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
