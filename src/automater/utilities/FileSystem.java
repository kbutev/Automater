/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines commonly used file system methods.
 *
 * @author Bytevi
 */
public class FileSystem {
    private static String absolutePath;
    
    public static @NotNull String getDirectorySeparator()
    {
        if (DeviceOS.isWindows())
        {
            return "\\";
        }
        
        return "/";
    }
    
    public synchronized static @NotNull String getLocalFilePath()
    {
        if (absolutePath == null)
        {
            absolutePath = new File("").getAbsolutePath();
        }
        
        return absolutePath;
    }
    
    public static @NotNull String createFilePathRelativeToLocalPath(@NotNull String path)
    {
        String base = getLocalFilePath();
        
        if (base.isEmpty())
        {
            return path;
        }
        
        return base + getDirectorySeparator() + path;
    }
    
    public static @NotNull String createFilePathWithBasePath(@NotNull String base, @NotNull String fileName)
    {
        if (base.isEmpty())
        {
            return fileName;
        }
        
        return base + getDirectorySeparator() + fileName;
    }
    
    public static @NotNull String createFilePathWithoutTheFileName(@NotNull String path)
    {
        String separator = getDirectorySeparator();
        int lastIndexOfSlash = path.lastIndexOf(separator);
        
        if (lastIndexOfSlash != -1 && lastIndexOfSlash+1 < path.length()) {
            path = path.substring(0, lastIndexOfSlash+1);
        }
        
        return path;
    }
    
    public static @NotNull String createFileNameFromFilePath(@NotNull String path)
    {
        String separator = getDirectorySeparator();
        int lastIndexOfSlash = path.lastIndexOf(separator);
        
        if (lastIndexOfSlash == -1)
        {
            return path;
        }
        
        if (lastIndexOfSlash+1 < path.length())
        {
            path = path.substring(lastIndexOfSlash+1);
            return path;
        }
        
        return "";
    }
    
    public static @NotNull String createFilePathEndingWithExtension(@NotNull String path, @NotNull String extension)
    {
        if (!path.endsWith(extension))
        {
            path = path + extension;
        }
        
        return path;
    }
    
    public static @NotNull List<File> getAllInDirectory(@NotNull String path)
    {
        ArrayList<File> files = new ArrayList<>();
        
        File folder = new File(path);
        
        if (!folder.isDirectory())
        {
            return files;
        }
        
        File[] listOfFiles = folder.listFiles();
        
        for (int e = 0; e < listOfFiles.length; e++)
        {
            File file = listOfFiles[e];
            
            files.add(file);
        }
        
        return files;
    }
    
    public static @NotNull List<File> getAllFilesInDirectory(@NotNull String path)
    {
        return getAllFilesInDirectory(path, new ArrayList<>());
    }
    
    public static @NotNull List<File> getAllFilesInDirectory(@NotNull String path, @NotNull String extension)
    {
        ArrayList<String> extensions = new ArrayList<>();
        extensions.add(extension);
        return getAllFilesInDirectory(path, extensions);
    }
    
    public static @NotNull List<File> getAllFilesInDirectory(@NotNull String path, @NotNull List<String> extensions)
    {
        ArrayList<File> files = new ArrayList<>();
        
        File folder = new File(path);
        
        if (!folder.isDirectory())
        {
            return files;
        }
        
        File[] listOfFiles = folder.listFiles();
        
        for (int e = 0; e < listOfFiles.length; e++)
        {
            File file = listOfFiles[e];
            
            if (file.isDirectory())
            {
                continue;
            }
            
            for (String extension : extensions)
            {
                if (file.getName().endsWith(extension))
                {
                    files.add(listOfFiles[e]);
                }
            }
        }
        
        return files;
    }
}
