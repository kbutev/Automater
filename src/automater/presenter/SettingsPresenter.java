/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.ui.view.SettingsPanel;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface SettingsPresenter {
    
    interface Delegate {
        
    }
    
    interface Protocol extends PresenterWithDelegate<Delegate> {
        
    }
    
    class Impl implements Protocol {
        
        private @NotNull Delegate delegate;
        
        public Impl(@NotNull SettingsPanel view) {
            
        }

        @Override
        public Delegate getDelegate() {
            return delegate;
        }
        
        @Override
        public void setDelegate(Delegate delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public void start() {
            assert delegate != null;
        }
        
        @Override
        public void stop() {
            
        }
        
        @Override
        public void reloadData() {
            
        }
    }
}
