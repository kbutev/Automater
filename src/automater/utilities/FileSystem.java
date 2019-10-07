/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

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
    
    public static String getDirectorySeparator()
    {
        if (DeviceOS.isWindows())
        {
            return "\\";
        }
        
        return "/";
    }
    
    public synchronized static String getLocalFilePath()
    {
        if (absolutePath == null)
        {
            absolutePath = new File("").getAbsolutePath();
        }
        
        return absolutePath;
    }
    
    public static String createFilePathRelativeToLocalPath(String path)
    {
        return getLocalFilePath() + getDirectorySeparator() + path;
    }
    
    public static String createFilePathWithBasePath(String base, String fileName)
    {
        return base + getDirectorySeparator() + fileName;
    }
    
    public static String createFilePathWithoutTheFileName(String path)
    {
        String separator = getDirectorySeparator();
        int lastIndexOfSlash = path.lastIndexOf(separator);
        
        if (lastIndexOfSlash != -1 && lastIndexOfSlash+1 < path.length()) {
            path = path.substring(0, lastIndexOfSlash+1);
        }
        
        return path;
    }
    
    public static String createFileNameFromFilePath(String path)
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
    
    public static String createFilePathEndingWithExtension(String path, String extension)
    {
        if (!path.endsWith(extension))
        {
            path = path + extension;
        }
        
        return path;
    }
    
    public static List<File> getAllInDirectory(String path)
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
    
    public static List<File> getAllFilesInDirectory(String path)
    {
        return getAllFilesInDirectory(path, new ArrayList<>());
    }
    
    public static List<File> getAllFilesInDirectory(String path, String extension)
    {
        ArrayList<String> extensions = new ArrayList<>();
        extensions.add(extension);
        return getAllFilesInDirectory(path, extensions);
    }
    
    public static List<File> getAllFilesInDirectory(String path, List<String> extensions)
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
