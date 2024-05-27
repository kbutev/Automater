/*
 * Created by Kristiyan Butev.
 * Copyright © 2021 Kristiyan Butev. All rights reserved.
 */
package automater.mutableaction;

import automater.utilities.TimeType;
import org.jetbrains.annotations.NotNull;

/**
 * String mutable action property.
 *
 * @author Byti
 */
public interface MutableActionPropertyTime extends BaseMutableActionProperty {
    public long getTimeInMS();
    public @NotNull TimeType getTimeType();
}
