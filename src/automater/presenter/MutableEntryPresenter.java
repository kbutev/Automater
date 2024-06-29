/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.di.DI;
import automater.model.InputKeystroke;
import automater.router.MutableEntryRouter;
import automater.storage.StorageValue;
import automater.utilities.Callback;
import automater.utilities.Errors;
import automater.utilities.Path;
import automater.validator.ValueValidator;
import java.awt.Component;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface MutableEntryPresenter {
    
    interface Protocol {
        
        @NotNull String getName();
        @NotNull String getValueAsString();
        
        void onEdit(@NotNull Component sender, @NotNull Callback.Blank completion);
    }
    
    class SimpleDouble implements Protocol {
        
        private final @NotNull String name;
        private final @NotNull StorageValue<Double> storage;
        private final @Nullable ValueValidator.Protocol<Double> validator;
        
        public SimpleDouble(@NotNull String name,
                @NotNull StorageValue<Double> storage,
                @Nullable ValueValidator.Protocol<Double> validator) {
            this.name = name;
            this.storage = storage;
            this.validator = validator;
        }
        
        @Override
        public String toString() {
            return name + "|" + getValueAsString();
        }
        
        @Override
        public @NotNull String getName() {
            return name;
        }
        
        @Override
        public @NotNull String getValueAsString() {
            return String.valueOf(storage.get());
        }
        
        @Override
        public void onEdit(@NotNull Component sender, @NotNull Callback.Blank completion) {
            completion.perform();
        }
        
        public void setValue(@NotNull Double value) {
            storage.set(value);
        }
    }
    
    class SimpleString implements Protocol {
        
        private final @NotNull String name;
        private final @NotNull StorageValue<String> storage;
        private final @Nullable ValueValidator.Protocol<String> validator;
        
        public SimpleString(@NotNull String name,
                @NotNull StorageValue<String> storage,
                @Nullable ValueValidator.Protocol<String> validator) {
            this.name = name;
            this.storage = storage;
            this.validator = validator;
        }
        
        @Override
        public String toString() {
            return name + "|" + getValueAsString();
        }
        
        @Override
        public @NotNull String getName() {
            return name;
        }
        
        @Override
        public @NotNull String getValueAsString() {
            return storage.get();
        }
        
        @Override
        public void onEdit(@NotNull Component sender, @NotNull Callback.Blank completion) {
            completion.perform();
        }
        
        public void setValue(@NotNull String value) {
            storage.set(value);
        }
    }
    
    class SystemPath implements Protocol {
        
        private final MutableEntryRouter.Protocol router = DI.get(MutableEntryRouter.Protocol.class);
        
        private final @NotNull String name;
        private final @NotNull StorageValue<Path> storage;
        private final @Nullable ValueValidator.Protocol<Path> validator;
        
        public SystemPath(@NotNull String name,
                @NotNull StorageValue<Path> storage,
                @Nullable ValueValidator.Protocol<Path> validator) {
            this.name = name;
            this.storage = storage;
            this.validator = validator;
        }
        
        @Override
        public String toString() {
            return name + "|" + getValueAsString();
        }
        
        @Override
        public @NotNull String getName() {
            return name;
        }
        
        @Override
        public @NotNull String getValueAsString() {
            var path = storage.get();
            var result = path.toString();
            var limit = 20;
            
            if (result.length() > limit) {
                result = "..." + result.substring(result.length() - limit);
            }
            
            return result;
        }
        
        @Override
        public void onEdit(@NotNull Component sender, @NotNull Callback.Blank completion) {
            router.openPickPathDialog(sender, storage.get(), (var result) -> {
                storage.set(result);
                completion.perform();
            }, completion);
        }
        
        public void setValue(@NotNull Path value) {
            this.storage.set(value);
        }
    }
    
    class Hotkey implements Protocol {
        
        private final MutableEntryRouter.Protocol router = DI.get(MutableEntryRouter.Protocol.class);
        
        private final @NotNull String name;
        private final @NotNull StorageValue<InputKeystroke.AWT> storage;
        
        public Hotkey(@NotNull String name, @NotNull StorageValue<InputKeystroke.AWT> storage) {
            this.name = name;
            this.storage = storage;
        }
        
        @Override
        public String toString() {
            return name + "|" + getValueAsString();
        }
        
        @Override
        public @NotNull String getName() {
            return name;
        }
        
        @Override
        public @NotNull String getValueAsString() {
            return storage.get().toString();
        }
        
        @Override
        public void onEdit(@NotNull Component sender, @NotNull Callback.Blank completion) {
            router.openPickHotkeyDialog(sender, storage.get(), (var result) -> {
                storage.set(result);
                completion.perform();
            }, completion);
        }
        
        public void setValue(@NotNull InputKeystroke.AWT value) {
            this.storage.set(value);
        }
    }
    
    class Keystroke extends Hotkey {
        
        public Keystroke(@NotNull String name, @NotNull StorageValue<InputKeystroke.AWT> storage) {
            super(name, storage);
        }
    }
    
    class Flag implements Protocol {
        
        private final @NotNull String name;
        private final @NotNull StorageValue<Boolean> storage;
        
        public Flag(@NotNull String name, @NotNull StorageValue<Boolean> storage) {
            this.name = name;
            this.storage = storage;
        }
        
        @Override
        public String toString() {
            return name + "|" + getValueAsString();
        }
        
        @Override
        public @NotNull String getName() {
            return name;
        }
        
        @Override
        public @NotNull String getValueAsString() {
            return storage.get() ? "yes" : "no";
        }
        
        @Override
        public void onEdit(@NotNull Component sender, @NotNull Callback.Blank completion) {
            storage.set(!storage.get());
            completion.perform();
        }
        
        public void setValue(@NotNull boolean value) {
            this.storage.set(value);
        }
    }
    
    class EnumList implements Protocol {
        
        private final @NotNull String name;
        private final @NotNull StorageValue<String> storage;
        private final @NotNull List<String> possibleValues;
        
        public EnumList(@NotNull String name,
                @NotNull StorageValue<String> storage,
                @NotNull List<String> possibleValues) {
            this.name = name;
            this.storage = storage;
            this.possibleValues = possibleValues;
            
            if (possibleValues.isEmpty()) {
                throw Errors.illegalStateError();
            }
        }
        
        @Override
        public String toString() {
            return name + "|" + getValueAsString();
        }
        
        @Override
        public @NotNull String getName() {
            return name;
        }
        
        @Override
        public @NotNull String getValueAsString() {
            return storage.get();
        }
        
        @Override
        public void onEdit(@NotNull Component sender, @NotNull Callback.Blank completion) {
            int currentIndex = possibleValues.indexOf(storage.get());
            
            if (currentIndex == -1) {
                throw Errors.illegalStateError();
            }
            
            if (currentIndex+1 < possibleValues.size()) {
                currentIndex += 1;
            } else {
                currentIndex = 0;
            }
            
            storage.set(possibleValues.get(currentIndex));
            
            completion.perform();
        }
        
        public void setValue(@NotNull String value) {
            this.storage.set(value);
        }
    }
}
