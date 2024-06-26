/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.di.DI;
import automater.presenter.ChooseFileNamePresenter;
import automater.ui.text.TextValue;
import automater.presenter.RecordMacroPresenter;
import automater.storage.MacroStorage;
import automater.storage.PreferencesStorage;
import automater.ui.view.ChooseFileNameDialog;
import automater.ui.view.RecordMacroPanel;
import automater.utilities.AlertWindows;
import automater.utilities.Logger;
import automater.utilities.Callback;
import automater.validator.CommonValidators;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface RecordMacroRouter {
    
    interface Protocol {
        
        void chooseFileName(@NotNull String initialValue, @NotNull ChooseFileNamePresenter.Delegate delegate);
        
        void showError(@NotNull Component sender, @NotNull String title, @NotNull String body);
    }
    
    class Impl implements Protocol, RecordMacroPresenter.Delegate {
        
        private final PreferencesStorage.Protocol preferences = DI.get(PreferencesStorage.Protocol.class);
        
        private final @Nullable MasterRouter.Protocol masterRouter;
        private final @NotNull RecordMacroPanel view;

        public Impl(@NotNull MasterRouter.Protocol router) {
            masterRouter = router;
            view = new RecordMacroPanel();
        }
        
        public @NotNull RecordMacroPanel getView() {
            return view;
        }
        
        @Override
        public void chooseFileName(@NotNull String initialValue, @NotNull ChooseFileNamePresenter.Delegate delegate) {
            var frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, view);
            
            if (frame == null) {
                delegate.onExit();
                return;
            }
            
            var extension = MacroStorage.MACRO_EXTENSION;
            var validator = CommonValidators.newFileName(preferences.getValues().macrosDirectory.get());
            
            var dialog = new ChooseFileNameDialog(frame, true);
            var presenter = new ChooseFileNamePresenter.Impl(dialog, initialValue, extension, validator);
            presenter.setDelegate(delegate);
            presenter.start();
        }
        
        @Override
        public void showError(@NotNull Component sender, @NotNull String title, @NotNull String body) {
            AlertWindows.showErrorMessage(sender, title, body, "OK");
        }
        
        // - RecordMacroPresenter.Delegate
        
        @Override
        public void onError(Exception e) {
            Logger.error(this, "Error encountered: " + e.toString());

            // Show message alert
            var textTitle = TextValue.getText(TextValue.Dialog_FailedTitle);
            var textMessage = TextValue.getText(TextValue.Dialog_FailedMessage, e.getMessage());
            var ok = TextValue.getText(TextValue.Dialog_OK);

            AlertWindows.showErrorMessage(view, textTitle, textMessage, ok);
        }
        
        @Override
        public void onStartRecording(Object sender) {
            masterRouter.enableTabs(false);
        }
        
        @Override
        public void onEndRecording(Object sender) {
            masterRouter.enableTabs(true);
        }
        
        @Override
        public void onRecordingSave(boolean success) {
            if (!success) {
                return;
            }

            // Show message alert
            var textTitle = TextValue.getText(TextValue.Dialog_SavedRecordingTitle);
            var textMessage = TextValue.getText(TextValue.Dialog_SavedRecordingMessage);
            var ok = TextValue.getText(TextValue.Dialog_OK);

            AlertWindows.showMessage(view, textTitle, textMessage, ok, new Callback.Blank() {
                @Override
                public void perform() {
                    //_presenter.onRecordingSavedSuccessufllyClosed();
                }
            });
        }
    }
}
