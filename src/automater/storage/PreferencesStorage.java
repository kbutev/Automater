/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.storage;

import automater.utilities.Archiver;
import automater.utilities.Errors;
import automater.utilities.FileSystem;
import automater.utilities.Logger;
import automater.work.model.MacroParameters;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 *
 * @author Bytevi
 */
public class PreferencesStorage {
    public static final String PREFERENCES_FILE = "preferences";
    
    private final Object _lock = new Object();
    
    private PreferencesStorageValues _values = PreferencesStorageValues.defaultValues();
    
    public PreferencesStorage()
    {
        loadPrefrencesFromDevice();
    }
    
    // # Properties
    
    public PreferencesStorageValues getValues()
    {
        synchronized (_lock)
        {
            return _values.copy();
        }
    }
    
    public void saveValues(PreferencesStorageValues values)
    {
        Logger.messageEvent(this, "Save new preference values");
        
        synchronized (_lock)
        {
            _values = values;
        }
        
        try {
            writeToFile(getFile());
        } catch (Exception e) {
            
        }
    }
    
    // # Public
    
    public void reloadPrefrencesFromStorage()
    {
        synchronized (_lock)
        {
            loadPrefrencesFromDevice();
        }
    }
    
    // # Private
    
    private String filePath()
    {
        String path = FileSystem.getLocalFilePath();
        return FileSystem.createFilePathWithBasePath(path, PREFERENCES_FILE);
    }
    
    private File getFile() throws Exception
    {
        File file = new File(filePath());
        return file;
    }
    
    private void loadPrefrencesFromDevice()
    {
        Logger.messageEvent(this, "Reload preference values from storage");
        
        String data;
        
        try {
            data = readFromFile(getFile());
        } catch (Exception e)
        {
            // Silent return, if file does not exist, use default preferences
            return;
        }
        
        PreferencesStorageValues values;
        
        try {
            values = Archiver.deserializeObject(PreferencesStorageValues.class, data);
        } catch (Exception e) {
            Logger.error(this, "Failed to deserialize the storage values");
            Errors.throwSerializationFailed("Failed to deserialize the storage values");
            return;
        }
        
        // Update values
        synchronized (_lock)
        {
            _values = values;
        }
    }
    
    private void writeToFile(File file) throws Exception
    {
        PreferencesStorageValues values;
        
        synchronized (_lock)
        {
            values = _values;
        }
        
        String data = Archiver.serializeObject(values);
        
        if (data == null)
        {
            Logger.error(this, "Failed to serialize the storage values");
            Errors.throwSerializationFailed("Failed to serialize the storage values");
            return;
        }
        
        PreferencesStorage.writeToFile(file, data);
    }
    
    private static void writeToFile(File file, String data) throws Exception
    {
        PrintWriter writer = null;
        
        try {
            writer = new PrintWriter(file);
            String[] lines = data.split("\n");
            
            for (String line : lines)
            {
                writer.println(line);
            } 
            
            writer.println();
            writer.close();
        } catch (Exception e) {
            try {
                if (writer != null)
                {
                    writer.close();
                }
            } catch (Exception e2) {
                
            }
        }
    }
    
    private static String readFromFile(File file)
    {
        String data = "";
        
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new FileReader(file));
            
            String line = reader.readLine();
            
            while (line != null)
            {
                data = data.concat(line);
                line = reader.readLine();
            }
            
            reader.readLine();
            reader.close();
        } catch (Exception e) {
            try {
                if (reader != null)
                {
                    reader.close();
                }
            } catch (Exception e2) {
            
            }
        }
        
        return data;
    }
}
