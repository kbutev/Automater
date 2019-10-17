/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.work.BaseExecutorTimer;
import org.jetbrains.annotations.NotNull;
import java.awt.Robot;
import java.util.HashSet;
import java.awt.Dimension;
import java.util.Set;

/**
 * Holds values for a specific action execution context.
 * 
 * Not thread safe.
 * 
 * @author Bytevi
 */
public class ActionContext implements BaseActionContext {
    @NotNull private final Robot _robot;
    @NotNull private final BaseExecutorTimer _timer;
    @NotNull private final Dimension _recordedScreenSize;
    @NotNull private final Dimension _currentScreenSize;
    
    @NotNull private ActionSystemKeyModifiers _modifiers = ActionSystemKeyModifiers.none();
    
    @NotNull private final HashSet<ActionSystemKey> _keysPressed = new HashSet<>();
    
    public ActionContext(@NotNull Robot robot, @NotNull BaseExecutorTimer timer, @NotNull Dimension recordedScreenSize, @NotNull Dimension currentScreenSize)
    {
        this._robot = robot;
        this._timer = timer;
        this._recordedScreenSize = recordedScreenSize;
        this._currentScreenSize = currentScreenSize;
    }

    @Override
    public @NotNull Robot getRobot() {
        return _robot;
    }
    
    @Override
    public @NotNull BaseExecutorTimer getTimer() {
        return _timer;
    }
    
    @Override
    public @NotNull Dimension getRecordedScreenSize()
    {
        return _recordedScreenSize;
    }
    
    @Override
    public @NotNull Dimension getCurrentScreenSize()
    {
        return _currentScreenSize;
    }
    
    @Override
    public @NotNull ActionSystemKeyModifiers getPressedModifiers()
    {
        return _modifiers;
    }
    
    @Override
    public @NotNull Set<ActionSystemKey> getPressedKeys()
    {
        return new HashSet(_keysPressed);
    }
    
    @Override
    public boolean isModifierPressed(@NotNull ActionSystemKeyModifierValue modifier)
    {
        return _modifiers.modifiers.contains(modifier);
    }
    
    @Override
    public boolean isCtrlModifierPressed()
    {
        return _modifiers.modifiers.contains(ActionSystemKeyModifierValue.CTRL);
    }
    
    @Override
    public boolean isAltModifierPressed()
    {
        return _modifiers.modifiers.contains(ActionSystemKeyModifierValue.ALT);
    }
    
    @Override
    public boolean isShiftModifierPressed()
    {
        return _modifiers.modifiers.contains(ActionSystemKeyModifierValue.SHIFT);
    }
    
    @Override
    public boolean isKeyPressed(@NotNull ActionSystemKey key)
    {
        return _keysPressed.contains(key);
    }
    
    @Override
    public void onPressKey(@NotNull ActionSystemKey key, @NotNull ActionSystemKeyModifiers modifiers)
    {
        _modifiers = _modifiers.combine(modifiers);
        
        _keysPressed.add(key);
    }
    
    @Override
    public void onReleaseKey(@NotNull ActionSystemKey key, @NotNull ActionSystemKeyModifiers modifiers)
    {
        for (ActionSystemKeyModifierValue value : modifiers.modifiers)
        {
            _modifiers = _modifiers.removeModifier(value);
        }
        
        _keysPressed.remove(key);
    }
    
    @Override
    public void cleanup()
    {
        for (ActionSystemKeyModifierValue modifier : _modifiers.modifiers)
        {
            _robot.keyRelease(modifier.getValue());
        }
        
        for (ActionSystemKey key : _keysPressed)
        {
            _robot.keyRelease(key.getValue());
        }
    }
}
