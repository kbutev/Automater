/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.storage;

import automater.di.DI;
import automater.model.InputKeystroke;
import static automater.storage.PreferencesStorage.DEFAULT_MACROS_DIRECTORY;
import static automater.storage.PreferencesStorage.defaultMediaHotkey;
import automater.utilities.FileSystem;
import automater.utilities.Logger;
import automater.utilities.Path;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class PreferencesStorageValues {
    
    public static final String VERSION = "1.0";
    public static final String VERSION_KEY = "version";
    
    private final Gson gson = DI.get(Gson.class);
    
    private @NotNull Model model;
    
    public final @NotNull StorageValue<Path> macrosDirectory =
            StorageValue.build(() -> { return getModel().macrosDirectory; }, (var value) -> { getModel().macrosDirectory = value; });
    
    public final @NotNull StorageValue<InputKeystroke.AWT> startRecordHotkey =
            StorageValue.build(() -> { return getModel().startRecordHotkey; }, (var value) -> { getModel().startRecordHotkey = value; });
    
    public final @NotNull StorageValue<InputKeystroke.AWT> stopRecordHotkey =
            StorageValue.build(() -> { return getModel().stopRecordHotkey; }, (var value) -> { getModel().stopRecordHotkey = value; });
    
    public final @NotNull StorageValue<InputKeystroke.AWT> playMacroHotkey =
            StorageValue.build(() -> { return getModel().playMacroHotkey; }, (var value) -> { getModel().playMacroHotkey = value; });
    
    public final @NotNull StorageValue<InputKeystroke.AWT> pauseMacroHotkey =
            StorageValue.build(() -> { return getModel().pauseMacroHotkey; }, (var value) -> { getModel().pauseMacroHotkey = value; });
    
    public final @NotNull StorageValue<InputKeystroke.AWT> stopMacroHotkey =
            StorageValue.build(() -> { return getModel().stopMacroHotkey; }, (var value) -> { getModel().stopMacroHotkey = value; });
    
    public final @NotNull StorageValue<Boolean> startNotification =
            StorageValue.build(() -> { return getModel().startNotification; }, (var value) -> { getModel().startNotification = value; });
    
    public final @NotNull StorageValue<Boolean> stopNotification =
            StorageValue.build(() -> { return getModel().stopNotification; }, (var value) -> { getModel().stopNotification = value; });
    
    public PreferencesStorageValues() {
        model = new Model();
    }
    
    public PreferencesStorageValues(@NotNull File file) {
        model = new Model();
        loadFromFileNow(file);
    }
    
    public void loadFromFile(@NotNull File file) {
        loadFromFileNow(file);
    }
    
    private void loadFromFileNow(@NotNull File file) {
        reset();
        
        if (file.exists()) {
            try {
                var contents = FileSystem.readFromFile(file);

                var versionValidation = validateVersion(contents);

                if (versionValidation.valid) {
                    model = gson.fromJson(contents, Model.class);

                    if (model == null) {
                        model = new Model();
                    }
                    
                    validateAndFixPaths(model);
                } else {
                    // Handle new version
                }
            } catch (Exception e) {
                Logger.error(this, "Failed to load preferences, error: " + e);
            }
        } else {
            validateAndFixPaths(model);
        }
    }
    
    public void reset() {
        model = new Model();
    }
    
    public @NotNull String toJSON() throws Exception {
        return gson.toJson(model);
    }
    
    private @NotNull Model getModel() {
        return model;
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

    private boolean validateAndFixPaths(@NotNull Model values) {
        var result = true;

        var defaults = new Model();

        var macrosDirectory = values.macrosDirectory;

        if (!macrosDirectory.exists()) {
            if (!macrosDirectory.startWith(Path.getLocalDirectory())) {
                Logger.warning(this, "Macros directory is invalid, resetting it to default value");

                values.macrosDirectory = defaults.macrosDirectory;
                result = false;
            }

            if (!macrosDirectory.exists()) {
                Logger.warning(this, "Macros directory doesn't exist, creating new directory now");

                FileSystem.createDirectory(macrosDirectory);
            }
        }

        return result;
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
    
    class Model {
        @SerializedName(VERSION_KEY)
        public @NotNull String version = VERSION;
        
        @SerializedName("macrosDirectory")
        public @NotNull Path macrosDirectory = Path.getLocalDirectory().withSubpath(DEFAULT_MACROS_DIRECTORY);
        
        @SerializedName("startRecordHotkey")
        public @NotNull InputKeystroke.AWT startRecordHotkey = defaultMediaHotkey;
        
        @SerializedName("stopRecordHotkey")
        public @NotNull InputKeystroke.AWT stopRecordHotkey = defaultMediaHotkey;
        
        @SerializedName("playMacroHotkey")
        public @NotNull InputKeystroke.AWT playMacroHotkey = defaultMediaHotkey;
        
        @SerializedName("pauseMacroHotkey")
        public @NotNull InputKeystroke.AWT pauseMacroHotkey = defaultMediaHotkey;
        
        @SerializedName("stopMacroHotkey")
        public @NotNull InputKeystroke.AWT stopMacroHotkey = defaultMediaHotkey;
        
        @SerializedName("startNotification")
        public boolean startNotification = false;
        
        @SerializedName("stopNotification")
        public boolean stopNotification = false;
        
        public @NotNull Model copy() {
            var result = new Model();
            result.version = version;
            result.macrosDirectory = macrosDirectory;
            
            result.startRecordHotkey = startRecordHotkey;
            result.stopRecordHotkey = stopRecordHotkey;
            result.playMacroHotkey = playMacroHotkey;
            result.pauseMacroHotkey = pauseMacroHotkey;
            result.stopMacroHotkey = stopMacroHotkey;
            
            result.startNotification = startNotification;
            result.stopNotification = stopNotification;
            
            return result;
        }
    }
}
