/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Describes an object.
 * 
 * @author Bytevi
 */
public interface Description {
    public @Nullable String getStandart();
    public @Nullable String getVerbose();
    public @Nullable String getStandartTooltip();
    public @Nullable String getVerboseTooltip();
    public @Nullable String getName();
    public @Nullable String getDebug();
    
    public static @NotNull Description createFromString(@NotNull final String value)
    {
        return new DescriptionString(value);
    }
}

class DescriptionString implements Description {
    @NotNull private final String value;
    
    DescriptionString(@NotNull String value)
    {
        this.value = value;
    }

    @Override
    public @Nullable String getStandart() {
        return value;
    }

    @Override
    public @Nullable String getVerbose() {
        return value;
    }

    @Override
    public @Nullable String getStandartTooltip() {
        return value;
    }

    @Override
    public @Nullable String getVerboseTooltip() {
        return value;
    }

    @Override
    public @Nullable String getName() {
        return value;
    }

    @Override
    public @Nullable String getDebug() {
        return value;
    }
}
