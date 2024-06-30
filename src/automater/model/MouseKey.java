/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public enum MouseKey {
    
    LEFT("left"),
    RIGHT("right"),
    CENTER("center"),
    M4("M4"),
    M5("M5");
    
    public static final List<String> allValuesAsStrings = allValuesAsStrings();
        
    public final @NotNull String value;

    MouseKey(@NotNull String value) {
        this.value = value;
    }
    
    public static @Nullable MouseKey named(@NotNull String string) {
        for (var kind : MouseKey.values()) {
            if (kind.value.equals(string)) {
                return kind;
            }
        }
        
        return null;
    }

    public static @NotNull List<String> allValuesAsStrings() {
        ArrayList<String> values = new ArrayList<>();

        for (var kind : MouseKey.values()) {
            values.add(kind.value);
        }

        return values;
    }
}
