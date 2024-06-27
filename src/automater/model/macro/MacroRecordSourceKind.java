/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.macro;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Kristiyan Butev
 */
public enum MacroRecordSourceKind {
    
    @SerializedName("unknown") UNKNOWN,
    @SerializedName("java.awt.robot") JAVA_AWT_ROBOT
}
