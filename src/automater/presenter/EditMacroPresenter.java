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
        
        void openMacroAction(@NotNull MacroAction action);
    }

    interface Protocol extends PresenterWithDelegate<Delegate> {

        
    }
    
    class Impl implements Protocol {

        private final DescriptionParser.Protocol descriptionParser = DI.get(DescriptionParser.Protocol.class);
        
        private final @NotNull EditMacroFrame view;
        private @Nullable Delegate delegate;
        
        private final @NotNull Macro.Protocol macro;
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
        
        private void editMacroAction(int index) {
            var actions = macro.getActions();
            
            if (index >= actions.size()) {
                throw Errors.illegalStateError();
            }
            
            var action = actions.get(index);
            
            Logger.message(this, "Edit macro action " + action);
            
            delegate.openMacroAction(action);
        }
        
        private void insertMacroAction(int index) {
            
        }
        
        private void deleteMacroAction(int index) {
            
        }
        
        private void saveMacro() {
            
        }
    }
}
