/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import automater.utilities.Errors;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public enum KeyEventKind {
    press("press"),
    release("release"),
    tap("tap");
    
    public static final List<String> allValues = allValues();
    
    public static @NotNull KeyEventKind build(@NotNull String value) {
        var result = KeyEventKind.valueOf(value);
        
        if (result == null) {
            throw Errors.unsupported("Invalid enum value");
        }
        
        return result;
    }
    
    public final @NotNull String value;
    
    KeyEventKind(@NotNull String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
    
    public boolean isReleaseOrTap() {
        return this == release || this == tap;
    }
    
    private static @NotNull List<String> allValues() {
        var result = new ArrayList<String>();
        
        for (var value : KeyEventKind.values()) {
            result.add(value.value);
        }
        
        return result;
    }
}
