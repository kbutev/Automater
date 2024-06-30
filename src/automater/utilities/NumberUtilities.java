/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import java.math.BigDecimal;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class NumberUtilities {
    
    public static boolean isNumber(@NotNull String value) {
        if (value.isEmpty()) {
            return false;
        }
        
        for (int index = 0; index < value.length(); index++) {
            var c = value.charAt(index);
            
            if (c == '+' || c == '-') {
                if (index > 0) {
                    return false;
                }
                
                continue;
            }
            
            if (c == '.') {
                if (index == 0 || index >= value.length()) {
                    return false;
                }
                
                continue;
            }
            
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        
        try {
            Double.valueOf(value);
        } catch (Exception e) {
            return false;
        }
        
        return true;
    }
    
    public static int numberOfDecimalDigits(double value) {
        var text = Double.toString(Math.abs(value));
        return new BigDecimal(text).scale();
    }
}
