/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.di.DI;
import automater.json.JSONDecoder;
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
import automater.model.action.MacroAction;
import automater.model.action.MacroHardwareAction;

/**
 * Parsers macro actions and other related data.
 * Immutable.
 * @author Kristiyan Butev
 */
public interface MacroActionParser {
    
    class Keys {
        static final String TYPE = "a";
    }
    
    interface Protocol {
        
        @NotNull MacroAction parseFromCapturedEvent(@NotNull CapturedEvent event, double timestamp) throws Exception;
        @NotNull MacroAction parseFromJSON(@NotNull JsonElement json) throws Exception;
        @NotNull JsonElement parseToJSON(@NotNull MacroAction action) throws Exception;
    }
    
    class Impl implements Protocol {
        
        final Gson gson = DI.get(Gson.class);
        
        @Override
        public @NotNull MacroAction parseFromCapturedEvent(@NotNull CapturedEvent event, double timestamp) throws Exception {
            if (event instanceof CapturedHardwareEvent.Click click) {
                return new MacroHardwareAction.Click(timestamp, click.kind, click.keystroke);
            } else if (event instanceof CapturedHardwareEvent.MouseMove mm) {
                return new MacroHardwareAction.MouseMove(timestamp, mm.point);
            } else if (event instanceof CapturedHardwareEvent.MouseScroll ms) {
                return new MacroHardwareAction.MouseScroll(timestamp, ms.point, ms.scroll);
            }
            
            throw new UnsupportedOperationException("Unrecognizable event");
        }
        
        @Override
        public @NotNull MacroAction parseFromJSON(@NotNull JsonElement json) throws Exception {
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
                var parser = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(matchedType, decoder).create();
                return parser.fromJson(jsonString, matchedType);
            }
            
            throw new JsonSyntaxException("Invalid action");
        }
        
        @Override
        public @NotNull JsonObject parseToJSON(@NotNull MacroAction action) throws Exception {
            var result = gson.toJsonTree(action);
            
            if (result instanceof JsonObject jsonObject) {
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
        MacroHardwareAction.Click.TYPE, ClassMapping.make(MacroHardwareAction.Click.class),
        MacroHardwareAction.MouseMove.TYPE, ClassMapping.make(MacroHardwareAction.MouseMove.class),
        MacroHardwareAction.MouseScroll.TYPE, ClassMapping.make(MacroHardwareAction.MouseScroll.class)
    );
}
