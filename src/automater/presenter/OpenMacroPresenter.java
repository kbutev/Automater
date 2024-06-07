/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.ui.viewcontroller.RootViewController;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.utilities.Looper;
import automater.utilities.LooperSwing;
import automater.utilities.SimpleCallback;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import automater.di.DI;
import automater.model.macro.MacroSummary;
import automater.model.macro.MacroSummaryDescription;
import automater.parser.DescriptionParser;
import automater.storage.MacroStorage;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;

/**
 * Presenter for the open macro screen.
 *
 * @author Bytevi
 */
public interface OpenMacroPresenter {

    interface Delegate {
        
        void onError(@NotNull Exception e);
        
        void onLoadedMacrosFromStorage(@NotNull List<String> macros);
    }
    
    interface Protocol {
        
        @Nullable Delegate getDelegate();
        void setDelegate(@NotNull Delegate delegate);
        
        void start();
        void stop();
        void reloadData();
        
        void onSwitchToRecordScreen();
    
        void openMacroAt(int index);
        void editMacroAt(int index);
        void deleteMacroAt(int index);
    }
    
    class Impl implements Protocol {
        
        private final MacroStorage.Protocol storage = DI.get(MacroStorage.Protocol.class);
        private final DescriptionParser.Protocol descriptionParser = DI.get(DescriptionParser.Protocol.class);
        
        private @NotNull final RootViewController rootViewController;
        private @Nullable Delegate delegate;

        private @NotNull List<MacroSummary> macros = new ArrayList<>();
        private final @NotNull List<MacroSummaryDescription> macrosAsDescriptions = new ArrayList<>();

        public Impl(@NotNull RootViewController rootViewController) {
            this.rootViewController = rootViewController;
        }

        @Override
        public void start() {
            if (delegate == null) {
                throw Errors.internalLogicError();
            }

            Logger.message(this, "Start.");

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
            if (this.delegate != null) {
                throw Errors.internalLogicError();
            }

            this.delegate = delegate;
        }

        @Override
        public void reloadData() {
            Logger.message(this, "Data update requested.");

            updateMacroData();
        }

        // # OpenMacroPresenter
        @Override
        public void onSwitchToRecordScreen() {
            rootViewController.navigateToRecordScreen();
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

            try {
                //macrosStorage.deleteMacro(macro);
            } catch (Exception e) {

            }

            updateMacroData();
        }

        // # Private
        private void updateMacroData() {
            var presenter = this;

            Looper.getShared().performAsyncCallbackInBackground(new SimpleCallback() {
                @Override
                public void perform() {
                    try {
                        var macros = presenter.storage.getMacroSummaryList();
                        
                        LooperSwing.getShared().performCallback(new SimpleCallback() {
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

        private void setMacroData(@NotNull List<MacroSummary> data) {
            macros = data;

            macrosAsDescriptions.clear();
            
            for (var macro : macros) {
                try {
                    macrosAsDescriptions.add(descriptionParser.parseMacroSummary(macro));
                } catch (Exception e) {}
            }
            
            var descriptions = macrosAsDescriptions.stream().map(Object::toString).collect(Collectors.toList());
            delegate.onLoadedMacrosFromStorage(descriptions);
        }
    }
}
