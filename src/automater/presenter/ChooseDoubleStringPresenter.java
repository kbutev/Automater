/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.model.MutableStorageValue;
import automater.ui.view.ChooseDoubleStringDialog;
import automater.utilities.Callback;
import automater.utilities.Errors;
import automater.utilities.Logger;
import java.awt.Component;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface ChooseDoubleStringPresenter {
    
    interface Delegate {
        
        void showError(@NotNull Component sender, @NotNull String title, @NotNull String body);
    }
    
    interface Protocol extends PresenterWithDelegate<Delegate> {
        
    }
    
    class Impl implements Protocol {
        
        private final @NotNull ChooseDoubleStringDialog view;
        
        private final @NotNull MutableStorageValue.SimpleStringProtocol storage1;
        private final @NotNull MutableStorageValue.SimpleStringProtocol storage2;
        
        private final @NotNull Strings initialValues;
        private final @NotNull Strings names;

        private @Nullable Delegate delegate;
        
        private @Nullable Callback.Param<Strings> success;
        private @Nullable Callback.Blank failure;
        
        public Impl(@NotNull ChooseDoubleStringDialog view,
                @NotNull MutableStorageValue.SimpleStringProtocol storage1,
                @NotNull MutableStorageValue.SimpleStringProtocol storage2) {
            this.view = view;
            this.storage1 = storage1;
            this.storage2 = storage2;
            this.initialValues = new Strings(storage1.getValue(), storage2.getValue());
            this.names = new Strings(storage1.getName(), storage2.getName());
            setup();
        }
        
        private void setup() {
            view.setFieldText1Name(names.value1);
            view.setFieldText1(initialValues.value1);
            view.setFieldText2Name(names.value2);
            view.setFieldText2(initialValues.value2);
            
            view.onText1Changed = (var text) -> {
                setStringValue1(text);
            };
            
            view.onText2Changed = (var text) -> {
                setStringValue2(text);
            };
            
            view.onSave = () -> {
                saveAndExit();
            };
        }
        
        public void setSuccessCallback(@Nullable Callback.Param<Strings> callback) {
            success = callback;
        }
        
        public void setFailureCallback(@Nullable Callback.Blank callback) {
            failure = callback;
        }
        
        @Override
        public @Nullable Delegate getDelegate() {
            return delegate;
        }
        
        @Override
        public void setDelegate(@NotNull Delegate delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public void start() {
            if (delegate == null) {
                throw Errors.delegateNotSet();
            }
            
            view.setVisible(true);
        }
        
        @Override
        public void stop() {
            
        }
        
        @Override
        public void reloadData() {
            
        }
        
        // # Private
        
        private void setStringValue1(@NotNull String value) {
            if (setStringValue(storage1, value)) {
                // Hide error only when both values are valid
                if (storage2.validate(view.getFieldText2()) == null) {
                    view.hideError();
                }
            }
        }
        
        private void setStringValue2(@NotNull String value) {
            if (setStringValue(storage2, value)) {
                // Hide error only when both values are valid
                if (storage1.validate(view.getFieldText1()) == null) {
                    view.hideError();
                }
            }
        }
        
        private boolean setStringValue(@NotNull MutableStorageValue.SimpleStringProtocol storage, @NotNull String value) {
            try {
                Logger.messageVerbose(this, "Text changed to '" + value + "'");
                storage.setValue(value);
                return true;
            } catch (Exception e) {
                var errorStr = e.getMessage() != null ? e.getMessage() : e.toString();
                Logger.messageVerbose(this, "Error: " + errorStr);
                view.setErrorText(errorStr);
                return false;
            }
        }
        
        private void saveAndExit() {
            if (view.isVisible()) {
                delegate.showError(view, "Cannot save", "Enter valid values.");
                return;
            }
            
            Logger.messageVerbose(this, "Save and exit with values '" + view.getFieldText1() + "'" + " and '" + view.getFieldText2() + "'");
            
            var result = new Strings(view.getFieldText1(), view.getFieldText2());
            view.dispose();
            
            performCallbacks(result);
            
            stop();
        }
        
        private void performCallbacks(@NotNull Strings result) {
            if (!result.equals(initialValues)) {
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
    
    class Strings {
        public final @NotNull String value1;
        public final @NotNull String value2;
        
        Strings(@NotNull String value1, @NotNull String value2) {
            this.value1 = value1;
            this.value2 = value2;
        }
        
        @Override
        public boolean equals(Object other) {
            if (other instanceof Strings otherStrings) {
                return value1.equals(otherStrings.value1) && value2.equals(otherStrings.value2);
            }
            
            return false;
        }
        
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.value1);
            hash = 97 * hash + Objects.hashCode(this.value2);
            return hash;
        }
    }
}
