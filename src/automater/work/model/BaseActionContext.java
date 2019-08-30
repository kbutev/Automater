/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.work.BaseExecutorTimer;
import java.awt.Robot;

/**
 * Contains information about the current state of action execution.
 * 
 * Has one single, unique robot instance, used to simulate keystrokes.
 * 
 * Keeps track of which keys are pressed and which are not.
 * 
 * The cleanup() method should be called only once, by the execution process
 * to make sure that all keys are released at the end.
 * 
 * @author Bytevi
 */
public interface BaseActionContext {
    public Robot getRobot();
    public BaseExecutorTimer getTimer();
    
    public ActionSystemKeyModifiers getPressedModifiers();
    public boolean isModifierPressed(ActionSystemKeyModifierValue modifier);
    public boolean isCtrlModifierPressed();
    public boolean isAltModifierPressed();
    public boolean isShiftModifierPressed();
    public boolean isKeyPressed(ActionSystemKey key);
    
    public void onPressKey(ActionSystemKey key, ActionSystemKeyModifiers modifiers);
    public void onReleaseKey(ActionSystemKey key, ActionSystemKeyModifiers modifiers);
    
    public void cleanup();
}
