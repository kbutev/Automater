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
public enum KeyEventKind {
    press("press"),
    release("release"),
    tap("tap");
    
    public static final List<String> allValues = allValues();
    
    public static @Nullable KeyEventKind named(@NotNull String string) {
        for (var kind : KeyEventKind.values()) {
            if (kind.value.equals(string)) {
                return kind;
            }
        }
        
        return null;
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
