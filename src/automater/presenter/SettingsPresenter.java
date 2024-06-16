/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.di.DI;
import automater.storage.PreferencesStorage;
import automater.ui.view.SettingsPanel;
import org.jetbrains.annotations.NotNull;
import automater.utilities.Callback;
import automater.utilities.Path;
import automater.utilities.Callback;
import automater.utilities.Logger;

/**
 *
 * @author Kristiyan Butev
 */
public interface SettingsPresenter {
    
    interface Delegate {
        
        void pickDirectory(Callback.WithParameter<Path> success, Callback.Blank failure);
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
                delegate.pickDirectory((Path result) -> {
                    onChooseMacrosDirectory(result);
                }, () -> {
                    
                });
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
            view.setScriptsDirectory(preferences.getValues().macrosDirectory.toString());
        }
        
        private void onChooseMacrosDirectory(@NotNull Path path) {
            Logger.message(this, "Changed macros directory - " + path.toStringWithQuotes());
            
            var values = preferences.getValues();
            values.macrosDirectory = path;
            preferences.setValues(values);
            preferences.save();
            
            reloadData();
        }
    }
}
