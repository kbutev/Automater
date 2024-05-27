/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;

/**
 * Collection of resource utility methods and some resource constant values.
 * 
 * @author Byti
 */
public class Resources {
    public final static String RESOURCES_DIRECTORY_PATH = "resources";
    
    public final static String DEFAULT_IMAGE_EXTENSION = "png";
    public final static String PNG_EXTENSION = "png";
    public final static String JPG_EXTENSION = "jpg";
    public final static String JPEG_EXTENSION = "jpeg";
    
    public static @NotNull String getImagePath(@NotNull String key)
    {
        String file = addImageExtensionIfNecessary(key);
        
        return FileSystem.createFilePathWithBasePath(RESOURCES_DIRECTORY_PATH, file);
    }
    
    private static @NotNull String addImageExtensionIfNecessary(@NotNull String key)
    {
        if (containsImageExtension(key))
        {
            return key;
        }
        
        return key + "." + DEFAULT_IMAGE_EXTENSION;
    }
    
    private static boolean containsImageExtension(@NotNull String key)
    {
        return key.contains("." + DEFAULT_IMAGE_EXTENSION) || 
                key.contains("." + PNG_EXTENSION) ||
                key.contains("." + JPG_EXTENSION) || 
                key.contains("." + JPEG_EXTENSION);
    }
}
