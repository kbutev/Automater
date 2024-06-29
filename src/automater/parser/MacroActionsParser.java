/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.di.DI;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import automater.model.action.MacroAction;
import com.google.gson.Gson;

/**
 * Parsers macro actions and other related data.
 * Immutable.
 * @author Kristiyan Butev
 */
public interface MacroActionsParser {
    
    interface Protocol {
        
        @NotNull List<MacroAction> parseFromJSON(@NotNull JsonElement json) throws Exception;
        @NotNull JsonElement parseToJSON(@NotNull List<MacroAction> actions) throws Exception;
    }
    
    class Impl implements Protocol {
        
        private final Gson gson = DI.get(Gson.class);
        
        private final MacroActionParser.Impl innerParser = new MacroActionParser.Impl();
        
        @Override
        public @NotNull List<MacroAction> parseFromJSON(@NotNull JsonElement json) throws Exception {
            if (!(json instanceof JsonArray array)) {
                throw new JsonSyntaxException("Invalid json");
            }
            
            if (array.isEmpty()) {
                return new ArrayList();
            }
            
            var result = new ArrayList<MacroAction>();
            
            for (var element : array) {
                var action = innerParser.parseFromJSON(element);
                result.add(action);
            }
            
            return result;
        }
        
        @Override
        public @NotNull JsonElement parseToJSON(@NotNull List<MacroAction> actions) throws Exception {
            if (actions.isEmpty()) {
                return new JsonObject();
            }
            
            var result = gson.toJsonTree(actions);
            
            if (result instanceof JsonArray array) {
                return array;
            }
            
            throw new JsonSyntaxException("Invalid action");
        }
    }
}
