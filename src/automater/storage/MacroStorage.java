/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.storage;

import automater.TextValue;
import automater.di.DI;
import automater.model.macro.Macro;
import automater.model.macro.MacroSummary;
import automater.parser.MacroParser;
import automater.utilities.Errors;
import automater.utilities.FileSystem;
import automater.utilities.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface MacroStorage {
    
    public static final String MACRO_EXTENSION = "macro";
    
    interface Protocol {
        
        @NotNull List<MacroSummary> getMacroSummaryList() throws Exception;
        @NotNull MacroSummary getMacroSummary(@NotNull String name) throws Exception;
        @NotNull Macro.Protocol getMacro(@NotNull String name) throws Exception;
        @NotNull void saveMacro(@NotNull Macro.Protocol macro) throws Exception;
        void deleteMacro(@NotNull String name);
    }
    
    class Impl implements Protocol {
        
        private final Gson gson = DI.get(Gson.class);
        
        private final MacroParser.Protocol macroParser = DI.get(MacroParser.Protocol.class);
        
        private @NotNull String storagePath;
        
        public Impl() {
            storagePath = FileSystem.getLocalFilePath();
        }

        @Override
        public List<MacroSummary> getMacroSummaryList() throws Exception {
            ArrayList<MacroSummary> result = new ArrayList<>();
            List<File> files = FileSystem.getAllFilesInDirectory(storagePath, MACRO_EXTENSION);
            
            for (var file : files) {
                try {
                    var summary = getMacroSummaryFromFile(file);
                    result.add(summary);
                } catch (Exception e) {
                    Logger.error(this, "Failed to read macro summary, error: " + e);
                    throw e;
                }
            }
            
            return result;
        }
        
        @Override
        public MacroSummary getMacroSummary(@NotNull String name) throws Exception {
            File file = FileSystem.getFile(storagePath, name);
            return getMacroSummaryFromFile(file);
        }
        
        private @NotNull MacroSummary getMacroSummaryFromFile(@NotNull File file) throws Exception {
            var json = FileSystem.readFromFile(file);
            
            var macroJSON = gson.fromJson(json, JsonObject.class);

            if (macroJSON == null) {
                throw new JsonParseException("Invalid json");
            }

            var macroSummaryJSON = macroJSON.get(Macro.KEY_SUMMARY);

            if (macroSummaryJSON == null) {
                throw new JsonParseException("Invalid json");
            }

            return gson.fromJson(macroSummaryJSON, MacroSummary.class);
        }
        
        @Override
        public Macro.Protocol getMacro(@NotNull String name) throws Exception {
            List<File> files = FileSystem.getAllFilesInDirectory(storagePath, MACRO_EXTENSION);
            
            for (var file : files) {
                try {
                    var json = FileSystem.readFromFile(file);
                    return gson.fromJson(json, Macro.Impl.class);
                } catch (Exception e) {
                    Logger.error(this, "Failed to read macro, error: " + e);
                    throw e;
                }
            }
            
            return null;
        }
        
        @Override
        public void saveMacro(@NotNull Macro.Protocol macro) throws Exception {
            Logger.message(this, "Saving new macro '" + macro.getSummary().name + "' to storage...");
            
            var fileName = macro.getSummary().name;
            var filePath = FileSystem.buildPath(storagePath, fileName);
            filePath = FileSystem.addFileExtension(filePath, MACRO_EXTENSION);
            
            var fileError = validateMacroPath(filePath);
            
            if (fileError != null) {
                throw fileError;
            }
            
            try {
                var json = gson.toJson(macroParser.parseToJSON(macro));
                
                File file = new File(filePath);

                if (file.exists()) {
                    file.delete();
                }
                
                FileSystem.writeToFile(file, json);
            } catch (Exception e) {
                Logger.error(this, "Failed to save macro, error: " + e);
                throw e;
            }
            
            Logger.message(this, "Saved to '" + filePath + "'");
        }
        
        @Override
        public void deleteMacro(@NotNull String name) {
            File file = FileSystem.getFile(storagePath, name);
            file.delete();
        }
        
        private @Nullable Exception validateMacroPath(@NotNull String path) {
            if (!FileSystem.validatePath(path)) {
                return Errors.invalidArgument("name");
            }
            
            var name = FileSystem.getLastComponentFromPath(path);
            
            if (name.isEmpty()) {
                return new Exception(TextValue.getText(TextValue.Error_NameIsEmpty));
            }
            
            return null;
        }
    }
}
