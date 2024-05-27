/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.mutableaction;

/**
 * String mutable action property.
 *
 * @author Byti
 */
public interface MutableActionPropertyString extends BaseMutableActionProperty {
    public int getMaxLength();
    public int getMinLength();
}
