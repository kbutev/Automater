/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.TextValue;
import automater.utilities.CollectionUtilities;
import automater.utilities.DeviceScreen;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.utilities.Looper;
import automater.utilities.LooperClient;
import automater.work.model.ActionContext;
import automater.work.model.ExecutorProgress;
import automater.work.model.Macro;
import automater.work.model.MacroParameters;
import java.awt.Dimension;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Performs actions, when the time is right.
 * 
 * @author Bytevi
 */
public class ExecutorProcess implements BaseExecutorProcess, LooperClient, ExecutorProgress {
    private final Object _lock = new Object();
    
    // Basic
    private final Robot _robot;
    private ActionContext _context;
    private final List<BaseAction> _actions;
    
    private BaseExecutorTimer _timer;
    private ExecutorListener _listener;
    
    private MacroParameters _parameters;
    
    // Current state
    private boolean _started = false;
    
    private Macro _macro;
    
    private int _playCount = 0;
    private BaseActionProcess _previousActionProcess;
    private BaseActionProcess _currentActionProcess;
    
    // Timer
    private Date _previousDate;
    
    public static ExecutorProcess create(Robot robot, List<BaseAction> actions, BaseExecutorTimer timer)
    {
        try {
            return new ExecutorProcess(robot, actions, timer);
        } catch (Exception e) {
            
        }
        
        return null;
    }
    
    protected ExecutorProcess(Robot robot, List<BaseAction> actions, BaseExecutorTimer timer) throws Exception
    {
        if (actions.isEmpty())
        {
            Errors.throwInvalidArgument("Process cannot be initialized with zero actions");
        }
        
        this._robot = robot;
        this._actions = CollectionUtilities.copyAsImmutable(actions);
        this._timer = timer;
        this._currentActionProcess = null;
    }
    
    // # BaseExecutorProcess
    
    @Override
    public boolean isIdle()
    {
        synchronized (_lock)
        {
            return _currentActionProcess == null;
        }
    }
    
    @Override
    public boolean isWaiting()
    {
        synchronized (_lock)
        {
            return _currentActionProcess == null && !isFinished();
        }
    }
    
    @Override
    public boolean isFinished()
    {
        synchronized (_lock)
        {
            if (_previousActionProcess == null)
            {
                return false;
            }
            
            return isLastAction(_previousActionProcess.getAction());
        }
    }
    
    @Override
    public Macro getMacro()
    {
        return _macro;
    }
    
    @Override
    public BaseActionProcess getCurrentActionProcess()
    {
        synchronized (_lock)
        {
            return _currentActionProcess;
        }
    }
    
    @Override
    public BaseActionProcess getPreviousActionProcess()
    {
        synchronized (_lock)
        {
            return _previousActionProcess;
        }
    }
    
    @Override
    public ExecutorProgress getProgress()
    {
        return this;
    }
    
    @Override
    public void setExecutorTimer(BaseExecutorTimer timer)
    {
        this._timer = timer;
    }
    
    @Override
    public void setListener(ExecutorListener listener)
    {
        this._listener = listener;
    }
    
    @Override
    public void start(Macro macro, MacroParameters parameters) throws Exception
    {
        Logger.messageEvent(this, "Start.");
        
        synchronized (_lock)
        {
            if (_started)
            {
                Errors.throwCannotStartTwice("Executor process already started.");
                return;
            }
            
            // Save macro
            _macro = macro;
            
            // Setup timer
            _previousDate = new Date();
            this._timer.setup(macro.actions, parameters);
            
            // Start
            _started = true;
            
            Looper.getShared().subscribe(this);
            
            // Setup context
            Dimension recordedScreenSize = macro.screenSize;
            Dimension currentScreenSize = DeviceScreen.getPrimaryScreenSize();
            _context = new ActionContext(_robot, _timer, recordedScreenSize, currentScreenSize);
            
            // Set paremeters
            _parameters = parameters;
            
            // Reset times played
            _playCount = 1;
        }
        
        // Listener alert
        if (_listener != null)
        {
            _listener.onStart(_parameters.repeatTimes);
        }
    }
    
    @Override
    public void stop() throws Exception
    {
        Logger.messageEvent(this, "Stop.");
        
        synchronized (_lock)
        {
            if (!_started)
            {
                Errors.throwInternalLogicError("Executor process cannot stop, never has been started");
                return;
            }
            
            cleanup();
        }
        
        // Listener alert
        if (_listener != null)
        {
            _listener.onCancel();
        }
    }
    
