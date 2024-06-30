/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import automater.storage.StorageValue;
import automater.utilities.Errors;
import automater.utilities.Path;
import automater.validator.ValueValidator;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface MutableStorageValue {
    
    interface Protocol {
        
        @NotNull String getName();
        @NotNull String getValueAsString();
    }
    
    /// Represents the value can represent a small list of possible values.
    interface Cycling {
        
        @NotNull List<String> allValues();
        void pickNextValue();
    }
    
    class SimpleDouble implements Protocol {
        
        public final @NotNull String name;
        public final @NotNull StorageValue<Double> storage;
        public final @Nullable ValueValidator.Protocol<Double> validator;
        
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
        
        public void setValue(@NotNull Double value) {
            storage.set(value);
        }
    }
    
    class Flag implements Protocol, Cycling {
        
        public static final String YES = "yes";
        public static final String NO = "no";
        public static final List<String> allValues = Arrays.asList(YES, NO);
        
        public final @NotNull String name;
        public final @NotNull StorageValue<Boolean> storage;
        
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
            return storage.get() ? YES : NO;
        }
        
        public void setValue(@NotNull boolean value) {
            storage.set(value);
        }
        
        @Override
        public @NotNull List<String> allValues() {
            return allValues;
        }
        
        @Override
        public void pickNextValue() {
            storage.set(!storage.get());
        }
    }
    
    class EnumList implements Protocol, Cycling {
        
        public final @NotNull String name;
        public final @NotNull StorageValue<String> storage;
        public final @NotNull List<String> allValues;
        
        public EnumList(@NotNull String name,
                @NotNull StorageValue<String> storage,
                @NotNull List<String> allValues) {
            this.name = name;
            this.storage = storage;
            this.allValues = allValues;
            
            if (allValues.isEmpty()) {
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
        
        public void setValue(@NotNull String value) {
            if (!allValues.contains(value)) {
                throw Errors.illegalStateError();
            }
            
            storage.set(value);
        }
        
        @Override
        public @NotNull List<String> allValues() {
            return allValues;
        }
        
        @Override
        public void pickNextValue() {
            int currentIndex = allValues.indexOf(storage.get());
            
            if (currentIndex == -1) {
                throw Errors.illegalStateError();
            }
            
            if (currentIndex+1 < allValues.size()) {
                currentIndex += 1;
            } else {
                currentIndex = 0;
            }
            
            storage.set(allValues.get(currentIndex));
        }
    }
    
    class SimpleString implements Protocol {
        
        public final @NotNull String name;
        public final @NotNull StorageValue<String> storage;
        public final @Nullable ValueValidator.Protocol<String> validator;
        
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
        
        public void setValue(@NotNull String value) {
            storage.set(value);
        }
    }
    
    class SystemPath implements Protocol {
        
        public final @NotNull String name;
        public final @NotNull StorageValue<Path> storage;
        public final @Nullable ValueValidator.Protocol<Path> validator;
        
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
        
        public void setValue(@NotNull Path value) {
            this.storage.set(value);
        }
    }
    
    class Hotkey implements Protocol {
        
        public final @NotNull String name;
        public final @NotNull StorageValue<InputKeystroke.AWT> storage;
        
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
        
        public void setValue(@NotNull InputKeystroke.AWT value) {
            this.storage.set(value);
        }
    }
    
    class Keystroke extends Hotkey {
        
        public Keystroke(@NotNull String name, @NotNull StorageValue<InputKeystroke.AWT> storage) {
            super(name, storage);
        }
    }
}
