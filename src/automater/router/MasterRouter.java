/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.ui.text.TextValue;
import automater.presenter.Presenter;
import automater.presenter.RecordMacroPresenter;
import automater.presenter.SettingsPresenter;
import automater.ui.view.PrimaryRootFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import automater.presenter.ShowMacrosPresenter;
import automater.ui.view.View;
import java.util.Map;

/**
 *
 * @author Kristiyan Butev
 */
public interface MasterRouter {
    
    interface Protocol {
        
        @NotNull javax.swing.JFrame getView();
        
        void start();
        
        void enableTabs(boolean value);
    }
    
    class Impl implements Protocol {
        private final @NotNull PrimaryRootFrame view;
        
        private @Nullable ShowMacrosPresenter.Protocol openMacrosPresenter;
        private @Nullable RecordMacroPresenter.Protocol recordMacroPresenter;
        private @Nullable SettingsPresenter.Protocol settingsPresenter;
        
        private @NotNull View currentlySelectedTab;
        
        private Map<View, Presenter> tabs;

        public Impl() {
            view = new PrimaryRootFrame();
            setup();
            currentlySelectedTab = view.getSelectedTab();
        }
        
        private void setup() {
            var openMacrosRouter = new ShowMacrosRouter.Impl(this);
            var openMacrosView = openMacrosRouter.getView();
            openMacrosPresenter = new ShowMacrosPresenter.Impl(openMacrosView);
            openMacrosPresenter.setDelegate(openMacrosRouter);
            view.addTab(TextValue.getText(TextValue.Root_TabShowMacros), 
                    openMacrosView,
                    TextValue.getText(TextValue.Root_TabShowMacrosTip));
            
            var recordMacroRouter = new RecordMacroRouter.Impl(this);
            var recordMacroView = recordMacroRouter.getView();
            recordMacroPresenter = new RecordMacroPresenter.Impl(recordMacroView);
            recordMacroPresenter.setDelegate(recordMacroRouter);
            view.addTab(TextValue.getText(TextValue.Root_TabRecordMacro),
                    recordMacroView, 
                    TextValue.getText(TextValue.Root_TabRecordMacroTip));
            
            var settingsRouter = new SettingsRouter.Impl(this);
            var settingsView = settingsRouter.getView();
            settingsPresenter = new SettingsPresenter.Impl(settingsView);
            settingsPresenter.setDelegate(settingsRouter);
            view.addTab(TextValue.getText(TextValue.Root_TabSettings),
                    settingsView,
                    TextValue.getText(TextValue.Root_TabSettingsTip));
            
            view.onSwitchTabCallback = (View selectedView) -> {
                onSwitchTab(selectedView);
            };
            
            tabs = Map.of(
                openMacrosView, openMacrosPresenter,
                recordMacroView, recordMacroPresenter,
                settingsView, settingsPresenter
            );
        }
        
        // # Protocol
        
        @Override
        public @NotNull javax.swing.JFrame getView() {
            return view;
        }
        
        @Override
        public void start() {
            // Start initial presenter
            openMacrosPresenter.start();
            
            // Present root view
            view.present();
        }
        
        @Override
        public void enableTabs(boolean value) {
            view.enableTabs(value);
        }
        
        // # Pritive
        
        private void onSwitchTab(@NotNull View selectedView) {
            var currentPresenter = tabs.get(currentlySelectedTab);
            currentPresenter.stop();
            
            currentlySelectedTab = selectedView;
            
            var nextPresenter = tabs.get(selectedView);
            nextPresenter.start();
        }
    }
}
