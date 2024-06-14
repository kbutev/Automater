/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.presenter.SettingsPresenter;
import automater.ui.view.RecordMacroPanel;
import automater.ui.view.SettingsPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface SettingsRouter {
    
    interface Protocol {
        
    }
    
    class Impl implements Protocol, SettingsPresenter.Delegate {
        private final @Nullable MasterRouter.Protocol masterRouter;
        private final @NotNull SettingsPanel view;
        
        public Impl(@NotNull MasterRouter.Protocol router) {
            masterRouter = router;
            view = new SettingsPanel();
        }
        
        public @NotNull SettingsPanel getView() {
            return view;
        }
        
        // # SettingsPresenter.Delegate
    }
}
