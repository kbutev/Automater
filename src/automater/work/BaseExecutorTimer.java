/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work;

import automater.work.model.MacroParameters;
import org.jetbrains.annotations.NotNull;
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
    public void setup(@NotNull List<BaseAction> actions, @NotNull MacroParameters parameters) throws Exception;
    
    public void reset();
    
    public long getTotalDuration();
    public long getDurationPassed();
    
    public long getCurrentTimeValue();
    
    public double getTimeScale();
    public void setTimeScale(double scale);
    
    public boolean canPerformNextAction(@NotNull BaseAction action);
    public void willPerformNextAction(@NotNull BaseAction action);
    
    public long updateCurrentTime(long dt);
}
