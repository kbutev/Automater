/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.storage;

import automater.utilities.Callback;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface StorageValue <T> {
    
    @NotNull T get();
    void set(@NotNull T value);
    
    static <T> @NotNull StorageValue<T> build(@NotNull Callback.Return<T> getCallback, @NotNull Callback.Param<T> set) {
        return new StorageValue<T>() {
            @Override
            public @NotNull T get() {
                return getCallback.perform();
            }
            
            @Override
            public void set(@NotNull T value) {
                set.perform(value);
            }
            
            @Override
            public String toString() {
                return get().toString();
            }
        };
    }
}
