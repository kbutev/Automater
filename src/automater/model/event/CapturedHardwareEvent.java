/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.event;

import automater.model.KeyEventKind;
import automater.model.InputKeystroke;
import automater.model.Point;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface CapturedHardwareEvent {
    
    class Click implements CapturedEvent {
        @NotNull public final KeyEventKind kind;
        @NotNull public final InputKeystroke keystroke;
        
        public Click() {
            this(0, KeyEventKind.tap, InputKeystroke.anyKey());
        }
        
        public Click(double timestamp, @NotNull KeyEventKind kind, @NotNull InputKeystroke keystroke) {
            this.kind = kind;
            this.keystroke = keystroke;
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
    }
}
