/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.builder.MacroActionBuilder;
import automater.datasource.MacroActionKindsDataSource;
import automater.datasource.MutableEntryDataSource;
import automater.model.action.MacroAction;
import automater.ui.view.EditMacroActionDialog;
import automater.utilities.Errors;
import automater.utilities.Logger;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import automater.model.MutableStorageValue;
import automater.router.MutableStorageValueRouter;
import java.util.Arrays;

/**
 *
 * @author Kristiyan Butev
 */
public interface EditMacroActionPresenter {
    
    interface Delegate {
        
        void exitWithoutChanges();
        void exitWithChanges(@NotNull MacroAction original, @NotNull MacroAction replacement);
        
        void showError(@NotNull String title, @NotNull String body);
    }

    interface Protocol extends PresenterWithDelegate<Delegate> {

    }
    
    class Impl implements Protocol {

        private final @NotNull EditMacroActionDialog view;
        private final @NotNull MacroAction originalAction;
        private @Nullable Delegate delegate;
        private final @NotNull MacroActionBuilder.Protocol builder;
        
        public Impl(@NotNull EditMacroActionDialog view, @NotNull MacroAction action) throws Exception {
            this.view = view;
            this.originalAction = action;
            builder = MacroActionBuilder.buildFromAction(action);
            setup();
        }
        
        private void setup() {
            view.onKindChange = (var index) -> {
                onKindChanged(index);
            };
            
            view.onEditItem = (var item) -> {
                editItem(item);
            };
            
            view.onSave = () -> {
                saveAndExit();
            };
            
            view.onWindowCloseClick = () -> {
                exit();
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
            var kindsData = buildKindsData();
            view.setKindsDataSource(new MacroActionKindsDataSource(kindsData));
            view.selectKind(getCurrentKind().value);
            view.setFieldsDataSource(new MutableEntryDataSource(buildFieldsData()));
            
            view.setActionName(getActionName());
        }
        
        // # Private
        
        private @NotNull String getActionName() {
            return builder.getActionName();
        }
        
        private @NotNull MacroActionBuilder.Kind getCurrentKind() {
            return builder.kind().get();
        }
        
        private void onKindChanged(int index) {
            if (index == -1) {
                return;
            }
            
            var selectedKind = buildKindsData().get(index);
            
            if (selectedKind == getCurrentKind()) {
                return;
            }
            
            Logger.message(this, "Kind changed to " + selectedKind.value);
            
            builder.kind().set(selectedKind);
            reloadData();
        }
        
        private void editItem(@NotNull MutableStorageValue.Protocol value) {
            var router = new MutableStorageValueRouter.Impl(view, value);
            router.start((var result) -> {
                if (result) {
                    onItemEdited(value);
                }
            });
        }
        
        private void onItemEdited(@NotNull MutableStorageValue.Protocol value) {
            Logger.message(this, "Edited item '" + value.getName() + "'" + " = " + value.getValueAsString());
            refreshData();
        }
        
        private void refreshData() {
            view.refreshData();
        }
        
        private void exit() {
            delegate.exitWithoutChanges();
        }
        
        private void saveAndExit() {
            try {
                var result = builder.build();
                delegate.exitWithChanges(originalAction, result);
            } catch (Exception e) {
                var errorStr = e.getMessage() != null ? e.getMessage() : "unknown";
                delegate.showError("Save failed", "Error: " + errorStr);
            }
        }
        
        private List<MacroActionBuilder.Kind> buildKindsData() {
            return Arrays.asList(MacroActionBuilder.Kind.values());
        }
        
        private List<MutableStorageValue.Protocol> buildFieldsData() {
            return builder.getMutableValues();
        }
    }
}
