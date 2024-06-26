/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.macro;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public class MacroSummary {
    
    public static final String VERSION = "1.0";
    
    @SerializedName("version")
    public final @NotNull String version;
    
    @SerializedName("name")
    public final @NotNull String name;
    
    @SerializedName("description")
    public final @NotNull String description;
    
    @SerializedName("actionsCount")
    public final int actionsCount;
    
    // date created, date modified, last date played

    @SerializedName("playCount")
    public final int playCount;
    
    public MacroSummary(@NotNull String name,
            @Nullable String description,
            int playCount,
            int actionsCount) {
        version = VERSION;
        this.name = name;
        this.description = description != null ? description : "";
        this.playCount = playCount;
        this.actionsCount = actionsCount;
    }
}
