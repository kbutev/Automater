/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.builder;

import automater.model.InputKeystroke;
import automater.model.KeyEventKind;
import automater.model.MouseKey;
import automater.model.action.MacroAction;
import automater.model.action.MacroHardwareAction;
import automater.storage.StorageValue;
import automater.utilities.Errors;
import automater.validator.CommonValidators;
import automater.validator.ValueValidator;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import automater.model.MutableStorageValue;

/**
 *
 * @author Kristiyan Butev
 */
public interface MacroActionBuilder {
    
    public static @NotNull Protocol buildFromAction(@NotNull MacroAction action) throws Exception {
        var result = new Impl();
        result.kind = KindMap.getKindFromAction(action);
        result.timestamp = action.getTimestamp();
        result.onKindChanged();
        return result;
    }
    
    enum Kind {
        
        DO_NOTHING,
        CLICK,
        MOUSE_CLICK,
        MOUSE_MOVE,
        MOUSE_SCROLL
    }
    
    interface Protocol {
        
        @NotNull StorageValue<Kind> kind();
        @NotNull StorageValue<Double> timestamp();
        
        @NotNull List<MutableStorageValue.Protocol> buildEntryPresenters();
    }
    
    class Impl implements Protocol {
        
        private @NotNull InternalMacroActionBuilder.Protocol internal = new InternalMacroActionBuilder.DoNothing();
        
        private @NotNull Kind kind = Kind.DO_NOTHING;
        private double timestamp = 0;
        
        final @NotNull StorageValue<Kind> kindStorage =
                StorageValue.build(() -> { return kind; }, (var value) -> { setKind(value); });
        
        final @NotNull StorageValue<Double> timestampStorage =
                StorageValue.build(() -> { return timestamp; }, (var value) -> { timestamp = value; });
        
        @Override
        public @NotNull StorageValue<Kind> kind() {
            return kindStorage;
        }
        
        @Override
        public @NotNull StorageValue<Double> timestamp() {
            return timestampStorage;
        }
        
        @Override
        public @NotNull List<MutableStorageValue.Protocol> buildEntryPresenters() {
            var presenters = new ArrayList<MutableStorageValue.Protocol>();
            
            presenters.add(new MutableStorageValue.SimpleDouble("timestamp",
                    timestampStorage, CommonValidators.nonNegativeDouble()));
            
            var internalPresenters = internal.buildEntryPresenters();
            presenters.addAll(internalPresenters);
            
            return presenters;
        }
        
        private void setKind(@NotNull Kind kind) {
            if (this.kind == kind) {
                return;
            }
            
            this.kind = kind;
            
            onKindChanged();
        }
        
        void onKindChanged() {
            switch (kind) {
                case DO_NOTHING:
                    internal = new InternalMacroActionBuilder.DoNothing(); break;
                case CLICK:
                    internal = new InternalMacroActionBuilder.MacroHardwareActionKeyboardClick(); break;
                case MOUSE_CLICK:
                    internal = new InternalMacroActionBuilder.MacroHardwareActionMouseClick(); break;
                case MOUSE_MOVE:
                    internal = new InternalMacroActionBuilder.MacroHardwareActionMouseMove(); break;
                case MOUSE_SCROLL:
                    internal = new InternalMacroActionBuilder.MacroHardwareActionMouseScroll(); break;
                default:
                    throw Errors.parsing();
            }
        }
    }
    
    class KindMap {
        
        static @NotNull Kind getKindFromAction(@NotNull MacroAction action) throws Exception {
            if (action instanceof MacroHardwareAction.Click click) {
                return click.keystroke.isKeyboard() ? Kind.CLICK : Kind.MOUSE_CLICK;
            } else if (action instanceof MacroHardwareAction.MouseMove mm) {
                return Kind.MOUSE_MOVE;
            } else if (action instanceof MacroHardwareAction.MouseScroll ms) {
                return Kind.MOUSE_SCROLL;
            }
            
            throw Errors.parsing();
        }
    }
}
