/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.event;

import automater.model.KeyEventKind;
import automater.model.Keystroke;
import automater.model.Point;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface CapturedHardwareEvent {
    
    abstract class Generic implements CapturedEvent {
        public final double timestamp;
        
        Generic(double timestamp) {
            this.timestamp = timestamp;
        }
        
        @Override
        public double getTimestamp() {
            return timestamp;
        }
    }
    
    class Click extends Generic {
        @NotNull public final KeyEventKind kind;
        @NotNull public final Keystroke keystroke;
        
        public Click() {
            this(0, KeyEventKind.tap, Keystroke.anyKey());
        }
        
        public Click(double timestamp, @NotNull KeyEventKind kind, @NotNull Keystroke keystroke) {
            super(timestamp);
            this.kind = kind;
            this.keystroke = keystroke;
        }
    }
    
    class MouseMove extends Generic {
        @NotNull public final Point point;
        
        public MouseMove() {
            this(0, Point.zero());
        }
        
        public MouseMove(double timestamp, @NotNull Point point) {
            super(timestamp);
            this.point = point;
        }
    }
    
    class MouseScroll extends Generic {
        @NotNull public final Point point;
        @NotNull public final Point scroll;
        
        public MouseScroll() {
            this(0, Point.zero(), Point.zero());
        }
        
        public MouseScroll(double timestamp, @NotNull Point point, @NotNull Point scroll) {
            super(timestamp);
            this.point = point;
            this.scroll = scroll;
        }
    }
}
