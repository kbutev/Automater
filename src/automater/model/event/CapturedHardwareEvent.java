/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.event;

import automater.model.KeyEventKind;
import automater.model.InputKeystroke;
import automater.utilities.Point;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface CapturedHardwareEvent {
    
    abstract class Click <KeystrokeT extends InputKeystroke.Protocol> implements CapturedEvent {
        public final double timestamp;
        public final @NotNull KeyEventKind kind;
        public final @NotNull KeystrokeT keystroke;
        
        public Click(double timestamp, @NotNull KeyEventKind kind, @NotNull KeystrokeT keystroke) {
            this.timestamp = timestamp;
            this.kind = kind;
            this.keystroke = keystroke;
        }
        
        @Override
        public String toString() {
            return kind.toString() + " " + keystroke.toString();
        }
        
        public abstract @NotNull Click withoutModifier();
    }
    
    class AWTClick extends Click<InputKeystroke.AWT> {
        
        public AWTClick() {
            super(0, KeyEventKind.tap, InputKeystroke.AWT.anyKey());
        }
        
        public AWTClick(double timestamp, @NotNull KeyEventKind kind, @NotNull InputKeystroke.AWT keystroke) {
            super(timestamp, kind, keystroke);
        }
        
        @Override
        public @NotNull Click withoutModifier() {
            return new AWTClick(timestamp, kind, keystroke.withoutModifier());
        }
    }
    
    class MouseMove implements CapturedEvent {
        public final double timestamp;
        public final @NotNull Point point;
        
        public MouseMove() {
            this(0, Point.zero());
        }
        
        public MouseMove(double timestamp, @NotNull Point point) {
            this.timestamp = timestamp;
            this.point = point;
        }
        
        @Override
        public String toString() {
            return "mmove " + point.toString();
        }
    }
    
    class MouseScroll implements CapturedEvent {
        public final double timestamp;
        public final @NotNull Point point;
        public final @NotNull Point scroll;
        
        public MouseScroll() {
            this(0, Point.zero(), Point.zero());
        }
        
        public MouseScroll(double timestamp, @NotNull Point point, @NotNull Point scroll) {
            this.timestamp = timestamp;
            this.point = point;
            this.scroll = scroll;
        }
        
        @Override
        public String toString() {
            return "mscroll " + point.toString() + " " + scroll.toString();
        }
    }
}
