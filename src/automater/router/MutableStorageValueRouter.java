/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.model.MutableStorageValue;
import automater.presenter.ChooseHotkeyPresenter;
import automater.ui.view.ChooseKeyDialog;
import automater.utilities.Callback;
import automater.utilities.Path;
import java.awt.Component;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface MutableStorageValueRouter {
    
    interface Protocol {
        
        void start(@NotNull Callback.Param<Boolean> completion);
    }
    
    class Impl implements Protocol {
        
        private final @NotNull Component parentView;
        private final @NotNull MutableStorageValue.Protocol value;
        
        public Impl(@NotNull Component parentView, @NotNull MutableStorageValue.Protocol value) {
            this.parentView = parentView;
            this.value = value;
        }
        
        @Override
        public void start(@NotNull Callback.Param<Boolean> completion) {
            if (value instanceof MutableStorageValue.SimpleString mutablePath) {
                openPickStringDialog(mutablePath, completion);
            } else if (value instanceof MutableStorageValue.SystemPath mutablePath) {
                openPickPathDialog(mutablePath, completion);
            } else if (value instanceof MutableStorageValue.Cycling cycling) {
                cycling.pickNextValue();
                completion.perform(true);
            } else if (value instanceof MutableStorageValue.Hotkey keystroke) {
                openPickHotkeyDialog(keystroke, completion);
            } else if (value instanceof MutableStorageValue.Keystroke keystroke) {
                openPickHotkeyDialog(keystroke, completion);
            } else {
                completion.perform(false);
            }
        }
        
        void openPickStringDialog(@NotNull MutableStorageValue.SimpleString string, @NotNull Callback.Param<Boolean> completion) {
            completion.perform(true);
        }
        
        void openPickPathDialog(@NotNull MutableStorageValue.SystemPath path, @NotNull Callback.Param<Boolean> completion) {
            var chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setCurrentDirectory(path.storage.get().getFile());
            
            var choice = chooser.showOpenDialog(parentView);

            if (choice != JFileChooser.APPROVE_OPTION) {
                completion.perform(false);
                return;
            }
            
            var file = chooser.getSelectedFile();
            
            var result = Path.buildAbsolute(file.getAbsolutePath());
            
            /// Update storage value
            path.setValue(result);
            
            completion.perform(true);
        }
        
        void openPickHotkeyDialog(@NotNull MutableStorageValue.Hotkey hotkey, @NotNull Callback.Param<Boolean> completion) {
            var frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, parentView);
            
            if (frame == null) {
                completion.perform(true);
                return;
            }
            
            var dialog = new ChooseKeyDialog(frame, true);
            var presenter = new ChooseHotkeyPresenter.Impl(dialog);
            presenter.setSuccessCallback((var result) -> {
                /// Update storage value
                hotkey.setValue(result);
                
                completion.perform(true);
            });
            presenter.setFailureCallback(() -> {
                completion.perform(true);
            });
            
            presenter.start();
        }
    }
}
