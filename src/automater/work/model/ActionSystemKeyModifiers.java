/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work.model;

import automater.utilities.CollectionUtilities;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An action's key modifier.
 *
 * @author Bytevi
 */
public class ActionSystemKeyModifiers implements Serializable {

    public final @NotNull Set<ActionSystemKeyModifierValue> modifiers;

    public ActionSystemKeyModifiers() {
        this.modifiers = new HashSet<>();
    }

    public ActionSystemKeyModifiers(@NotNull Set<ActionSystemKeyModifierValue> modifiers) {
        this.modifiers = CollectionUtilities.copyAsImmutable(modifiers);
    }

    public static @NotNull ActionSystemKeyModifiers none() {
        return new ActionSystemKeyModifiers();
    }

    public static @NotNull ActionSystemKeyModifiers createModifierValues(@NotNull Set<ActionSystemKeyModifierValue> modifiers) {
        return new ActionSystemKeyModifiers(modifiers);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ActionSystemKeyModifiers) {
            ActionSystemKeyModifiers other = (ActionSystemKeyModifiers) o;

            return modifiers.equals(other.modifiers);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.modifiers);
        return hash;
    }

    public @NotNull ActionSystemKeyModifiers combine(@NotNull ActionSystemKeyModifiers modifiers) {
        if (modifiers.isNone()) {
            return this;
        }

        HashSet<ActionSystemKeyModifierValue> values = new HashSet<>();

        for (ActionSystemKeyModifierValue value : modifiers.modifiers) {
            values.add(value);
        }

        return ActionSystemKeyModifiers.createModifierValues(values);
    }

    public @NotNull ActionSystemKeyModifiers removeModifier(@NotNull ActionSystemKeyModifierValue value) {
        HashSet<ActionSystemKeyModifierValue> values = new HashSet<>(this.modifiers);

        values.remove(value);

        return ActionSystemKeyModifiers.createModifierValues(values);
    }

    public boolean isNone() {
        return this.modifiers.isEmpty();
    }
}
