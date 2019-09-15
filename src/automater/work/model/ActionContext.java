/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.work.BaseExecutorTimer;
import java.awt.Robot;
import java.util.HashSet;
import java.awt.Dimension;

/**
 *
 * @author Bytevi
 */
public class ActionContext implements BaseActionContext {
    private final Robot _robot;
    private final BaseExecutorTimer _timer;
    private final Dimension _recordedScreenSize;
    private final Dimension _currentScreenSize;
    
    private ActionSystemKeyModifiers _modifiers = ActionSystemKeyModifiers.none();
    
    private final HashSet<ActionSystemKey> _keysPressed = new HashSet<>();
    
    public ActionContext(Robot robot, BaseExecutorTimer timer, Dimension recordedScreenSize, Dimension currentScreenSize)
    {
        this._robot = robot;
        this._timer = timer;
        this._recordedScreenSize = recordedScreenSize;
        this._currentScreenSize = currentScreenSize;
    }

    @Override
    public Robot getRobot() {
        return _robot;
    }
    
    @Override
    public BaseExecutorTimer getTimer() {
        return _timer;
    }
    
    @Override
    public Dimension getRecordedScreenSize()
    {
        return _recordedScreenSize;
    }
    
    @Override
    public Dimension getCurrentScreenSize()
    {
        return _currentScreenSize;
    }
    
    @Override
    public ActionSystemKeyModifiers getPressedModifiers()
    {
        return _modifiers;
    }
    
    @Override
    public boolean isModifierPressed(ActionSystemKeyModifierValue modifier)
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
    public boolean isKeyPressed(ActionSystemKey key)
    {
        return _keysPressed.contains(key);
    }
    
    @Override
    public void onPressKey(ActionSystemKey key, ActionSystemKeyModifiers modifiers)
    {
        _modifiers = _modifiers.combine(modifiers);
        
        _keysPressed.add(key);
    }
    
    @Override
    public void onReleaseKey(ActionSystemKey key, ActionSystemKeyModifiers modifiers)
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
