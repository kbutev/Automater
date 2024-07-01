/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.ui.view.ChooseFileNameDialog;
import automater.utilities.Errors;
import automater.utilities.Path;
import automater.validator.ValueValidator;
import java.awt.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface ChooseFileNamePresenter {
    
    interface Delegate {
        
        void onExit();
        void onExit(@NotNull String fileName);
        
        void showError(@NotNull Component sender, @NotNull String title, @NotNull String body);
    }
    
    interface Protocol extends PresenterWithDelegate<Delegate> {
        
    }
    
    class Impl implements Protocol {
        
        private final @NotNull ChooseFileNameDialog view;
        private final @NotNull String extension;
        private final @NotNull ValueValidator.Protocol<String> validator;
        
        private @Nullable Delegate delegate;
        
        public Impl(@NotNull ChooseFileNameDialog view,
                @NotNull String initialValue,
                @NotNull String extension,
                @NotNull ValueValidator.Protocol<String> validator) {
            this.view = view;
            this.extension = extension;
            this.validator = validator;
            setup(initialValue);
        }
        
        // # Private
        
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
        
        private void setup(@NotNull String initialValue) {
            view.setFileName(initialValue);
            
            view.onSave = (var string) -> {
                onSave(string);
            };
            
            view.onWindowCloseClick = () -> {
                delegate.onExit();
            };
        }
        
        private void onSave(@NotNull String string) {
            var message = "unknown";
            
            if (!string.isEmpty()) {
                var path = new Path(string).withFileExtension(extension);
                var fileName = path.lastComponent();
                var validate = validator.validate(fileName);

                if (validate.isSuccess()) {
                    delegate.onExit(string); // Pass without the extension
                    exit();
                    return;
                }
                
                var errorMessage = validate.error.getMessage();
                message = errorMessage != null ? errorMessage : message;
            }
            
            delegate.showError(view, "Error", message);
        }
        
        private void exit() {
            view.dispose();
        }
    }
}
