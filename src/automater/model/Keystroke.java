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
public class Keystroke {

    @SerializedName("v")
    public final @NotNull KeyValue value;

    @SerializedName("m")
    @JSONDecoder.Optional
    private final @Nullable KeyModifier modifier;

    public static @NotNull
    Keystroke anyKey() {
        return new Keystroke(KeyValue.X);
    }

    public static @NotNull
    Keystroke build(@NotNull KeyValue value) {
        return new Keystroke(value);
    }

    public Keystroke(@NotNull KeyValue value) {
        this.value = value;
        this.modifier = null;
    }

    public Keystroke(@NotNull KeyValue value, @Nullable KeyModifier modifier) {
        this.value = value;
        this.modifier = modifier != null ? modifier.copy() : null;
    }

    public @NotNull
    KeyModifier getModifier() {
        return modifier != null ? modifier : KeyModifier.none();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Keystroke other) {
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
        return value == KeyValue.MOUSE_LEFT_CLICK
                || value == KeyValue.MOUSE_RIGHT_CLICK
                || value == KeyValue.MOUSE_MIDDLE_CLICK
                || value == KeyValue.MOUSE_4_CLICK
                || value == KeyValue.MOUSE_5_CLICK;
    }

    public static String extractKeyValueFromKeyString(@NotNull String value) {
        int lastIndexOfModifierSeparator = value.lastIndexOf(KeyModifierValue.getSeparatorSymbol());

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
        int lastIndexOfModifierSeparator = value.lastIndexOf(KeyModifierValue.getSeparatorSymbol());

        if (lastIndexOfModifierSeparator == -1) {
            return "";
        }

        String result = value.substring(0, lastIndexOfModifierSeparator + 1);
        return result;
    }
}
