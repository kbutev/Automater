/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.storage;

import automater.di.DI;
import automater.model.KeyValue;
import automater.model.Keystroke;
import automater.utilities.FileSystem;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

/**
 * Holds the application general preference settings, such as UI preferences.
 *
 * @author Bytevi
 */
public interface PreferencesStorage {
    
    public static final String PREFERENCES_FILE_NAME = "preferences.json";
    
    class Values {
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
            result.recordHotkey = recordHotkey;
            result.playHotkey = playHotkey;
            result.recordHotkey = recordHotkey;
            result.stopNotification = stopNotification;
            return result;
        }
    }
    
    interface Protocol {
        @NotNull Values getValues();
        
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
            return values;
        }
        
        @Override
        public void load() {
            var file = FileSystem.getFile(PREFERENCES_FILE_NAME);
            
            if (file.exists()) {
                try {
                    var contents = FileSystem.readFromFile(file);
                    this.values = gson.fromJson(contents, Values.class);
                } catch (Exception e) {

                }
            } else {
                save();
            }
        }
        
        @Override
        public void save() {
            var file = FileSystem.getFile(PREFERENCES_FILE_NAME);
            var json = gson.toJson(values);
            
            try {
                FileSystem.writeToFile(file, json);
            } catch (Exception e) {
                
            }
        }
        
        @Override
        public void resetToDefaults() {
            values = new Values();
            save();
        }
    }
}
