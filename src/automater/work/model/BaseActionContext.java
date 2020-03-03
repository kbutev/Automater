/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work.model;

import automater.work.BaseExecutorTimer;
import org.jetbrains.annotations.NotNull;
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
    public @NotNull Robot getRobot();
    public @NotNull BaseExecutorTimer getTimer();
    public @NotNull Dimension getRecordedScreenSize();
    public @NotNull Dimension getCurrentScreenSize();
    
    public @NotNull ActionSystemKeyModifiers getPressedModifiers();
    public @NotNull Set<ActionSystemKey> getPressedKeys();
    public boolean isModifierPressed(@NotNull ActionSystemKeyModifierValue modifier);
    public boolean isCtrlModifierPressed();
    public boolean isAltModifierPressed();
    public boolean isShiftModifierPressed();
    public boolean isKeyPressed(@NotNull ActionSystemKey key);
    
    public void onPressKey(@NotNull ActionSystemKey key, @NotNull ActionSystemKeyModifiers modifiers);
    public void onReleaseKey(@NotNull ActionSystemKey key, @NotNull ActionSystemKeyModifiers modifiers);
    
    public void cleanup();
}
