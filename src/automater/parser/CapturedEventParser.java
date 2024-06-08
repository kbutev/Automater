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
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import static java.util.Map.entry;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelEvent;

/**
 * Parsers captured events and other related data.
 *
 * @author Kristiyan Butev
 */
public interface CapturedEventParser {

    interface Protocol {

        @NotNull CapturedEvent parseNativeEvent(@NotNull NativeInputEvent event, double time) throws Exception;
        @NotNull CapturedEvent parseNativeKeyboardEvent(@NotNull NativeKeyEvent event, double time, @NotNull KeyEventKind kind) throws Exception;
        @NotNull CapturedEvent parseNativeMouseKeyEvent(@NotNull NativeMouseEvent event, double time, @NotNull KeyEventKind kind) throws Exception;
    }

    class Impl implements Protocol {

        @Override
        public @NotNull CapturedEvent parseNativeEvent(@NotNull NativeInputEvent event, double time) throws Exception {
            if (event instanceof NativeMouseEvent mmove) {
                var point = Point.make(mmove.getX(), mmove.getY());
                return new CapturedHardwareEvent.MouseMove(time, point);
            } else if (event instanceof NativeMouseWheelEvent scroll) {
                var y = (double) scroll.getScrollAmount();

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
                
                if (keyValue.isModifier()) {
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
    }

    final static Map<Integer, KeyValue> mouseKeyMappings = Map.ofEntries(
            entry(NativeMouseEvent.BUTTON1, KeyValue.MOUSE_LEFT_CLICK),
            entry(NativeMouseEvent.BUTTON2, KeyValue.MOUSE_RIGHT_CLICK),
            entry(NativeMouseEvent.BUTTON3, KeyValue.MOUSE_MIDDLE_CLICK),
            entry(NativeMouseEvent.BUTTON4, KeyValue.MOUSE_4_CLICK),
            entry(NativeMouseEvent.BUTTON5, KeyValue.MOUSE_5_CLICK)
    );

    final static Map<Integer, KeyValue> keyboardMappings = Map.ofEntries(
            entry(NativeKeyEvent.VC_1, KeyValue.n1),
            entry(NativeKeyEvent.VC_2, KeyValue.n2),
            entry(NativeKeyEvent.VC_3, KeyValue.n3),
            entry(NativeKeyEvent.VC_4, KeyValue.n4),
            entry(NativeKeyEvent.VC_5, KeyValue.n5),
            entry(NativeKeyEvent.VC_6, KeyValue.n6),
            entry(NativeKeyEvent.VC_7, KeyValue.n7),
            entry(NativeKeyEvent.VC_8, KeyValue.n8),
            entry(NativeKeyEvent.VC_9, KeyValue.n9),
            entry(NativeKeyEvent.VC_0, KeyValue.n0),
            entry(NativeKeyEvent.VC_A, KeyValue.A),
            entry(NativeKeyEvent.VC_B, KeyValue.B),
            entry(NativeKeyEvent.VC_C, KeyValue.C),
            entry(NativeKeyEvent.VC_D, KeyValue.D),
            entry(NativeKeyEvent.VC_E, KeyValue.E),
            entry(NativeKeyEvent.VC_F, KeyValue.F),
            entry(NativeKeyEvent.VC_G, KeyValue.G),
            entry(NativeKeyEvent.VC_H, KeyValue.H),
            entry(NativeKeyEvent.VC_I, KeyValue.I),
            entry(NativeKeyEvent.VC_J, KeyValue.J),
            entry(NativeKeyEvent.VC_K, KeyValue.K),
            entry(NativeKeyEvent.VC_L, KeyValue.L),
            entry(NativeKeyEvent.VC_M, KeyValue.M),
            entry(NativeKeyEvent.VC_N, KeyValue.N),
            entry(NativeKeyEvent.VC_O, KeyValue.O),
            entry(NativeKeyEvent.VC_P, KeyValue.P),
            entry(NativeKeyEvent.VC_Q, KeyValue.Q),
            entry(NativeKeyEvent.VC_R, KeyValue.R),
            entry(NativeKeyEvent.VC_S, KeyValue.S),
            entry(NativeKeyEvent.VC_T, KeyValue.T),
            entry(NativeKeyEvent.VC_U, KeyValue.U),
            entry(NativeKeyEvent.VC_V, KeyValue.V),
            entry(NativeKeyEvent.VC_W, KeyValue.W),
            entry(NativeKeyEvent.VC_X, KeyValue.X),
            entry(NativeKeyEvent.VC_Y, KeyValue.Y),
            entry(NativeKeyEvent.VC_Z, KeyValue.Z),
            entry(NativeKeyEvent.VC_SHIFT, KeyValue.SHIFT),
            entry(NativeKeyEvent.VC_CONTROL, KeyValue.CONTROL),
            entry(NativeKeyEvent.VC_ALT, KeyValue.ALT),
            entry(NativeKeyEvent.VC_SPACE, KeyValue.SPACE),
            entry(NativeKeyEvent.VC_ENTER, KeyValue.ENTER),
            entry(NativeKeyEvent.VC_ESCAPE, KeyValue.ESCAPE),
            entry(NativeKeyEvent.VC_BACKSPACE, KeyValue.BACKSPACE),
            entry(NativeKeyEvent.VC_MINUS, KeyValue.MINUS),
            entry(NativeKeyEvent.VC_EQUALS, KeyValue.EQUALS),
            entry(NativeKeyEvent.VC_TAB, KeyValue.TAB),
            entry(NativeKeyEvent.VC_CAPS_LOCK, KeyValue.CAPS_LOCK),
            entry(NativeKeyEvent.VC_F1, KeyValue.F1),
            entry(NativeKeyEvent.VC_F2, KeyValue.F2),
            entry(NativeKeyEvent.VC_F3, KeyValue.F3),
            entry(NativeKeyEvent.VC_F4, KeyValue.F4),
            entry(NativeKeyEvent.VC_F5, KeyValue.F5),
            entry(NativeKeyEvent.VC_F6, KeyValue.F6),
            entry(NativeKeyEvent.VC_F7, KeyValue.F7),
            entry(NativeKeyEvent.VC_F8, KeyValue.F8),
            entry(NativeKeyEvent.VC_F9, KeyValue.F9),
            entry(NativeKeyEvent.VC_F10, KeyValue.F10),
            entry(NativeKeyEvent.VC_F11, KeyValue.F11),
            entry(NativeKeyEvent.VC_F12, KeyValue.F12),
            entry(NativeKeyEvent.VC_BACKQUOTE, KeyValue.BACKQUOTE),
            entry(NativeKeyEvent.VC_OPEN_BRACKET, KeyValue.OPEN_BRACKET),
            entry(NativeKeyEvent.VC_CLOSE_BRACKET, KeyValue.CLOSE_BRACKET),
            entry(NativeKeyEvent.VC_BACK_SLASH, KeyValue.BACK_SLASH),
            entry(NativeKeyEvent.VC_SEMICOLON, KeyValue.SEMICOLON),
            entry(NativeKeyEvent.VC_QUOTE, KeyValue.QUOTE),
            entry(NativeKeyEvent.VC_COMMA, KeyValue.COMMA),
            entry(NativeKeyEvent.VC_PERIOD, KeyValue.PERIOD),
            entry(NativeKeyEvent.VC_SLASH, KeyValue.SLASH),
            entry(NativeKeyEvent.VC_PRINTSCREEN, KeyValue.PRINTSCREEN),
            entry(NativeKeyEvent.VC_SCROLL_LOCK, KeyValue.SCROLL_LOCK),
            entry(NativeKeyEvent.VC_PAUSE, KeyValue.PAUSE),
            entry(NativeKeyEvent.VC_INSERT, KeyValue.INSERT),
            entry(NativeKeyEvent.VC_DELETE, KeyValue.DELETE),
            entry(NativeKeyEvent.VC_HOME, KeyValue.HOME),
            entry(NativeKeyEvent.VC_END, KeyValue.END),
            entry(NativeKeyEvent.VC_PAGE_UP, KeyValue.PAGE_UP),
            entry(NativeKeyEvent.VC_PAGE_DOWN, KeyValue.PAGE_DOWN),
            entry(NativeKeyEvent.VC_UP, KeyValue.UP),
            entry(NativeKeyEvent.VC_LEFT, KeyValue.LEFT),
            entry(NativeKeyEvent.VC_CLEAR, KeyValue.CLEAR),
            entry(NativeKeyEvent.VC_RIGHT, KeyValue.RIGHT),
            entry(NativeKeyEvent.VC_DOWN, KeyValue.DOWN),
            entry(NativeKeyEvent.VC_NUM_LOCK, KeyValue.NUM_LOCK),
            entry(NativeKeyEvent.VC_SEPARATOR, KeyValue.SEPARATOR),
            entry(NativeKeyEvent.VC_META, KeyValue.META),
            entry(NativeKeyEvent.VC_CONTEXT_MENU, KeyValue.CONTEXT_MENU),
            entry(NativeKeyEvent.VC_POWER, KeyValue.POWER),
            entry(NativeKeyEvent.VC_SLEEP, KeyValue.SLEEP),
            entry(NativeKeyEvent.VC_WAKE, KeyValue.WAKE),
            entry(NativeKeyEvent.VC_MEDIA_PLAY, KeyValue.MEDIA_PLAY),
            entry(NativeKeyEvent.VC_MEDIA_STOP, KeyValue.MEDIA_STOP),
            entry(NativeKeyEvent.VC_MEDIA_PREVIOUS, KeyValue.MEDIA_PREVIOUS),
            entry(NativeKeyEvent.VC_MEDIA_NEXT, KeyValue.MEDIA_NEXT),
            entry(NativeKeyEvent.VC_MEDIA_SELECT, KeyValue.MEDIA_SELECT),
            entry(NativeKeyEvent.VC_MEDIA_EJECT, KeyValue.MEDIA_EJECT),
            entry(NativeKeyEvent.VC_VOLUME_MUTE, KeyValue.VOLUME_MUTE),
            entry(NativeKeyEvent.VC_VOLUME_UP, KeyValue.VOLUME_UP),
            entry(NativeKeyEvent.VC_VOLUME_DOWN, KeyValue.VOLUME_DOWN)
    );
}
