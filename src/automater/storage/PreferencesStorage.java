/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.storage;

import automater.model.InputKeystroke;
import automater.utilities.FileSystem;
import automater.utilities.Logger;
import automater.utilities.Path;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Holds the application general preference settings, such as UI preferences.
 *
 * @author Bytevi
 */
public interface PreferencesStorage {
    
    public static final String PREFERENCES_FILE_NAME = "preferences.json";
    public static final String DEFAULT_MACROS_DIRECTORY = "macros";
    
    public static final InputKeystroke.AWT defaultMediaHotkey = InputKeystroke.AWT.buildFromCode(NativeKeyEvent.VC_F4);
    
    interface Protocol {
        @NotNull PreferencesStorageValues getValues();
        
        void load();
        void save();
        void resetToDefaults();
    }
    
    class Impl implements Protocol {
        
        private final @NotNull PreferencesStorageValues values = new PreferencesStorageValues();
        
        public Impl() {
            
        }
        
        @Override
        public @NotNull PreferencesStorageValues getValues() {
            return values;
        }
        
        @Override
        public void load() {
            values.reset();
            values.loadFromFile(getFile());
            save();
        }
        
        @Override
        public void save() {
            try {
                var file = getFile();
                var json = values.toJSON();
                FileSystem.writeToFile(file, json);
                Logger.message(this, "Saved preferences");
            } catch (Exception e) {
                Logger.error(this, "Failed to save preferences, error: " + e);
            }
        }
        
        @Override
        public void resetToDefaults() {
            values.reset();
            save();
        }
        
        private @NotNull File getFile() {
            return Path.getLocalDirectory().withSubpath(PREFERENCES_FILE_NAME).getFile();
        }
    }
}
