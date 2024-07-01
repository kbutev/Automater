/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.validator;

import automater.utilities.Errors;
import automater.utilities.FileSystem;
import automater.utilities.Path;
import automater.utilities.Range;
import automater.validator.ValueValidator.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public class CommonValidators {
    
    public static @NotNull ValueValidator.Protocol<Double> doubleRange(double min, double max) {
        return (var value) -> { return new ValueValidator.Result(value >= min && value <= max ? null : Errors.unknownError()); };
    }
    
    public static @NotNull ValueValidator.Protocol<Double> nonNegativeDouble() {
        return nonNegativeDouble(null);
    }
    
    public static @NotNull ValueValidator.Protocol<Double> nonNegativeDouble(@Nullable Integer maxDecimalDigits) {
        return new ValueValidator.SimpleNumber(Range.makeMin(0), maxDecimalDigits);
    }
    
    public static @NotNull ValueValidator.Protocol<String> newFileName(@NotNull Path directory) {
        var internal = fileName();
        
        return (String value) -> {
            var newFile = directory.withSubpath(value).getFile();
            
            if (newFile.exists()) {
                return new Result(CommonErrors.nameTaken());
            }
            
            return internal.validate(value);
        };
    }
    
    public static @NotNull ValueValidator.Protocol<String> fileName() {
        return (String value) -> {
            if (!FileSystem.isFileNameValid(value)) {
                return new Result(CommonErrors.invalidName());
            }
            
            return Result.SUCCESS;
        };
    }
}
