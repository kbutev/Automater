/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.di.DI;
import automater.model.macro.Macro;
import automater.model.macro.MacroRecordSource;
import automater.model.macro.MacroSummary;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

/**
 * Parsers macros.
 * Immutable.
 * @author Kristiyan Butev
 */
public interface MacroParser {
    
    interface Protocol {
        
        @NotNull Macro.Protocol parseFromJSON(@NotNull JsonElement json) throws Exception;
        @NotNull JsonElement parseToJSON(@NotNull Macro.Protocol macro) throws Exception;
    }
    
    class Impl implements Protocol {
        
        static final String KEY_ACTIONS = Macro.KEY_ACTIONS;
        static final String KEY_RECORD_SOURCE = Macro.KEY_RECORD_SOURCE;
        static final String KEY_SUMMARY = Macro.KEY_SUMMARY;
        
        final @NotNull Gson gson = DI.get(Gson.class);
        final @NotNull MacroActionsParser.Protocol actionsParser = DI.get(MacroActionsParser.Protocol.class);
        
        @Override
        public @NotNull Macro.Protocol parseFromJSON(@NotNull JsonElement json) throws Exception {
            if (!(json instanceof JsonObject macro)) {
                throw new JsonSyntaxException("Invalid json");
            }
            
            var summaryJSON = macro.get(KEY_SUMMARY);
            var recordSourceJSON = macro.get(KEY_RECORD_SOURCE);
            var actionsJSON = macro.get(KEY_ACTIONS);
            
            if (summaryJSON == null || recordSourceJSON == null || actionsJSON == null) {
                throw new JsonSyntaxException("Invalid json");
            }
            
            var summary = gson.fromJson(summaryJSON, MacroSummary.class);
            var recordSource = gson.fromJson(recordSourceJSON, MacroRecordSource.class);
            var actions = actionsParser.parseFromJSON(actionsJSON);
            
            return Macro.build(summary, recordSource, actions);
        }
        
        @Override
        public @NotNull JsonElement parseToJSON(@NotNull Macro.Protocol macro) throws Exception {
            var result = new JsonObject();
            var actions = actionsParser.parseToJSON(macro.getActions());
            var summary = gson.toJsonTree(macro.getSummary());
            var recordSource = gson.toJsonTree(macro.getRecordSource());
            
            result.add(KEY_SUMMARY, summary);
            result.add(KEY_RECORD_SOURCE, recordSource);
            result.add(KEY_ACTIONS, actions);
            
            return result;
        }
    }
}
