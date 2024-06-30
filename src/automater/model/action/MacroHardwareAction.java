/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.action;

import automater.model.KeyEventKind;
import automater.model.InputKeystroke;
import automater.utilities.Point;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface MacroHardwareAction {

    public static @NotNull String TYPE_PREFIX = "hardware.";

    abstract class Generic implements MacroAction {

        @SerializedName("a")
        @NotNull public final String actionType;

        @SerializedName("t")
        public final double timestamp;

        public Generic(@NotNull String actionType, double timestamp) {
            this.actionType = actionType;
            this.timestamp = timestamp > 0 ? timestamp : 0;
        }

        @Override
        public @NotNull String getActionType() {
            return actionType;
        }

        @Override
        public double getTimestamp() {
            return timestamp;
        }
    }
    
    abstract class Click <KeystrokeT extends InputKeystroke.Protocol> extends Generic {

        public static final String TYPE = TYPE_PREFIX + "c";

        @SerializedName("k")
        @NotNull public final KeyEventKind kind;

        @SerializedName("v")
        @NotNull public final KeystrokeT keystroke;

        public Click(double timestamp, @NotNull KeyEventKind kind, @NotNull KeystrokeT value) {
            super(TYPE, timestamp);
            this.kind = kind;
            this.keystroke = value;
        }

        @Override
        abstract public @NotNull MacroAction copy();

        public KeyEventKind getEventKind() {
            return kind;
        }
    }
    
    class AWTClick extends Click<InputKeystroke.AWT> {
        
        public AWTClick() {
            super(0, KeyEventKind.tap, InputKeystroke.AWT.anyKey());
        }
        
        public AWTClick(@NotNull AWTClick other) {
            super(other.timestamp, other.kind, other.keystroke);
        }
        
        public AWTClick(double timestamp, @NotNull KeyEventKind kind, @NotNull InputKeystroke.AWT value) {
            super(timestamp, kind, value);
        }

        @Override
        public @NotNull MacroAction copy() {
            return new AWTClick(this);
        }
        
        @Override
        public @NotNull String getName() {
            return keystroke.isKeyboard() ? "Keyboard Click" : "Mouse Click";
        }
    }

    class MouseMove extends Generic {

        public static final String TYPE = TYPE_PREFIX + "mm";

        @SerializedName("p")
        @NotNull public final Point point;

        public MouseMove() {
            this(0, Point.zero());
        }

        public MouseMove(double timestamp, @NotNull Point point) {
            super(TYPE, timestamp);
            this.point = point;
        }

        public MouseMove(@NotNull MouseMove other) {
            super(TYPE, other.getTimestamp());
            this.point = other.point;
        }

        @Override
        public @NotNull MacroAction copy() {
            return new MouseMove(this);
        }
        
        @Override
        public @NotNull String getName() {
            return "Move Mouse";
        }
    }

    class MouseScroll extends Generic {

        public static final String TYPE = TYPE_PREFIX + "scrl";

        @SerializedName("p1")
        @NotNull public final Point point;

        @SerializedName("p2")
        @NotNull public final Point scroll;

        public MouseScroll() {
            this(0, Point.zero(), Point.zero());
        }

        public MouseScroll(double timestamp, @NotNull Point point, @NotNull Point scroll) {
            super(TYPE, timestamp);
            this.point = point;
            this.scroll = scroll;
        }

        public MouseScroll(@NotNull MouseScroll other) {
            super(TYPE, other.getTimestamp());
            this.point = other.point;
            this.scroll = other.scroll;
        }

        @Override
        public @NotNull MacroAction copy() {
            return new MouseScroll(this);
        }
        
        @Override
        public @NotNull String getName() {
            return "Scroll Mouse";
        }
    }
}
