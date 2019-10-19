/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.work.model.MacroParameters;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Standard implementation of the executor timer.
 * 
 * It implements all the basic functionality.
 * 
 * This timer is thread safe.
 * 
 * @author Byti
 */
public class StandardExecutorTimer implements BaseExecutorTimer {
    @NotNull private final Object _lock = new Object();
    
    @NotNull private List<BaseAction> _actions;
    
    private long _durationTotal = 0;
    
    private long _currentTimeValue = 0;
    private double _timeScale = 1.0;
    
    private long _totalTimeWaited = 0;
    private long _timeToWait = 0;
    
    private long _totalActionsWaitTime = 0;
    
    public StandardExecutorTimer()
    {
        
    }
    
    // # ExecutorTimer
    
    @Override
    public void setup(@NotNull List<BaseAction> actions, @NotNull MacroParameters parameters) throws Exception {
        BaseAction firstAction = actions.get(0);
        
        synchronized (_lock)
        {
            _actions = actions;
            
            // Start with first time value
            _currentTimeValue = firstAction.getPerformTime();
            
            // Setup time scale
            setTimeScale(parameters.playSpeed);
            
            // Compute the total wait time for all the actions
            _totalActionsWaitTime = 0;
            
            for (BaseAction a : actions)
            {
                _totalActionsWaitTime += a.getWaitTime();
            }
            
            // Compute total duration
            _durationTotal = _actions.get(_actions.size()-1).getPerformEndTime();
            _durationTotal += _totalActionsWaitTime;
        }
    }
    
    @Override
    public void reset() {
        BaseAction firstAction = _actions.get(0);
        
        synchronized (_lock)
        {
            _currentTimeValue = firstAction.getPerformTime();
            _totalTimeWaited = 0;
            _timeToWait = 0;
        }
    }
    
    @Override
    public long getTotalDuration() {
        return _durationTotal;
    }
    
    @Override
    public long getDurationPassed() {
        return _currentTimeValue + _totalTimeWaited;
    }
    
    @Override
    public long getCurrentTimeValue() {
        synchronized (_lock)
        {
            return _currentTimeValue;
        }
    }

    @Override
    public double getTimeScale() {
        synchronized (_lock)
        {
            return _timeScale;
        }
    }
    
    @Override
    public void setTimeScale(double scale) {
        synchronized (_lock)
        {
            _timeScale = scale;
        }
    }
    
    @Override
    public boolean canPerformNextAction(@NotNull BaseAction action) {
        long currentTime = getCurrentTimeValue();
        long actionPerformTime = action.getPerformTime();
        
        return actionPerformTime <= currentTime;
    }
    
    @Override
    public void willPerformNextAction(@NotNull BaseAction action) {
        if (isActionATypeOfWaiting(action))
        {
            _timeToWait = action.getWaitTime();
        }
    }
    
    @Override
    public long updateCurrentTime(long dt) {
        synchronized (_lock)
        {
            // Apply timescale
            dt *= _timeScale;
            
            if (isWaiting())
            {
                dt = updateWaitTime(dt);
            }
            
            _currentTimeValue += dt;
            
            return _currentTimeValue;
        }
    }
    
    private boolean isWaiting()
    {
        return _timeToWait > 0;
    }
    
    private long updateWaitTime(long dt)
    {
        _totalTimeWaited += dt;
        _timeToWait -= dt;
        
        if (_timeToWait <= 0)
        {
            long newDt = -_timeToWait;
            
            _timeToWait = 0;
            _totalTimeWaited -= newDt;
            
            return newDt;
        }
        
        return 0;
    }
    
    private boolean isActionATypeOfWaiting(@NotNull BaseAction action) 
    {
        return action.getWaitTime() > 0;
    }
}
