/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.TextValue;
import automater.presenter.RecordMacroPresenter;
import automater.ui.view.RecordMacroPanel;
import automater.utilities.AlertWindows;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface RecordMacroRouter {
    
    interface Protocol {
        
        void goBack();
    }
    
    class Impl implements Protocol, RecordMacroPresenter.Delegate {
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
        public void goBack() {
            assert masterRouter != null;
            
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

            AlertWindows.showMessage(view, textTitle, textMessage, ok, new SimpleCallback() {
                @Override
                public void perform() {
                    //_presenter.onRecordingSavedSuccessufllyClosed();
                }
            });
        }
    }
}
