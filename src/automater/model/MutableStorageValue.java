/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import automater.storage.StorageValue;
import automater.utilities.Errors;
import automater.utilities.NumberUtilities;
import automater.utilities.Path;
import automater.utilities.Point;
import automater.validator.CommonErrors;
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
    interface Cycling extends Protocol {
        
        @NotNull List<String> allValues();
        void pickNextValue();
    }
    
    interface SimpleStringProtocol extends Protocol {
        
        @NotNull String getValue();
        void setValue(@NotNull String value) throws Exception;
        @Nullable Exception validate(@NotNull String value);
    }
    
    class SimpleString implements SimpleStringProtocol {
        
        public final @NotNull String name;
        private final @NotNull StorageValue<String> storage;
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
        
        @Override
        public @NotNull String getValue() {
            return storage.get();
        }
        
        @Override
        public void setValue(@NotNull String value) throws Exception {
            var exception = validate(value);
            
            if (exception != null) {
                throw exception;
            }
            
            storage.set(value);
        }
        
        @Override
        public @Nullable Exception validate(@NotNull String value) {
            if (validator != null) {
                var result = validator.validate(value);
                
                if (!result.isSuccess()) {
                    return result.error;
                }
            }
            
            return null;
        }
    }
    
    interface SimpleNumberProtocol extends Protocol {
        
        double getValue();
        void setValue(double value) throws Exception;
        @NotNull SimpleStringProtocol asStringStorageValue();
    }
    
    class SimpleNumber implements SimpleNumberProtocol {
        
        public static final int MAX_STRING_LENGTH = 16;
        
        public final @NotNull String name;
        private final @NotNull StorageValue<Double> storage;
        public final @Nullable ValueValidator.Protocol<Double> validator;
        
        public SimpleNumber(@NotNull String name,
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
        public double getValue() {
            return storage.get();
        }
        
        @Override
        public void setValue(double value) throws Exception {
            if (validator != null) {
                var result = validator.validate(value);
                
                if (!result.isSuccess()) {
                    throw result.error;
                }
            }
            
            storage.set(value);
        }
        
        @Override
        public @NotNull SimpleStringProtocol asStringStorageValue() {
            var self = this;
            
            return new SimpleStringProtocol() {
                @Override
                public String getValue() {
                    return self.getValueAsString();
                }
                
                @Override
                public void setValue(String value) throws Exception {
                    var exception = validate(value);
                    
                    if (exception != null) {
                        throw exception;
                    }
                    
                    self.setValue(Double.parseDouble(value));
                }
                
                @Override
                public String getName() {
                    return name;
                }
                
                @Override
                public String getValueAsString() {
                    return self.getValueAsString();
                }
                
                @Override
                public @Nullable Exception validate(@NotNull String value) {
                    if (value.length() > MAX_STRING_LENGTH) {
                        return CommonErrors.valueTooLarge();
                    }
                    
                    if (!NumberUtilities.isNumber(value)) {
                        return CommonErrors.notANumber;
                    }
                    
                    try {
                        Double.parseDouble(value);
                        return null;
                    } catch (NumberFormatException e) {
                        return CommonErrors.notANumber;
                    }
                }
            };
        }
    }
    
    interface FlagProtocol extends Protocol, Cycling {
        
        boolean getValue();
        void setValue(boolean value);
    }
    
    class Flag implements FlagProtocol {
        
        public static final String YES = "yes";
        public static final String NO = "no";
        public static final List<String> allValues = Arrays.asList(YES, NO);
        
        public final @NotNull String name;
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
            return storage.get() ? YES : NO;
        }
        
        @Override
        public boolean getValue() {
            return storage.get();
        }
        
        @Override
        public void setValue(boolean value) {
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
    
    interface EnumListProtocol extends SimpleStringProtocol, Cycling {
        
    }
    
    class EnumList implements EnumListProtocol {
        
        public final @NotNull String name;
        private final @NotNull StorageValue<String> storage;
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
        
        @Override
        public @NotNull String getValue() {
            return storage.get();
        }
        
        @Override
        public void setValue(@NotNull String value) throws Exception {
            var exception = validate(value);
            
            if (exception != null) {
                throw exception;
            }
            
            storage.set(value);
        }
        
        @Override
        public @Nullable Exception validate(@NotNull String value) {
            return !allValues.contains(value) ? Errors.illegalStateError() : null;
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
    
    interface SystemPathProtocol extends Protocol {
        
        @NotNull Path getValue();
        void setValue(@NotNull Path value) throws Exception;
    }
    
    class SystemPath implements SystemPathProtocol {
        
        public final @NotNull String name;
        private final @NotNull StorageValue<Path> storage;
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
        
        @Override
        public @NotNull Path getValue() {
            return storage.get();
        }
        
        @Override
        public void setValue(@NotNull Path value) throws Exception {
            if (validator != null) {
                var result = validator.validate(value);
                
                if (!result.isSuccess()) {
                    throw result.error;
                }
            }
            
            storage.set(value);
        }
    }
    
    interface HotkeyProtocol extends Protocol {
        
        @NotNull InputKeystroke.AWT getValue();
        void setValue(@NotNull InputKeystroke.AWT value) throws Exception;
    }
    
    class Hotkey implements HotkeyProtocol {
        
        public final @NotNull String name;
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
        public @NotNull InputKeystroke.AWT getValue() {
            return storage.get();
        }
        
        @Override
        public void setValue(@NotNull InputKeystroke.AWT value) throws Exception {
            storage.set(value);
        }
    }
    
    interface KeystrokeProtocol extends Protocol {
        
    }
    
    class Keystroke extends Hotkey implements KeystrokeProtocol {
        
        public Keystroke(@NotNull String name, @NotNull StorageValue<InputKeystroke.AWT> storage) {
            super(name, storage);
        }
    }
    
    interface PointXYProtocol extends Protocol {
        
        @NotNull Point getValue();
        void setValue(@NotNull Point value) throws Exception;
        
        @NotNull SimpleStringProtocol xAsStringStorageValue();
        @NotNull SimpleStringProtocol yAsStringStorageValue();
    }
    
    class PointXY implements PointXYProtocol {
        
        public final int MAX_STRING_LENGTH = 12;
        
        public final @NotNull String name;
        private final @NotNull StorageValue<Point> storage;
        public final @Nullable ValueValidator.Protocol<Point> validator;
        
        public PointXY(@NotNull String name,
                @NotNull StorageValue<Point> storage,
                @Nullable ValueValidator.Protocol<Point> validator) {
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
        public @NotNull Point getValue() {
            return storage.get();
        }
        
        @Override
        public void setValue(@NotNull Point value) throws Exception {
            if (validator != null) {
                var result = validator.validate(value);
                
                if (!result.isSuccess()) {
                    throw result.error;
                }
            }
            
            storage.set(value);
        }
        
        @Override
        public @NotNull SimpleStringProtocol xAsStringStorageValue() {
            var self = this;
            
            return new SimpleStringProtocol() {
                @Override
                public String getValue() {
                    return String.valueOf(self.storage.get().x);
                }
                
                @Override
                public void setValue(String value) throws Exception {
                    var exception = validate(value);
                    
                    if (exception != null) {
                        throw exception;
                    }
                    
                    var x = Double.parseDouble(value);
                    self.setValue(Point.make(x, self.storage.get().y));
                }
                
                @Override
                public String getName() {
                    return "x";
                }
                
                @Override
                public String getValueAsString() {
                    return self.getValueAsString();
                }
                
                @Override
                public @Nullable Exception validate(@NotNull String value) {
                    return self.validateStringValue(value);
                }
            };
        }
        
        @Override
        public @NotNull SimpleStringProtocol yAsStringStorageValue() {
            var self = this;
            
            return new SimpleStringProtocol() {
                @Override
                public String getValue() {
                    return String.valueOf(self.storage.get().y);
                }
                
                @Override
                public void setValue(String value) throws Exception {
                    var exception = validate(value);
                    
                    if (exception != null) {
                        throw exception;
                    }
                    
                    var y = Double.parseDouble(value);
                    self.setValue(Point.make(self.storage.get().x, y));
                }
                
                @Override
                public String getName() {
                    return "y";
                }
                
                @Override
                public String getValueAsString() {
                    return self.getValueAsString();
                }
                
                @Override
                public @Nullable Exception validate(@NotNull String value) {
                    return self.validateStringValue(value);
                }
            };
        }
        
        private @Nullable Exception validateStringValue(@NotNull String value) {
            if (value.length() > MAX_STRING_LENGTH) {
                return CommonErrors.valueTooLarge();
            }

            if (!NumberUtilities.isNumber(value)) {
                return CommonErrors.notANumber;
            }

            try {
                Double.parseDouble(value);
                return null;
            } catch (NumberFormatException e) {
                return CommonErrors.notANumber;
            }
        }
    }
}
