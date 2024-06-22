/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.model.InputKeyModifierValue;
import automater.model.InputKeyValue;
import automater.model.OutputKeyModifierValue;
import automater.model.OutputKeyValue;
import automater.utilities.Errors;
import java.awt.event.KeyEvent;
import java.util.Map;
import static java.util.Map.entry;
import org.jetbrains.annotations.NotNull;

/**
 * Immutable.
 * @author Kristiyan Butev
 */
public interface OutputKeyValueParser {
    
    interface Protocol {
        @NotNull OutputKeyValue parse(@NotNull InputKeyValue value) throws Exception;
        @NotNull OutputKeyModifierValue parseModifier(@NotNull InputKeyModifierValue value) throws Exception;
    }
    
    class Impl implements Protocol {
        
        @Override
        public @NotNull OutputKeyValue parse(@NotNull InputKeyValue value) throws Exception {
            var code = keyboardMappings.get(value);
            
            if (code == null) {
                throw Errors.parsing();
            }
            
            return new OutputKeyValue(code);
        }
        
        @Override
        public @NotNull OutputKeyModifierValue parseModifier(@NotNull InputKeyModifierValue value) throws Exception {
            switch (value) {
                case InputKeyModifierValue.CTRL:
                    return OutputKeyModifierValue.CTRL;
                case InputKeyModifierValue.ALT:
                    return OutputKeyModifierValue.ALT;
                case InputKeyModifierValue.SHIFT:
                    return OutputKeyModifierValue.SHIFT;
            }
            
            throw Errors.parsing();
        }
    }
    
