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
    }
    
    class Impl implements Protocol, PlayMacroPresenter.Delegate {
        
        private final @NotNull PlayMacroFrame view;
        private final @NotNull MasterRouter.Protocol masterRouter;
        private @NotNull PlayMacroPresenter.Protocol playMacroPresenter;

        public Impl(@NotNull MasterRouter.Protocol router, @NotNull Macro.Protocol macro) {
            view = new PlayMacroFrame();
            masterRouter = router;
            setup(macro);
        }
        
        private void setup(@NotNull Macro.Protocol macro) {
            playMacroPresenter = new PlayMacroPresenter.Impl(view, macro);
            playMacroPresenter.setDelegate(this);
        }
        
        @Override
        public void start() {
            playMacroPresenter.start();
            view.present();
        }
        
        // # PlayMacroPresenter.Delegate
        
        @Override
        public void onExit() {
            playMacroPresenter.stop();
        }
    }
}
