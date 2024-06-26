/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.macro;

import automater.model.action.MacroAction;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import automater.utilities.Size;

/**
 *
 * @author Kristiyan Butev
 */
public interface Macro {
    
    public static final String KEY_SUMMARY = "summary";
    public static final String KEY_ACTIONS = "actions";
    
    public static @NotNull Macro.Protocol build(@NotNull MacroSummary summary, @NotNull List<MacroAction> actions) {
        return new Macro.Impl(summary, actions);
    }
    
    public static @NotNull Macro.Protocol build(@NotNull String name, @NotNull List<MacroAction> actions) {
        return new Macro.Impl(new MacroSummary(name, null, 0, actions.size(), Size.zero()), actions);
    }
    
    interface Protocol {
        
        @NotNull MacroSummary getSummary();
        @NotNull List<MacroAction> getActions();
    }
    
    class Impl implements Protocol {
        @SerializedName(KEY_SUMMARY)
        public final @NotNull MacroSummary summary;

        @SerializedName(KEY_ACTIONS)
        public final List<MacroAction> actions;
        
        Impl(@NotNull MacroSummary summary, @NotNull List<MacroAction> actions) {
            this.summary = summary;
            this.actions = actions;
        }
        
        @Override
        public MacroSummary getSummary() {
            return summary;
        }
        
        @Override
        public List<MacroAction> getActions() {
            return actions;
        }
    }
}
