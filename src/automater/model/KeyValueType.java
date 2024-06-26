/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Kristiyan Butev
 */
public enum KeyValueType {
    
    @SerializedName("k") keyboard,
    @SerializedName("m") mouse;
    
    @Override
    public String toString() {
        try {
            return getClass().getField(name()).getAnnotation(SerializedName.class).value();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public int getRaw() {
        return ordinal();
    }
}
