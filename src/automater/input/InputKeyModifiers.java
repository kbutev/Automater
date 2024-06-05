/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.input;

import org.jetbrains.annotations.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Combination of modifier values.
 *
 * @author Bytevi
 */
public class InputKeyModifiers implements Serializable {

    private final HashSet<InputKeyModifierValue> _values = new HashSet<>();

    public static InputKeyModifiers none() {
        return new InputKeyModifiers();
    }

    public InputKeyModifiers() {

    }

    public InputKeyModifiers(@NotNull InputKeyModifierValue value) {
        _values.add(value);
    }

    public InputKeyModifiers(@NotNull HashSet<InputKeyModifierValue> values) {
        for (InputKeyModifierValue value : values) {
            _values.add(value);
        }
    }

    public InputKeyModifiers(@NotNull String string) {
        String suffix = InputKeyModifierValue.getSeparatorSymbol();

        String[] strings = string.split(Pattern.quote(suffix));

        for (int e = 0; e < string.length(); e++) {
            try {
                InputKeyModifierValue modifier = InputKeyModifierValue.valueOf(strings[e]);
                _values.add(modifier);
            } catch (Exception exc) {

            }
        }
    }

    @Override
    public String toString() {
        String value = "";

        for (InputKeyModifierValue flag : _values) {
            value = value.concat(flag.toString());
        }

        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof InputKeyModifiers) {
            InputKeyModifiers other = (InputKeyModifiers) o;
            return _values.equals(other._values);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this._values);
        return hash;
    }

    public Set<InputKeyModifierValue> getValues() {
        return _values;
    }

    public InputKeyModifiers copy() {
        return new InputKeyModifiers(this._values);
    }

    public InputKeyModifiers createWithNewAddedModifier(@NotNull InputKeyModifierValue value) {
        InputKeyModifiers modifiers = copy();

        if (value == InputKeyModifierValue.NONE) {
            return modifiers;
        }

        modifiers._values.add(value);

        return modifiers;
    }

    public InputKeyModifiers createWithRemovedModifier(@NotNull InputKeyModifierValue value) {
        InputKeyModifiers modifiers = copy();

        if (value == InputKeyModifierValue.NONE) {
            return modifiers;
        }

        modifiers._values.remove(value);

        return modifiers;
    }
}
