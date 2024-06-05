/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
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
    @Nullable String getStandart();
    @Nullable String getVerbose();
    @Nullable String getStandartTooltip();
    @Nullable String getVerboseTooltip();
    @Nullable String getName();
    @Nullable String getDebug();

    public static @NotNull Description createFromString(@NotNull String value)
    {
        return new DescriptionString(value);
    }
}

class DescriptionString implements Description {
    
    private final @NotNull String value;
    
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
