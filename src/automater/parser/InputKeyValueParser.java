/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.model.InputKeyModifierValue;
import automater.model.InputKeyValue;
import automater.utilities.Errors;
import java.util.Map;
import static java.util.Map.entry;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;

/**
 * Immutable.
 * @author Kristiyan Butev
 */
public interface InputKeyValueParser {
    
    interface Protocol {
        @NotNull InputKeyValue parseFromString(@NotNull String value);
        @NotNull String parseToString(@NotNull InputKeyValue value);
        @NotNull InputKeyValue parseFromKeyboardCode(int code) throws Exception;
        @NotNull InputKeyValue parseFromMouseCode(int code) throws Exception;
        @NotNull InputKeyModifierValue parseFromModifierKeycode(int code) throws Exception;
    }
    
    class Impl implements Protocol {
        
        @Override
        public @NotNull InputKeyValue parseFromString(@NotNull String value) {
            if (!value.startsWith("_")) {
                value = "_" + value;
            }
            
            return InputKeyValue.valueOf(value);
        }
        
        @Override
        public @NotNull String parseToString(@NotNull InputKeyValue value) {
            var name = value.name();
            
            name = name.replaceFirst("_", "");
            
            return name;
        }
        
        @Override
        public @NotNull InputKeyValue parseFromKeyboardCode(int code) throws Exception {
            var result = keyboardMappings.get(code);
            
            if (result == null) {
                throw Errors.parsing();
            }
            
            return result;
        }
        
        @Override
        public @NotNull InputKeyValue parseFromMouseCode(int code) throws Exception {
            var result = mouseKeyMappings.get(code);
            
            if (result == null) {
                throw Errors.parsing();
            }
            
            return result;
        }
        
        @Override
        public @NotNull InputKeyModifierValue parseFromModifierKeycode(int code) throws Exception {
            var keyCode = keyboardMappings.get(code);
            
            if (keyCode == null) {
                throw Errors.parsing();
            }
            
            var result = InputKeyModifierValue.build(keyCode);
            
            if (result == null) {
                throw Errors.parsing();
            }
            
            return result;
        }
    }
    
    final static Map<Integer, InputKeyValue> mouseKeyMappings = Map.ofEntries(entry(NativeMouseEvent.BUTTON1, InputKeyValue.MOUSE_LEFT_CLICK),
            entry(NativeMouseEvent.BUTTON2, InputKeyValue.MOUSE_RIGHT_CLICK),
            entry(NativeMouseEvent.BUTTON3, InputKeyValue.MOUSE_MIDDLE_CLICK),
            entry(NativeMouseEvent.BUTTON4, InputKeyValue.MOUSE_4_CLICK),
            entry(NativeMouseEvent.BUTTON5, InputKeyValue.MOUSE_5_CLICK)
    );

