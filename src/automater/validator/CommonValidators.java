/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.validator;

import automater.utilities.Errors;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class CommonValidators {
    
    public static @NotNull ValueValidator.Protocol<Double> doubleRange(double min, double max) {
        return (var value) -> { return new ValueValidator.Result(value >= min && value <= max ? null : Errors.unknownError()); };
    }
    
    public static @NotNull ValueValidator.Protocol<Double> nonNegativeDouble() {
        return (var value) -> { return new ValueValidator.Result(value > 0 ? null : Errors.unknownError()); };
    }
}
