/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.model.action.MacroAction;
import automater.presenter.EditMacroActionPresenter;
import automater.presenter.EditMacroPresenter;
import automater.ui.view.EditMacroActionDialog;
import automater.utilities.AlertWindows;
import automater.utilities.Errors;
import automater.utilities.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface EditMacroActionRouter {
    
    interface Delegate {
        
        void onExitWithoutChanges();
        void onExitWithChanges(@NotNull MacroAction original, @NotNull MacroAction replacement);
    }
    
    interface Protocol {
        
        @Nullable Delegate getDelegate();
        void setDelegate(@NotNull Delegate delegate);
        
        void start();
        void exitWithoutChanges();
        void exitWithChanges(@NotNull MacroAction original, @NotNull MacroAction replacement);
        
        void showError(@NotNull String title, @NotNull String body);
    }
    
    class Impl implements Protocol, EditMacroActionPresenter.Delegate {
        
        private final @NotNull EditMacroActionDialog view;
        private final @NotNull EditMacroRouter.Protocol masterRouter;
        private @NotNull EditMacroActionPresenter.Protocol presenter;
        
        private @Nullable Delegate delegate;

        public Impl(@NotNull EditMacroRouter.Protocol router, @NotNull MacroAction action) throws Exception {
            view = new EditMacroActionDialog(router.getView(), true);
            masterRouter = router;
            setup(action);
        }
        
        private void setup(@NotNull MacroAction action) throws Exception {
            presenter = new EditMacroActionPresenter.Impl(view, action);
            presenter.setDelegate(this);
        }
        
        @Override
        public @Nullable Delegate getDelegate() {
            return delegate;
        }
        
        @Override
        public void setDelegate(@NotNull Delegate delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public void start() {
            if (delegate == null) {
                throw Errors.delegateNotSet();
            }
            
            presenter.start();
            view.present();
        }
        
        // # EditMacroActionPresenter.Delegate
        
        @Override
        public void exitWithoutChanges() {
            exit();
        }
        
        @Override
        public void exitWithChanges(@NotNull MacroAction original, @NotNull MacroAction replacement) {
            Logger.message(this, "exit");
            
            exit();
            
            delegate.onExitWithChanges(original, replacement);
        }
        
        @Override
        public void showError(@NotNull String title, @NotNull String body) {
            AlertWindows.showErrorMessage(view, title, body, "OK");
        }
        
        // # Private
        
        private void exit() {
            presenter.stop();
            view.dispose();
        }
    }
}
