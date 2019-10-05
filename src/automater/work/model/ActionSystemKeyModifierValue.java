/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import java.awt.event.KeyEvent;

/**
 * An action's key modifier value.
 *
 * @author Bytevi
 */
public enum ActionSystemKeyModifierValue {
    CTRL(KeyEvent.VK_CONTROL),
    ALT(KeyEvent.VK_ALT),
    SHIFT(KeyEvent.VK_SHIFT);
    
    private final int value;
    ActionSystemKeyModifierValue(int value) { this.value = value; }
    public int getValue() { return value; }
}
