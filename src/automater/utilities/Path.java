/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import java.io.File;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public class Path {
    
    public static final String EXTENSION_DOT = ".";
    
    // # Build
    
    public static @NotNull Path getLocalDirectory() {
        return new Path(java.nio.file.Path.of(System.getProperty("user.dir")));
    }
    
    public static @NotNull Path build(@NotNull String lastComponent) {
        return getLocalDirectory().withSubpath(lastComponent);
    }
    
    public Path(@NotNull java.nio.file.Path value) {
        assert value != null;
        stringValue = value.toString();
    }
    
    public Path(@NotNull String value) {
        assert value != null;
        stringValue = value;
    }
    
    // # Manipulation
    
    @Override
    public @NotNull String toString() {
        return stringValue;
    }
    
    public @NotNull String toStringWithQuotes() {
        return "'" + stringValue + "'";
    }
    
    public @NotNull String lastComponent() {
        var result = toJavaPath().getFileName();
        return result != null ? result.toString() : "";
    }
    
    public @Nullable Path parent() {
        var result = toJavaPath().getParent();
        
        if (result == null) {
            return null;
        }
        
        return new Path(result);
    }
    
    public @NotNull Path withSubpath(@NotNull String lastComponent) {
        return new Path(toJavaPath().resolve(lastComponent));
    }
    
    public @NotNull Path withSubpath(@NotNull Path lastComponent) {
        return new Path(toJavaPath().resolve(lastComponent.stringValue));
    }
    
    public @NotNull java.nio.file.Path toJavaPath() {
        return java.nio.file.Path.of(stringValue);
    }
    
    public @NotNull Path withFileExtension(@NotNull String extension) {
        if (!hasExtension(extension)) {
            if (!extension.startsWith(EXTENSION_DOT)) {
                extension = EXTENSION_DOT + extension;
            }
            
            return new Path(stringValue + extension);
        }
        
        return this;
    }
    
    // # Extension
    
    public @Nullable String extension() {
        int index = stringValue.lastIndexOf(EXTENSION_DOT);
        
        if (index == -1  || index >= stringValue.length()) {
            return null;
        }
        
        return stringValue.substring(index+1);
    }
    
    public boolean hasExtension(@NotNull String extension) {
        if (extension.startsWith(EXTENSION_DOT)) {
            extension = extension.substring(EXTENSION_DOT.length());
        }
        
        var ex = extension();
        return ex != null ? ex.equals(extension) : false;
    }
    
    // # File
    
    public boolean exists() {
        return getFile().exists();
    }
    
    public @NotNull File getFile() {
        return toJavaPath().toFile();
    }
    
    // # Private
    
    private final @NotNull String stringValue;
}
