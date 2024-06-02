/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.di.DI;
import automater.json.JSONDecoder;
import automater.model.action.ScriptAction;
import automater.model.action.ScriptActionDescription;
import automater.model.action.ScriptHardwareAction;
import automater.model.event.CapturedEvent;
import automater.model.event.CapturedHardwareEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Parsers script actions and other related data.
 * @author Kristiyan Butev
 */
public interface ScriptActionParser {
    
    class Keys {
        static final String TYPE = "a";
    }
    
    interface Protocol {
        @NotNull ScriptAction parseFromCapturedEvent(@NotNull CapturedEvent event) throws Exception;
        @NotNull ScriptActionDescription parseToDescription(@NotNull ScriptAction action) throws Exception;
        
        @NotNull ScriptAction parseFromJSON(@NotNull JsonElement json) throws Exception;
        @NotNull JsonElement parseToJSON(@NotNull ScriptAction event) throws Exception;
    }
    
    class Impl implements Protocol {
        final Gson gson = DI.get(Gson.class);
        
        @Override
        public @NotNull ScriptAction parseFromCapturedEvent(@NotNull CapturedEvent event) throws Exception {
            if (event instanceof CapturedHardwareEvent.Click eventObject) {
                return new ScriptHardwareAction.Click(eventObject.timestamp, eventObject.kind, eventObject.keystroke);
            }
            
            throw new UnsupportedOperationException("Unrecognizable event");
        }
        
        @Override
        public @NotNull ScriptActionDescription parseToDescription(@NotNull ScriptAction action) throws Exception {
            var timestamp = String.format("%.1f", action.getTimestamp());
            
            if (action instanceof ScriptHardwareAction.Click click) {
                return new ScriptActionDescription(timestamp, "click", click.keystroke.toString());
            } else if (action instanceof ScriptHardwareAction.MouseMove mmove) {
                return new ScriptActionDescription(timestamp, "mouse move", mmove.point.toString());
            } else if (action instanceof ScriptHardwareAction.MouseScroll scroll) {
                return new ScriptActionDescription(timestamp, "mouse scroll", scroll.scroll.toString());
            }
            
            throw new UnsupportedOperationException("Unrecognizable native event");
        }
        
        @Override
        public @NotNull ScriptAction parseFromJSON(@NotNull JsonElement json) throws Exception {
            if (!(json instanceof JsonObject jsonObject)) {
                throw new JsonSyntaxException("Invalid json");
            }
            
            var type = jsonObject.get(Keys.TYPE).getAsString();
            
            if (type == null) {
                throw new JsonSyntaxException("Invalid action");
            }
            
            var match = type_mappings.get(type);
            
            if (match != null) {
                var jsonString = jsonObject.toString();
                Type matchedType = match.type;
                var decoder = match.decoder;
                var parser = new GsonBuilder().registerTypeAdapter(matchedType, decoder).create();
                return parser.fromJson(jsonString, matchedType);
            }
            
            throw new JsonSyntaxException("Invalid action");
        }
        
        @Override
        public @NotNull JsonObject parseToJSON(@NotNull ScriptAction event) throws Exception {
            var result = gson.toJsonTree(event);
            
            if (result instanceof JsonObject jsonObject) {
                jsonObject.addProperty(Keys.TYPE, event.getActionType());
                return jsonObject;
            }
            
            throw new JsonSyntaxException("Invalid action");
        }
    }
    
    class ClassMapping {
        Type type;
        Type listType;
        JSONDecoder decoder;
        
        static <T> ClassMapping make(Class<T> type) {
            var result = new ClassMapping();
            result.type = TypeToken.get(type).getType();
            result.listType = TypeToken.getParameterized(ArrayList.class, type).getType();
            result.decoder = new JSONDecoder<T>();
            return result;
        }
    }
    
    // Action types mapped to their respective class types
    final static Map<String, ClassMapping> type_mappings = Map.of(
        ScriptHardwareAction.Click.TYPE, ClassMapping.make(ScriptHardwareAction.Click.class),
        ScriptHardwareAction.MouseMove.TYPE, ClassMapping.make(ScriptHardwareAction.MouseMove.class),
        ScriptHardwareAction.MouseScroll.TYPE, ClassMapping.make(ScriptHardwareAction.MouseScroll.class)
    );
}
