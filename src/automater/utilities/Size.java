/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import com.google.gson.annotations.SerializedName;
import java.awt.Dimension;
import org.jetbrains.annotations.NotNull;

/**
 * 2D size.
 *
 * @author Kristiyan Butev
 */
public class Size {

    @SerializedName("w")
    public final double width;

    @SerializedName("h")
    public final double height;

    public Size() {
        width = 0;
        height = 0;
    }

    public Size(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public static Size zero() {
        return new Size();
    }

    public static Size make(double width, double height) {
        return new Size(width, height);
    }
    
    public static Size make(@NotNull Dimension dimension) {
        return new Size(dimension.width, dimension.height);
    }

    @Override
    public String toString() {
        return String.format("%.0fx%.0f", width, height);
    }
}
