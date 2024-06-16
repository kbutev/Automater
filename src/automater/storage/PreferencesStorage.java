/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.storage;

import automater.di.DI;
import automater.model.KeyValue;
import automater.model.Keystroke;
import automater.utilities.FileSystem;
import automater.utilities.Logger;
import automater.utilities.Path;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import org.jetbrains.annotations.NotNull;

/**
 * Holds the application general preference settings, such as UI preferences.
 *
 * @author Bytevi
 */
public interface PreferencesStorage {
    
    public static final String VERSION = "1.0";
    public static final String VERSION_KEY = "version";
    public static final String PREFERENCES_FILE_NAME = "preferences.json";
    
    class Values {
        @SerializedName(VERSION_KEY)
        public @NotNull String version = VERSION;
        
        @SerializedName("macrosDirectory")
        public @NotNull Path macrosDirectory = Path.getLocalDirectory();
        
        @SerializedName("recordHotkey")
        public @NotNull Keystroke recordHotkey = Keystroke.build(KeyValue.F4); // Record and stop
        
        @SerializedName("playHotkey")
        public @NotNull Keystroke playHotkey = Keystroke.build(KeyValue.F4); // Play and stop
        
        @SerializedName("startNotification")
        public boolean startNotification = false;
        
        @SerializedName("stopNotification")
        public boolean stopNotification = false;
        
        public @NotNull Values copy() {
            var result = new Values();
            result.version = version;
            result.macrosDirectory = macrosDirectory;
            result.recordHotkey = recordHotkey;
            result.playHotkey = playHotkey;
            result.recordHotkey = recordHotkey;
            result.stopNotification = stopNotification;
            return result;
        }
    }
    
    interface Protocol {
        @NotNull Values getValues();
        void setValues(@NotNull Values values);
        
        void load();
        void save();
        void resetToDefaults();
    }
    
    class Impl implements Protocol {
        
        private final Gson gson = DI.get(Gson.class);
        
        @NotNull Values values = new Values();
        
        public Impl() {
            
        }
        
        @Override
        public @NotNull Values getValues() {
            return values.copy();
        }
        
        @Override
        public void setValues(@NotNull Values values) {
            this.values = values.copy();
        }
        
        @Override
        public void load() {
            var file = getFile();
            
            if (file.exists()) {
                try {
                    var contents = FileSystem.readFromFile(file);
                    
                    var versionValidation = validateVersion(contents);
                    
                    if (versionValidation.valid) {
                        this.values = gson.fromJson(contents, Values.class);
                        
                        if (validateAndFixPaths(this.values)) {
                            return;
                        }
                    } else {
                        // Handle new version
                    }
                } catch (Exception e) {
                    Logger.error(this, "Failed to load preferences, error: " + e);
                }
            }
            
            save();
        }
        
        @Override
        public void save() {
            var file = getFile();
            var json = gson.toJson(values);
            
            try {
                FileSystem.writeToFile(file, json);
            } catch (Exception e) {
                Logger.error(this, "Failed to save preferences, error: " + e);
            }
        }
        
        @Override
        public void resetToDefaults() {
            values = new Values();
            save();
        }
        
        private @NotNull File getFile() {
            return Path.getLocalDirectory().withSubpath(PREFERENCES_FILE_NAME).getFile();
        }
        
        private @NotNull VersionValidation validateVersion(@NotNull String contents) {
            var jsonObject = gson.fromJson(contents, JsonObject.class);

            if (jsonObject == null) {
                return new VersionValidation();
            }
            
            var versionField = jsonObject.get(VERSION_KEY);
            
            if (versionField == null) {
                return new VersionValidation();
            }
            
            var string = versionField.getAsString();
            
            if (string == null) {
                return new VersionValidation();
            }
            
            return new VersionValidation(string.equals(VERSION), VERSION);
        }
        
        private boolean validateAndFixPaths(@NotNull Values values) {
            var result = true;
            
            var defaults = new Values();
            
            var macrosDirectory = values.macrosDirectory;
            
            if (!macrosDirectory.exists()) {
                Logger.warning(this, "Macros directory is invalid, resetting it to default value");
                
                values.macrosDirectory = defaults.macrosDirectory;
                result = false;
            }
            
            return result;
        }
    }
    
    class VersionValidation {
        
        final boolean valid;
        final @NotNull String version;
        
        VersionValidation() {
            this.valid = false;
            this.version = "";
        }
        
        VersionValidation(boolean valid, @NotNull String version) {
            this.valid = valid;
            this.version = version;
        }
    }
}
