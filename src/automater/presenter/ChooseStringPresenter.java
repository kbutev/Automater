/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.model.MutableStorageValue;
import automater.ui.view.ChooseStringDialog;
import automater.utilities.Callback;
import automater.utilities.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface ChooseStringPresenter {
    
    interface Protocol extends Presenter {
        
    }
    
    class Impl implements ChooseStringPresenter.Protocol {
        
        private final @NotNull ChooseStringDialog view;
        
        private final @NotNull MutableStorageValue.SimpleStringProtocol storage;
        
        private final @NotNull String initialValue;

        private @Nullable Callback.Param<String> success;
        private @Nullable Callback.Blank failure;
        
        public Impl(@NotNull ChooseStringDialog view, @NotNull MutableStorageValue.SimpleStringProtocol storage) {
            this.view = view;
            this.storage = storage;
            this.initialValue = storage.getValueAsString();
            setup();
        }
        
        private void setup() {
            view.setFieldText(initialValue);
            
            view.onTextChanged = (var text) -> {
                setStringValue(text);
            };
            
            view.onSave = () -> {
                saveAndExit();
            };
        }
        
        public void setSuccessCallback(@Nullable Callback.Param<String> callback) {
            success = callback;
        }
        
        public void setFailureCallback(@Nullable Callback.Blank callback) {
            failure = callback;
        }
        
        @Override
        public void start() {
            view.setVisible(true);
        }
        
        @Override
        public void stop() {
            
        }
        
        @Override
        public void reloadData() {
            
        }
        
        // # Private
        
        private void setStringValue(@NotNull String value) {
            try {
                Logger.messageVerbose(this, "Text changed to '" + value + "'");
                storage.setValue(value);
                view.hideInfoText();
            } catch (Exception e) {
                var errorStr = e.getMessage() != null ? e.getMessage() : e.toString();
                Logger.messageVerbose(this, "Error: " + errorStr);
                view.setInfoText(errorStr);
            }
        }
        
        private void saveAndExit() {
            Logger.messageVerbose(this, "Save and exit with value '" + view.getFieldText() + "'");
            
            var text = view.getFieldText();
            view.dispose();
            
            performCallbacks(text);
            
            stop();
        }
        
        private void performCallbacks(@NotNull String text) {
            if (!text.equals(initialValue)) {
                if (success != null) {
                    success.perform(text);
                }
            } else {
                if (failure != null) {
                    failure.perform();
                }
            }
        }
    }
}
