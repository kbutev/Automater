/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.presenter.SettingsPresenter;
import automater.ui.view.SettingsPanel;
import automater.utilities.Callback;
import automater.utilities.Path;
import javax.swing.JFileChooser;
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
        
        @Override
        public void pickDirectory(Callback.WithParameter<Path> success, Callback.Blank failure) {
            var chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            var choice = chooser.showOpenDialog(view);

            if (choice != JFileChooser.APPROVE_OPTION) {
                failure.perform();
                return;
            }
            
            var file = chooser.getSelectedFile();
            var path = Path.build(file.getAbsolutePath());
            success.perform(path);
        }
    }
}
