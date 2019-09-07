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
 *
 * @author Bytevi
 */
public class FileSystem {
    private static String absolutePath;
    
    public static String getDirectorySeparator()
    {
        return "\\";
    }
    
    public synchronized static String getLocalFilePath()
    {
        if (absolutePath == null)
        {
            absolutePath = new File("").getAbsolutePath();
        }
        
        return absolutePath;
    }
    
    public static String filePathRelativeToLocalPath(String path)
    {
        return getLocalFilePath() + getDirectorySeparator() + path;
    }
    
    public static String createFilePathWithBasePath(String base, String fileName)
    {
        return base + getDirectorySeparator() + fileName;
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
