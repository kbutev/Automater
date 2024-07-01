/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.di.DI;
import automater.model.KeyEventKind;
import automater.model.macro.Macro;
import automater.model.action.MacroAction;
import automater.model.event.CapturedEvent;
import automater.model.event.CapturedHardwareEvent;
import automater.parser.DescriptionParser;
import automater.parser.MacroActionParser;
import automater.service.EventMonitor;
import automater.service.HotkeyMonitor;
import automater.storage.MacroStorage;
import automater.storage.PreferencesStorage;
import automater.ui.view.RecordMacroPanel;
import automater.datasource.StandardDescriptionDataSource;
import automater.model.InputKeystroke;
import automater.model.macro.MacroRecordSource;
import automater.model.macro.MacroRecordSourceKind;
import automater.model.macro.MacroSummary;
import automater.utilities.Logger;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Presenter for the record macro screen.
 *
 * @author Bytevi
 */
public interface RecordMacroPresenter {

    interface Delegate {
        
        void chooseFileName(@NotNull String initialValue, @NotNull ChooseFileNamePresenter.Delegate delegate);
        
        void onError(@NotNull Exception e);
        void onStartRecording(@Nullable Object sender);
        void onEndRecording(@Nullable Object sender);
        void onRecordingSave(boolean success);
        
        void showError(@NotNull Component sender, @NotNull String title, @NotNull String body);
    }

    interface Protocol extends PresenterWithDelegate<Delegate> {
        
        double getCurrentRecordTime();

        void beginRecording(@Nullable Object sender);
        void endRecording(@Nullable Object sender);
        void saveRecording(@Nullable Object sender);
    }

    class Impl implements Protocol, EventMonitor.Listener, HotkeyMonitor.Listener, ChooseFileNamePresenter.Delegate {

        private final MacroStorage.Protocol storage = DI.get(MacroStorage.Protocol.class);
        private final PreferencesStorage.Protocol preferences = DI.get(PreferencesStorage.Protocol.class);
        private final MacroActionParser.Protocol actionParser = DI.get(MacroActionParser.Protocol.class);
        private final DescriptionParser.Protocol descriptionParser = DI.get(DescriptionParser.Protocol.class);

        private final @NotNull RecordMacroPanel view;
        private @Nullable Delegate delegate;

        private final EventMonitor.Protocol recorder = new EventMonitor.Impl();
        private final HotkeyMonitor.Protocol actionHotkeyMonitor;

        private final @NotNull ArrayList<MacroAction> actions = new ArrayList<>();
        private final @NotNull ArrayList<String> descriptions = new ArrayList<>();
        
        private final @NotNull StopWatch timer = new StopWatch();

        public Impl(@NotNull RecordMacroPanel view) {
            this.view = view;
            actionHotkeyMonitor = HotkeyMonitor.build(PreferencesStorage.defaultMediaHotkey);
            setup();
        }
        
        private void setup() {
            view.onSwitchToPlayButton = () -> {
                stop();
            };

            view.onBeginRecordMacroButton = () -> {
                beginRecording(view);
            };

            view.onStopRecordMacroButton = () -> {
                endRecording(view);
            };

            view.onSaveMacroButton = () -> {
                saveRecording(view);
            };
            
            setupText();
        }
        
        private void setupText() {
            view.setHotkeyText(preferences.getValues().startRecordHotkey.toString(),
                    preferences.getValues().stopRecordHotkey.toString());
        }

        @Override
        public void start() {
            if (delegate == null) {
                assert false;
            }

            Logger.message(this, "Start");
            
            setupText();
            
            try {
                actionHotkeyMonitor.setHotkeys(getActionHotkeys());
                actionHotkeyMonitor.setListener(this);
                actionHotkeyMonitor.start();
            } catch (Exception e) {
                delegate.onError(e);
            }
            
            reloadData();
        }

        @Override
        public void stop() {
            Logger.message(this, "Stop");

            try {
                actionHotkeyMonitor.stop();
            } catch (Exception e) {}
            
            reloadData();
        }

        @Override
        public @Nullable Delegate getDelegate() {
            return delegate;
        }

        @Override
        public void setDelegate(@NotNull Delegate delegate) {
            assert delegate != null;
            this.delegate = delegate;
        }

