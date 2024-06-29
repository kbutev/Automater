/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.presenter.SettingsPresenter;
import automater.ui.view.SettingsPanel;
import automater.utilities.AlertWindows;
import automater.utilities.Callback;
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
        
        @Override
        public void showConfirmationDialog(@NotNull String title,
                    @NotNull String body, 
                    @NotNull Callback.Param<Boolean> completion) {
            AlertWindows.showConfirmationMessage(view, title, body, completion);
        }
    }
}
