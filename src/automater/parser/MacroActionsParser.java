/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.di.DI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import automater.model.action.MacroAction;

/**
 * Parsers macro actions and other related data.
 * @author Kristiyan Butev
 */
public interface MacroActionsParser {
    
    interface Protocol {
        
        @NotNull List<MacroAction> parseFromJSON(@NotNull JsonElement json) throws Exception;
        @NotNull JsonElement parseToJSON(@NotNull List<MacroAction> actions) throws Exception;
    }
    
    class Impl implements Protocol {
        
        final Gson gson = DI.get(Gson.class);
        
        @Override
        public @NotNull List<MacroAction> parseFromJSON(@NotNull JsonElement json) throws Exception {
            if (!(json instanceof JsonArray array)) {
                throw new JsonSyntaxException("Invalid json");
            }
            
            if (array.isEmpty()) {
                return new ArrayList();
            }
            
            var type = array.get(0).getAsJsonObject().get(MacroActionParser.Keys.TYPE).getAsString();
            
            if (type == null) {
                throw new JsonSyntaxException("Invalid action");
            }
            
            var match = MacroActionParser.type_mappings.get(type);
            
            if (match != null) {
                var jsonString = array.toString();
                Type matchedType = match.listType;
                var decoder = match.decoder;
                var parser = new GsonBuilder().registerTypeAdapter(matchedType, decoder).create();
                return parser.fromJson(jsonString, matchedType);
            }
            
            throw new JsonSyntaxException("Invalid action");
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
