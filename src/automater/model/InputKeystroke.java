/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import automater.json.JSONDecoder;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 *
 * @author Kristiyan Butev
 */
public interface InputKeystroke {
    
    interface Protocol {
        
        boolean isKeyboard();
        boolean isModifier();
        boolean isMouse();
        boolean equalsIgnoreModifier(Object o);
    }

    class AWT implements Protocol {
        @SerializedName("v")
        public final @NotNull InputKeyValue.AWT value;

        @SerializedName("m")
        @JSONDecoder.Optional
        private final @Nullable InputKeyModifier.AWT modifier;

        public static @NotNull AWT anyKey() {
            return new AWT(InputKeyValue.AWT.buildKeyboardKey(NativeKeyEvent.VC_X));
        }

        public static @NotNull AWT build(@NotNull InputKeyValue.AWT value) {
            return new AWT(value);
        }

        public static @NotNull AWT buildFromCode(int code) {
            return new AWT(InputKeyValue.AWT.buildKeyboardKey(code));
        }

        public static @NotNull AWT buildFromCode(int code, @NotNull InputKeyModifier.AWT modifier) {
            return new AWT(InputKeyValue.AWT.buildKeyboardKey(code), modifier);
        }

        public AWT(@NotNull InputKeyValue.AWT value) {
            this.value = value;
            this.modifier = null;
        }

        public AWT(int code, @NotNull InputKeyModifier.AWT modifier) {
            this.value = InputKeyValue.AWT.buildKeyboardKey(code);
            this.modifier = modifier;
        }

        public AWT(@NotNull InputKeyValue.AWT value, @NotNull InputKeyModifier.AWT modifier) {
            this.value = value;
            this.modifier = modifier;
        }

        public @NotNull InputKeyModifier.AWT getModifier() {
            return modifier != null ? modifier : InputKeyModifier.AWT.none();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof InputKeystroke.AWT other) {
                return value.equals(other.value) && getModifier().equals(other.getModifier());
            }

            return false;
        }

        @Override
        public boolean equalsIgnoreModifier(Object o) {
            if (o instanceof InputKeystroke.AWT other) {
                return value.equals(other.value);
            }

            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + Objects.hashCode(value);
            hash = 59 * hash + Objects.hashCode(getModifier());
            return hash;
        }

        @Override
        public String toString() {
            var mod = "";

            if (modifier != null) {
                mod = modifier.isBlank() ? mod : modifier.toString() + "+";
            }

            return mod + value.toString();
        }

        @Override
        public boolean isKeyboard() {
            return !isMouse();
        }

        @Override
        public boolean isModifier() {
            return value.isModifier();
        }
        
        @Override
        public boolean isMouse() {
            return value.isMouse();
        }
        
        public boolean isModifierKey() {
            return value.isKeyboard();
        }
        
        public @NotNull AWT withoutModifier() {
            return new AWT(value);
        }
    }
}
