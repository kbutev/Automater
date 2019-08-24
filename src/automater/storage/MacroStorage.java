/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.storage;

import automater.TextValue;
import automater.utilities.Archiver;
import automater.utilities.CollectionUtilities;
import automater.utilities.Errors;
import automater.utilities.FileSystem;
import automater.utilities.Logger;
import automater.work.model.Macro;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the storage for macros.
 *
 * @author Bytevi
 */
public class MacroStorage {
    public static final String FILE_NAME = "macros_storage.txt";
    public static final int MACRO_NAME_MIN_LENGTH = 3;
    public static final int MACRO_NAME_MAX_LENGTH = 18;
    
    private final Object _lock = new Object();
    
    private ArrayList<Macro> _macros = new ArrayList<Macro>();
    
    public MacroStorage()
    {
        loadMacrosFromFile();
    }
    
    public List<Macro> getMacros()
    {
        return CollectionUtilities.copyAsImmutable(macros());
    }
    
    public void reloadMacrosFromStorage()
    {
        synchronized (_lock)
        {
            loadMacrosFromFile();
        }
    }
    
    public boolean macroExistsWithName(String name)
    {
        synchronized (_lock)
        {
            ArrayList<Macro> currentMacros = macros();
            
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
    
    public void saveMacroToStorage(Macro macro) throws Exception
    {
        Logger.message(this, "Saving macro '" + macro.name + "' to storage...");
        
        ArrayList<Macro> currentMacros = macros();
        
        if (macroExistsWithName(macro.name))
        {
            Logger.error(this, "Failed to save macro '" + macro.name + "' to storage, already saved.");
            return;
        }
        
        currentMacros.add(macro);
        
        try {
            writeMacrosToFile(currentMacros);
            Logger.messageEvent(this, "Macro '" + macro.name + "' saved to storage!");
        } catch (Exception e) {
            // Remove the failed archived object
            currentMacros.remove(macro);
            
            String message = "Failed to save '" + macro.name + "' macro to storage!";
            Logger.error(this, message);
            Errors.throwSerializationFailed(message);
        }
    }
    
    public void saveMacrosToStorage(ArrayList<Macro> macros) throws Exception
    {
        for (int e = 0; e < macros.size(); e++)
        {
            saveMacroToStorage(macros.get(e));
        }
    }
    
    public void updateMacroInStorage(Macro macro) throws Exception
    {
        String macroName = macro.name;
        
        if (!macroExistsWithName(macroName))
        {
            Logger.error(this, "Failed to update macro '" + macroName + "' in storage, macro does not exist.");
            return;
        }
        
        ArrayList<Macro> currentMacros = macros();
        int index = -1;
        
        for (Macro m : currentMacros)
        {
            if (m.name.equals(macroName))
            {
                index = currentMacros.indexOf(m);
                break;
            }
        }
        
        if (index == -1)
        {
            Errors.throwInternalLogicError("Failed to update macro, cannot find macro.");
        }
        
        // Replace old macro with new macro
        currentMacros.set(index, macro);
        
        // Save changes
        writeMacrosToFile(currentMacros);
    }
    
    public void deleteMacro(Macro macro) throws Exception
    {
        if (!_macros.contains(macro))
        {
            Errors.throwInvalidArgument("Macro does not exist in storage");
        }
        
        Logger.message(this, "Deleting macro " + macro.toString() + " from storage.");
        
        _macros.remove(macro);
        
        writeMacrosToFile(_macros);
        
        reloadMacrosFromStorage();
    }
    
    public void clearMacros()
    {
        Logger.message(this, "Wiping out all macros from storage...");
        
        clearFileData();
    }
    
    public boolean isMacroNameAvailable(String name)
    {
        return getMacroNameIsUnavailableError(name) == null;
    }
    
    public Exception getMacroNameIsUnavailableError(String name)
    {
        if (name.isEmpty())
        {
            return new Exception(TextValue.getText(TextValue.Error_NameIsEmpty));
        }
        
        if (name.length() < MACRO_NAME_MIN_LENGTH)
        {
            return new Exception(TextValue.getText(TextValue.Error_NameIsTooShort,  String.valueOf(MACRO_NAME_MIN_LENGTH)));
        }
        
        if (name.length() > MACRO_NAME_MAX_LENGTH)
        {
            return new Exception(TextValue.getText(TextValue.Error_NameIsTooLong,  String.valueOf(MACRO_NAME_MAX_LENGTH)));
        }
        
        if (GeneralStorage.getDefault().getMacrosStorage().macroExistsWithName(name))
        {
            return new Exception(TextValue.getText(TextValue.Error_NameIsTaken));
        }
        
        return null;
    }
    
    public boolean canSaveMacro(Macro macro)
    {
        return getSaveMacroError(macro) == null;
    }
    
    public Exception getSaveMacroError(Macro macro)
    {
        if (macro.getData().userInputs.isEmpty())
        {
            return new Exception("Macro requires at least one user input action.");
        }
        
        return getMacroNameIsUnavailableError(macro.name);
    }
    
    // # Private
    
    private ArrayList<Macro> macros()
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
    
    private void writeMacrosToFile(ArrayList<Macro> macros) throws Exception
    {
        String data = Archiver.serializeObject(macros);
        
        if (data == null)
        {
            Errors.throwSerializationFailed("Failed to serialize macros");
        }
        
        writeToFile(data);
    }
}