        @Override
        public void reloadData() {
            var result = StandardDescriptionDataSource.createDataSource(descriptions);
            view.setListDataSource(result);
        }
        
        @Override
        public double getCurrentRecordTime() {
            return timer.getTime() / 1000.0; // Milliseconds to seconds
        }

        @Override
        public void beginRecording(@Nullable Object sender) {
            Logger.message(this, "Begin recording");
            
            actions.clear();
            descriptions.clear();
            
            try {
                recorder.setListener(this);
                recorder.start();
            } catch (Exception e) {
                if (delegate != null) {
                    delegate.onError(e);
                }
            }
            
            reloadData();
            
            if (delegate != null) {
                delegate.onStartRecording(sender);
            }
            
            if (sender != view) {
                view.beginRecording();
            }
            
            try {
                timer.reset();
                timer.start();
            } catch (Exception e) {}
        }

        @Override
        public void endRecording(@Nullable Object sender) {
            Logger.message(this, "End recording");
            
            try {
                timer.stop();
            } catch (Exception e) {}
            
            try {
                recorder.stop();
            } catch (Exception e) {
                if (delegate != null) {
                    delegate.onError(e);
                }
            }
            
            if (delegate != null) {
                delegate.onEndRecording(sender);
            }
            
            if (sender != view) {
                view.endRecording();
            }
        }

        @Override
        public void saveRecording(@Nullable Object sender) {
            delegate.chooseFileName("Macro", this);
        }

        // # EventMonitor.Listener
        
        @Override
        public boolean shouldCaptureEvent(@NotNull CapturedEvent event) {
            // Ignore hotkey
            if (event instanceof CapturedHardwareEvent.Click click) {
                if (click.keystroke.equalsIgnoreModifier(actionHotkeyMonitor.getHotkey())) {
                    return false;
                }
            }
            
            return true;
        }
        
        @Override
        public void onCaptureEvent(@NotNull CapturedEvent event) {
            var timestamp = getCurrentRecordTime();
            
            try {
                var action = actionParser.parseFromCapturedEvent(event, timestamp);
                actions.add(action);
                
                var description = descriptionParser.parseMacroAction(action);
                descriptions.add(description.toString());
            } catch (Exception e) {
                Logger.error(this, "Parsing error: " + e);
            }
            
            reloadData();
        }

        // # HotkeyMonitor.Listener
        
        @Override
        public void onHotkeyEvent(@NotNull InputKeystroke.Protocol key, @NotNull KeyEventKind kind) {
            if (!kind.isReleaseOrTap()) {
                return;
            }
            
            /// Start
            var values = preferences.getValues();
            
            if (values.startRecordHotkey.get().equals(key) && !recorder.isRecording()) {
                beginRecording(this);
            } else if (values.stopRecordHotkey.get().equals(key) && recorder.isRecording()) {
                endRecording(this);
            }
        }
        
        // # ChooseFileNamePresenter.Delegate
        
        @Override
        public void onExit() {
            
        }
        
        @Override
        public void onExit(@NotNull String fileName) {
            onSaveMacro(fileName);
        }
        
        @Override
        public void showError(@NotNull Component sender, @NotNull String title, @NotNull String body) {
            delegate.showError(sender, title, body);
        }
        
        // # Private
        
        private @NotNull List<InputKeystroke.Protocol> getActionHotkeys() {
            var values = preferences.getValues();
            return Arrays.asList(values.startRecordHotkey.get(), values.stopRecordHotkey.get());
        }
        
        private void onSaveMacro(@NotNull String name) {
            var result = false;
            
            try {
                var primaryScreen = recorder.getPrimaryScreenSize();
                var summary = new MacroSummary(name, "", 0, actions.size());
                var recordSource = new MacroRecordSource(MacroRecordSourceKind.JAVA_AWT_ROBOT, primaryScreen);
                var macro = Macro.build(summary, recordSource, actions);
                storage.saveMacro(macro);
                result = true;
                
                view.enableSaveButton(false);
            } catch (Exception e) {
                Logger.error(this, "Failed to save macro: " + e);
            }
            
            if (delegate != null) {
                delegate.onRecordingSave(result);
            }
        }
    }
}
