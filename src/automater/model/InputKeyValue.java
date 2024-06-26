/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import static java.util.Map.entry;
import java.util.Objects;
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
        boolean isMouse();
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
        public boolean isMouse() {
            return type == KeyValueType.mouse;
        }

        public static boolean isModifier(@NotNull NativeKeyEvent event) {
            var isLCtrl = event.getModifiers() & NativeKeyEvent.CTRL_L_MASK;
            var isRCtrl = event.getModifiers() & NativeKeyEvent.CTRL_R_MASK;
            var isCtrl = event.getModifiers() & NativeKeyEvent.CTRL_MASK;

            return isLCtrl == 1 || isRCtrl == 1 || isCtrl == 1;
        }

        @Override
        public String toString() {
            if (isKeyboard()) {
                var result = NativeKeyEvent.getKeyText(code);
                return result != null ? result : "?";
            } else {
                return mouseStringMappings.getOrDefault(code, "?");
            }
        }

        public final static Map<Integer, String> mouseStringMappings = Map.ofEntries(
            entry(NativeMouseEvent.BUTTON1, "L MOUSE"),
            entry(NativeMouseEvent.BUTTON2, "R MOUSE"),
            entry(NativeMouseEvent.BUTTON3, "M MOUSE"),
            entry(NativeMouseEvent.BUTTON4, "MOUSE 4"),
            entry(NativeMouseEvent.BUTTON5, "MOUSE 5")
        );
    }
}

