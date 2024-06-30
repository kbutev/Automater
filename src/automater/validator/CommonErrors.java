/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.validator;

import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class CommonErrors {
    
    public final static Exception notANumber = new Exception("Enter a number value");
    
    public static @NotNull Exception valueTooSmall() {
        return new Exception("Value is too small");
    }
    
    public static @NotNull Exception valueTooSmall(double value) {
        return new Exception("Cannot be less than " + String.format("%.3f", value));
    }
    
    public static @NotNull Exception valueTooLarge() {
        return new Exception("Value is too large");
    }
    
    public static @NotNull Exception valueTooLarge(double value) {
        return new Exception("Cannot be greater than " + String.format("%.3f", value));
    }
    
    public static @NotNull Exception tooManyDecimalDigits(int digits) {
        return new Exception("Cannot have more decimal digits than " + digits);
    }
}
