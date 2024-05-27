/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.mutableaction;

import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * List of strings mutable action property.
 *
 * @author Byti
 */
public interface MutableActionPropertyList extends BaseMutableActionProperty {
    public @NotNull List<String> getValues();
}