    // # LooperClient
    
    @Override
    public void loop()
    {
        update();
    }
    
    // # ExecutorProgress
    
    @Override
    public String getCurrentStatus() {
        if (isFinished())
        {
            return TextValue.getText(TextValue.Play_StatusFinished);
        }
        
        if (isWaiting())
        {
            BaseActionProcess previous = getPreviousActionProcess();
            
            if (previous == null)
            {
                return TextValue.getText(TextValue.Play_StatusWaiting);
            }
            
            return TextValue.getText(TextValue.Play_StatusPerformedWaiting, previous.getAction().getDescription().getStandart());
        }
        
        BaseActionProcess current = getCurrentActionProcess();
        
        if (current == null)
        {
            return TextValue.getText(TextValue.Play_StatusIdle);
        }
        
        String actionDescription = current.getAction().getDescription().getStandart();
        
        if (getTimesWillPlay() > 1 && !isRepeatForever())
        {
            int timesPlayed = getPlayCount();
            int timesWillPlay = getTimesWillPlay();
            
            String repeatText = String.valueOf(timesPlayed) + "/" + String.valueOf(timesWillPlay);
            return TextValue.getText(TextValue.Play_StatusPerformingRepeat, repeatText, actionDescription);
        }
        
        return TextValue.getText(TextValue.Play_StatusPerforming, actionDescription);
    }

    @Override
    public double getPercentageDone() {
        if (isFinished())
        {
            return 1;
        }
        
        long durationPassed = _timer.getDurationPassed();
        long duration = _timer.getTotalDuration();
        
        double result = (double)durationPassed / (double)duration;
        
        if (result > 1)
        {
            result = 1;
        }
        
        if (result < 0)
        {
            result = 0;
        }
        
        return result;
    }

    @Override
    public int getCurrentActionIndex() {
        BaseActionProcess process = getCurrentActionProcess();
        
        if (process == null)
        {
            process = getPreviousActionProcess();
        }
        
        return _actions.indexOf(process.getAction());
    }
    
    // # Public
    
    public int getPlayCount()
    {
        synchronized (_lock)
        {
            return _playCount;
        }
    }
    
    public int getRepeatCount()
    {
        return _parameters.repeatTimes;
    }
    
    public int getTimesWillPlay()
    {
        return _parameters.repeatTimes + 1;
    }
    
    public boolean isRepeatForever()
    {
        return _parameters.repeatForever;
    }
    
    // # Private
    
    private boolean canRepeat()
    {
        return _parameters.repeatForever || _playCount <= _parameters.repeatTimes;
    }
    
    private void repeat()
    {
        _playCount += 1;
        
        _currentActionProcess = null;
        _previousActionProcess = null;
        
        _timer.reset();
    }
    
    private void setCurrentActionProcessSafely(BaseActionProcess p)
    {
        synchronized (_lock)
        {
            _currentActionProcess = p;
        }
    }
    
    private void setPreviousActionProcessSafely(BaseActionProcess p)
    {
        synchronized (_lock)
        {
            _previousActionProcess = p;
        }
    }
    
    private void markCurrentActionProcessAsDone()
    {
        BaseActionProcess current = getCurrentActionProcess();
        
        if (current == null)
        {
            Logger.error(this, "Internal logic error in markCurrentActionProcessAsDone(), no process is running");
            return;
        }
        
        setPreviousActionProcessSafely(current);
        setCurrentActionProcessSafely(null);
    }
    
    private void performCurrentActionProcess()
    {
        BaseActionProcess current = getCurrentActionProcess();
        
        try {
            current.perform(_context);
        } catch (Exception e) {
            Logger.error(this, "Failed to perform action " + current.toString() + ": + " + e.toString());
            e.printStackTrace(System.out);
        }
    }
    
    private List<BaseAction> getRemainingActions() {
        BaseActionProcess previousActionProcess = getPreviousActionProcess();
        
        if (previousActionProcess == null)
        {
            return _actions;
        }
        
        int index = _actions.indexOf(previousActionProcess.getAction());
        
        if (index < 0)
        {
            Logger.error(this, "Internal logic error in getRemainingActions() for action process " + previousActionProcess.toString());
            return new ArrayList<>();
        }
        
        ArrayList<BaseAction> remaining = new ArrayList<>();
        
        for (int e = index + 1; e < _actions.size(); e++) 
        {
            remaining.add(_actions.get(e));
        }
        
        return remaining;
    }
    
