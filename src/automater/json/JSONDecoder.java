/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.json;

import com.google.gson.*;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.annotations.SerializedName;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang3.ClassUtils;

/**
 * Deserializes JSON objects.
 * 
 * Only fields marked by @SerializedName are evaluated.
 * Lists are enumerated.
 * 
 * @param <T> Model to parse to.
 * @author Kristiyan Butev
 */
public class JSONDecoder<T> implements JsonDeserializer<T> {
    @Retention(RetentionPolicy.RUNTIME) // to make reading of this field possible at the runtime
    @Target(ElementType.FIELD)          // to make annotation accessible through reflection
    public @interface Optional {}
    
    private final Gson parser = new Gson();
    
    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        // Parsing object as usual.
        try {
            T result = parser.fromJson(je, type);
            if (je instanceof JsonObject json) {
                verifyObject(result, json);
            }
            return result;
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
    
    private void verifyObject(@NotNull Object object, @NotNull JsonObject json) throws JsonParseException {
        // Checking nested list items too.
        if (object instanceof List list) {
            for (var item : list) {
                verifyObject(item, json);
            }
            
            return;
        }
        
        try {
            var allFields = getAllFields(object);
            verifyFields(object, allFields, json);
            
            for (var field : allFields) {
                var serName = field.getAnnotation(SerializedName.class).value();
                var clazz = field.getType();
                
                if (json.get(serName) instanceof JsonObject subJSON) {
                    verifyObject(field.get(object), subJSON);
                } else if (!ClassUtils.isPrimitiveOrWrapper(clazz) && !clazz.equals(String.class) && !clazz.isEnum()) {
                    throw new JsonParseException("Missing JSON map for object '" + serName + "'");
                }
            }
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
    
    private void verifyFields(@NotNull Object object, @NotNull List<Field> fields, @NotNull JsonObject json) throws Exception {
        for (var field : fields) {
            // Only fields with SerializedName are evaluated
            // Fields marked as optional are skipped
            var annotation = field.getAnnotation(SerializedName.class);
            if (json.get(annotation.value()) == null) {
                throw new JsonParseException("Missing JSON field '" + annotation.value() + "'");
            }
            
            var clazz = field.getType();
            
            // Check for null objects (includes enums, which are set to null if given invalid value)
            if (!ClassUtils.isPrimitiveOrWrapper(clazz) && field.get(object) == null) {
                throw new JsonParseException("Invalid JSON field '" + annotation.value() + "'");
            }
        }
    }
    
    private <T> @NotNull List<Field> getAllFields(@NotNull T t) {
        List<Field> result = new ArrayList<>();
        Class clazz = t.getClass();
        
        while (clazz != Object.class) {
            for (var field : clazz.getDeclaredFields()) {
                if (!isRequiredJSONField(field)) {
                    continue;
                }
                
                result.add(field);
            }
            
            clazz = clazz.getSuperclass();
        }
        
        return result;
    }
    
    private boolean isRequiredJSONField(@NotNull Field field) {
        if (field.getAnnotation(Optional.class) != null || field.getAnnotation(SerializedName.class) == null) {
            return false;
        }
        
        return field.getClass().getSuperclass() != null;
    }
}
