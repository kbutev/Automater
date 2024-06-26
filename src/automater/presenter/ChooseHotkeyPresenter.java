/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.model.InputKeystroke;
import automater.service.HotkeyListener;
import automater.ui.view.ChooseKeyDialog;
import automater.utilities.Callback;
import automater.utilities.Errors;
import automater.utilities.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface ChooseHotkeyPresenter {
    
    interface Protocol extends Presenter {
        
    }
    
    class Impl implements Protocol, HotkeyListener.Delegate {

        @Nullable Callback.Param<InputKeystroke.AWT> success;
        @Nullable Callback.Blank failure;
        
        private final @NotNull ChooseKeyDialog view;
        
        private final HotkeyListener.Protocol monitor = new HotkeyListener.Impl();
        
        private @Nullable InputKeystroke.AWT result;
        
        public Impl(@NotNull ChooseKeyDialog view) {
            this.view = view;
            setup();
        }
        
        private void setup() {
            view.onWindowCloseClick = () -> {
                saveAndExit(view);
            };
        }
        
        public void setSuccessCallback(@Nullable Callback.Param<InputKeystroke.AWT> callback) {
            success = callback;
        }
        
        public void setFailureCallback(@Nullable Callback.Blank callback) {
            failure = callback;
        }
        
        @Override
        public void start() {
            result = null;
            
            monitor.setDelegate(this);
            monitor.start();
            
            view.setVisible(true);
        }
        
        @Override
        public void stop() {
            try {
                monitor.stop();
            } catch (Exception e) {
                
            }
        }
        
        @Override
        public void reloadData() {
            
        }
        
        // # HotkeyListener.Delegate
        
        @Override
        public boolean shouldStop() {
            return false;
        }
        
        @Override
        public void onKeyPressed(@NotNull InputKeystroke.Protocol key) {
            if (key instanceof InputKeystroke.AWT awtKeystroke) {
                Logger.message(this, "Chose hotkey " + key);
                result = awtKeystroke;
                saveAndExit(monitor);
            } else {
                throw Errors.illegalStateError();
            }
        }
        
        @Override
        public void onExit() {
            
        }
        
        // # Private
        
        private void saveAndExit(Object sender) {
            if (result != null) {
                Logger.messageVerbose(this, "Save and exit with value '" + result.toString() + "'");
            } else {
                Logger.messageVerbose(this, "Exit without value");
            }
            
            if (sender != view) {
                view.dispose();
            }
            
            performCallbacks();
            stop();
        }
        
        private void performCallbacks() {
            if (result != null) {
                if (success != null) {
                    success.perform(result);
                }
            } else {
                if (failure != null) {
                    failure.perform();
                }
            }
        }
    }
}
