/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.model.macro.Macro;
import automater.presenter.PlayMacroPresenter;
import automater.ui.view.PlayMacroFrame;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface PlayMacroRouter {
    
    interface Protocol {
        
        void start();
        void exit(@NotNull Macro.Protocol macro);
    }
    
    class Impl implements Protocol, PlayMacroPresenter.Delegate {
        
        private final @NotNull PlayMacroFrame view;
        private final @NotNull MasterRouter.Protocol masterRouter;
        private @NotNull PlayMacroPresenter.Protocol presenter;

        public Impl(@NotNull MasterRouter.Protocol router, @NotNull Macro.Protocol macro) {
            view = new PlayMacroFrame();
            masterRouter = router;
            setup(macro);
        }
        
        private void setup(@NotNull Macro.Protocol macro) {
            presenter = new PlayMacroPresenter.Impl(view, macro);
            presenter.setDelegate(this);
        }
        
        @Override
        public void start() {
            presenter.start();
            view.present();
        }
        
        @Override
        public void exit(@NotNull Macro.Protocol macro) {
            presenter.stop();
        }
    }
}
