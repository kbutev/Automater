/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.di.DI;
import automater.model.Keystroke;
import automater.storage.PreferencesStorage;
import automater.ui.view.SettingsPanel;
import org.jetbrains.annotations.NotNull;
import automater.utilities.Path;
import automater.utilities.Callback;
import automater.utilities.Logger;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface SettingsPresenter {
    
    interface Delegate {
        
        void chooseDirectory(@NotNull Path directory, @Nullable Callback.WithParameter<Path> success, @Nullable Callback.Blank failure);
        void chooseHotkey(@Nullable Callback.WithParameter<Keystroke> success, @Nullable Callback.Blank failure);
    }
    
    interface Protocol extends PresenterWithDelegate<Delegate> {
        
    }
    
    class Impl implements Protocol {
        
        private final PreferencesStorage.Protocol preferences = DI.get(PreferencesStorage.Protocol.class);
        
        private @NotNull SettingsPanel view;
        private @NotNull Delegate delegate;
        
        public Impl(@NotNull SettingsPanel view) {
            this.view = view;
            setup();
        }
        
        private void setup() {
            view.onPickMacrosDirectory = () -> {
                delegate.chooseDirectory(Path.getLocalDirectory(), (Path result) -> {
                    onChooseMacrosDirectory(result);
                }, null);
            };
            
            view.onStartRecordHotkeyClick = () -> {
                delegate.chooseHotkey((Keystroke result) -> {
                    var values = preferences.getValues();
                    values.startRecordHotkey = result;
                    onSavePreferences(values);
                    setupStartRecordHotkey(values);
                }, null);
            };
            
            view.onStopRecordHotkeyClick = () -> {
                delegate.chooseHotkey((Keystroke result) -> {
                    var values = preferences.getValues();
                    values.stopRecordHotkey = result;
                    onSavePreferences(values);
                    setupStopRecordHotkey(values);
                }, null);
            };
            
            view.onPlayMacroHotkeyClick = () -> {
                delegate.chooseHotkey((Keystroke result) -> {
                    var values = preferences.getValues();
                    values.playMacroHotkey = result;
                    onSavePreferences(values);
                    setupStartMacroHotkey(values);
                }, null);
            };
            
            view.onStopMacroHotkeyClick = () -> {
                delegate.chooseHotkey((Keystroke result) -> {
                    var values = preferences.getValues();
                    values.stopMacroHotkey = result;
                    onSavePreferences(values);
                    setupStopMacroHotkey(values);
                }, null);
            };
        }

        @Override
        public Delegate getDelegate() {
            return delegate;
        }
        
        @Override
        public void setDelegate(Delegate delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public void start() {
            assert delegate != null;
            reloadData();
        }
        
        @Override
        public void stop() {
            
        }
        
        @Override
        public void reloadData() {
            var values = preferences.getValues();
            setupStartRecordHotkey(values);
            setupStopRecordHotkey(values);
            setupStartMacroHotkey(values);
            setupStopMacroHotkey(values);
            view.setMacrosDirectory(values.macrosDirectory.toString());
        }
        
        private void onChooseMacrosDirectory(@NotNull Path path) {
            Logger.message(this, "Changed macros directory - " + path.toStringWithQuotes());
            
            var values = preferences.getValues();
            values.macrosDirectory = path;
            preferences.setValues(values);
            preferences.save();
            
            reloadData();
        }
        
        private void onSavePreferences(@NotNull PreferencesStorage.Values values) {
            preferences.setValues(values);
            preferences.save();
        }
        
        private @NotNull Path getCurrentMacroDirectory() {
            return preferences.getValues().macrosDirectory;
        }
        
        private void setupStartRecordHotkey(@NotNull PreferencesStorage.Values values) {
            view.setStartRecordHotkey(values.startRecordHotkey.toString());
        }
        
        private void setupStopRecordHotkey(@NotNull PreferencesStorage.Values values) {
            view.setStopRecordHotkey(values.stopRecordHotkey.toString());
        }
        
        private void setupStartMacroHotkey(@NotNull PreferencesStorage.Values values) {
            view.setPlayMacroHotkey(values.playMacroHotkey.toString());
        }
        
        private void setupStopMacroHotkey(@NotNull PreferencesStorage.Values values) {
            view.setStopMacroHotkey(values.stopMacroHotkey.toString());
        }
    }
}
