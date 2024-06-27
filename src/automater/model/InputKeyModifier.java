/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 *
 * @author Kristiyan Butev
 */
public interface InputKeyModifier {
    
    interface Protocol {
        
    }

    class AWT implements Protocol {
        @SerializedName("v") private final int value;

        public static AWT none() {
            return new AWT();
        }

        public AWT() {
            value = 0;
        }

        public AWT(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            var mod = getValue();

            if (mod == null) {
                return "n/a";
            }

            return mod.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof InputKeyModifier.AWT other) {
                return value == other.value;
            }

            return false;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + Objects.hashCode(value);
            return hash;
        }

        public boolean isBlank() {
            return getValue() == null;
        }

        public @Nullable InputKeyModifierValue getValue() {
            if (c(NativeKeyEvent.CTRL_L_MASK) || c(NativeKeyEvent.CTRL_MASK) || c(NativeKeyEvent.CTRL_R_MASK)) {
                return InputKeyModifierValue.CTRL;
            }

            if (c(NativeKeyEvent.SHIFT_L_MASK) || c(NativeKeyEvent.SHIFT_MASK) || c(NativeKeyEvent.SHIFT_R_MASK)) {
                return InputKeyModifierValue.SHIFT;
            }

            if (c(NativeKeyEvent.ALT_L_MASK) || c(NativeKeyEvent.ALT_MASK) || c(NativeKeyEvent.ALT_R_MASK)) {
                return InputKeyModifierValue.ALT;
            }

            if (c(NativeKeyEvent.META_L_MASK) || c(NativeKeyEvent.META_MASK) || c(NativeKeyEvent.META_R_MASK)) {
                return InputKeyModifierValue.META;
            }

            return null;
        }

        private boolean c(int mask) {
            return (value & mask) == mask;
        }
    }
}
