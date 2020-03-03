/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.mutableaction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A mutable action property.
 *
 * @author Byti
 */
public interface BaseMutableActionProperty {
    public boolean isValid();
    public @Nullable String getInvalidError();
    
    public @NotNull String getValue();
    public void setValue(@NotNull String value);
    
    public @NotNull String getName();
    public void setName(@NotNull String name);
}
