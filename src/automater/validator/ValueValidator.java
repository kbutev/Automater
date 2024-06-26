/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.validator;

import automater.utilities.CollectionUtilities;
import automater.utilities.NumberUtilities;
import automater.utilities.Range;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface ValueValidator {
    
    class Result {
        public final Exception error;
        
        public static Result SUCCESS = new Result(null);
        
        public Result(@Nullable Exception error) {
            this.error = error;
        }
        
        public boolean isSuccess() {
            return error == null;
        }
    }
    
    interface Protocol <T> {
        
        @NotNull Result validate(@NotNull T value);
    }
    
    class SimpleString implements Protocol<String> {
        
        final @Nullable Set<Character> forbiddenChars;
        final @Nullable Integer maxLength;
        final @Nullable Integer minLength;
        
        public SimpleString(@Nullable Set<Character> forbiddenChars, @Nullable Integer max, @Nullable Integer min) {
            this.forbiddenChars = forbiddenChars != null ? CollectionUtilities.copy(forbiddenChars) : null;
            this.maxLength = max;
            this.minLength = min;
        }
        
        @Override
        public @NotNull Result validate(@NotNull String value) {
            return Result.SUCCESS;
        }
    }
    
    class SimpleNumber implements Protocol<Double> {
        
        final @Nullable Range range;
        final @Nullable Integer maxDecimalDigits;
        
        public SimpleNumber(@Nullable Range range, @Nullable Integer maxDecimalDigits) {
            this.range = range;
            this.maxDecimalDigits = maxDecimalDigits;
        }
        
        @Override
        public @NotNull Result validate(@NotNull Double value) {
            if (range != null && value < range.min) {
                return new Result(CommonErrors.valueTooSmall(range.min));
            }
            
            if (range != null && value > range.max) {
                return new Result(CommonErrors.valueTooSmall(range.max));
            }
            
            if (maxDecimalDigits != null && NumberUtilities.numberOfDecimalDigits(value) > maxDecimalDigits) {
                return new Result(CommonErrors.tooManyDecimalDigits(maxDecimalDigits));
            }
            
            return Result.SUCCESS;
        }
    }
    
    class SystemPath extends SimpleString {
        
        final @Nullable String expectedPrefix;
        final @Nullable String expectedSuffix;
        
        public SystemPath(@Nullable String expectedPrefix, @Nullable String expectedSuffix) {
            super(getForbiddenCharacters(), getMinLength(), getMaxLength());
            this.expectedPrefix = expectedPrefix;
            this.expectedSuffix = expectedSuffix;
        }
        
        @Override
        public @NotNull Result validate(@NotNull String value) {
            return Result.SUCCESS;
        }
        
        static int getMinLength() {
            return 3;
        }
        
        static int getMaxLength() {
            return 128;
        }
        
        static Set<Character> getForbiddenCharacters() {
            // TODO
            return null;
        }
    }
}
