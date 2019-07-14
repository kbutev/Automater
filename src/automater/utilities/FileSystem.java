/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import java.io.File;

/**
 *
 * @author Bytevi
 */
public class FileSystem {
    private static String absolutePath;
    
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
        return getLocalFilePath() + "\\" + path;
    }
}
