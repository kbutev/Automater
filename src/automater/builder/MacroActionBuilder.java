/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.builder;

import automater.model.action.MacroAction;
import automater.model.action.MacroHardwareAction;
import automater.storage.StorageValue;
import automater.utilities.Errors;
import automater.validator.CommonValidators;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import automater.model.MutableStorageValue;
import automater.model.action.DoNothing;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface MacroActionBuilder {
    
    public static @NotNull Protocol buildFromAction(@NotNull MacroAction action) throws Exception {
        var result = new Impl();
        result.setupWithAction(action);
        return result;
    }
    
    enum Kind {
        
        DO_NOTHING("do nothing"),
        CLICK("click"),
        MOUSE_CLICK("mouse click"),
        MOUSE_MOVE("mouse move"),
        MOUSE_SCROLL("mouse scroll");
        
        public static final List<String> allValuesAsStrings = allValuesAsStrings();
        
        public final @NotNull String value;
        
        Kind(@NotNull String value) {
            this.value = value;
        }
        
        public static @NotNull List<String> allValuesAsStrings() {
            ArrayList<String> values = new ArrayList<>();
            
            for (var kind : Kind.values()) {
                values.add(kind.value);
            }
            
            return values;
        }
    }
    
    interface Protocol {
        
        @NotNull String getActionName();
        @NotNull StorageValue<Kind> kind();
        @NotNull StorageValue<Double> timestamp();
        
        @NotNull List<MutableStorageValue.Protocol> getMutableValues();
        
        @NotNull MacroAction build() throws Exception;
    }
    
    class Impl implements Protocol {
        
        public static final int TIMESTAMP_MAX_DECIMAL_DIGITS = 3;
        
        private @NotNull InternalMacroActionBuilder.Protocol internal = new InternalMacroActionBuilder.DoNothing();
        
        private @NotNull Kind kind = Kind.DO_NOTHING;
        private double timestamp = 0;
        
        final @NotNull StorageValue<Kind> kindStorage =
                StorageValue.build(() -> { return kind; }, (var value) -> { setKind(value); });
        
        final @NotNull StorageValue<Double> timestampStorage =
                StorageValue.build(() -> { return timestamp; }, (var value) -> { timestamp = value; });
        
        @Override
        public @NotNull String getActionName() {
            return internal.getActionName();
        }
        
        @Override
        public @NotNull StorageValue<Kind> kind() {
            return kindStorage;
        }
        
        @Override
        public @NotNull StorageValue<Double> timestamp() {
            return timestampStorage;
        }
        
        @Override
        public @NotNull List<MutableStorageValue.Protocol> getMutableValues() {
            var values = new ArrayList<MutableStorageValue.Protocol>();
            
            values.add(new MutableStorageValue.SimpleNumber("timestamp",
                    timestampStorage, CommonValidators.nonNegativeDouble(TIMESTAMP_MAX_DECIMAL_DIGITS)));
            
            var internalValues = internal.buildValues();
            values.addAll(internalValues);
            
            return values;
        }
        
        @Override
        public @NotNull MacroAction build() throws Exception {
            return internal.build(timestamp);
        }
        
        private void setKind(@NotNull Kind kind) {
            if (this.kind == kind) {
                return;
            }
            
            this.kind = kind;
            
            onKindChanged();
        }
        
        private void onKindChanged() {
            onKindChanged(null);
        }
        
        private void onKindChanged(@Nullable MacroAction action) {
            switch (kind) {
                case DO_NOTHING:
                    internal = setupDoNothing();
                    break;
                case CLICK:
                    internal = setupKeyboardClickBuilder(action);
                    break;
                case MOUSE_CLICK:
                    internal = setupMouseClickBuilder(action);
                    break;
                case MOUSE_MOVE:
                    internal = setupMouseMoveBuilder(action);
                    break;
                case MOUSE_SCROLL:
                    internal = setupMouseScrollBuilder(action);
                    break;
                default:
                    throw Errors.parsing();
            }
        }
        
        private void setupWithAction(@NotNull MacroAction action) throws Exception {
            kind = KindMap.getKindFromAction(action);
            timestamp = action.getTimestamp();
            onKindChanged(action);
        }
        
        private InternalMacroActionBuilder.Protocol setupDoNothing() {
            return new InternalMacroActionBuilder.DoNothing();
        }
        
        private InternalMacroActionBuilder.Protocol setupKeyboardClickBuilder(@Nullable MacroAction action) {
            var builder = new InternalMacroActionBuilder.MacroHardwareActionKeyboardClick();

            if (action instanceof MacroHardwareAction.AWTClick click) {
                builder.keystroke = click.keystroke;
                builder.eventKind = click.kind;
            }
            
            return builder;
        }
        
        private InternalMacroActionBuilder.Protocol setupMouseClickBuilder(@Nullable MacroAction action) {
            var builder = new InternalMacroActionBuilder.MacroHardwareActionMouseClick();

            if (action instanceof MacroHardwareAction.AWTClick click) {
                builder.key = click.keystroke.value.getMouseKey();
                builder.eventKind = click.kind;
            }
            
            return builder;
        }
        
        private InternalMacroActionBuilder.Protocol setupMouseMoveBuilder(@Nullable MacroAction action) {
            var builder = new InternalMacroActionBuilder.MacroHardwareActionMouseMove();

            if (action instanceof MacroHardwareAction.MouseMove move) {
                builder.point = move.point;
            }
            
            return builder;
        }
        
        private InternalMacroActionBuilder.Protocol setupMouseScrollBuilder(@Nullable MacroAction action) {
            var builder = new InternalMacroActionBuilder.MacroHardwareActionMouseScroll();

            if (action instanceof MacroHardwareAction.MouseScroll scroll) {
                builder.scrollValue = scroll.scroll.y;
            }
            
            return builder;
        }
    }
    
    class KindMap {
        
        static @NotNull Kind getKindFromAction(@NotNull MacroAction action) throws Exception {
            if (action instanceof DoNothing) {
                return Kind.DO_NOTHING;
            } else if (action instanceof MacroHardwareAction.Click click) {
                return click.keystroke.isKeyboard() ? Kind.CLICK : Kind.MOUSE_CLICK;
            } else if (action instanceof MacroHardwareAction.MouseMove) {
                return Kind.MOUSE_MOVE;
            } else if (action instanceof MacroHardwareAction.MouseScroll) {
                return Kind.MOUSE_SCROLL;
            }
            
            throw Errors.parsing();
        }
    }
}
