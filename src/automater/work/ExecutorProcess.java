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
import automater.utilities.SimpleCallback;
import automater.work.model.ActionContext;
import automater.work.model.ActionSystemKey;
import automater.work.model.ActionSystemKeyModifierValue;
import automater.work.model.ActionSystemKeyModifiers;
import automater.work.model.ExecutorProgress;
import automater.work.model.Macro;
import automater.work.model.MacroParameters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.Dimension;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Performs a given number of actions, one by one, when the time is right.
 * 
 * Has a state. When created, the process is idle.
 * Once started, the process can never be restarted , it is a one time execution.
 * 
 * The state can be read by using these methods:
 * isIdle() - waiting to start
 * isWaiting() - is waiting for specific time to perform next actions
 * isFinished() - performed all actions or was stopped manually
 * 
 * The ExecutorListener delegate methods are always called on the java AWT queue.
 * 
 * @author Bytevi
 */
public class ExecutorProcess implements BaseExecutorProcess, LooperClient, ExecutorProgress {
    @NotNull private final Object _lock = new Object();
    
    // Basic
    @NotNull private final Robot _robot;
    @NotNull private ActionContext _context;
    @NotNull private final List<BaseAction> _actions;
    
    @NotNull private BaseExecutorTimer _timer;
    @Nullable private ExecutorListenerDelegateOperations _listener;
    
    @Nullable private MacroParameters _parameters;
    
    // Current state
    private boolean _started = false;
    private boolean _cancelled = false;
    
    @Nullable private Macro _macro;
    
    private int _playCount = 0;
    @Nullable private BaseActionProcess _previousActionProcess;
    @Nullable private BaseActionProcess _currentActionProcess;
    
    // Timer
    @Nullable private Date _previousDate;
    
    public static ExecutorProcess create(@NotNull Robot robot, @NotNull List<BaseAction> actions, @NotNull BaseExecutorTimer timer)
    {
        try {
            return new ExecutorProcess(robot, actions, timer);
        } catch (Exception e) {
            
        }
        
        return null;
    }
    
    protected ExecutorProcess(@NotNull Robot robot, @NotNull List<BaseAction> actions, @NotNull BaseExecutorTimer timer) throws Exception
    {
        if (actions.isEmpty())
        {
            Errors.throwInvalidArgument("Process cannot be initialized with zero actions");
        }
        
        this._robot = robot;
        this._actions = CollectionUtilities.copyAsImmutable(actions);
        this._timer = timer;
        this._currentActionProcess = null;
        this._listener = new ExecutorListenerDelegateOperations(null);
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
    public @Nullable Macro getMacro()
    {
        return _macro;
    }
    
    @Override
    public @Nullable BaseActionProcess getCurrentActionProcess()
    {
        synchronized (_lock)
        {
            return _currentActionProcess;
        }
    }
    
    @Override
    public @Nullable BaseActionProcess getPreviousActionProcess()
    {
        synchronized (_lock)
        {
            return _previousActionProcess;
        }
    }
    
    @Override
    public @NotNull ExecutorProgress getProgress()
    {
        return this;
    }
    
    @Override
    public void setExecutorTimer(@NotNull BaseExecutorTimer timer)
    {
        this._timer = timer;
    }
    
    @Override
    public void setListener(@NotNull ExecutorListener listener)
    {
        this._listener = new ExecutorListenerDelegateOperations(listener);
    }
    
    @Override
    public void start(@NotNull Macro macro, @NotNull MacroParameters parameters) throws Exception
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
            
            _cancelled = true;
        }
    }
    
    // # LooperClient
    
    @Override
    public void loop()
    {
        boolean cancelled;
        
        synchronized (_lock)
        {
            cancelled = _cancelled;
        }
        
        if (cancelled)
        {
            cancel();
            return;
        }
        
        update();
    }
    
    // # ExecutorProgress
    
    @Override
    public @NotNull String getCurrentStatus() {
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
            
            return TextValue.getText(TextValue.Play_StatusPerformedWaiting, previous.getAction().getStandart());
        }
        
        BaseActionProcess current = getCurrentActionProcess();
        
        if (current == null)
        {
            return TextValue.getText(TextValue.Play_StatusIdle);
        }
        
        String actionDescription = current.getAction().getStandart();
        
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
    
    private void cancel()
    {
        ExecutorListenerDelegateOperations listener;
        
        synchronized (_lock)
        {
            listener = _listener;
            
            // Release all pressed keys, to avoid keys getting stuck
            releaseAllPressedKeys();
            
            // Reset values to their defaults
            cleanup();
        }
        
        // Listener alert
        listener.onCancel();
    }
    
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
    
    private void setCurrentActionProcessSafely(@NotNull BaseActionProcess p)
    {
        synchronized (_lock)
        {
            _currentActionProcess = p;
        }
    }
    
    private void setPreviousActionProcessSafely(@NotNull BaseActionProcess p)
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
    
    private @NotNull List<BaseAction> getRemainingActions() {
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
    
    private @NotNull BaseAction getFirstAction() {
        return _actions.get(0);
    }
    
    private @NotNull BaseAction getLastAction() {
        return _actions.get(_actions.size()-1);
    }
    
    private boolean isLastAction(@NotNull BaseAction action) {
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
            _listener.onActionUpdate(action);
            
            // Process finished? Mark as done
            if (!currentActionProcess.isActive())
            {
                String actionDescription = action.getStandart();
                
                Logger.messageEvent(this, "Finished action: " + actionDescription + "!");
                
                markCurrentActionProcessAsDone();
                
                isIdle = true;
                
                // Listener alert
                _listener.onActionFinish(action);
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
                
                String actionDescription = nextAction.getStandart();
                
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
                _listener.onActionExecute(nextAction);
                
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
                _listener.onActionFinish(nextAction);
            }
        }
        
        // Finished?
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
                _listener.onRepeat(timesPlayed, timesWillPlay);
                
                return;
            }
            
            Logger.messageEvent(this, "Finished.");
            
            // Listener alert
            _listener.onFinish();
            
            // Stop
            synchronized (_lock)
            {
                // Release all pressed keys, to avoid keys getting stuck
                releaseAllPressedKeys();
                
                // Reset values to their defaults
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
            _listener.onWait();
        }
    }
    
    private void releaseAllPressedKeys()
    {
        if (_context == null)
        {
            return;
        }
        
        Robot robot = _context.getRobot();
        
        ActionSystemKeyModifiers modifiers = _context.getPressedModifiers();
        
        for (ActionSystemKeyModifierValue value : modifiers.modifiers)
        {
            robot.keyRelease(value.getValue());
        }
        
        for (ActionSystemKey key : _context.getPressedKeys())
        {
            robot.keyRelease(key.getValue());
        }
    }
    
    private void cleanup()
    {
       _previousActionProcess = new ActionProcess(getLastAction());
        _context = null;
       _currentActionProcess = null;
       _listener = new ExecutorListenerDelegateOperations(null);
        Looper.getShared().unsubscribe(this);
    }
}

