/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.storage;

import automater.ui.text.TextValue;
import automater.di.DI;
import automater.model.macro.Macro;
import automater.model.macro.MacroFileSummary;
import automater.model.macro.MacroSummary;
import automater.parser.MacroParser;
import automater.utilities.Errors;
import automater.utilities.FileSystem;
import automater.utilities.Logger;
import automater.utilities.Path;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
        
        @NotNull Path getDirectory();
        
        @NotNull List<MacroFileSummary> getMacroSummaryList() throws Exception;
        @NotNull MacroFileSummary getMacroSummary(@NotNull String name) throws Exception;
        @NotNull Macro.Protocol getMacro(@NotNull Path path) throws Exception;
        @NotNull void saveMacro(@NotNull Macro.Protocol macro) throws Exception;
        void deleteMacro(@NotNull MacroFileSummary summary);
    }
    
    class Impl implements Protocol {
        
        private final Gson gson = DI.get(Gson.class);
        
        private final PreferencesStorage.Protocol preferences = DI.get(PreferencesStorage.Protocol.class);
        private final MacroParser.Protocol macroParser = DI.get(MacroParser.Protocol.class);
        
        public Impl() {
            
        }

        @Override
        public @NotNull Path getDirectory() {
            return preferences.getValues().macrosDirectory;
        }
        
        @Override
        public @NotNull List<MacroFileSummary> getMacroSummaryList() throws Exception {
            var result = new ArrayList<MacroFileSummary>();
            var files = FileSystem.getAllFilesInDirectory(getDirectory(), MACRO_EXTENSION);
            
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
        public @Nullable MacroFileSummary getMacroSummary(@NotNull String name) throws Exception {
            var summaries = getMacroSummaryList();
            
            for (var summary : summaries) {
                if (summary.summary.name.equals(name)) {
                    return summary;
                }
            }
            
            return null;
        }
        
        private @NotNull MacroFileSummary getMacroSummaryFromFile(@NotNull File file) throws Exception {
            var json = FileSystem.readFromFile(file);
            
            var macroJSON = gson.fromJson(json, JsonObject.class);

            if (macroJSON == null) {
                throw new JsonParseException("Invalid json");
            }

            var macroSummaryJSON = macroJSON.get(Macro.KEY_SUMMARY);

            if (macroSummaryJSON == null) {
                throw new JsonParseException("Invalid json");
            }

            var summary = gson.fromJson(macroSummaryJSON, MacroSummary.class);
            return new MacroFileSummary(summary, Path.buildAbsolute(file.getAbsolutePath()));
        }
        
        @Override
        public @NotNull Macro.Protocol getMacro(@NotNull Path path) throws Exception {
            try {
                var json = FileSystem.readFromFile(path.getFile());
                return macroParser.parseFromJSON(gson.fromJson(json, JsonElement.class));
            } catch (Exception e) {
                Logger.error(this, "Failed to read macro, error: " + e);
                throw e;
            }
        }
        
        @Override
        public void saveMacro(@NotNull Macro.Protocol macro) throws Exception {
            Logger.message(this, "Saving new macro '" + macro.getSummary().name + "' to storage...");
            
            var filePath = getMacroPath(macro.getSummary().name);
            filePath = filePath.withFileExtension(MACRO_EXTENSION);
            
            try {
                var json = gson.toJson(macroParser.parseToJSON(macro));
                
                var file = filePath.getFile();

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
        public void deleteMacro(@NotNull MacroFileSummary summary) {
            var file = summary.path.getFile();
            
            if (!file.exists()) {
                throw Errors.fileOrDirectoryNotFound();
            }
            
            file.delete();
        }
        
        private @NotNull Path getMacroPath(@NotNull String name) {
            return getDirectory().withSubpath(name);
        }
    }
}
