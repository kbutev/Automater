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
        public final @NotNull KeyEventKind kind;
        public final @NotNull KeystrokeT keystroke;
        
        public Click(double timestamp, @NotNull KeyEventKind kind, @NotNull KeystrokeT keystroke) {
            this.kind = kind;
            this.keystroke = keystroke;
        }
        
        @Override
        public String toString() {
            return kind.toString() + " " + keystroke.toString();
        }
    }
    
    class AWTClick extends Click<InputKeystroke.AWT> {
        
        public AWTClick() {
            super(0, KeyEventKind.tap, InputKeystroke.AWT.anyKey());
        }
        
        public AWTClick(double timestamp, @NotNull KeyEventKind kind, @NotNull InputKeystroke.AWT keystroke) {
            super(timestamp, kind, keystroke);
        }
    }
    
    class MouseMove implements CapturedEvent {
        @NotNull public final Point point;
        
        public MouseMove() {
            this(0, Point.zero());
        }
        
        public MouseMove(double timestamp, @NotNull Point point) {
            this.point = point;
        }
        
        @Override
        public String toString() {
            return "mmove " + point.toString();
        }
    }
    
    class MouseScroll implements CapturedEvent {
        @NotNull public final Point point;
        @NotNull public final Point scroll;
        
        public MouseScroll() {
            this(0, Point.zero(), Point.zero());
        }
        
        public MouseScroll(double timestamp, @NotNull Point point, @NotNull Point scroll) {
            this.point = point;
            this.scroll = scroll;
        }
        
        @Override
        public String toString() {
            return "mscroll " + point.toString() + " " + scroll.toString();
        }
    }
}
