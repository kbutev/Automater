/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work;

import org.jetbrains.annotations.NotNull;

/**
 * Forwards requests when the executor does something.
 * 
 * @author Bytevi
 */
public interface ExecutorListener {
    public void onStart(int repeatTimes);
    
    public void onActionExecute(@NotNull BaseAction action);
    public void onActionUpdate(@NotNull BaseAction action);
    public void onActionFinish(@NotNull BaseAction action);
    
    public void onWait();
    public void onCancel();
    public void onRepeat(int numberOfTimesPlayed, int numberOfTimesToPlay);
    public void onFinish();
}
