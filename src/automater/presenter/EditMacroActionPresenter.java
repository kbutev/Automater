/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.builder.MacroActionBuilder;
import automater.datasource.MutableEntryDataSource;
import automater.model.action.MacroAction;
import automater.ui.view.EditMacroActionDialog;
import automater.utilities.Errors;
import automater.utilities.Logger;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface EditMacroActionPresenter {
    
    interface Delegate {
        
        void exit();
    }

    interface Protocol extends PresenterWithDelegate<Delegate> {

    }
    
    class Impl implements Protocol {

        private final @NotNull EditMacroActionDialog view;
        private final @NotNull MacroAction action;
        private @Nullable Delegate delegate;
        
        public Impl(@NotNull EditMacroActionDialog view, @NotNull MacroAction action) {
            this.view = view;
            this.action = action;
            setup();
        }
        
        private void setup() {
            view.onSaveCallback = () -> {
                delegate.exit();
            };
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
            
            Logger.message(this, "Start.");
            
            reloadData();
        }
        
        @Override
        public void stop() {
            Logger.message(this, "Stop.");
        }
        
        @Override
        public void reloadData() {
            var dataSource = new MutableEntryDataSource(buildDataSource());
            view.setDataSource(dataSource);
        }
        
        private void refreshData() {
            view.refreshData();
        }
        
        private List<MutableEntryPresenter.Protocol> buildDataSource() {
            var result = new ArrayList<MutableEntryPresenter.Protocol>();
            
            try {
                var builder = MacroActionBuilder.buildFromAction(action);
                var presenters = builder.buildEntryPresenters();
                result.addAll(presenters);
            } catch (Exception e) {
                Logger.error(this, "Failed to build action values, error: " + e);
                return result;
            }
            
            return result;
        }
    }
}
