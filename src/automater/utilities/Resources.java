/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

/**
 *
 * @author Byti
 */
public class Resources {
    public final static String RESOURCES_DIRECTORY_PATH = "resources";
    
    public final static String DEFAULT_IMAGE_EXTENSION = "png";
    public final static String PNG_EXTENSION = "png";
    public final static String JPG_EXTENSION = "jpg";
    public final static String JPEG_EXTENSION = "jpeg";
    
    public static String getImagePath(String key)
    {
        String file = addImageExtensionIfNecessary(key);
        
        return FileSystem.createFilePathWithBasePath(RESOURCES_DIRECTORY_PATH, file);
    }
    
    private static String addImageExtensionIfNecessary(String key)
    {
        if (containsImageExtension(key))
        {
            return key;
        }
        
        return key + "." + DEFAULT_IMAGE_EXTENSION;
    }
    
    private static boolean containsImageExtension(String key)
    {
        return key.contains("." + DEFAULT_IMAGE_EXTENSION) || 
                key.contains("." + PNG_EXTENSION) ||
                key.contains("." + JPG_EXTENSION) || 
                key.contains("." + JPEG_EXTENSION);
    }
}