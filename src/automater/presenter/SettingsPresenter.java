/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.datasource.MutableEntryDataSource;
import automater.di.DI;
import automater.storage.PreferencesStorage;
import automater.ui.view.SettingsPanel;
import automater.utilities.Callback;
import org.jetbrains.annotations.NotNull;
import automater.utilities.Logger;
import java.util.ArrayList;
import java.util.List;
import automater.model.MutableStorageValue;
import automater.router.MutableStorageValueRouter;

/**
 *
 * @author Kristiyan Butev
 */
public interface SettingsPresenter {
    
    interface Delegate {
        
        void showConfirmationDialog(@NotNull String title,
                @NotNull String body, 
                @NotNull Callback.Param<Boolean> completion);
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
            var self = this;
            
            view.onEditItem = (var item) -> {
                editItem(item);
            };
            
            view.onResetAll = () -> {
                delegate.showConfirmationDialog("Reset to defaults", "Are you sure?", (var result) -> {
                    if (result) {
                        preferences.resetToDefaults();
                        refreshData();
                    }
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
            var dataSource = new MutableEntryDataSource(buildDataSource());
            view.setDataSource(dataSource);
        }
        
        private void editItem(@NotNull MutableStorageValue.Protocol value) {
            var router = new MutableStorageValueRouter.Impl(view, value);
            router.start((var result) -> {
                if (result) {
                    onItemEdited(value);
                }
            });
        }
        
        private void onItemEdited(@NotNull MutableStorageValue.Protocol value) {
            Logger.message(this, "Edited item '" + value.getName() + "'" + " = " + value.getValueAsString());
            saveCurrentData();
            refreshData();
        }
        
        private void saveCurrentData() {
            preferences.save();
        }
        
        private void refreshData() {
            view.refreshData();
        }
        
        private List<MutableStorageValue.Protocol> buildDataSource() {
            var v = preferences.getValues();
            var result = new ArrayList<MutableStorageValue.Protocol>();
            result.add(new MutableStorageValue.SystemPath("directory", v.macrosDirectory, null));
            result.add(new MutableStorageValue.Hotkey("start recording hotkey", v.startRecordHotkey));
            result.add(new MutableStorageValue.Hotkey("stop recording hotkey", v.stopRecordHotkey));
            result.add(new MutableStorageValue.Hotkey("play recording hotkey", v.playMacroHotkey));
            result.add(new MutableStorageValue.Hotkey("pause recording hotkey", v.pauseMacroHotkey));
            result.add(new MutableStorageValue.Hotkey("stop recording hotkey", v.stopMacroHotkey));
            result.add(new MutableStorageValue.Flag("start notification", v.startNotification));
            result.add(new MutableStorageValue.Flag("stop notification", v.stopNotification));
            return result;
        }
    }
}
