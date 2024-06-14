/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.ui.view.ShowMacrosPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import automater.presenter.ShowMacrosPresenter;

/**
 *
 * @author Kristiyan Butev
 */
public interface ShowMacrosRouter {
    
    interface Protocol {
        
    }
    
    class Impl implements Protocol, ShowMacrosPresenter.Delegate {
        private final @Nullable MasterRouter.Protocol masterRouter;
        private final @NotNull ShowMacrosPanel view;

        public Impl(@NotNull MasterRouter.Protocol router) {
            masterRouter = router;
            view = new ShowMacrosPanel();
        }
        
        public @NotNull ShowMacrosPanel getView() {
            return view;
        }
    }
}
