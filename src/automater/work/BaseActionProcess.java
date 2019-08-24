/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.work.model.ActionContext;

/**
 * Represents a wrapper of an action, that has a state: active, as long as the action
 * is performing, and inactive if the action is not performing.
 * 
 * The perform(ActionContext) will never block the main thread, even if the action
 * is complex.
 * 
 * @author Bytevi
 */
public interface BaseActionProcess {
    public boolean isActive();
    public BaseAction getAction();
    public void perform(ActionContext context) throws Exception;
}
