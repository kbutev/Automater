/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
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
