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
import com.sun.istack.internal.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Represents the storage for macros.
 *
 * @author Bytevi
 */
public class MacroStorage {
    public static final String FILE_NAME = "macros_storage.txt";
    public static final int MACRO_NAME_MIN_LENGTH = 3;
    
    private final Object _lock = new Object();
    
    private ArrayList<Macro> _macros = new ArrayList<Macro>();
    
    public MacroStorage()
    {
        loadMacrosFromFile();
    }
    
    public ArrayList<Macro> getMacros()
    {
        synchronized (_lock)
        {
            // Load macros
            if (_macros.isEmpty())
            {
                loadMacrosFromFile();
            }
            
            return _macros;
        }
    }
    
    public boolean macroExistsWithName(String name)
    {
        synchronized (_lock)
        {
            ArrayList<Macro> currentMacros = getMacros();
            
            for (Macro macro : currentMacros)
            {
                if (macro.name.equals(name))
                {
                    return true;
                }
            }
            
            return false;
        }
    }
    
    public void clearMacros()
    {
        Logger.message(this, "Wiping out all macros from storage...");
        
        clearFileData();
    }
    
    public void addMacroToStorage(Macro macro) throws Exception
    {
        Logger.message(this, "Saving macro '" + macro.name + "' to storage...");
        
        ArrayList<Macro> currentMacros = getMacros();
        currentMacros.add(macro);
        
        String data = Archiver.serializeObject(currentMacros);
        
        if (data != null)
        {
            writeToFile(data);
            Logger.message(this, "Macro '" + macro.name + "' saved to storage!");
        }
    }
    
    public void addMacrosToStorage(ArrayList<Macro> macros) throws Exception
    {
        for (int e = 0; e < macros.size(); e++)
        {
            addMacroToStorage(macros.get(e));
        }
    }
    
    public boolean isMacroNameAvailable(String name)
    {
        return getMacroNameIsUnavailableError(name) == null;
    }
    
    public @Nullable Exception getMacroNameIsUnavailableError(String name)
    {
        if (name.isEmpty())
        {
            return new Exception("Name cannot be empty");
        }
        
        if (name.length() < MACRO_NAME_MIN_LENGTH)
        {
            return new Exception("Name must be at least " + String.valueOf(MACRO_NAME_MIN_LENGTH) + " characters long");
        }
        
        if (GeneralStorage.getDefault().getMacrosStorage().macroExistsWithName(name))
        {
            return new Exception("Name already taken");
        }
        
        return null;
    }
    
    public boolean canSaveMacro(Macro macro)
    {
        return getSaveMacroError(macro) == null;
    }
    
    public @Nullable Exception getSaveMacroError(Macro macro)
    {
        if (macro.r.userInputs.isEmpty())
        {
            return new Exception("Macro requires at least one user input action.");
        }
        
        return getMacroNameIsUnavailableError(macro.name);
    }
    
    // # Private
    
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
            
            writer.close();
        }
    }
    
    private String readFromFile()
    {
        String data = "";
        
        synchronized (_lock)
        {
            try {
                File file = getFile();
                
                BufferedReader reader = new BufferedReader(new FileReader(file));
                
                String line = reader.readLine();
                
                while (line != null)
                {
                    data = data.concat(line);
                    line = reader.readLine();
                }
                
                reader.readLine();
                reader.close();
            } catch (Exception e) {
                
            }
        }
        
        return data;
    }
    
    private void loadMacrosFromFile()
    {
        String macrosData = readFromFile();
        
        ArrayList<Macro> macrosLoaded = Archiver.deserializeArray(Macro.class, macrosData);
        
        if (macrosLoaded != null)
        {
            Logger.message(this, "Retrieved " + macrosLoaded.size() + " macros from storage");
            
            this._macros = macrosLoaded;
        }
    }
}
