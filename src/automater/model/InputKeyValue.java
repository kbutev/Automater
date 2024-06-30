/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import static java.util.Map.entry;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;

/**
 * Represents a keyboard or mouse key value.
 * 
 * @author Bytevi
 */
public interface InputKeyValue {
    
    interface Protocol {
        
        boolean isKeyboard();
        boolean isModifier();
        boolean isMouse();
        
        @Nullable InputKeyModifierValue toModifier();
    }
    
    /// Java awt robot input key.
    class AWT implements Protocol {
        static final String KEYBOARD_PREFIX = "k.";
        static final String MOUSE_PREFIX = "m.";

        @SerializedName("c")
        public final int code;

        @SerializedName("t")
        public final @NotNull KeyValueType type;

        public static @Nullable AWT build(@NotNull String string) {
            if (string.startsWith(KEYBOARD_PREFIX)) {
                try {
                    var code = Integer.valueOf(string.substring(KEYBOARD_PREFIX.length()));
                    return AWT.buildKeyboardKey(code);
                } catch (Exception e) {}
            } else if (string.startsWith(MOUSE_PREFIX)) {
                try {
                    var code = Integer.valueOf(string.substring(MOUSE_PREFIX.length()));
                    return AWT.buildMouseKey(code);
                } catch (Exception e) {}
            }

            return null;
        }

        public static @NotNull AWT buildKeyboardKey(int code) {
            return new AWT(code, KeyValueType.keyboard);
        }

        public static @NotNull AWT buildKeyboardKey(@NotNull NativeKeyEvent key) {
            return new AWT(key.getKeyCode(), KeyValueType.keyboard);
        }

        public static @NotNull AWT buildMouseKey(int code) {
            return new AWT(code, KeyValueType.mouse);
        }

        AWT(int code, @NotNull KeyValueType isKeyboard) {
            this.code = code;
            this.type = isKeyboard;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof InputKeyValue.AWT other) {
                return this.code == other.code && this.type == other.type;
            }

            return false;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + Objects.hashCode(code) + Objects.hashCode(type);
            return hash;
        }

        @Override
        public boolean isKeyboard() {
            return type == KeyValueType.keyboard;
        }

        @Override
        public boolean isModifier() {
            if (!isKeyboard()) {
                return false;
            }
            
            return toModifier() != null;
        }
        
        @Override
        public boolean isMouse() {
            return type == KeyValueType.mouse;
        }
        
        @Override
        public @Nullable InputKeyModifierValue toModifier() {
            return modifierCodeMappings.get(code);
        }

        @Override
        public String toString() {
            if (isKeyboard()) {
                var result = NativeKeyEvent.getKeyText(code);
                return result != null ? result : "?";
            } else {
                return mouseMappings.getOrDefault(code, MouseKey.LEFT).value;
            }
        }
        
        public @Nullable MouseKey getMouseKey() {
            if (!isMouse()) {
                return null;
            }
            
            return mouseMappings.get(code);
        }
        
        public final static Map<Integer, InputKeyModifierValue> modifierCodeMappings = Map.ofEntries(
            entry(NativeKeyEvent.VC_CONTROL, InputKeyModifierValue.CTRL),
            entry(NativeKeyEvent.VC_SHIFT, InputKeyModifierValue.SHIFT),
            entry(NativeKeyEvent.VC_ALT, InputKeyModifierValue.ALT),
            entry(NativeKeyEvent.VC_META, InputKeyModifierValue.META)
        );

        public final static Map<Integer, MouseKey> mouseMappings = Map.ofEntries(
            entry(NativeMouseEvent.BUTTON1, MouseKey.LEFT),
            entry(NativeMouseEvent.BUTTON2, MouseKey.RIGHT),
            entry(NativeMouseEvent.BUTTON3, MouseKey.CENTER),
            entry(NativeMouseEvent.BUTTON4, MouseKey.M4),
            entry(NativeMouseEvent.BUTTON5, MouseKey.M5)
        );
        
        public final static Map<MouseKey, Integer> mouseMappingsReversed = mouseMappings.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}

