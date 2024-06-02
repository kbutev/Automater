/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.di.DI;
import automater.model.action.ScriptAction;
import automater.model.action.ScriptActionDescription;
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

/**
 * Parsers script actions and other related data.
 * @author Kristiyan Butev
 */
public interface ScriptActionsParser {
    
    interface Protocol {
        @NotNull List<ScriptActionDescription> parseToDescription(@NotNull List<ScriptAction> action) throws Exception;
        
        @NotNull List<ScriptAction> parseFromJSON(@NotNull JsonElement json) throws Exception;
        @NotNull JsonElement parseToJSON(@NotNull List<ScriptAction> events) throws Exception;
    }
    
    class Impl implements Protocol {
        final Gson gson = DI.get(Gson.class);
        
        @Override
        public @NotNull List<ScriptActionDescription> parseToDescription(@NotNull List<ScriptAction> action) throws Exception {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
        
        @Override
        public @NotNull List<ScriptAction> parseFromJSON(@NotNull JsonElement json) throws Exception {
            if (!(json instanceof JsonArray array)) {
                throw new JsonSyntaxException("Invalid json");
            }
            
            if (array.isEmpty()) {
                return new ArrayList();
            }
            
            var type = array.get(0).getAsJsonObject().get(ScriptActionParser.Keys.TYPE).getAsString();
            
            if (type == null) {
                throw new JsonSyntaxException("Invalid action");
            }
            
            var match = ScriptActionParser.type_mappings.get(type);
            
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
        public @NotNull JsonElement parseToJSON(@NotNull List<ScriptAction> events) throws Exception {
            if (events.isEmpty()) {
                return new JsonObject();
            }
            
            var result = gson.toJsonTree(events);
            var first = events.get(0);
            
            if (result instanceof JsonArray array) {
                array.get(0).getAsJsonObject().addProperty(ScriptActionParser.Keys.TYPE, first.getActionType());
                return array;
            }
            
            throw new JsonSyntaxException("Invalid action");
        }
    }
}
