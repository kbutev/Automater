/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class Range {
    
    public final double min;
    public final double max;
    
    public static @NotNull Range make(double min, double max) {
        return new Range(min, max);
    }
    
    public static @NotNull Range makeMin(double min) {
        return new Range(min, Double.MAX_VALUE);
    }
    
    public static @NotNull Range makeMax(double max) {
        return new Range(Double.MIN_VALUE, max);
    }
    
    public Range() {
        min = 0;
        max = 0;
    }
    
    public Range(double min, double max) {
        this.min = min;
        this.max = max;
    }
    
    public boolean isInRange(double value) {
        return value >= min && value <= max;
    }
}
