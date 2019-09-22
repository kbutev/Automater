/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.work.model.MacroParameters;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Byti
 */
public class StandartExecutorTimer implements BaseExecutorTimer {
    private final Object _timerLock = new Object();
    
    private List<BaseAction> _actions;
    
    private long _currentTimeValue = 0;
    private double _timeScale = 1.0;
    private double _totalActionsWaitTime = 0;
    private double _timeWaited = 0;
    private double _waitTimeLeft = 0;
    
    public StandartExecutorTimer()
    {
        
    }
    
    // # ExecutorTimer
    
    @Override
    public void setup(List<BaseAction> actions, MacroParameters parameters) throws Exception {
        BaseAction firstAction = actions.get(0);
        
        synchronized (_timerLock)
        {
            _actions = actions;
            
            _currentTimeValue = firstAction.getPerformTime();
            
            setTimeScale(parameters.playSpeed);
            
            _totalActionsWaitTime = 0;
            
            for (BaseAction a : actions)
            {
                _totalActionsWaitTime += a.getWaitTime();
            }
        }
    }
    
    @Override
    public void reset() {
        BaseAction firstAction = _actions.get(0);
        
        synchronized (_timerLock)
        {
            _currentTimeValue = firstAction.getPerformTime();
        }
    }

    @Override
    public long getCurrentTimeValue() {
        synchronized (_timerLock)
        {
            return _currentTimeValue;
        }
    }

    @Override
    public long getFirstTimeValue() {
        BaseAction a = _actions.get(0);
        return a.getPerformTime();
    }

    @Override
    public long getFinalTimeValue() {
        BaseAction a = _actions.get(_actions.size()-1);
        return a.getPerformEndTime();
    }

    @Override
    public double getTimeScale() {
        synchronized (_timerLock)
        {
            return _timeScale;
        }
    }
    
    @Override
    public void setTimeScale(double scale) {
        synchronized (_timerLock)
        {
            _timeScale = scale;
        }
    }
    
    @Override
    public boolean canPerformNextAction(BaseAction action) {
        long currentTime = getCurrentTimeValue();
        return action.getPerformTime() <= currentTime;
    }
    
    @Override
    public void willPerformNextAction(BaseAction action) {
        
    }
    
    @Override
    public long updateCurrentTime(long dt) {
        synchronized (_timerLock)
        {
            dt *= _timeScale;
            
            _currentTimeValue += dt;
            
            return _currentTimeValue;
        }
    }
}
