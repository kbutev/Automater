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
        public final float timestamp;
        
        Generic(float timestamp) {
            this.timestamp = timestamp;
        }
        
        @Override
        public double getTimestamp() {
            return timestamp;
        }
    }
    
    class Click extends Generic {
        @NotNull public final KeyEventKind kind;
        @NotNull public final Keystroke value;
        
        public Click() {
            this(0, KeyEventKind.tap, Keystroke.anyKey());
        }
        
        public Click(float timestamp, @NotNull KeyEventKind kind, @NotNull Keystroke value) {
            super(timestamp);
            this.kind = kind;
            this.value = value;
        }
    }
    
    class MouseMove extends Generic {
        @NotNull public final Point point;
        
        public MouseMove() {
            this(0, Point.zero());
        }
        
        public MouseMove(float timestamp, @NotNull Point point) {
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
        
        public MouseScroll(float timestamp, @NotNull Point point, @NotNull Point scroll) {
            super(timestamp);
            this.point = point;
            this.scroll = scroll;
        }
    }
}
