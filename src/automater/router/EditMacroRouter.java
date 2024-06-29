/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.model.action.MacroAction;
import automater.model.macro.Macro;
import automater.presenter.EditMacroPresenter;
import automater.ui.view.EditMacroFrame;
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
        void openMacroAction(@NotNull MacroAction action);
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
        public void openMacroAction(@NotNull MacroAction action) {
            var router = new EditMacroActionRouter.Impl(this, action);
            router.start();
        }
        
        // # EditMacroPresenter.Delegate
    }
}
