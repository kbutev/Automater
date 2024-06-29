/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.model.InputKeystroke;
import automater.presenter.ChooseHotkeyPresenter;
import automater.ui.view.ChooseKeyDialog;
import automater.utilities.Callback;
import automater.utilities.Path;
import java.awt.Component;
import java.awt.Frame;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface MutableEntryRouter {
    
    interface Protocol {
        
        void openPickStringDialog(@NotNull Component sender,
                @NotNull String value,
                @NotNull Callback.Param<String> success,
                @NotNull Callback.Blank failure);
        
        void openPickPathDialog(@NotNull Component sender,
                @NotNull Path value,
                @NotNull Callback.Param<Path> success,
                @NotNull Callback.Blank failure);
        
        void openPickHotkeyDialog(@NotNull Component sender,
                @NotNull InputKeystroke.AWT value,
                @NotNull Callback.Param<InputKeystroke.AWT> success,
                @NotNull Callback.Blank failure);
    }
    
    class Impl implements Protocol {
        
        public Impl() {
            
        }
        
        @Override
        public void openPickStringDialog(@NotNull Component sender,
                @NotNull String value,
                @NotNull Callback.Param<String> success,
                @NotNull Callback.Blank failure) {
            
        }
        
        @Override
        public void openPickPathDialog(@NotNull Component sender,
                @NotNull Path value,
                @NotNull Callback.Param<Path> success,
                @NotNull Callback.Blank failure) {
            var chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setCurrentDirectory(value.getFile());
            
            var choice = chooser.showOpenDialog(sender);

            if (choice != JFileChooser.APPROVE_OPTION) {
                failure.perform();
                return;
            }
            
            var file = chooser.getSelectedFile();
            var path = Path.buildAbsolute(file.getAbsolutePath());
            
            success.perform(path);
        }
        
        @Override
        public void openPickHotkeyDialog(@NotNull Component sender,
                @NotNull InputKeystroke.AWT value,
                @NotNull Callback.Param<InputKeystroke.AWT> success,
                @NotNull Callback.Blank failure) {
            var frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, sender);
            
            if (frame == null) {
                failure.perform();
                return;
            }
            
            var dialog = new ChooseKeyDialog(frame, true);
            var presenter = new ChooseHotkeyPresenter.Impl(dialog);
            presenter.setSuccessCallback(success);
            presenter.setFailureallback(failure);
            presenter.start();
        }
    }
}
