/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import automater.json.JSONDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public class InputKeyModifier {

    @JSONDecoder.Optional
    private @Nullable List<InputKeyModifierValue> values;

    public static InputKeyModifier none() {
        return new InputKeyModifier();
    }

    public @NotNull InputKeyModifier withModifierAdded(@NotNull InputKeyModifierValue value) {
        InputKeyModifier modifiers = copy();

        if (modifiers.getRawValues().contains(value)) {
            return modifiers;
        }

        modifiers.getRawValues().add(value);

        return modifiers;
    }

    public @NotNull InputKeyModifier withModifierRemoved(@NotNull InputKeyModifierValue value) {
        InputKeyModifier modifiers = copy();

        if (!modifiers.getRawValues().contains(value)) {
            return modifiers;
        }

        modifiers.getRawValues().remove(value);

        return modifiers;
    }

    public InputKeyModifier() {

    }

    public InputKeyModifier(@NotNull InputKeyModifierValue value) {
        getRawValues().add(value);
    }

    public InputKeyModifier(@NotNull List<InputKeyModifierValue> values) {
        if (values.isEmpty()) {
            return;
        }

        for (InputKeyModifierValue value : values) {
            getRawValues().add(value);
        }
    }

    public InputKeyModifier(@NotNull String string) {
        String suffix = InputKeyModifierValue.getSeparatorSymbol();

        String[] strings = string.split(Pattern.quote(suffix));

        for (int e = 0; e < string.length(); e++) {
            try {
                InputKeyModifierValue modifier = InputKeyModifierValue.valueOf(strings[e]);
                values.add(modifier);
            } catch (Exception exc) {

            }
        }
    }

    @Override
    public String toString() {
        String value = "";

        for (InputKeyModifierValue flag : getRawValues()) {
            value = value.concat(flag.toString());
        }

        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof InputKeyModifier other) {
            return getRawValues().equals(other.getRawValues());
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.getRawValues());
        return hash;
    }

    public boolean isEmpty() {
        return values != null && values.isEmpty();
    }

    private List<InputKeyModifierValue> getRawValues() {
        if (values == null) {
            values = new ArrayList<>();
        }

        return values;
    }

    public Set<InputKeyModifierValue> getValues() {
        return new HashSet(getRawValues());
    }

    public InputKeyModifier copy() {
        return new InputKeyModifier(getRawValues());
    }

    public boolean contains(@NotNull InputKeyModifierValue value) {
        return values != null ? getRawValues().contains(value) : false;
    }
}
