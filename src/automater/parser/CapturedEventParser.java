/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.di.DI;
import automater.model.KeyEventKind;
import automater.model.InputKeyModifier;
import automater.model.InputKeystroke;
import automater.utilities.Point;
import automater.model.event.CapturedEvent;
import automater.model.event.CapturedHardwareEvent;
import automater.utilities.Errors;
import java.awt.event.KeyEvent;
import org.jetbrains.annotations.NotNull;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jetbrains.annotations.Nullable;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelEvent;

/**
 * Parsers captured events and other related data.
 * 
 * Mutable. Do not share this parser between different services.
 *
 * @author Kristiyan Butev
 */
public interface CapturedEventParser {

    @automater.di.UniqueDependency()
    interface AWTProtocol {

        @NotNull CapturedEvent parseNativeEvent(@NotNull NativeInputEvent event, double time) throws Exception;
        @Nullable CapturedEvent parseNativeKeyboardEvent(@NotNull NativeKeyEvent event, double time, @NotNull KeyEventKind kind) throws Exception;
        @NotNull CapturedEvent parseNativeMouseKeyEvent(@NotNull NativeMouseEvent event, double time, @NotNull KeyEventKind kind) throws Exception;
    }

    class AWTImpl implements AWTProtocol {
        
        private final InputKeyValueParser.AWTProtocol keyValueParser = DI.get(InputKeyValueParser.AWTProtocol.class);

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

            throw Errors.unsupported("Unrecognizable native event");
        }

        @Override
        public @Nullable CapturedEvent parseNativeKeyboardEvent(@NotNull NativeKeyEvent event, double time, @NotNull KeyEventKind kind) throws Exception {
            if (event instanceof NativeKeyEvent key) {
                var code = key.getKeyCode();
                var modifierCode = key.getModifiers();
                
                if (code == KeyEvent.VK_UNDEFINED) {
                    throw Errors.unsupported("Unrecognizablekeyboard key value");
                }
                
                var keyValue = keyValueParser.parseFromKeyboardCode(code);
                var modifier = InputKeyModifier.AWT.buildFromModifierCode(modifierCode);
                var keystroke = new InputKeystroke.AWT(keyValue, modifier);
                return new CapturedHardwareEvent.AWTClick(time, kind, keystroke);
            }

            throw Errors.unsupported("Unrecognizable native event");
        }

        @Override
        public @NotNull CapturedEvent parseNativeMouseKeyEvent(@NotNull NativeMouseEvent event, double time, @NotNull KeyEventKind kind) throws Exception {
            var code = event.getButton();

            if (code == KeyEvent.VK_UNDEFINED) {
                throw Errors.unsupported("Unrecognizablekeyboard mousekey value");
            }

            var keyValue = keyValueParser.parseFromMouseCode(code);
            var keystroke = new InputKeystroke.AWT(keyValue);
            return new CapturedHardwareEvent.AWTClick(time, kind, keystroke);
        }
    }
}