    final static Map<InputKeyValue, Integer> keyboardMappings = Map.ofEntries(
        entry(InputKeyValue.n0, KeyEvent.VK_0),
        entry(InputKeyValue.n1, KeyEvent.VK_1),
        entry(InputKeyValue.n2, KeyEvent.VK_2),
        entry(InputKeyValue.n3, KeyEvent.VK_3),
        entry(InputKeyValue.n4, KeyEvent.VK_4),
        entry(InputKeyValue.n5, KeyEvent.VK_5),
        entry(InputKeyValue.n6, KeyEvent.VK_6),
        entry(InputKeyValue.n7, KeyEvent.VK_7),
        entry(InputKeyValue.n8, KeyEvent.VK_8),
        entry(InputKeyValue.n9, KeyEvent.VK_9),
        
        entry(InputKeyValue.A, KeyEvent.VK_A),
        entry(InputKeyValue.B, KeyEvent.VK_B),
        entry(InputKeyValue.C, KeyEvent.VK_C),
        entry(InputKeyValue.D, KeyEvent.VK_D),
        entry(InputKeyValue.E, KeyEvent.VK_E),
        entry(InputKeyValue.F, KeyEvent.VK_F),
        entry(InputKeyValue.G, KeyEvent.VK_G),
        entry(InputKeyValue.H, KeyEvent.VK_H),
        entry(InputKeyValue.I, KeyEvent.VK_I),
        entry(InputKeyValue.J, KeyEvent.VK_J),
        entry(InputKeyValue.K, KeyEvent.VK_K),
        entry(InputKeyValue.L, KeyEvent.VK_L),
        entry(InputKeyValue.M, KeyEvent.VK_M),
        entry(InputKeyValue.N, KeyEvent.VK_N),
        entry(InputKeyValue.O, KeyEvent.VK_O),
        entry(InputKeyValue.P, KeyEvent.VK_P),
        entry(InputKeyValue.Q, KeyEvent.VK_Q),
        entry(InputKeyValue.R, KeyEvent.VK_R),
        entry(InputKeyValue.S, KeyEvent.VK_S),
        entry(InputKeyValue.T, KeyEvent.VK_T),
        entry(InputKeyValue.U, KeyEvent.VK_U),
        entry(InputKeyValue.V, KeyEvent.VK_V),
        entry(InputKeyValue.W, KeyEvent.VK_W),
        entry(InputKeyValue.X, KeyEvent.VK_X),
        entry(InputKeyValue.Y, KeyEvent.VK_Y),
        entry(InputKeyValue.Z, KeyEvent.VK_Z),
        
        entry(InputKeyValue.SPACE, KeyEvent.VK_SPACE),
        entry(InputKeyValue.ENTER, KeyEvent.VK_ENTER),
        entry(InputKeyValue.ESCAPE, KeyEvent.VK_ESCAPE),
        entry(InputKeyValue.BACKSPACE, KeyEvent.VK_BACK_SPACE),
        entry(InputKeyValue.MINUS, KeyEvent.VK_MINUS),
        entry(InputKeyValue.EQUALS, KeyEvent.VK_EQUALS),
        entry(InputKeyValue.TAB, KeyEvent.VK_TAB),
        entry(InputKeyValue.CAPS_LOCK, KeyEvent.VK_CAPS_LOCK),
        
        entry(InputKeyValue.F1, KeyEvent.VK_F1),
        entry(InputKeyValue.F2, KeyEvent.VK_F2),
        entry(InputKeyValue.F3, KeyEvent.VK_F3),
        entry(InputKeyValue.F4, KeyEvent.VK_F4),
        entry(InputKeyValue.F5, KeyEvent.VK_F5),
        entry(InputKeyValue.F6, KeyEvent.VK_F6),
        entry(InputKeyValue.F7, KeyEvent.VK_F7),
        entry(InputKeyValue.F8, KeyEvent.VK_F8),
        entry(InputKeyValue.F9, KeyEvent.VK_F9),
        entry(InputKeyValue.F10, KeyEvent.VK_F10),
        entry(InputKeyValue.F11, KeyEvent.VK_F11),
        entry(InputKeyValue.F12, KeyEvent.VK_F12),
        
        entry(InputKeyValue.BACKQUOTE, KeyEvent.VK_BACK_QUOTE),
        entry(InputKeyValue.OPEN_BRACKET, KeyEvent.VK_OPEN_BRACKET),
        entry(InputKeyValue.CLOSE_BRACKET, KeyEvent.VK_CLOSE_BRACKET),
        entry(InputKeyValue.BACK_SLASH, KeyEvent.VK_BACK_SLASH),
        entry(InputKeyValue.SEMICOLON, KeyEvent.VK_SEMICOLON),
        entry(InputKeyValue.QUOTE, KeyEvent.VK_QUOTE),
        entry(InputKeyValue.COMMA, KeyEvent.VK_COMMA),
        entry(InputKeyValue.PERIOD, KeyEvent.VK_PERIOD),
        entry(InputKeyValue.SLASH, KeyEvent.VK_SLASH),
        entry(InputKeyValue.PRINTSCREEN, KeyEvent.VK_PRINTSCREEN),
        entry(InputKeyValue.SCROLL_LOCK, KeyEvent.VK_SCROLL_LOCK),
        entry(InputKeyValue.PAUSE, KeyEvent.VK_PAUSE),
        entry(InputKeyValue.INSERT, KeyEvent.VK_INSERT),
        entry(InputKeyValue.DELETE, KeyEvent.VK_DELETE),
        entry(InputKeyValue.HOME, KeyEvent.VK_HOME),
        entry(InputKeyValue.END, KeyEvent.VK_END),
        entry(InputKeyValue.PAGE_UP, KeyEvent.VK_PAGE_UP),
        entry(InputKeyValue.PAGE_DOWN, KeyEvent.VK_PAGE_DOWN),
        entry(InputKeyValue.UP, KeyEvent.VK_UP),
        entry(InputKeyValue.LEFT, KeyEvent.VK_LEFT),
        entry(InputKeyValue.RIGHT, KeyEvent.VK_RIGHT),
        entry(InputKeyValue.DOWN, KeyEvent.VK_DOWN),
        entry(InputKeyValue.NUM_LOCK, KeyEvent.VK_NUM_LOCK),
        entry(InputKeyValue.SEPARATOR, KeyEvent.VK_SEPARATOR),
        entry(InputKeyValue.CONTEXT_MENU, KeyEvent.VK_CONTEXT_MENU)
    );
}