    private BaseAction getFirstAction() {
        return _actions.get(0);
    }
    
    private BaseAction getLastAction() {
        return _actions.get(_actions.size()-1);
    }
    
    private boolean isLastAction(BaseAction action) {
        return action == getLastAction();
    }
    
    private void update()
    {
        if (isFinished())
        {
            return;
        }
        
        // Update time
        // Calculate the delta value (time difference between current and previous update)
        Date current = new Date();
        
        long dt = current.getTime() - _previousDate.getTime();
        
        _previousDate = current;
        
        this._timer.updateCurrentTime(dt);
        
        // Evaluate current action process
        boolean isIdle = isIdle();
        boolean isWaiting = isWaiting();
        
        if (!isIdle)
        {
            BaseActionProcess currentActionProcess = getCurrentActionProcess();
            BaseAction action = currentActionProcess.getAction();
            
            // Listener alert
            if (_listener != null)
            {
                _listener.onActionUpdate(action);
            }
            
            // Process finished? Mark as done
            if (!currentActionProcess.isActive())
            {
                String actionDescription = action.getDescription().getStandart();
                
                Logger.messageEvent(this, "Finished action: " + actionDescription + "!");
                
                markCurrentActionProcessAsDone();
                
                isIdle = true;
                
                // Listener alert
                if (_listener != null)
                {
                    _listener.onActionFinish(action);
                }
            }
        }
        
        // Idle? Perform next action
        while (isIdle)
        {
            isIdle = false;
            List<BaseAction> remainingActions = getRemainingActions();
            
            if (remainingActions.isEmpty())
            {
                break;
            }
            
            BaseAction nextAction = remainingActions.get(0);
            
            boolean performNext = _timer.canPerformNextAction(nextAction);
            
            if (performNext)
            {
                boolean isComplex = nextAction.isComplex();
                boolean waits = nextAction.getWaitTime() > 0;
                
                BaseActionProcess p = new ActionProcess(nextAction);
                setCurrentActionProcessSafely(p);
                
                String actionDescription = nextAction.getDescription().getStandart();
                
                if (!isComplex && !waits)
                {
                    Logger.messageEvent(this, "Perform next action: " + actionDescription);
                }
                else
                {
                    if (!waits)
                    {
                        Logger.messageEvent(this, "Perform next complex action: " + actionDescription);
                    }
                    else
                    {
                        Logger.messageEvent(this, "Perform next wait action: " + actionDescription);
                    }
                }
                
                // Timer alert
                _timer.willPerformNextAction(nextAction);
                
                // Listener alert
                if (_listener != null)
                {
                    _listener.onActionExecute(nextAction);
                }
                
                // Perform
                performCurrentActionProcess();
                
                if (p.isActive())
                {
                    return;
                }
                
                // Non-complex action was immediately performed, go to next action
                markCurrentActionProcessAsDone();
                isIdle = true;
                
                // Listener alert
                if (_listener != null)
                {
                    _listener.onActionFinish(nextAction);
                }
            }
        }
        
        // Finished? Just log message event
        if (isFinished())
        {
            // Repeat
            if (canRepeat())
            {
                int timesPlayed = getPlayCount();
                int timesWillPlay = getTimesWillPlay();
                
                Logger.messageEvent(this, "Repeat execution (" + timesPlayed + "/" + String.valueOf(timesWillPlay) + ")");
                
                synchronized (_lock)
                {
                    repeat();
                }
                
                // Listener alert
                if (_listener != null)
                {
                    _listener.onRepeat(timesPlayed, timesWillPlay);
                }
                
                return;
            }
            
            Logger.messageEvent(this, "Finished.");
            
            // Listener alert
            if (_listener != null)
            {
                _listener.onFinish();
            }
            
            // Cleanup
            synchronized (_lock)
            {
                cleanup();
            }
            
            return;
        }
        
        // Waiting?
        if (isWaiting())
        {
            if (!isWaiting)
            {
                Logger.messageEvent(this, "Waiting for next action...");
            }
            
            // Listener alert
            if (_listener != null)
            {
                _listener.onWait();
            }
        }
    }
    
    private void cleanup()
    {
        _context.cleanup();
       _previousActionProcess = new ActionProcess(getLastAction());
       _currentActionProcess = null;
        Looper.getShared().unsubscribe(this);
    }
}
