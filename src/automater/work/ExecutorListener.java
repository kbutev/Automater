/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

/**
 * Forwards requests when the executor does something.
 * 
 * @author Bytevi
 */
public interface ExecutorListener {
    public void onStart(int numberOfActions);
    
    public void onActionExecute(BaseAction action);
    public void onActionUpdate(BaseAction action);
    public void onActionFinish(BaseAction action);
    
    public void onWait();
    public void onCancel();
    public void onFinish();
}
