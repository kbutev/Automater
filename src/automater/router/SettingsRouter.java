/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.model.InputKeystroke;
import automater.presenter.ChooseHotkeyPresenter;
import automater.presenter.SettingsPresenter;
import automater.ui.view.ChooseKeyDialog;
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
        public void chooseDirectory(@NotNull Path directory,
                @Nullable Callback.WithParameter<Path> success,
                @Nullable Callback.Blank failure) {
            var chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setCurrentDirectory(directory.getFile());
            
            var choice = chooser.showOpenDialog(view);

            if (choice != JFileChooser.APPROVE_OPTION) {
                if (failure != null) {
                    failure.perform();
                }
                return;
            }
            
            var file = chooser.getSelectedFile();
            var path = Path.buildAbsolute(file.getAbsolutePath());
            
            if (success != null) {
                success.perform(path);
            }
        }
        
        @Override
        public void chooseHotkey(@Nullable Callback.WithParameter<InputKeystroke> success, @Nullable Callback.Blank failure) {
            var dialog = new ChooseKeyDialog(masterRouter.getView(), true);
            var presenter = new ChooseHotkeyPresenter.Impl(dialog);
            presenter.setSuccessCallback(success);
            presenter.setFailureallback(failure);
            presenter.start();
        }
    }
}
