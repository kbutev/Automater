/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.di.DI;
import automater.model.KeyEventKind;
import automater.model.InputKeyModifier;
import automater.model.InputKeyModifierValue;
import automater.model.InputKeystroke;
import automater.model.Point;
import automater.model.event.CapturedEvent;
import automater.model.event.CapturedHardwareEvent;
import automater.utilities.Errors;
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
    interface Protocol {

        @NotNull CapturedEvent parseNativeEvent(@NotNull NativeInputEvent event, double time) throws Exception;
        @Nullable CapturedEvent parseNativeKeyboardEvent(@NotNull NativeKeyEvent event, double time, @NotNull KeyEventKind kind) throws Exception;
        @NotNull CapturedEvent parseNativeMouseKeyEvent(@NotNull NativeMouseEvent event, double time, @NotNull KeyEventKind kind) throws Exception;
    }

    class Impl implements Protocol {
        
        private final InputKeyValueParser.Protocol keyValueParser = DI.get(InputKeyValueParser.Protocol.class);
        private @NotNull InputKeyModifier currentModifier = new InputKeyModifier();

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
                var keyValue = keyValueParser.parseFromKeyboardCode(key.getKeyCode());

                if (keyValue == null) {
                    throw Errors.unsupported("Unrecognizablekeyboard key value");
                }
                
                if (keyValue.isModifier()) {
                    var modifier = InputKeyModifierValue.build(keyValue);
                    
                    if (modifier != null) {
                        if (kind == KeyEventKind.press) {
                            currentModifier = currentModifier.withModifierAdded(modifier);
                        } else if (kind == KeyEventKind.release) {
                            currentModifier = currentModifier.withModifierRemoved(modifier);
                        }
                    }
                    
                    return null;
                }
                
                var keystroke = new InputKeystroke(keyValue, currentModifier);
                return new CapturedHardwareEvent.Click(time, kind, keystroke);
            }

            throw Errors.unsupported("Unrecognizable native event");
        }

        @Override
        public @NotNull CapturedEvent parseNativeMouseKeyEvent(@NotNull NativeMouseEvent event, double time, @NotNull KeyEventKind kind) throws Exception {
            var keyValue = keyValueParser.parseFromMouseCode(event.getButton());

            if (keyValue == null) {
                throw Errors.unsupported("Unrecognizablekeyboard mousekey value");
            }

            var keystroke = new InputKeystroke(keyValue);
            return new CapturedHardwareEvent.Click(time, kind, keystroke);
        }
    }
}