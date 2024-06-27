/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.macro;

import automater.utilities.Size;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class MacroRecordSource {
    
    public static String VERSION = "1.0";
    
    @SerializedName("version")
    public final @NotNull String version;
    
    @SerializedName("kind")
    public final @NotNull MacroRecordSourceKind kind;
    
    @SerializedName("primaryScreenSize")
    public final @NotNull Size primaryScreenSize;
    
    public MacroRecordSource(@NotNull MacroRecordSourceKind kind, @NotNull Size primaryScreenSize) {
        version = VERSION;
        this.kind = kind;
        this.primaryScreenSize = primaryScreenSize;
    }
}
