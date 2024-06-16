/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.TextValue;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.utilities.Looper;
import automater.utilities.LooperSwing;
import automater.utilities.Callback.Blank;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import automater.di.DI;
import automater.model.macro.MacroFileSummary;
import automater.model.macro.MacroSummaryDescription;
import automater.parser.DescriptionParser;
import automater.storage.MacroStorage;
import automater.ui.view.ShowMacrosPanel;
import automater.ui.view.StandardDescriptionDataSource;
import automater.utilities.AlertWindows;
import automater.utilities.Callback;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;

/**
 * Presenter for the show macros screen.
 *
 * @author Bytevi
 */
public interface ShowMacrosPresenter {

    interface Delegate {
        
    }
    
    interface Protocol extends PresenterWithDelegate<Delegate> {
        
        void openMacroAt(int index);
        void editMacroAt(int index);
        void deleteMacroAt(int index);
    }
    
    class Impl implements Protocol {
        
        private final MacroStorage.Protocol storage = DI.get(MacroStorage.Protocol.class);
        private final DescriptionParser.Protocol descriptionParser = DI.get(DescriptionParser.Protocol.class);
        
        private final @NotNull ShowMacrosPanel view;
        private @Nullable Delegate delegate;
        private @Nullable StandardDescriptionDataSource dataSource;
        
        private @NotNull List<MacroFileSummary> macros = new ArrayList<>();
        private final @NotNull List<MacroSummaryDescription> macrosAsDescriptions = new ArrayList<>();

        public Impl(@NotNull ShowMacrosPanel view) {
            this.view = view;
            setup();
        }
        
        private void setup() {
            view.onSelectItem = (Integer index) -> {
                openMacroAt(index);
            };

            view.onDoubleClickItem = (Integer index) -> {
                editMacroAt(index);
            };

            view.onOpenItem = (Integer index) -> {
                editMacroAt(index);
            };

            view.onEditItem = (Integer index) -> {
                editMacroAt(index);
            };

            view.onDeleteItem = (Integer index) -> {
                deleteItem(index);
            };
        }

        @Override
        public void start() {
            if (delegate == null) {
                throw Errors.internalLogicError();
            }

            Logger.message(this, "Start.");

            view.onViewStart();
            updateMacroData();
        }
        
        @Override
        public void stop() {
            
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
        public void reloadData() {
            Logger.message(this, "Data update requested.");

            updateMacroData();
        }

        @Override
        public void openMacroAt(int index) {
            Logger.messageEvent(this, "Open macro at " + String.valueOf(index));

            if (index < 0 || index >= macros.size()) {
                Logger.error(this, "Cannot open macro at " + String.valueOf(index) + ", invalid index");
                return;
            }

            var macro = macros.get(index);

            //rootViewController.navigateToPlayScreen(macro);
        }

        @Override
        public void editMacroAt(int index) {
            Logger.messageEvent(this, "Edit macro at " + String.valueOf(index));

            if (index < 0 || index >= macros.size()) {
                Logger.error(this, "Cannot open macro at " + String.valueOf(index) + ", invalid index");
                return;
            }

            var macro = macros.get(index);

            //rootViewController.navigateToEditScreen(macro);
        }

        @Override
        public void deleteMacroAt(int index) {
            Logger.messageEvent(this, "Delete macro at " + String.valueOf(index));

            if (index < 0 || index >= macros.size()) {
                Logger.error(this, "Cannot delete macro at " + String.valueOf(index) + ", invalid index");
                return;
            }

            var macro = macros.get(index);
            
            Logger.message(this, "Delete macro '" + macro.filePath + "'");

            try {
                storage.deleteMacro(macro);
            } catch (Exception e) {
                Logger.error(this, "Failed to delete macro, error: " + e);
            }

            updateMacroData();
        }
        
        public void deleteItem(int index) {
            if (dataSource == null) {
                return;
            }

            var macroDescriptions = dataSource.data;

            if (index < 0 || index >= macroDescriptions.size()) {
                return;
            }

            var macro = macroDescriptions.get(index);

            var confirm = new Callback.Blank() {
                @Override
                public void perform() {
                    deleteMacroAt(index);
                }
            };

            var textTitle = TextValue.getText(TextValue.Dialog_ConfirmDeleteMacroTitle);
            var textMessage = TextValue.getText(TextValue.Dialog_ConfirmDeleteMacroMessage, "macro name");

            AlertWindows.showConfirmationMessage(view, textTitle, textMessage, confirm, null);
        }

        private void updateMacroData() {
            var presenter = this;

            Looper.getShared().performAsyncCallbackInBackground(new Callback.Blank() {
                @Override
                public void perform() {
                    try {
                        var macros = presenter.storage.getMacroSummaryList();
                        
                        LooperSwing.getShared().performCallback(new Callback.Blank() {
                            @Override
                            public void perform() {
                                Logger.message(this, "Performing data update: " + macros.size() + " macros");

                                presenter.setMacroData(macros);
                            }
                        });
                    } catch (Exception e) {
                        Logger.error(this, "Failed to load macros, error: " + e);
                    }
                }
            });
        }

        private void setMacroData(@NotNull List<MacroFileSummary> data) {
            macros = data;

            macrosAsDescriptions.clear();
            
            for (var macro : macros) {
                try {
                    macrosAsDescriptions.add(descriptionParser.parseMacroSummary(macro.summary));
                } catch (Exception e) {}
            }
            
            var descriptions = macrosAsDescriptions.stream().map(Object::toString).collect(Collectors.toList());
            
            this.dataSource = StandardDescriptionDataSource.createDataSource(descriptions);
            view.setListDataSource(dataSource);
        }
    }
}
