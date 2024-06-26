/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.model.InputKeyValue;
import automater.model.OutputKeyValue;
import automater.utilities.Errors;
import java.awt.event.KeyEvent;
import java.util.Map;
import static java.util.Map.entry;
import org.jetbrains.annotations.NotNull;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Immutable.
 * @author Kristiyan Butev
 */
public interface OutputKeyValueParser {
    
    interface Protocol {
        @NotNull OutputKeyValue.Protocol parse(@NotNull InputKeyValue.Protocol value) throws Exception;
    }
    
    class Impl implements Protocol {
        
        @Override
        public @NotNull OutputKeyValue.Protocol parse(@NotNull InputKeyValue.Protocol value) throws Exception {
            if (value instanceof InputKeyValue.AWT awt) {
                if (awt.isKeyboard()) {
                    var code = keyboardMappings.get(awt.code);

                    if (code == null) {
                        throw Errors.parsing();
                    }

                    return OutputKeyValue.AWT.buildKeyboard(code);
                } else {
                    return OutputKeyValue.AWT.buildMouse(awt.code);
                }
            }
            
            throw Errors.parsing();
        }
    }
    
    final static Map<Integer, Integer> keyboardMappings = Map.ofEntries(
        entry(NativeKeyEvent.VC_CONTROL, KeyEvent.VK_CONTROL),
        entry(NativeKeyEvent.VC_SHIFT, KeyEvent.VK_SHIFT),
        entry(NativeKeyEvent.VC_ALT, KeyEvent.VK_ALT),
        entry(NativeKeyEvent.VC_META, KeyEvent.VK_META),
        
        entry(NativeKeyEvent.VC_0, KeyEvent.VK_0),
        entry(NativeKeyEvent.VC_1, KeyEvent.VK_1),
        entry(NativeKeyEvent.VC_2, KeyEvent.VK_2),
        entry(NativeKeyEvent.VC_3, KeyEvent.VK_3),
        entry(NativeKeyEvent.VC_4, KeyEvent.VK_4),
        entry(NativeKeyEvent.VC_5, KeyEvent.VK_5),
        entry(NativeKeyEvent.VC_6, KeyEvent.VK_6),
        entry(NativeKeyEvent.VC_7, KeyEvent.VK_7),
        entry(NativeKeyEvent.VC_8, KeyEvent.VK_8),
        entry(NativeKeyEvent.VC_9, KeyEvent.VK_9),
        
        entry(NativeKeyEvent.VC_A, KeyEvent.VK_A),
        entry(NativeKeyEvent.VC_B, KeyEvent.VK_B),
        entry(NativeKeyEvent.VC_C, KeyEvent.VK_C),
        entry(NativeKeyEvent.VC_D, KeyEvent.VK_D),
        entry(NativeKeyEvent.VC_E, KeyEvent.VK_E),
        entry(NativeKeyEvent.VC_F, KeyEvent.VK_F),
        entry(NativeKeyEvent.VC_G, KeyEvent.VK_G),
        entry(NativeKeyEvent.VC_H, KeyEvent.VK_H),
        entry(NativeKeyEvent.VC_I, KeyEvent.VK_I),
        entry(NativeKeyEvent.VC_J, KeyEvent.VK_J),
        entry(NativeKeyEvent.VC_K, KeyEvent.VK_K),
        entry(NativeKeyEvent.VC_L, KeyEvent.VK_L),
        entry(NativeKeyEvent.VC_M, KeyEvent.VK_M),
        entry(NativeKeyEvent.VC_N, KeyEvent.VK_N),
        entry(NativeKeyEvent.VC_O, KeyEvent.VK_O),
        entry(NativeKeyEvent.VC_P, KeyEvent.VK_P),
        entry(NativeKeyEvent.VC_Q, KeyEvent.VK_Q),
        entry(NativeKeyEvent.VC_R, KeyEvent.VK_R),
        entry(NativeKeyEvent.VC_S, KeyEvent.VK_S),
        entry(NativeKeyEvent.VC_T, KeyEvent.VK_T),
        entry(NativeKeyEvent.VC_U, KeyEvent.VK_U),
        entry(NativeKeyEvent.VC_V, KeyEvent.VK_V),
        entry(NativeKeyEvent.VC_W, KeyEvent.VK_W),
        entry(NativeKeyEvent.VC_X, KeyEvent.VK_X),
        entry(NativeKeyEvent.VC_Y, KeyEvent.VK_Y),
        entry(NativeKeyEvent.VC_Z, KeyEvent.VK_Z),
        
        entry(NativeKeyEvent.VC_SPACE, KeyEvent.VK_SPACE),
        entry(NativeKeyEvent.VC_ENTER, KeyEvent.VK_ENTER),
        entry(NativeKeyEvent.VC_ESCAPE, KeyEvent.VK_ESCAPE),
        entry(NativeKeyEvent.VC_BACKSPACE, KeyEvent.VK_BACK_SPACE),
        entry(NativeKeyEvent.VC_MINUS, KeyEvent.VK_MINUS),
        entry(NativeKeyEvent.VC_EQUALS, KeyEvent.VK_EQUALS),
        entry(NativeKeyEvent.VC_TAB, KeyEvent.VK_TAB),
        entry(NativeKeyEvent.VC_CAPS_LOCK, KeyEvent.VK_CAPS_LOCK),
        
        entry(NativeKeyEvent.VC_F1, KeyEvent.VK_F1),
        entry(NativeKeyEvent.VC_F2, KeyEvent.VK_F2),
        entry(NativeKeyEvent.VC_F3, KeyEvent.VK_F3),
        entry(NativeKeyEvent.VC_F4, KeyEvent.VK_F4),
        entry(NativeKeyEvent.VC_F5, KeyEvent.VK_F5),
        entry(NativeKeyEvent.VC_F6, KeyEvent.VK_F6),
        entry(NativeKeyEvent.VC_F7, KeyEvent.VK_F7),
        entry(NativeKeyEvent.VC_F8, KeyEvent.VK_F8),
        entry(NativeKeyEvent.VC_F9, KeyEvent.VK_F9),
        entry(NativeKeyEvent.VC_F10, KeyEvent.VK_F10),
        entry(NativeKeyEvent.VC_F11, KeyEvent.VK_F11),
        entry(NativeKeyEvent.VC_F12, KeyEvent.VK_F12),
        
        entry(NativeKeyEvent.VC_BACKQUOTE, KeyEvent.VK_BACK_QUOTE),
        entry(NativeKeyEvent.VC_OPEN_BRACKET, KeyEvent.VK_OPEN_BRACKET),
        entry(NativeKeyEvent.VC_CLOSE_BRACKET, KeyEvent.VK_CLOSE_BRACKET),
        entry(NativeKeyEvent.VC_BACK_SLASH, KeyEvent.VK_BACK_SLASH),
        entry(NativeKeyEvent.VC_SEMICOLON, KeyEvent.VK_SEMICOLON),
        entry(NativeKeyEvent.VC_QUOTE, KeyEvent.VK_QUOTE),
        entry(NativeKeyEvent.VC_COMMA, KeyEvent.VK_COMMA),
        entry(NativeKeyEvent.VC_PERIOD, KeyEvent.VK_PERIOD),
        entry(NativeKeyEvent.VC_SLASH, KeyEvent.VK_SLASH),
        entry(NativeKeyEvent.VC_PRINTSCREEN, KeyEvent.VK_PRINTSCREEN),
        entry(NativeKeyEvent.VC_SCROLL_LOCK, KeyEvent.VK_SCROLL_LOCK),
        entry(NativeKeyEvent.VC_PAUSE, KeyEvent.VK_PAUSE),
        entry(NativeKeyEvent.VC_INSERT, KeyEvent.VK_INSERT),
        entry(NativeKeyEvent.VC_DELETE, KeyEvent.VK_DELETE),
        entry(NativeKeyEvent.VC_HOME, KeyEvent.VK_HOME),
        entry(NativeKeyEvent.VC_END, KeyEvent.VK_END),
        entry(NativeKeyEvent.VC_PAGE_UP, KeyEvent.VK_PAGE_UP),
        entry(NativeKeyEvent.VC_PAGE_DOWN, KeyEvent.VK_PAGE_DOWN),
        entry(NativeKeyEvent.VC_UP, KeyEvent.VK_UP),
        entry(NativeKeyEvent.VC_LEFT, KeyEvent.VK_LEFT),
        entry(NativeKeyEvent.VC_RIGHT, KeyEvent.VK_RIGHT),
        entry(NativeKeyEvent.VC_DOWN, KeyEvent.VK_DOWN),
        entry(NativeKeyEvent.VC_NUM_LOCK, KeyEvent.VK_NUM_LOCK),
        entry(NativeKeyEvent.VC_SEPARATOR, KeyEvent.VK_SEPARATOR),
        entry(NativeKeyEvent.VC_CONTEXT_MENU, KeyEvent.VK_CONTEXT_MENU)
    );
}
