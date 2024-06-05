/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.storage;

import automater.TextValue;
import automater.utilities.Errors;
import automater.utilities.FileSystem;
import automater.utilities.Logger;
import automater.work.model.Macro;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    private final @NotNull Object _lock = new Object();

    private final @NotNull ArrayList<MacroStorageFile> _macros = new ArrayList<>();

    public MacroStorage() {
        loadMacrosFromDevice();
    }

    public @NotNull List<Macro> getMacros() {
        ArrayList<MacroStorageFile> currentMacros = macros();
        ArrayList<Macro> macros = new ArrayList<>();

        for (MacroStorageFile macroFile : currentMacros) {
            macros.add(macroFile.getMacro());
        }

        return macros;
    }

    public void reloadMacrosFromStorage() {
        synchronized (_lock) {
            loadMacrosFromDevice();
        }
    }

    // # Validators
    public boolean macroExistsWithName(@NotNull String name) {
        synchronized (_lock) {
            ArrayList<MacroStorageFile> currentMacros = macros();

            for (MacroStorageFile macro : currentMacros) {
                if (macro.name().equals(name)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean isMacroNameAvailable(@NotNull String name) {
        return MacroStorageFile.getMacroNameIsUnavailableError(name) == null;
    }

    public @Nullable Exception getSaveMacroError(@NotNull Macro macro) {
        if (macro.actions.isEmpty()) {
            return new Exception("Macro requires at least one action.");
        }

        return null;
    }

    public @Nullable Exception getSaveMacroNameError(@NotNull String name, boolean checkIfNameIsTaken) {
        if (checkIfNameIsTaken && macroExistsWithName(name)) {
            return new Exception(TextValue.getText(TextValue.Error_NameIsTaken));
        }

        return MacroStorageFile.getMacroNameIsUnavailableError(name);
    }

    // # Operations
    public void saveMacroToStorage(@NotNull Macro macro) throws Exception {
        Logger.message(this, "Saving new macro '" + macro.name + "' to storage...");

        Exception saveMacroError = getSaveMacroError(macro);

        if (saveMacroError != null) {
            Logger.error(this, "Failed to save macro '" + macro.name + "' to storage: " + saveMacroError.getMessage());
            throw saveMacroError;
        }

        Exception saveMacroNameError = getSaveMacroNameError(macro.name, true);

        if (saveMacroNameError != null) {
            Logger.error(this, "Failed to save macro '" + macro.name + "' to storage: " + saveMacroNameError.getMessage());
            throw saveMacroNameError;
        }

        ArrayList<MacroStorageFile> currentMacros = macros();

        MacroStorageFile macroFile = MacroStorageFile.createFromMacro(macro);

        synchronized (_lock) {
            try {
                // Try to create
                macroFile.create();

                // Add to macros collection
                currentMacros.add(macroFile);

                Logger.messageEvent(this, "Succesfully saved macro '" + macroFile.name() + "' to storage!");
            } catch (Exception e) {
                String message = "Failed to save '" + macroFile.name() + "' macro to storage: " + e.getMessage();
                Logger.error(this, message);
                throw Errors.serializationFailed();
            }
        }
    }

    public void updateMacroInStorage(@NotNull Macro macro) throws Exception {
        Logger.message(this, "Updating macro " + macro.toString() + ".");

        Exception saveMacroError = getSaveMacroError(macro);

        if (saveMacroError != null) {
            Logger.error(this, "Failed to save macro '" + macro.name + "' to storage: " + saveMacroError.toString());
            throw saveMacroError;
        }

        Exception saveMacroNameError = getSaveMacroNameError(macro.name, false);

        if (saveMacroNameError != null) {
            Logger.error(this, "Failed to save macro '" + macro.name + "' to storage: " + saveMacroNameError.toString());
            throw saveMacroNameError;
        }

        ArrayList<MacroStorageFile> currentMacros = macros();
        MacroStorageFile fileToUpdate = null;

        for (MacroStorageFile m : currentMacros) {
            if (m.name().equals(macro.name)) {
                fileToUpdate = m;
                break;
            }
        }

        if (fileToUpdate == null) {
            Logger.error(this, "Failed to update macro '" + macro.name + "' in storage, macro does not exist.");
            throw Errors.internalLogicError();
        }

        // Replace old macro with new macro
        fileToUpdate.update(macro);

        // Success
        Logger.messageEvent(this, "Succesfully updated macro '" + macro.name + "' in storage!");
    }

    public void deleteMacro(@NotNull Macro macro) throws Exception {
        Logger.message(this, "Deleting macro " + macro.toString() + " from storage.");

        ArrayList<MacroStorageFile> currentMacros = macros();

        if (!macroExistsWithName(macro.name)) {
            throw Errors.invalidArgument("macro");
        }

        MacroStorageFile fileToDelete = null;

        for (MacroStorageFile file : currentMacros) {
            if (file.name().equals(macro.name)) {
                fileToDelete = file;
                break;
            }
        }

        if (fileToDelete == null) {
            Logger.error(this, "Failed to delete macro '" + macro.name + "' from storage");
            return;
        }

        // Try to delete
        fileToDelete.delete();

        // Remove from macros collection
        synchronized (_lock) {
            currentMacros.remove(fileToDelete);
        }
    }

    public void clearMacros() {
        Logger.message(this, "Deleting out all macros from storage...");

        clearFileData();
    }

    // # Private
    private @NotNull ArrayList<MacroStorageFile> macros() {
        synchronized (_lock) {
            // Load macros
            if (_macros.isEmpty()) {
                loadMacrosFromDevice();
            }

            return _macros;
        }
    }

    private void loadMacrosFromDevice() {
        // None of this is thread safe
        // Call this method from synchronized context

        ArrayList<MacroStorageFile> currentMacros = _macros;

        String localPath = FileSystem.getLocalFilePath();

        List<File> files = FileSystem.getAllFilesInDirectory(localPath, MacroStorageFile.FILE_NAME_EXTENSION);

        Logger.messageEvent(this, "Loading macros from storage from '" + localPath + "'...");
        Logger.messageEvent(this, "Found " + String.valueOf(files.size()) + " macro files");

        currentMacros.clear();

        for (File file : files) {
            MacroStorageFile macroFile = MacroStorageFile.createFromFile(file);

            if (macroFile != null) {
                currentMacros.add(macroFile);
            }
        }
    }

    private void clearFileData() {
        ArrayList<MacroStorageFile> currentMacros = macros();

        synchronized (_lock) {
            for (MacroStorageFile file : currentMacros) {
                try {
                    file.delete();
                } catch (Exception e) {

                }
            }

            currentMacros.clear();
        }
    }
}
