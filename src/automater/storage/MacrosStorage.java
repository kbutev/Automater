/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.storage;

import automater.utilities.Archiver;
import automater.utilities.FileSystem;
import automater.utilities.Logger;
import automater.work.Macro;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Bytevi
 */
public class MacrosStorage {
    public static final String FILE_NAME = "macros_storage.txt";
    
    private final Object _lock = new Object();
    
    public MacrosStorage()
    {
        
    }
    
    public ArrayList<Macro> getMacros()
    {
        ArrayList<Macro> macros = new ArrayList<Macro>();
        
        return macros;
    }
    
    public void clearMacros()
    {
        Logger.message(this, "Wiping out all macros from storage...");
        
        clearFileData();
    }
    
    public void addMacrosToStorage(ArrayList<Macro> macros) throws Exception
    {
        Logger.message(this, "Saving macros to storage...");
        
        ArrayList<Macro> currentMacros = getMacros();
        
        String data = Archiver.serializeObject(currentMacros);
        
        if (data != null)
        {
            writeToFile(data);
        }
    }
    
    private File getFile() throws Exception
    {
        File file = new File(FileSystem.filePathRelativeToLocalPath(FILE_NAME));
        return file;
    }
    
    private void clearFileData()
    {
        try {
            writeToFile("");
        } catch (Exception e) {
            
        }
    }
    
    private void writeToFile(String data) throws Exception
    {
        synchronized (_lock)
        {
            File file = getFile();
            
            PrintWriter writer = new PrintWriter(file);
            
            String[] lines = data.split("\n");
            
            for (String line : lines)
            {
                writer.println(line);
            }
            
            writer.println();
        }
    }
    
    private String readFromFile()
    {
        String data = "";
        
        synchronized (_lock)
        {
            try {
                File file = getFile();
            } catch (Exception e) {
                
            }
        }
        
        return data;
    }
}
