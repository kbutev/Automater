/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.datasource.MacroActionDataSource;
import automater.di.DI;
import automater.model.action.MacroAction;
import automater.model.action.MacroActionDescription;
import automater.model.macro.Macro;
import automater.parser.DescriptionParser;
import automater.router.EditMacroActionRouter;
import automater.storage.MacroStorage;
import automater.ui.view.EditMacroFrame;
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
public interface EditMacroPresenter {
    
    interface Delegate {
        
        void exit();
        
        void editMacroAction(@NotNull MacroAction action);
        
        void showError(@NotNull String title, @NotNull String body);
    }

    interface Protocol extends PresenterWithDelegate<Delegate>, EditMacroActionRouter.Delegate {

    }
    
    class Impl implements Protocol {

        private final MacroStorage.Protocol storage = DI.get(MacroStorage.Protocol.class);
        private final DescriptionParser.Protocol descriptionParser = DI.get(DescriptionParser.Protocol.class);
        
        private final @NotNull EditMacroFrame view;
        private @Nullable Delegate delegate;
        
        private @NotNull Macro.Protocol macro;
        private final @NotNull List<MacroActionDescription> actionDescriptions;
        
        public Impl(@NotNull EditMacroFrame view, @NotNull Macro.Protocol macro) {
            this.view = view;
            this.macro = macro;
            actionDescriptions = new ArrayList<>();
            setup();
        }
        
        private void setup() {
            view.onInsertItemCallback = (var index) -> {
                insertMacroAction(index);
            };
            
            view.onEditItemCallback = (var index) -> {
                editMacroAction(index);
            };
            
            view.onDeleteItemCallback = (var index) -> {
                deleteMacroAction(index);
            };
            
            view.onSaveCallback = () -> {
                saveMacro();
            };
            
            setupMacroActionDescriptions();
        }
        
        private void setupMacroActionDescriptions() {
            actionDescriptions.clear();
            
            for (var action : macro.getActions()) {
                try {
                    var description = descriptionParser.parseMacroAction(action);
                    actionDescriptions.add(description);
                } catch (Exception e) {}
            }
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
            view.setMacroName(macro.getSummary().name);
            
            var dataSource = new MacroActionDataSource(actionDescriptions);
            view.setDataSource(dataSource);
        }
        
        // # EditMacroActionRouter.Delegate
        
        @Override
        public void onExitWithoutChanges() {
            
        }
        
        @Override
        public void onExitWithChanges(@NotNull MacroAction original, @NotNull MacroAction replacement) {
            macro = macro.withActionReplaced(original, replacement);
            
            view.enableSaveButton(true);
            
            setupMacroActionDescriptions();
            reloadData();
        }
        
        // # Private
        
        private void editMacroAction(int index) {
            var actions = macro.getActions();
            
            if (index >= actions.size()) {
                throw Errors.illegalStateError();
            }
            
            var action = actions.get(index);
            
            Logger.message(this, "Edit macro action " + action);
            
            delegate.editMacroAction(action);
        }
        
        private void insertMacroAction(int index) {
            
        }
        
        private void deleteMacroAction(int index) {
            
        }
        
        private void saveMacro() {
            Logger.message(this, "Saving macro... ");
            
            try {
                storage.saveMacro(macro);
                delegate.exit();
            } catch (Exception e) {
                var errorStr = e.getMessage() != null ? e.getMessage() : "unknown";
                delegate.showError("Save failed", "Error: " + errorStr);
            }
        }
    }
}
