/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.datasource.MutableEntryDataSource;
import automater.di.DI;
import automater.storage.PreferencesStorage;
import automater.ui.view.SettingsPanel;
import org.jetbrains.annotations.NotNull;
import automater.utilities.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kristiyan Butev
 */
public interface SettingsPresenter {
    
    interface Delegate {
        
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
                Logger.message(self, "Edited item '" + item.getName() + "'" + " = " + item.getValueAsString());
                saveCurrentData();
                refreshData();
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
            var dataSource = new MutableEntryDataSource(getPresenterList());
            view.setDataSource(dataSource);
        }
        
        private void saveCurrentData() {
            preferences.save();
        }
        
        private void refreshData() {
            view.refreshData();
        }
        
        private List<MutableEntryPresenter.Protocol> getPresenterList() {
            var v = preferences.getValues();
            var result = new ArrayList<MutableEntryPresenter.Protocol>();
            result.add(new MutableEntryPresenter.SystemPath("directory", v.macrosDirectory, null));
            result.add(new MutableEntryPresenter.Hotkey("start recording hotkey", v.startRecordHotkey));
            result.add(new MutableEntryPresenter.Hotkey("stop recording hotkey", v.stopRecordHotkey));
            result.add(new MutableEntryPresenter.Hotkey("play recording hotkey", v.playMacroHotkey));
            result.add(new MutableEntryPresenter.Hotkey("pause recording hotkey", v.pauseMacroHotkey));
            result.add(new MutableEntryPresenter.Hotkey("stop recording hotkey", v.stopMacroHotkey));
            result.add(new MutableEntryPresenter.Flag("start notification", v.startNotification));
            result.add(new MutableEntryPresenter.Flag("stop notification", v.stopNotification));
            return result;
        }
    }
}
