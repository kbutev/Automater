/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.action;

import automater.model.KeyEventKind;
import automater.model.Keystroke;
import automater.model.Point;
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
        public @NotNull
        String getActionType() {
            return actionType;
        }

        @Override
        public double getTimestamp() {
            return timestamp;
        }
    }

    class Click extends Generic {

        public static final String TYPE = TYPE_PREFIX + "c";

        @SerializedName("k")
        @NotNull public final KeyEventKind kind;

        @SerializedName("v")
        @NotNull public final Keystroke keystroke;

        public Click() {
            this(0, KeyEventKind.tap, Keystroke.anyKey());
        }

        public Click(double timestamp, @NotNull KeyEventKind kind, @NotNull Keystroke value) {
            super(TYPE, timestamp);
            this.kind = kind;
            this.keystroke = value;
        }

        public Click(@NotNull Click other) {
            super(TYPE, other.getTimestamp());
            this.kind = other.kind;
            this.keystroke = other.keystroke;
        }

        @Override
        public @NotNull
        MacroAction copy() {
            return new Click(this);
        }

        public KeyEventKind getEventKind() {
            return kind;
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
        public @NotNull
        MacroAction copy() {
            return new MouseMove(this);
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
        public @NotNull
        MacroAction copy() {
            return new MouseScroll(this);
        }
    }
}
