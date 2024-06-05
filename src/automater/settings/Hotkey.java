/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.settings;

import automater.input.InputKey;
import automater.input.InputKeyModifiers;
import automater.input.InputKeyValue;
import org.jetbrains.annotations.NotNull;

/**
 * A keystroke.
 *
 * @author Bytevi
 */
public class Hotkey {

    public final @NotNull InputKey key;

    public Hotkey(@NotNull InputKey key) {
        this.key = key;
    }

    public Hotkey(@NotNull InputKeyValue keyValue) {
        this.key = new InputKey(keyValue);
    }

    public boolean isEqualTo(@NotNull InputKey key) {
        if (!key.modifiers.equals(InputKeyModifiers.none())) {
            return false;
        }

        return this.key.equals(key);
    }

    @Override
    public String toString() {
        return key.toString();
    }
}
