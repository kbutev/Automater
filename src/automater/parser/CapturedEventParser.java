/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.model.KeyEventKind;
import automater.model.KeyValue;
import automater.model.Keystroke;
import automater.model.Point;
import automater.model.event.CapturedEvent;
import automater.model.event.CapturedHardwareEvent;
import automater.model.event.EventDescription;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import static java.util.Map.entry;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelEvent;

/**
 * Parsers captured events and other related data.
 * @author Kristiyan Butev
 */
public interface CapturedEventParser {
    
    interface Protocol {
        @NotNull CapturedEvent parseNativeEvent(@NotNull NativeInputEvent event, double time) throws Exception;
        @NotNull CapturedEvent parseNativeKeyboardEvent(@NotNull NativeKeyEvent event, double time, @NotNull KeyEventKind kind) throws Exception;
        @NotNull CapturedEvent parseNativeMouseKeyEvent(@NotNull NativeMouseEvent event, double time, @NotNull KeyEventKind kind) throws Exception;
        @NotNull EventDescription parseToDescription(@NotNull CapturedEvent event) throws Exception;
    }
    
    class Impl implements Protocol {
        @Override
        public @NotNull CapturedEvent parseNativeEvent(@NotNull NativeInputEvent event, double time) throws Exception {
            if (event instanceof NativeMouseEvent mmove) {
                var point = Point.make(mmove.getX(), mmove.getY());
                return new CapturedHardwareEvent.MouseMove(time, point);
            } else if (event instanceof NativeMouseWheelEvent scroll) {
                var y = (double)scroll.getScrollAmount();
                
                if (scroll.getWheelRotation() == 1) {
                    y *= -1;
                }
                
                var scrollPoint = Point.make(0, y);
                return new CapturedHardwareEvent.MouseScroll(time, Point.zero(), scrollPoint);
            }
            
            throw new UnsupportedOperationException("Unrecognizable native event");
        }
        
        @Override
        public @NotNull CapturedEvent parseNativeKeyboardEvent(@NotNull NativeKeyEvent event, double time, @NotNull KeyEventKind kind) throws Exception {
            if (event instanceof NativeKeyEvent key) {
                var code = key.getKeyCode();
                var keyValue = keyboardMappings.get(code);
                
                if (keyValue == null) {
                    throw new UnsupportedOperationException("Unrecognizable keyboard key value");
                }
                
                var keystroke = new Keystroke(keyValue);
                return new CapturedHardwareEvent.Click(time, kind, keystroke);
            }
            
            throw new UnsupportedOperationException("Unrecognizable native event");
        }
        
        @Override
        public @NotNull CapturedEvent parseNativeMouseKeyEvent(@NotNull NativeMouseEvent event, double time, @NotNull KeyEventKind kind) throws Exception {
            var keyValue = mouseKeyMappings.get(event.getButton());
            
            if (keyValue == null) {
                throw new UnsupportedOperationException("Unrecognizable mouse key value");
            }
            
            var keystroke = new Keystroke(keyValue);
            return new CapturedHardwareEvent.Click(time, kind, keystroke);
        }
        
        @Override
        public @NotNull EventDescription parseToDescription(@NotNull CapturedEvent event) throws Exception {
            var timestamp = String.format("%.1f", event.getTimestamp());
            
            if (event instanceof CapturedHardwareEvent.Click click) {
                return new EventDescription(timestamp, "click", click.keystroke.toString());
            } else if (event instanceof CapturedHardwareEvent.MouseMove mmove) {
                return new EventDescription(timestamp, "mouse move", mmove.point.toString());
            } else if (event instanceof CapturedHardwareEvent.MouseScroll scroll) {
                return new EventDescription(timestamp, "mouse scroll", scroll.scroll.toString());
            }
            
            throw new UnsupportedOperationException("Unrecognizable native event");
        }
    }
    
    final static Map<Integer, KeyValue> mouseKeyMappings = Map.ofEntries(
        entry(NativeMouseEvent.BUTTON1, KeyValue._MOUSE_LEFT_CLICK),
        entry(NativeMouseEvent.BUTTON2, KeyValue._MOUSE_RIGHT_CLICK),
        entry(NativeMouseEvent.BUTTON3, KeyValue._MOUSE_MIDDLE_CLICK),
        entry(NativeMouseEvent.BUTTON4, KeyValue._MOUSE_4_CLICK),
        entry(NativeMouseEvent.BUTTON5, KeyValue._MOUSE_5_CLICK)
    );
    