class ExecutorListenerDelegateOperations {
    @Nullable final ExecutorListener listener;
    
    ExecutorListenerDelegateOperations(@Nullable ExecutorListener listener)
    {
        this.listener = listener;
    }
    
    public void onStart(int repeatTimes)
    {
        if (listener == null)
        {
            return;
        }
        
        Looper.getShared().performSyncCallbackOnAWTQueue(new SimpleCallback() {
            @Override
            public void perform() {
                listener.onStart(repeatTimes);
            }
        });
    }
    
    public void onActionExecute(@NotNull BaseAction action)
    {
        if (listener == null)
        {
            return;
        }
        
        Looper.getShared().performSyncCallbackOnAWTQueue(new SimpleCallback() {
            @Override
            public void perform() {
                listener.onActionExecute(action);
            }
        });
    }
    
    public void onActionUpdate(@NotNull BaseAction action)
    {
        if (listener == null)
        {
            return;
        }
        
        Looper.getShared().performSyncCallbackOnAWTQueue(new SimpleCallback() {
            @Override
            public void perform() {
                listener.onActionUpdate(action);
            }
        });
    }
    
    public void onActionFinish(BaseAction action)
    {
        if (listener == null)
        {
            return;
        }
        
        Looper.getShared().performSyncCallbackOnAWTQueue(new SimpleCallback() {
            @Override
            public void perform() {
                listener.onActionFinish(action);
            }
        });
    }
    
    public void onWait()
    {
        if (listener == null)
        {
            return;
        }
        
        Looper.getShared().performSyncCallbackOnAWTQueue(new SimpleCallback() {
            @Override
            public void perform() {
                listener.onWait();
            }
        });
    }
    
    public void onCancel()
    {
        if (listener == null)
        {
            return;
        }
        
        Looper.getShared().performSyncCallbackOnAWTQueue(new SimpleCallback() {
            @Override
            public void perform() {
                listener.onCancel();
            }
        });
    }
    
    public void onRepeat(int numberOfTimesPlayed, int numberOfTimesToPlay)
    {
        if (listener == null)
        {
            return;
        }
        
        Looper.getShared().performSyncCallbackOnAWTQueue(new SimpleCallback() {
            @Override
            public void perform() {
                listener.onRepeat(numberOfTimesPlayed, numberOfTimesToPlay);
            }
        });
    }
    
    public void onFinish()
    {
        if (listener == null)
        {
            return;
        }
        
        Looper.getShared().performSyncCallbackOnAWTQueue(new SimpleCallback() {
            @Override
            public void perform() {
                listener.onFinish();
            }
        });
    }
}
