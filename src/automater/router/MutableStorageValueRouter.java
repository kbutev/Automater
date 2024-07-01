/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.model.MutableStorageValue;
import automater.presenter.ChooseDoubleStringPresenter;
import automater.presenter.ChooseHotkeyPresenter;
import automater.presenter.ChooseStringPresenter;
import automater.ui.view.ChooseDoubleStringDialog;
import automater.ui.view.ChooseKeyDialog;
import automater.ui.view.ChooseStringDialog;
import automater.utilities.AlertWindows;
import automater.utilities.Callback;
import automater.utilities.Logger;
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
        
        void showError(@NotNull Component sender, @NotNull String title, @NotNull String body);
    }
    
    class Impl implements Protocol, ChooseStringPresenter.Delegate, ChooseDoubleStringPresenter.Delegate {
        
        private final @NotNull Component parentView;
        private final @NotNull MutableStorageValue.Protocol value;
        
        public Impl(@NotNull Component parentView, @NotNull MutableStorageValue.Protocol value) {
            this.parentView = parentView;
            this.value = value;
        }
        
        @Override
        public void start(@NotNull Callback.Param<Boolean> completion) {
            if (value instanceof MutableStorageValue.Cycling cycling) {
                cycling.pickNextValue();
                completion.perform(true);
            } else if (value instanceof MutableStorageValue.SystemPathProtocol mutablePath) {
                openPickPathDialog(mutablePath, completion);
            } else if (value instanceof MutableStorageValue.HotkeyProtocol keystroke) {
                openPickHotkeyDialog(keystroke, completion);
            } else if (value instanceof MutableStorageValue.Keystroke keystroke) {
                openPickHotkeyDialog(keystroke, completion);
            } else if (value instanceof MutableStorageValue.SimpleNumberProtocol dnumber) {
                openPickNumberDialog(dnumber, completion);
            } else if (value instanceof MutableStorageValue.SimpleStringProtocol string) {
                openPickStringDialog(string, completion);
            } else if (value instanceof MutableStorageValue.PointXYProtocol point) {
                var x = point.xAsStringStorageValue();
                var y = point.yAsStringStorageValue();
                openPickDoubleStringDialog(x, y, completion);
            } else {
                completion.perform(false);
            }
        }
        
        @Override
        public void showError(@NotNull Component sender, @NotNull String title, @NotNull String body) {
            Logger.error(this, "Error: " + body);
            
            AlertWindows.showErrorMessage(sender, title, body, "OK");
        }
        
        void openPickStringDialog(@NotNull MutableStorageValue.SimpleStringProtocol string, @NotNull Callback.Param<Boolean> completion) {
            var frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, parentView);
            
            if (frame == null) {
                completion.perform(true);
                return;
            }
            
            var dialog = new ChooseStringDialog(frame, true);
            var presenter = new ChooseStringPresenter.Impl(dialog, string);
            presenter.setDelegate(this);
            presenter.setSuccessCallback((var result) -> {
                /// Update storage value
                try {
                    string.setValue(result);
                } catch (Exception e) {
                    
                }
                
                completion.perform(true);
            });
            presenter.setFailureCallback(() -> {
                completion.perform(true);
            });
            
            presenter.start();
        }
        
        void openPickNumberDialog(@NotNull MutableStorageValue.SimpleNumberProtocol number, @NotNull Callback.Param<Boolean> completion) {
            var frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, parentView);
            
            if (frame == null) {
                completion.perform(true);
                return;
            }
            
            var dialog = new ChooseStringDialog(frame, true);
            var presenter = new ChooseStringPresenter.Impl(dialog, number.asStringStorageValue());
            presenter.setDelegate(this);
            presenter.setSuccessCallback((var result) -> {
                /// Update storage value
                try { number.setValue(Double.valueOf(result)); } catch (Exception e) {}
                
                completion.perform(true);
            });
            presenter.setFailureCallback(() -> {
                completion.perform(true);
            });
            
            presenter.start();
        }
        
        void openPickPathDialog(@NotNull MutableStorageValue.SystemPathProtocol path, @NotNull Callback.Param<Boolean> completion) {
            var chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setCurrentDirectory(path.getValue().getFile());
            
            var choice = chooser.showOpenDialog(parentView);

            if (choice != JFileChooser.APPROVE_OPTION) {
                completion.perform(false);
                return;
            }
            
            var file = chooser.getSelectedFile();
            
            var result = Path.buildAbsolute(file.getAbsolutePath());
            
            /// Update storage value
            try {
                path.setValue(result);
            } catch (Exception e) {}
            
            completion.perform(true);
        }
        
        void openPickHotkeyDialog(@NotNull MutableStorageValue.HotkeyProtocol hotkey, @NotNull Callback.Param<Boolean> completion) {
            var frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, parentView);
            
            if (frame == null) {
                completion.perform(true);
                return;
            }
            
            var dialog = new ChooseKeyDialog(frame, true);
            var presenter = new ChooseHotkeyPresenter.Impl(dialog);
            presenter.setSuccessCallback((var result) -> {
                /// Update storage value
                try {
                    hotkey.setValue(result);
                } catch (Exception e) {}
                
                completion.perform(true);
            });
            presenter.setFailureCallback(() -> {
                completion.perform(true);
            });
            
            presenter.start();
        }
        
         void openPickDoubleStringDialog(@NotNull MutableStorageValue.SimpleStringProtocol string1,
                 @NotNull MutableStorageValue.SimpleStringProtocol string2,
                 @NotNull Callback.Param<Boolean> completion) {
            var frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, parentView);
            
            if (frame == null) {
                completion.perform(true);
                return;
            }
            
            var dialog = new ChooseDoubleStringDialog(frame, true);
            var presenter = new ChooseDoubleStringPresenter.Impl(dialog, string1, string2);
            presenter.setDelegate(this);
            presenter.setSuccessCallback((var result) -> {
                /// Update storage value
                try {
                    string1.setValue(result.value1);
                    string2.setValue(result.value2);
                } catch (Exception e) {
                    
                }
                
                completion.perform(true);
            });
            presenter.setFailureCallback(() -> {
                completion.perform(true);
            });
            
            presenter.start();
        }
    }
}
