/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.storage;

import automater.TextValue;
import automater.utilities.Errors;
import automater.utilities.FileSystem;
import automater.utilities.Logger;
import automater.work.model.Macro;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the macros storage.
 * 
 * Macro files can be created, updated and deleted from here.
 *
 * @author Bytevi
 */
public class MacroStorage {
    private final Object _lock = new Object();
    
    private final ArrayList<MacroStorageFile> _macros = new ArrayList<>();
    
    public MacroStorage()
    {
        loadMacrosFromDevice();
    }
    
    public List<Macro> getMacros()
    {
        ArrayList<MacroStorageFile> currentMacros = macros();
        ArrayList<Macro> macros = new ArrayList<>();
        
        for (MacroStorageFile macroFile : currentMacros)
        {
            macros.add(macroFile.getMacro());
        }
        
        return macros;
    }
    
    public void reloadMacrosFromStorage()
    {
        synchronized (_lock)
        {
            loadMacrosFromDevice();
        }
    }
    
    // # Validators
    
    public boolean macroExistsWithName(String name)
    {
        synchronized (_lock)
        {
            ArrayList<MacroStorageFile> currentMacros = macros();
            
            for (MacroStorageFile macro : currentMacros)
            {
                if (macro.name().equals(name))
                {
                    return true;
                }
            }
            
            return false;
        }
    }
    
    public boolean isMacroNameAvailable(String name)
    {
        return MacroStorageFile.getMacroNameIsUnavailableError(name) == null;
    }
    
    public Exception getSaveMacroError(Macro macro)
    {
        if (macro.actions.isEmpty())
        {
            return new Exception("Macro requires at least one action.");
        }
        
        return null;
    }
    
    public Exception getSaveMacroNameError(String name, boolean checkIfNameIsTaken)
    {
        if (checkIfNameIsTaken && GeneralStorage.getDefault().getMacrosStorage().macroExistsWithName(name))
        {
            return new Exception(TextValue.getText(TextValue.Error_NameIsTaken));
        }
        
        return MacroStorageFile.getMacroNameIsUnavailableError(name);
    }
    
    // # Operations
    
    public void saveMacroToStorage(Macro macro) throws Exception
    {
        Logger.message(this, "Saving new macro '" + macro.name + "' to storage...");
        
        Exception saveMacroError = getSaveMacroError(macro);
        
        if (saveMacroError != null)
        {
            Logger.error(this, "Failed to save macro '" + macro.name + "' to storage: " + saveMacroError.toString());
            throw saveMacroError;
        }
        
        Exception saveMacroNameError = getSaveMacroNameError(macro.name, true);
        
        if (saveMacroNameError != null)
        {
            Logger.error(this, "Failed to save macro '" + macro.name + "' to storage: " + saveMacroNameError.toString());
            throw saveMacroNameError;
        }
        
        ArrayList<MacroStorageFile> currentMacros = macros();
        
        MacroStorageFile macroFile = MacroStorageFile.createFromMacro(macro);
        
        synchronized (_lock)
        {
            try {
                // Try to create
                macroFile.create();
                
                // Add to macros collection
                currentMacros.add(macroFile);
                
                Logger.messageEvent(this, "Succesfully saved macro '" + macroFile.name() + "' to storage!");
            } catch (Exception e) {
                String message = "Failed to save '" + macroFile.name() + "' macro to storage!";
                Logger.error(this, message);
                Errors.throwSerializationFailed(message);
            }
        }
    }
    
    public void updateMacroInStorage(Macro macro) throws Exception
    {
        Logger.message(this, "Updating macro " + macro.toString() + ".");
        
        Exception saveMacroError = getSaveMacroError(macro);
        
        if (saveMacroError != null)
        {
            Logger.error(this, "Failed to save macro '" + macro.name + "' to storage: " + saveMacroError.toString());
            throw saveMacroError;
        }
        
        Exception saveMacroNameError = getSaveMacroNameError(macro.name, false);
        
        if (saveMacroNameError != null)
        {
            Logger.error(this, "Failed to save macro '" + macro.name + "' to storage: " + saveMacroNameError.toString());
            throw saveMacroNameError;
        }
        
        ArrayList<MacroStorageFile> currentMacros = macros();
        MacroStorageFile fileToUpdate = null;
        
        for (MacroStorageFile m : currentMacros)
        {
            if (m.name().equals(macro.name))
            {
                fileToUpdate = m;
                break;
            }
        }
        
        if (fileToUpdate == null)
        {
            Logger.error(this, "Failed to update macro '" + macro.name + "' in storage, macro does not exist.");
            Errors.throwInternalLogicError("Failed to update macro, macro does not exist.");
            return;
        }
        
        // Replace old macro with new macro
        fileToUpdate.update(macro);
        
        // Success
        Logger.messageEvent(this, "Succesfully updated macro '" + macro.name + "' in storage!");
    }
    
    public void deleteMacro(Macro macro) throws Exception
    {
        Logger.message(this, "Deleting macro " + macro.toString() + " from storage.");
        
        ArrayList<MacroStorageFile> currentMacros = macros();
        
        if (!macroExistsWithName(macro.name))
        {
            Errors.throwInvalidArgument("Macro does not exist in storage");
        }
        
        MacroStorageFile fileToDelete = null;
        
        for (MacroStorageFile file : currentMacros)
        {
            if (file.name().equals(macro.name))
            {
                fileToDelete = file;
                break;
            }
        }
        
        if (fileToDelete == null)
        {
            Logger.error(this, "Failed to delete macro '" + macro.name + "' from storage");
            return;
        }
        
        // Try to delete
        fileToDelete.delete();
        
        // Remove from macros collection
        synchronized (_lock)
        {
            currentMacros.remove(fileToDelete);
        }
    }
    
    public void clearMacros()
    {
        Logger.message(this, "Deleting out all macros from storage...");
        
        clearFileData();
    }
    
    // # Private
    
    private ArrayList<MacroStorageFile> macros()
    {
        synchronized (_lock)
        {
            // Load macros
            if (_macros.isEmpty())
            {
                loadMacrosFromDevice();
            }
            
            return _macros;
        }
    }
    
    private void loadMacrosFromDevice()
    {
        // None of this is thread safe
        // Call this method from synchronized context
        
        ArrayList<MacroStorageFile> currentMacros = _macros;
        
        String localPath = FileSystem.getLocalFilePath();
        
        List<File> files = FileSystem.getAllFilesInDirectory(localPath, MacroStorageFile.FILE_NAME_EXTENSION);
        
        Logger.message(this, "Loading macros from storage, found " + String.valueOf(files.size()) + " files");
        
        currentMacros.clear();
        
        for (File file : files)
        {
            MacroStorageFile macroFile = MacroStorageFile.createFromFile(file);
            
            if (macroFile != null)
            {
                currentMacros.add(macroFile);
            }
        }
    }
    
    private void clearFileData()
    {
        ArrayList<MacroStorageFile> currentMacros = macros();
        
        synchronized (_lock)
        {
            for (MacroStorageFile file : currentMacros)
            {
                try {
                    file.delete();
                } catch (Exception e) {
                    
                }
            }
            
            currentMacros.clear();
        }
    }
}
