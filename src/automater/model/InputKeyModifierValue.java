/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

/**
 * Represents a modifier for a system key value.
 *
 * @author Bytevi
 */
public enum InputKeyModifierValue {
    CTRL,
    ALT,
    SHIFT,
    META;
    
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

        return value;
    }
}
