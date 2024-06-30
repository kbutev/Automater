/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.model.action.MacroAction;
import automater.model.macro.Macro;
import automater.presenter.EditMacroPresenter;
import automater.ui.view.EditMacroFrame;
import automater.utilities.AlertWindows;
import automater.utilities.Logger;
import java.awt.Frame;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface EditMacroRouter {
    
    interface Protocol {
        
        @NotNull Frame getView();
        
        void start();
        void exit();
        
        void editMacroAction(@NotNull MacroAction action);
        
        void showError(@NotNull String title, @NotNull String body);
    }
    
    class Impl implements Protocol, EditMacroPresenter.Delegate {
        
        private final @NotNull EditMacroFrame view;
        private final @NotNull MasterRouter.Protocol masterRouter;
        private @NotNull EditMacroPresenter.Protocol presenter;

        public Impl(@NotNull MasterRouter.Protocol router, @NotNull Macro.Protocol macro) {
            view = new EditMacroFrame();
            masterRouter = router;
            setup(macro);
        }
        
        private void setup(@NotNull Macro.Protocol macro) {
            presenter = new EditMacroPresenter.Impl(view, macro);
            presenter.setDelegate(this);
        }
        
        @Override
        public @NotNull Frame getView() {
            return view;
        }
        
        @Override
        public void start() {
            presenter.start();
            view.present();
        }
        
        @Override
        public void exit() {
            presenter.stop();
            view.dispose();
        }
        
        @Override
        public void editMacroAction(@NotNull MacroAction action) {
            try {
                var router = new EditMacroActionRouter.Impl(this, action);
                router.setDelegate(presenter);
                router.start();
            } catch (Exception e) {
                Logger.error(this, "Failed to open macro action, error: " + e);
            }
        }
        
        // # EditMacroPresenter.Delegate
        
        @Override
        public void showError(@NotNull String title, @NotNull String body) {
            AlertWindows.showErrorMessage(view, title, body, "OK");
        }
    }
}
