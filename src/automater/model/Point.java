/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Kristiyan Butev
 */
public class Point {
    @SerializedName("x")
    public final double x;
    
    @SerializedName("y")
    public final double y;
    
    public Point() {
        x = 0;
        y = 0;
    }
    
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public static Point zero() { return new Point(); }
    public static Point make(float x, float y) { return new Point(x, y); }
}
