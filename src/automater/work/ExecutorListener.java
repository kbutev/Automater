/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import com.sun.istack.internal.NotNull;

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
