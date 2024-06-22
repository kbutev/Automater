/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import java.awt.event.KeyEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Represents a modifier for a system key value.
 *
 * @author Bytevi
 */
public enum InputKeyModifierValue {
    CTRL(NativeKeyEvent.CTRL_MASK),
    ALT(KeyEvent.VK_ALT),
    SHIFT(KeyEvent.VK_SHIFT);
    
    private final int value;
    
    InputKeyModifierValue(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static @Nullable InputKeyModifierValue build(@NotNull InputKeyValue value) {
        switch (value) {
            //case KeyValue.WINDOWS_OR_COMMAND:
            //    return WINDOWS_OR_COMMAND;
            //case KeyValue.FUNCTION:
            //    return FUNCTION;
            // TODO
            default:
                return null;
        }
    }

    public static @NotNull String getSeparatorSymbol() {
        return "+";
    }

    public static boolean isOnWindowsPlatform() {
        return true;
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
            default ->
                "";
        };

        if (value.length() > 0) {
            value = value.concat(getSeparatorSymbol());
        }

        return value;
    }
}
