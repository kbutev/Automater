/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.work.BaseExecutorTimer;
import java.awt.Dimension;
import java.awt.Robot;
import java.util.Set;

/**
 * Contains information about the current state of action execution.
 * 
 * The cleanup() method should be called only once, by the execution process
 * to make sure that all keys are released at the end.
 * 
 * Has one single, unique robot instance, used to simulate keystrokes.
 * 
 * 
 * When an action is simulating a user keyboard or mouse key, the action should
 * call one of these two methods: onPressKey() onReleaseKey()
 * 
 * Keeps track of which keys are pressed and which are not.
 * 
 * Context objects are not guaranteed to be thread safe.
 * 
 * @author Bytevi
 */
public interface BaseActionContext {
    public Robot getRobot();
    public BaseExecutorTimer getTimer();
    public Dimension getRecordedScreenSize();
    public Dimension getCurrentScreenSize();
    
    public ActionSystemKeyModifiers getPressedModifiers();
    public Set<ActionSystemKey> getPressedKeys();
    public boolean isModifierPressed(ActionSystemKeyModifierValue modifier);
    public boolean isCtrlModifierPressed();
    public boolean isAltModifierPressed();
    public boolean isShiftModifierPressed();
    public boolean isKeyPressed(ActionSystemKey key);
    
    public void onPressKey(ActionSystemKey key, ActionSystemKeyModifiers modifiers);
    public void onReleaseKey(ActionSystemKey key, ActionSystemKeyModifiers modifiers);
    
    public void cleanup();
}
