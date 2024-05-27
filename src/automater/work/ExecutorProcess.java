/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
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
 * Once started, the process can never be restarted, it is a one time execution.
 * 
 * The state can be read by using these methods:
 * isIdle() - waiting to start
 * isWaiting() - is waiting for specific time to perform next actions
 * isFinished() - performed all actions or was stopped manually
 * 
 * The ExecutorListener delegate methods are always called on the java AWT queue.
 * 
 * Note for subclasses: Only the public and protected methods are thread safe.
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
    @NotNull private ListenerDelegate _listener;
    
    @Nullable private MacroParameters _parameters;
    
    // Current state
    private boolean _started = false;
    private boolean _cancelled = false;
    private boolean _finished = false;
    
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
        this._listener = new ListenerDelegate(null);
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
            return _finished;
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
        synchronized (_lock)
        {
            _timer = timer;
        }
    }
    
    @Override
    public void setListener(@NotNull ExecutorListener listener)
    {
        synchronized (_lock)
        {
            _listener = new ListenerDelegate(listener);
        }
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
        _listener.onStart(_parameters.repeatTimes);
    }
    
    @Override
    public void stop() throws Exception
    {
        Logger.messageEvent(this, "Stopping executor process...");
        
        synchronized (_lock)
        {
            if (!_started)
            {
                Errors.throwInternalLogicError("Executor process cannot stop, never has been started");
                return;
            }
            
            if (_finished)
            {
                Errors.throwInternalLogicError("Executor process cannot stop, already stopped");
                return;
            }
            
            _cancelled = true;
        }
    }
    
    // # LooperClient
    
    @Override
    public void loop()
    {
        // Cancel check first
        boolean shouldCancel;
        
        synchronized (_lock)
        {
            shouldCancel = shouldCancel();
        }
        
        if (shouldCancel)
        {
            cancel();
            return;
        }
        
        // Update
        try {
            update();
        } catch (Exception e) {
            Logger.error(this, "Failed to update process: " + e.toString());
            e.printStackTrace(System.out);
            
            // Cancel immediately upon catching update error
            synchronized (_lock)
            {
                _cancelled = true;
            }
        }
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
        
        if (process == null)
        {
            return 0;
        }
        
        return _actions.indexOf(process.getAction());
    }
    
    // # Private properties
    
    private int getPlayCount()
    {
        synchronized (_lock)
        {
            return _playCount;
        }
    }
    
    private int getTimesWillPlay()
    {
        return _parameters.repeatTimes + 1;
    }
    
    private boolean isRepeatForever()
    {
        return _parameters.repeatForever;
    }
    
    private @NotNull List<BaseAction> getRemainingActions(@Nullable BaseActionProcess previousAction) {
        if (previousAction == null)
        {
            return _actions;
        }
        
        int index = _actions.indexOf(previousAction.getAction());
        
        if (index < 0)
        {
            Logger.error(this, "Internal logic error in getRemainingActions() for action " + previousAction.toString());
            return new ArrayList<>();
        }
        
        ArrayList<BaseAction> remaining = new ArrayList<>();
        
        for (int e = index + 1; e < _actions.size(); e++) 
        {
            remaining.add(_actions.get(e));
        }
        
        return remaining;
    }
    
    private @NotNull BaseAction getLastAction() {
        return _actions.get(_actions.size()-1);
    }
    
    private boolean isLastAction(@NotNull BaseAction action) {
        return action == getLastAction();
    }
    
    // # Lifecycle
    
    protected void update() throws Exception
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
        
        // Get a copy of the listener
        ListenerDelegate listener;
        
        synchronized (_lock)
        {
            listener = _listener;
        }
        
        // Record the waiting flag here
        boolean wasWaitingWhenUpdateStarted = isWaiting();
        
        // Evaluate current action process
        BaseActionProcess currentActionProcess = getCurrentActionProcess();
        
        if (currentActionProcess != null)
        {
            EvaluateActionResult result = evaluateCurrentAction(currentActionProcess, listener);
            
            if (result.isFinished())
            {
                synchronized (_lock)
                {
                    cleanupFinishedCurrentAction();
                }
            }
        }
        
        // Should perform next action?
        ShouldPerformNextResult performNextResult;
        
        synchronized (_lock)
        {
            performNextResult = shouldPerformNextAction();
        }
        
        while (performNextResult.isTrue())
        {
            BaseAction nextAction = performNextResult.getNextAction();
            
            ActionProcess actionProcess;
            
            synchronized (_lock)
            {
                actionProcess = preparePerformNextAction(nextAction);
            }
            
            PerformNextResult result = performNextAction(actionProcess, listener);
            
            if (!result.isFinished())
            {
                break;
            }
            
            synchronized (_lock)
            {
                cleanupFinishedCurrentAction();
                performNextResult = shouldPerformNextAction();
            }
        }
        
        // Should finish?
        if (shouldFinish())
        {
            // Repeat
            if (shouldRepeat())
            {
                repeat(listener.listener);
                return;
            }
            
            // Finish
            finish(listener.listener);
            
            return;
        }
        
        // Waiting?
        if (isWaiting())
        {
            if (!wasWaitingWhenUpdateStarted)
            {
                Logger.messageEvent(this, "Waiting for next action...");
                
                // Listener alert
                listener.onWait();
            }
        }
    }
    
    protected void repeat(@Nullable ExecutorListener listener)
    {
        int timesPlayed = getPlayCount();
        int timesWillPlay = getTimesWillPlay();
                
        Logger.messageEvent(this, "Repeat execution (" + timesPlayed + "/" + String.valueOf(timesWillPlay) + ")");
                
        synchronized (_lock)
        {
            _playCount += 1;
            
            _currentActionProcess = null;
            _previousActionProcess = null;
            
            _timer.reset();
        }

        // Listener alert
        if (listener != null)
        {
            listener.onRepeat(timesPlayed, timesWillPlay);
        }
    }
    
    protected void finish(@Nullable ExecutorListener listener)
    {
        Logger.messageEvent(this, "Finish...");
       
        // Stop
        synchronized (_lock)
        {
            _finished = true;
            
            // Reset values to their defaults
            cleanup();
        }
        
        Logger.messageEvent(this, "Finished");
        
        // Listener alert
        if (listener != null)
        {
            listener.onFinish();
        }
    }
    
    protected void cancel()
    {
        ListenerDelegate listener;
        
        synchronized (_lock)
        {
            listener = _listener;
            
            _finished = true;
            
            // Reset values to their defaults
            cleanup();
        }
        
        Logger.messageEvent(this, "Stopped.");
        
        // Listener alert
        listener.onCancel();
    }
    
    // # Perform
    
    private @NotNull EvaluateActionResult evaluateCurrentAction(@NotNull BaseActionProcess actionProcess, @NotNull ListenerDelegate listener)
    {
        BaseAction action = actionProcess.getAction();
        
        // Listener alert
        listener.onActionUpdate(action);
        
        // If inactive, the the process is finished
        if (!actionProcess.isActive())
        {
            String actionDescription = action.getStandart();
            
            Logger.messageEvent(this, "Finished action: " + actionDescription + "!");
            
            // Listener alert
            listener.onActionFinish(action);
            
            return EvaluateActionResult.createFinished();
        }
        
        return EvaluateActionResult.createInProgress();
    }
    
    private @NotNull ActionProcess preparePerformNextAction(@NotNull BaseAction nextAction)
    {
        ActionProcess process = new ActionProcess(nextAction);
        _currentActionProcess = process;
        return process;
    }
    
    private @NotNull PerformNextResult performNextAction(@NotNull ActionProcess actionProcess, @NotNull ListenerDelegate listener)
    {
        BaseAction nextAction = actionProcess.getAction();
        
        boolean isComplex = nextAction.isComplex();
        boolean waits = nextAction.getWaitTime() > 0;
        
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
        listener.onActionExecute(nextAction);
        
        // Perform
        try {
            actionProcess.perform(_context);
        } catch (Exception e) {
            Logger.error(this, "Failed to perform action " + actionProcess.toString() + ": + " + e.toString());
            e.printStackTrace(System.out);
        }
        
        if (actionProcess.isActive())
        {
            return PerformNextResult.createInProgress();
        }
        
        // Listener alert
        listener.onActionFinish(nextAction);
        
        return PerformNextResult.createFinished();
    }
    
    private void cleanupFinishedCurrentAction()
    {
        _previousActionProcess = _currentActionProcess;
        _currentActionProcess = null;
    }
    
    // # Validators
    
    private boolean shouldCancel()
    {
        if (_finished)
        {
            return false;
        }
        
        return _cancelled;
    }
    
    private ShouldPerformNextResult shouldPerformNextAction()
    {
        if (_finished)
        {
            return ShouldPerformNextResult.createProcessFinished();
        }
        
        if (_currentActionProcess != null)
        {
            return ShouldPerformNextResult.createWaitingForCurrent();
        }
        
        List<BaseAction> remainingActions = getRemainingActions(_previousActionProcess);
        
        if (remainingActions.isEmpty())
        {
            // All actions were performed, time to finish the process
            return ShouldPerformNextResult.createProcessFinished();
        }
        
        BaseAction nextAction = remainingActions.get(0);
        
        // Returns true if its time to perform next action, otherwise wait
        if (_timer.canPerformNextAction(nextAction))
        {
            return ShouldPerformNextResult.createNext(nextAction);
        }
        
        return ShouldPerformNextResult.createWaitingForNext();
    }
    
    private boolean shouldFinish()
    {
        if (_finished)
        {
            return false;
        }
        
        if (_previousActionProcess == null)
        {
            return false;
        }
        
        return isLastAction(_previousActionProcess.getAction());
    }
    
    private boolean shouldRepeat()
    {
        return _parameters.repeatForever || _playCount <= _parameters.repeatTimes;
    }
    
    // # Cleanup
    
    private void cleanup()
    {
       _previousActionProcess = new ActionProcess(getLastAction());
       _context.cleanup();
        _context = null;
       _currentActionProcess = null;
       _listener = new ListenerDelegate(null);
        Looper.getShared().unsubscribe(this);
    }
}

