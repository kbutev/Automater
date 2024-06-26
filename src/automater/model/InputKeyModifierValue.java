/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import java.awt.event.KeyEvent;

/**
 * Represents a modifier for a system key value.
 *
 * @author Bytevi
 */
public enum InputKeyModifierValue {
    CTRL(KeyEvent.VK_CONTROL),
    ALT(KeyEvent.VK_ALT),
    SHIFT(KeyEvent.VK_SHIFT),
    META(KeyEvent.VK_META);
    
    private final int value;
    
    InputKeyModifierValue(int value) {
        this.value = value;
    }
    
    public int getKeyEventValue() {
        return value;
    }

    @Override
    public String toString() {
        String value;

        value = switch (this) {
            case CTRL ->
                "CTRL";
            case ALT ->
                "ALT";
            case SHIFT ->
                "SHIFT";
            case META ->
                "META";
            default ->
                "";
        };

        if (value.length() > 0) {
            value = value.concat("+");
        }

        return value;
    }
}
