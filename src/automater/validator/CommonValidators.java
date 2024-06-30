/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.validator;

import automater.utilities.Errors;
import automater.utilities.Range;
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
}