class EvaluateActionResult {
    private final boolean _finished;
    
    public static EvaluateActionResult createInProgress()
    {
        return new EvaluateActionResult(false);
    }
    
    public static EvaluateActionResult createFinished()
    {
        return new EvaluateActionResult(true);
    }
    
    private EvaluateActionResult(boolean finished)
    {
        _finished = finished;
    }
    
    public boolean isFinished()
    {
        return _finished;
    }
}

class ShouldPerformNextResult {
    private final boolean _waiting;
    private final boolean _finished;
    private final BaseAction _next;
    
    public static ShouldPerformNextResult createNext(@NotNull BaseAction next)
    {
        return new ShouldPerformNextResult(false, false, next);
    }
    
    public static ShouldPerformNextResult createWaitingForCurrent()
    {
        return new ShouldPerformNextResult(true, false, null);
    }
    
    public static ShouldPerformNextResult createWaitingForNext()
    {
        return new ShouldPerformNextResult(true, false, null);
    }
    
    public static ShouldPerformNextResult createProcessFinished()
    {
        return new ShouldPerformNextResult(false, true, null);
    }
    
    private ShouldPerformNextResult(boolean waiting, boolean finished, @Nullable BaseAction next)
    {
        _waiting = waiting;
        _finished = finished;
        _next = next;
    }
    
    public boolean isTrue()
    {
        return _next != null;
    }
    
    public boolean isWaiting()
    {
        return _waiting;
    }
    
    public boolean isFinished()
    {
        return _finished;
    }
    
    public @NotNull BaseAction getNextAction() throws Exception
    {
        if (_next == null)
        {
            Errors.throwIllegalStateError("ShouldPerformNextResult has no next action");
        }
        
        return _next;
    }
}

class PerformNextResult {
    private final boolean _finished;
    
    public static PerformNextResult createInProgress()
    {
        return new PerformNextResult(false);
    }
    
    public static PerformNextResult createFinished()
    {
        return new PerformNextResult(true);
    }
    
    private PerformNextResult(boolean finished)
    {
        _finished = finished;
    }
    
    public boolean isFinished()
    {
        return _finished;
    }
}

class ListenerDelegate {
    public @Nullable final ExecutorListener listener;
    
    ListenerDelegate(@Nullable ExecutorListener listener)
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
