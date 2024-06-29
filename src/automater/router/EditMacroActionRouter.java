/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.model.action.MacroAction;
import automater.presenter.EditMacroActionPresenter;
import automater.ui.view.EditMacroActionDialog;
import automater.utilities.Logger;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface EditMacroActionRouter {
    
    interface Protocol {
        
        void start();
        void exit();
    }
    
    class Impl implements Protocol, EditMacroActionPresenter.Delegate {
        
        private final @NotNull EditMacroActionDialog view;
        private final @NotNull EditMacroRouter.Protocol masterRouter;
        private @NotNull EditMacroActionPresenter.Protocol presenter;

        public Impl(@NotNull EditMacroRouter.Protocol router, @NotNull MacroAction macro) {
            view = new EditMacroActionDialog(router.getView(), true);
            masterRouter = router;
            setup(macro);
        }
        
        private void setup(@NotNull MacroAction macro) {
            presenter = new EditMacroActionPresenter.Impl(view, macro);
            presenter.setDelegate(this);
        }
        
        @Override
        public void start() {
            presenter.start();
            view.present();
        }
        
        // # EditMacroActionPresenter.Delegate
        
        @Override
        public void exit() {
            Logger.message(this, "exit");
            
            presenter.stop();
            view.dispose();
        }
    }
}
