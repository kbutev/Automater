/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.macro;

import automater.model.action.MacroAction;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface Macro {
    
    public static final String KEY_SUMMARY = "summary";
    public static final String KEY_RECORD_SOURCE = "source";
    public static final String KEY_ACTIONS = "actions";
    
    public static @NotNull Macro.Protocol build(@NotNull MacroSummary summary, @NotNull MacroRecordSource recordSource, @NotNull List<MacroAction> actions) {
        return new Macro.Impl(summary, recordSource, actions);
    }
    
    public static @NotNull Macro.Protocol build(@NotNull String name, @NotNull MacroRecordSource recordSource, @NotNull List<MacroAction> actions) {
        return new Macro.Impl(new MacroSummary(name, null, 0, actions.size()), recordSource, actions);
    }
    
    interface Protocol {
        
        @NotNull MacroSummary getSummary();
        @NotNull MacroRecordSource getRecordSource();
        @NotNull List<MacroAction> getActions();
    }
    
    class Impl implements Protocol {
        @SerializedName(KEY_SUMMARY)
        public final @NotNull MacroSummary summary;

        @SerializedName(KEY_RECORD_SOURCE)
        public final @NotNull MacroRecordSource recordSource;
        
        @SerializedName(KEY_ACTIONS)
        public final List<MacroAction> actions;
        
        Impl(@NotNull MacroSummary summary, @NotNull MacroRecordSource recordSource, @NotNull List<MacroAction> actions) {
            this.summary = summary;
            this.recordSource = recordSource;
            this.actions = actions;
        }
        
        @Override
        public MacroSummary getSummary() {
            return summary;
        }
        
        @Override
        public @NotNull MacroRecordSource getRecordSource() {
            return recordSource;
        }
        
        @Override
        public List<MacroAction> getActions() {
            return actions;
        }
    }
}
