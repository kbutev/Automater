/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.mutableaction;

/**
 * Number mutable action property.
 *
 * @author Byti
 */
public interface MutableActionPropertyNumber extends BaseMutableActionProperty {
    public double getMaxValue();
    public double getMinValue();
}
