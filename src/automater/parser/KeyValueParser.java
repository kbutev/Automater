/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.model.KeyValue;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface KeyValueParser {
    
    interface Protocol {
        @NotNull KeyValue parseFromString(@NotNull String value);
        @NotNull String parseToString(@NotNull KeyValue value);
    }
    
    class Impl implements Protocol {
        @Override
        public @NotNull KeyValue parseFromString(@NotNull String value) {
            if (!value.startsWith("_")) {
                value = "_" + value;
            }
            
            return KeyValue.valueOf(value);
        }
        
        @Override
        public @NotNull String parseToString(@NotNull KeyValue value) {
            var name = value.name();
            
            name = name.replaceFirst("_", "");
            
            return name;
        }
    }
}
