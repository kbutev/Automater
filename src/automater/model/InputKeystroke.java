/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import automater.json.JSONDecoder;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public class InputKeystroke {

    @SerializedName("v")
    public final @NotNull InputKeyValue value;

    @SerializedName("m")
    @JSONDecoder.Optional
    private final @Nullable InputKeyModifier modifier;

    public static @NotNull InputKeystroke anyKey() {
        return new InputKeystroke(InputKeyValue.X);
    }

    public static @NotNull InputKeystroke build(@NotNull InputKeyValue value) {
        return new InputKeystroke(value);
    }

    public InputKeystroke(@NotNull InputKeyValue value) {
        this.value = value;
        this.modifier = null;
    }

    public InputKeystroke(@NotNull InputKeyValue value, @Nullable InputKeyModifier modifier) {
        this.value = value;
        this.modifier = modifier != null ? modifier.copy() : null;
    }

    public @NotNull
    InputKeyModifier getModifier() {
        return modifier != null ? modifier : InputKeyModifier.none();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof InputKeystroke other) {
            return value == other.value && getModifier().equals(other.getModifier());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(value);
        hash = 59 * hash + Objects.hashCode(getModifier());
        return hash;
    }

    @Override
    public String toString() {
        return getModifier().toString() + value.toString();
    }

    public boolean isKeyboard() {
        return !isMouse();
    }

    public boolean isMouse() {
        return value == InputKeyValue.MOUSE_LEFT_CLICK
                || value == InputKeyValue.MOUSE_RIGHT_CLICK
                || value == InputKeyValue.MOUSE_MIDDLE_CLICK
                || value == InputKeyValue.MOUSE_4_CLICK
                || value == InputKeyValue.MOUSE_5_CLICK;
    }

    public static String extractKeyValueFromKeyString(@NotNull String value) {
        int lastIndexOfModifierSeparator = value.lastIndexOf(InputKeyModifierValue.getSeparatorSymbol());

        if (lastIndexOfModifierSeparator == -1) {
            return value;
        }

        if (lastIndexOfModifierSeparator + 1 >= value.length()) {
            return "";
        }

        String result = value.substring(lastIndexOfModifierSeparator + 1);
        return result;
    }

    public static String extractKeyModifiersFromKeyString(@NotNull String value) {
        int lastIndexOfModifierSeparator = value.lastIndexOf(InputKeyModifierValue.getSeparatorSymbol());

        if (lastIndexOfModifierSeparator == -1) {
            return "";
        }

        String result = value.substring(0, lastIndexOfModifierSeparator + 1);
        return result;
    }
}