    final static Map<Integer, InputKeyValue> keyboardMappings = Map.ofEntries(entry(NativeKeyEvent.VC_1, InputKeyValue.n1),
            entry(NativeKeyEvent.VC_2, InputKeyValue.n2),
            entry(NativeKeyEvent.VC_3, InputKeyValue.n3),
            entry(NativeKeyEvent.VC_4, InputKeyValue.n4),
            entry(NativeKeyEvent.VC_5, InputKeyValue.n5),
            entry(NativeKeyEvent.VC_6, InputKeyValue.n6),
            entry(NativeKeyEvent.VC_7, InputKeyValue.n7),
            entry(NativeKeyEvent.VC_8, InputKeyValue.n8),
            entry(NativeKeyEvent.VC_9, InputKeyValue.n9),
            entry(NativeKeyEvent.VC_0, InputKeyValue.n0),
            entry(NativeKeyEvent.VC_A, InputKeyValue.A),
            entry(NativeKeyEvent.VC_B, InputKeyValue.B),
            entry(NativeKeyEvent.VC_C, InputKeyValue.C),
            entry(NativeKeyEvent.VC_D, InputKeyValue.D),
            entry(NativeKeyEvent.VC_E, InputKeyValue.E),
            entry(NativeKeyEvent.VC_F, InputKeyValue.F),
            entry(NativeKeyEvent.VC_G, InputKeyValue.G),
            entry(NativeKeyEvent.VC_H, InputKeyValue.H),
            entry(NativeKeyEvent.VC_I, InputKeyValue.I),
            entry(NativeKeyEvent.VC_J, InputKeyValue.J),
            entry(NativeKeyEvent.VC_K, InputKeyValue.K),
            entry(NativeKeyEvent.VC_L, InputKeyValue.L),
            entry(NativeKeyEvent.VC_M, InputKeyValue.M),
            entry(NativeKeyEvent.VC_N, InputKeyValue.N),
            entry(NativeKeyEvent.VC_O, InputKeyValue.O),
            entry(NativeKeyEvent.VC_P, InputKeyValue.P),
            entry(NativeKeyEvent.VC_Q, InputKeyValue.Q),
            entry(NativeKeyEvent.VC_R, InputKeyValue.R),
            entry(NativeKeyEvent.VC_S, InputKeyValue.S),
            entry(NativeKeyEvent.VC_T, InputKeyValue.T),
            entry(NativeKeyEvent.VC_U, InputKeyValue.U),
            entry(NativeKeyEvent.VC_V, InputKeyValue.V),
            entry(NativeKeyEvent.VC_W, InputKeyValue.W),
            entry(NativeKeyEvent.VC_X, InputKeyValue.X),
            entry(NativeKeyEvent.VC_Y, InputKeyValue.Y),
            entry(NativeKeyEvent.VC_Z, InputKeyValue.Z),
            entry(NativeKeyEvent.VC_SPACE, InputKeyValue.SPACE),
            entry(NativeKeyEvent.VC_ENTER, InputKeyValue.ENTER),
            entry(NativeKeyEvent.VC_ESCAPE, InputKeyValue.ESCAPE),
            entry(NativeKeyEvent.VC_BACKSPACE, InputKeyValue.BACKSPACE),
            entry(NativeKeyEvent.VC_MINUS, InputKeyValue.MINUS),
            entry(NativeKeyEvent.VC_EQUALS, InputKeyValue.EQUALS),
            entry(NativeKeyEvent.VC_TAB, InputKeyValue.TAB),
            entry(NativeKeyEvent.VC_CAPS_LOCK, InputKeyValue.CAPS_LOCK),
            entry(NativeKeyEvent.VC_F1, InputKeyValue.F1),
            entry(NativeKeyEvent.VC_F2, InputKeyValue.F2),
            entry(NativeKeyEvent.VC_F3, InputKeyValue.F3),
            entry(NativeKeyEvent.VC_F4, InputKeyValue.F4),
            entry(NativeKeyEvent.VC_F5, InputKeyValue.F5),
            entry(NativeKeyEvent.VC_F6, InputKeyValue.F6),
            entry(NativeKeyEvent.VC_F7, InputKeyValue.F7),
            entry(NativeKeyEvent.VC_F8, InputKeyValue.F8),
            entry(NativeKeyEvent.VC_F9, InputKeyValue.F9),
            entry(NativeKeyEvent.VC_F10, InputKeyValue.F10),
            entry(NativeKeyEvent.VC_F11, InputKeyValue.F11),
            entry(NativeKeyEvent.VC_F12, InputKeyValue.F12),
            entry(NativeKeyEvent.VC_BACKQUOTE, InputKeyValue.BACKQUOTE),
            entry(NativeKeyEvent.VC_OPEN_BRACKET, InputKeyValue.OPEN_BRACKET),
            entry(NativeKeyEvent.VC_CLOSE_BRACKET, InputKeyValue.CLOSE_BRACKET),
            entry(NativeKeyEvent.VC_BACK_SLASH, InputKeyValue.BACK_SLASH),
            entry(NativeKeyEvent.VC_SEMICOLON, InputKeyValue.SEMICOLON),
            entry(NativeKeyEvent.VC_QUOTE, InputKeyValue.QUOTE),
            entry(NativeKeyEvent.VC_COMMA, InputKeyValue.COMMA),
            entry(NativeKeyEvent.VC_PERIOD, InputKeyValue.PERIOD),
            entry(NativeKeyEvent.VC_SLASH, InputKeyValue.SLASH),
            entry(NativeKeyEvent.VC_PRINTSCREEN, InputKeyValue.PRINTSCREEN),
            entry(NativeKeyEvent.VC_SCROLL_LOCK, InputKeyValue.SCROLL_LOCK),
            entry(NativeKeyEvent.VC_PAUSE, InputKeyValue.PAUSE),
            entry(NativeKeyEvent.VC_INSERT, InputKeyValue.INSERT),
            entry(NativeKeyEvent.VC_DELETE, InputKeyValue.DELETE),
            entry(NativeKeyEvent.VC_HOME, InputKeyValue.HOME),
            entry(NativeKeyEvent.VC_END, InputKeyValue.END),
            entry(NativeKeyEvent.VC_PAGE_UP, InputKeyValue.PAGE_UP),
            entry(NativeKeyEvent.VC_PAGE_DOWN, InputKeyValue.PAGE_DOWN),
            entry(NativeKeyEvent.VC_UP, InputKeyValue.UP),
            entry(NativeKeyEvent.VC_LEFT, InputKeyValue.LEFT),
            entry(NativeKeyEvent.VC_CLEAR, InputKeyValue.CLEAR),
            entry(NativeKeyEvent.VC_RIGHT, InputKeyValue.RIGHT),
            entry(NativeKeyEvent.VC_DOWN, InputKeyValue.DOWN),
            entry(NativeKeyEvent.VC_NUM_LOCK, InputKeyValue.NUM_LOCK),
            entry(NativeKeyEvent.VC_SEPARATOR, InputKeyValue.SEPARATOR),
            entry(NativeKeyEvent.VC_META, InputKeyValue.META),
            entry(NativeKeyEvent.VC_CONTEXT_MENU, InputKeyValue.CONTEXT_MENU),
            entry(NativeKeyEvent.VC_POWER, InputKeyValue.POWER),
            entry(NativeKeyEvent.VC_SLEEP, InputKeyValue.SLEEP),
            entry(NativeKeyEvent.VC_WAKE, InputKeyValue.WAKE),
            entry(NativeKeyEvent.VC_MEDIA_PLAY, InputKeyValue.MEDIA_PLAY),
            entry(NativeKeyEvent.VC_MEDIA_STOP, InputKeyValue.MEDIA_STOP),
            entry(NativeKeyEvent.VC_MEDIA_PREVIOUS, InputKeyValue.MEDIA_PREVIOUS),
            entry(NativeKeyEvent.VC_MEDIA_NEXT, InputKeyValue.MEDIA_NEXT),
            entry(NativeKeyEvent.VC_MEDIA_SELECT, InputKeyValue.MEDIA_SELECT),
            entry(NativeKeyEvent.VC_MEDIA_EJECT, InputKeyValue.MEDIA_EJECT),
            entry(NativeKeyEvent.VC_VOLUME_MUTE, InputKeyValue.VOLUME_MUTE),
            entry(NativeKeyEvent.VC_VOLUME_UP, InputKeyValue.VOLUME_UP),
            entry(NativeKeyEvent.VC_VOLUME_DOWN, InputKeyValue.VOLUME_DOWN)
    );
    
    final static Map<InputKeyValue, Integer> keyboardMappingsInverted = keyboardMappings.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
}
