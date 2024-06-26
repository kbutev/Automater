/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.model.InputKeyModifierValue;
import automater.model.InputKeyValue;
import automater.utilities.Errors;
import org.jetbrains.annotations.NotNull;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Immutable.
 * @author Kristiyan Butev
 */
public interface InputKeyValueParser {
    
    interface AWTProtocol {
        @NotNull InputKeyValue.AWT parseFromString(@NotNull String value);
        @NotNull String parseToString(@NotNull InputKeyValue value);
        @NotNull InputKeyValue.AWT parseFromKeyboardCode(int code) throws Exception;
        @NotNull InputKeyValue.AWT parseFromMouseCode(int code) throws Exception;
        @NotNull InputKeyModifierValue parseFromModifierKeycode(int code) throws Exception;
    }
    
    class AWTImpl implements AWTProtocol {
        
        @Override
        public @NotNull InputKeyValue.AWT parseFromString(@NotNull String value) {
            var result = InputKeyValue.AWT.build(value);
            
            if (result == null) {
                throw Errors.parsing();
            }
            
            return result;
        }
        
        @Override
        public @NotNull String parseToString(@NotNull InputKeyValue value) {
            return value.toString();
        }
        
        @Override
        public @NotNull InputKeyValue.AWT parseFromKeyboardCode(int code) throws Exception {
            var result = InputKeyValue.AWT.buildKeyboardKey(code);
            
            if (result == null) {
                throw Errors.parsing();
            }
            
            return result;
        }
        
        @Override
        public @NotNull InputKeyValue.AWT parseFromMouseCode(int code) throws Exception {
            var result = InputKeyValue.AWT.buildMouseKey(code);
            
            if (result == null) {
                throw Errors.parsing();
            }
            
            return result;
        }
        
        @Override
        public @NotNull InputKeyModifierValue parseFromModifierKeycode(int code) throws Exception {
            if (code == NativeKeyEvent.ALT_MASK ||
                    code == NativeKeyEvent.ALT_L_MASK ||
                    code == NativeKeyEvent.ALT_R_MASK) {
                return InputKeyModifierValue.ALT;
            }

            if (code == NativeKeyEvent.CTRL_MASK ||
                    code == NativeKeyEvent.CTRL_L_MASK ||
                    code == NativeKeyEvent.CTRL_R_MASK) {
                return InputKeyModifierValue.CTRL;
            }

            if (code == NativeKeyEvent.SHIFT_MASK ||
                    code == NativeKeyEvent.SHIFT_L_MASK ||
                    code == NativeKeyEvent.SHIFT_R_MASK) {
                return InputKeyModifierValue.SHIFT;
            }
            
            throw Errors.parsing();
        }
    }
}
