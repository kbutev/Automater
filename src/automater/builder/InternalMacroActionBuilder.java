/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.builder;

import automater.model.InputKeystroke;
import automater.model.KeyEventKind;
import automater.model.MouseKey;
import automater.storage.StorageValue;
import automater.validator.CommonValidators;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import automater.model.MutableStorageValue;

/**
 *
 * @author Kristiyan Butev
 */
public interface InternalMacroActionBuilder {
    
    interface Protocol {
        
        @NotNull List<MutableStorageValue.Protocol> buildEntryPresenters();
    }
    
    class DoNothing implements Protocol {
        
        @Override
        public @NotNull List<MutableStorageValue.Protocol> buildEntryPresenters() {
            return new ArrayList<>();
        }
    }
    
    class MacroHardwareActionKeyboardClick implements Protocol {
        
        @NotNull InputKeystroke.AWT keystroke = InputKeystroke.AWT.anyKey();
        @NotNull KeyEventKind eventKind = KeyEventKind.press;
        
        final @NotNull StorageValue<InputKeystroke.AWT> keystrokeStorage =
                StorageValue.build(() -> { return keystroke; }, (var value) -> { keystroke = value; });
        
        final @NotNull StorageValue<KeyEventKind> eventKindStorage =
                StorageValue.build(() -> { return eventKind; }, (var value) -> { eventKind = value; });
        
        @Override
        public @NotNull List<MutableStorageValue.Protocol> buildEntryPresenters() {
            var presenters = new ArrayList<MutableStorageValue.Protocol>();
            
            presenters.add(new MutableStorageValue.Keystroke("keystroke", keystrokeStorage));
            
            var eventStorage =
                StorageValue.build(() -> { return eventKind.value; }, (var value) -> { eventKind = KeyEventKind.build(value); });
            
            presenters.add(new MutableStorageValue.EnumList("event", eventStorage, KeyEventKind.allValues));
            
            return presenters;
        }
    }
    
    class MacroHardwareActionMouseClick implements Protocol {
        
        @NotNull MouseKey key = MouseKey.LEFT;
        @NotNull KeyEventKind eventKind = KeyEventKind.press;
        
        final @NotNull StorageValue<MouseKey> keyStorage =
                StorageValue.build(() -> { return key; }, (var value) -> { key = value; });
        
        final @NotNull StorageValue<KeyEventKind> eventKindStorage =
                StorageValue.build(() -> { return eventKind; }, (var value) -> { eventKind = value; });
        
        @Override
        public @NotNull List<MutableStorageValue.Protocol> buildEntryPresenters() {
            return new ArrayList<>();
        }
    }
    
    class MacroHardwareActionMouseMove implements Protocol {
        
        @Override
        public @NotNull List<MutableStorageValue.Protocol> buildEntryPresenters() {
            return new ArrayList<>();
        }
    }
    
    class MacroHardwareActionMouseScroll implements Protocol {
        
        @Override
        public @NotNull List<MutableStorageValue.Protocol> buildEntryPresenters() {
            return new ArrayList<>();
        }
    }
}
