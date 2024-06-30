/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model.macro;

import automater.model.action.MacroAction;
import automater.utilities.CollectionUtilities;
import com.google.gson.annotations.SerializedName;
import java.util.Comparator;
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
        
        @NotNull Protocol withActionInserted(@NotNull MacroAction action);
        @NotNull Protocol withActionRemoved(@NotNull MacroAction action);
        @NotNull Protocol withActionReplaced(@NotNull MacroAction original, @NotNull MacroAction modified);
    }
    
    class Impl implements Protocol {
        @SerializedName(KEY_SUMMARY)
        public final @NotNull MacroSummary summary;

        @SerializedName(KEY_RECORD_SOURCE)
        public final @NotNull MacroRecordSource recordSource;
        
        @SerializedName(KEY_ACTIONS)
        public final List<MacroAction> actions;
        
        public Impl(@NotNull MacroSummary summary, @NotNull MacroRecordSource recordSource, @NotNull List<MacroAction> actions) {
            this.summary = summary;
            this.recordSource = recordSource;
            var actionsCopy = CollectionUtilities.copy(actions);
            actionsCopy.sort(Comparator.comparingDouble(MacroAction::getTimestamp));
            this.actions = CollectionUtilities.copyAsImmutable(actionsCopy);
        }
        
        public Impl(@NotNull Protocol other) {
            this(other.getSummary(), other.getRecordSource(), other.getActions());
        }
        
        @Override
        public @NotNull MacroSummary getSummary() {
            return summary;
        }
        
        @Override
        public @NotNull MacroRecordSource getRecordSource() {
            return recordSource;
        }
        
        @Override
        public @NotNull List<MacroAction> getActions() {
            return actions;
        }
        
        @Override
        public @NotNull Protocol withActionInserted(@NotNull MacroAction action) {
            var newActions = CollectionUtilities.copy(this.actions);
            
            newActions.add(action);
            
            return new Impl(summary, recordSource, newActions);
        }
        
        @Override
        public @NotNull Protocol withActionRemoved(@NotNull MacroAction action) {
            var newActions = CollectionUtilities.copy(this.actions);
            
            newActions.remove(action);
            
            return new Impl(summary, recordSource, newActions);
        }
        
        @Override
        public @NotNull Protocol withActionReplaced(@NotNull MacroAction original, @NotNull MacroAction modified) {
            var newActions = CollectionUtilities.copy(this.actions);
            
            newActions.remove(original);
            newActions.add(modified);
            
            return new Impl(summary, recordSource, newActions);
        }
    }
}