    final static Map<Integer, KeyValue> keyboardMappings = Map.ofEntries(
        entry(NativeKeyEvent.VC_1, KeyValue._1),
        entry(NativeKeyEvent.VC_2, KeyValue._2),
        entry(NativeKeyEvent.VC_3, KeyValue._3),
        entry(NativeKeyEvent.VC_4, KeyValue._4),
        entry(NativeKeyEvent.VC_5, KeyValue._5),
        entry(NativeKeyEvent.VC_6, KeyValue._6),
        entry(NativeKeyEvent.VC_7, KeyValue._7),
        entry(NativeKeyEvent.VC_8, KeyValue._8),
        entry(NativeKeyEvent.VC_9, KeyValue._9),
        
        entry(NativeKeyEvent.VC_A, KeyValue._A),
        entry(NativeKeyEvent.VC_B, KeyValue._B),
        entry(NativeKeyEvent.VC_C, KeyValue._C),
        entry(NativeKeyEvent.VC_D, KeyValue._D),
        entry(NativeKeyEvent.VC_E, KeyValue._E),
        entry(NativeKeyEvent.VC_F, KeyValue._F),
        entry(NativeKeyEvent.VC_G, KeyValue._G),
        entry(NativeKeyEvent.VC_H, KeyValue._H),
        entry(NativeKeyEvent.VC_I, KeyValue._I),
        entry(NativeKeyEvent.VC_J, KeyValue._J),
        entry(NativeKeyEvent.VC_K, KeyValue._K),
        entry(NativeKeyEvent.VC_L, KeyValue._L),
        entry(NativeKeyEvent.VC_M, KeyValue._M),
        entry(NativeKeyEvent.VC_N, KeyValue._N),
        entry(NativeKeyEvent.VC_O, KeyValue._O),
        entry(NativeKeyEvent.VC_P, KeyValue._P),
        entry(NativeKeyEvent.VC_Q, KeyValue._Q),
        entry(NativeKeyEvent.VC_R, KeyValue._R),
        entry(NativeKeyEvent.VC_S, KeyValue._S),
        entry(NativeKeyEvent.VC_T, KeyValue._T),
        entry(NativeKeyEvent.VC_U, KeyValue._U),
        entry(NativeKeyEvent.VC_V, KeyValue._V),
        entry(NativeKeyEvent.VC_W, KeyValue._W),
        entry(NativeKeyEvent.VC_X, KeyValue._X),
        entry(NativeKeyEvent.VC_Y, KeyValue._Y),
        entry(NativeKeyEvent.VC_Z, KeyValue._Z),
        
        entry(NativeKeyEvent.VC_SHIFT, KeyValue._SHIFT),
        entry(NativeKeyEvent.VC_CONTROL, KeyValue._CONTROL),
        entry(NativeKeyEvent.VC_ALT, KeyValue._ALT),
        
        entry(NativeKeyEvent.VC_SPACE, KeyValue._SPACE),
        entry(NativeKeyEvent.VC_ENTER, KeyValue._ENTER),
        entry(NativeKeyEvent.VC_ESCAPE, KeyValue._ESCAPE),
        entry(NativeKeyEvent.VC_BACKSPACE, KeyValue._BACKSPACE),
        entry(NativeKeyEvent.VC_MINUS, KeyValue._MINUS),
        entry(NativeKeyEvent.VC_EQUALS, KeyValue._EQUALS),
        entry(NativeKeyEvent.VC_TAB, KeyValue._TAB),
        entry(NativeKeyEvent.VC_CAPS_LOCK, KeyValue._CAPS_LOCK),
        
        entry(NativeKeyEvent.VC_F1, KeyValue._F1),
        entry(NativeKeyEvent.VC_F2, KeyValue._F2),
        entry(NativeKeyEvent.VC_F3, KeyValue._F3),
        entry(NativeKeyEvent.VC_F4, KeyValue._F4),
        entry(NativeKeyEvent.VC_F5, KeyValue._F5),
        entry(NativeKeyEvent.VC_F6, KeyValue._F6),
        entry(NativeKeyEvent.VC_F7, KeyValue._F7),
        entry(NativeKeyEvent.VC_F8, KeyValue._F8),
        entry(NativeKeyEvent.VC_F9, KeyValue._F9),
        entry(NativeKeyEvent.VC_F10, KeyValue._F10),
        entry(NativeKeyEvent.VC_F11, KeyValue._F11),
        entry(NativeKeyEvent.VC_F12, KeyValue._F12),
        
        entry(NativeKeyEvent.VC_BACKQUOTE, KeyValue._BACKQUOTE),
        entry(NativeKeyEvent.VC_OPEN_BRACKET, KeyValue._OPEN_BRACKET),
        entry(NativeKeyEvent.VC_CLOSE_BRACKET, KeyValue._CLOSE_BRACKET),
        entry(NativeKeyEvent.VC_BACK_SLASH, KeyValue._BACK_SLASH),
        entry(NativeKeyEvent.VC_SEMICOLON, KeyValue._SEMICOLON),
        entry(NativeKeyEvent.VC_QUOTE, KeyValue._QUOTE),
        entry(NativeKeyEvent.VC_COMMA, KeyValue._COMMA),
        entry(NativeKeyEvent.VC_PERIOD, KeyValue._PERIOD),
        entry(NativeKeyEvent.VC_SLASH, KeyValue._SLASH),
        entry(NativeKeyEvent.VC_PRINTSCREEN, KeyValue._PRINTSCREEN),
        entry(NativeKeyEvent.VC_SCROLL_LOCK, KeyValue._SCROLL_LOCK),
        entry(NativeKeyEvent.VC_PAUSE, KeyValue._PAUSE),
        entry(NativeKeyEvent.VC_INSERT, KeyValue._INSERT),
        entry(NativeKeyEvent.VC_DELETE, KeyValue._DELETE),
        entry(NativeKeyEvent.VC_HOME, KeyValue._HOME),
        entry(NativeKeyEvent.VC_END, KeyValue._END),
        entry(NativeKeyEvent.VC_PAGE_UP, KeyValue._PAGE_UP),
        entry(NativeKeyEvent.VC_PAGE_DOWN, KeyValue._PAGE_DOWN),
        entry(NativeKeyEvent.VC_UP, KeyValue._UP),
        entry(NativeKeyEvent.VC_LEFT, KeyValue._LEFT),
        entry(NativeKeyEvent.VC_CLEAR, KeyValue._CLEAR),
        entry(NativeKeyEvent.VC_RIGHT, KeyValue._RIGHT),
        entry(NativeKeyEvent.VC_DOWN, KeyValue._DOWN),
        entry(NativeKeyEvent.VC_NUM_LOCK, KeyValue._NUM_LOCK),
        entry(NativeKeyEvent.VC_SEPARATOR, KeyValue._SEPARATOR),
        entry(NativeKeyEvent.VC_META, KeyValue._META),
        entry(NativeKeyEvent.VC_CONTEXT_MENU, KeyValue._CONTEXT_MENU),
        entry(NativeKeyEvent.VC_POWER, KeyValue._POWER),
        entry(NativeKeyEvent.VC_SLEEP, KeyValue._SLEEP),
        entry(NativeKeyEvent.VC_WAKE, KeyValue._WAKE),
        entry(NativeKeyEvent.VC_MEDIA_PLAY, KeyValue._MEDIA_PLAY),
        entry(NativeKeyEvent.VC_MEDIA_STOP, KeyValue._MEDIA_STOP),
        entry(NativeKeyEvent.VC_MEDIA_PREVIOUS, KeyValue._MEDIA_PREVIOUS),
        entry(NativeKeyEvent.VC_MEDIA_NEXT, KeyValue._MEDIA_NEXT),
        entry(NativeKeyEvent.VC_MEDIA_SELECT, KeyValue._MEDIA_SELECT),
        entry(NativeKeyEvent.VC_MEDIA_EJECT, KeyValue._MEDIA_EJECT),
        entry(NativeKeyEvent.VC_VOLUME_MUTE, KeyValue._VOLUME_MUTE),
        entry(NativeKeyEvent.VC_VOLUME_UP, KeyValue._VOLUME_UP),
        entry(NativeKeyEvent.VC_VOLUME_DOWN, KeyValue._VOLUME_DOWN)
    );
}
