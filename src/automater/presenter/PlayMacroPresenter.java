/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.datasource.MacroActionDataSource;
import automater.ui.text.TextValue;
import automater.di.DI;
import automater.execution.Command;
import automater.execution.MacroProcess;
import automater.execution.MacroProcessBuilder;
import automater.model.InputKeystroke;
import automater.model.KeyEventKind;
import automater.model.action.MacroActionDescription;
import automater.model.macro.Macro;
import automater.parser.DescriptionParser;
import automater.service.HotkeyMonitor;
import automater.storage.PreferencesStorage;
import automater.ui.view.PlayMacroFrame;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.utilities.DeviceNotifications;
import automater.utilities.Looper;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Presenter for the play macro screen.
 *
 * @author Bytevi
 */
public interface PlayMacroPresenter {

    interface Delegate {
        void onExit();
    }

    interface Protocol extends PresenterWithDelegate<Delegate> {

        boolean isPlaying();
        
        void startMacroExecution(@Nullable Object sender);
        void stopMacroExecution(@Nullable Object sender);
        void pauseMacroExecution(@Nullable Object sender);
        void resumeMacroExecution(@Nullable Object sender);
        void navigateBack();
    }

    class Impl implements Protocol, Looper.Subscriber, HotkeyMonitor.Listener, MacroProcess.Listener {

        private final Looper.Main looper = DI.get(Looper.Main.class);
        private final PreferencesStorage.Protocol preferences = DI.get(PreferencesStorage.Protocol.class);
        private final DescriptionParser.Protocol descriptionParser = DI.get(DescriptionParser.Protocol.class);

        private final @NotNull PlayMacroFrame view;
        private @Nullable Delegate delegate;

        private final HotkeyMonitor.Protocol actionHotkeyMonitor;

        private final @NotNull Macro.Protocol macro;
        private final @NotNull List<MacroActionDescription> actionDescriptions;
        
        private @Nullable MacroProcess.Protocol runningProcess;

        public Impl(@NotNull PlayMacroFrame view, @NotNull Macro.Protocol macro) {
            this.view = view;
            this.macro = macro;
            actionDescriptions = new ArrayList<>();

            actionHotkeyMonitor = HotkeyMonitor.build(PreferencesStorage.defaultMediaHotkey);
            
            setup();
        }
        
        private void setup() {
            var self = this;
            
            view.onPlayButtonCallback = () -> {
                startMacroExecution(self);
            };
            
            view.onStopButtonCallback = () -> {
                stopMacroExecution(self);
            };
            
            view.onResumeButtonCallback = () -> {
                resumeMacroExecution(self);
            };
            
            view.onPauseButtonCallback = () -> {
                pauseMacroExecution(self);
            };
            
            view.onWindowClosedCallback = () -> {
                delegate.onExit();
            };
            
            setupMacroActionDescriptions();
            setupText();
        }
        
        private void setupMacroActionDescriptions() {
            for (var action : macro.getActions()) {
                try {
                    var description = descriptionParser.parseMacroAction(action);
                    actionDescriptions.add(description);
                } catch (Exception e) {}
            }
        }
        
        private void setupText() {
            view.setHotkeyText(preferences.getValues().playMacroHotkey.toString(),
                    preferences.getValues().stopMacroHotkey.toString());
        }

        // # Protocol
        
        @Override
        public void start() {
            if (delegate == null) {
                throw Errors.delegateNotSet();
            }
            
            Logger.message(this, "Start");

            setupText();
            
            actionHotkeyMonitor.setHotkeys(getActionHotkeys());
            actionHotkeyMonitor.setListener(this);
            
            try {
                actionHotkeyMonitor.start();
            } catch (Exception e) {}

            looper.subscribe(this);
            
            reloadData();
        }

        @Override
        public void stop() {
            Logger.message(this, "Stop.");
            
            try {
                actionHotkeyMonitor.stop();
            } catch (Exception e) {}
            
            looper.unsubscribe(this);
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
            view.setMacroName(macro.getSummary().name);
            
            var dataSource = new MacroActionDataSource(actionDescriptions);
            view.setDataSource(dataSource);
            
            if (runningProcess != null) {
                var currentTime = runningProcess.getCurrentTime();
                var selection = dataSource.getSelectIndexForTime(currentTime);
                view.setSelectedIndex(selection);
            }
        }
        
        @Override
        public void navigateBack() {
            Logger.message(this, "Navigate back.");

            try {
                actionHotkeyMonitor.stop();
            } catch (Exception e) {
            }
        }
        
        @Override
        public boolean isPlaying() {
            return runningProcess != null;
        }

        @Override
        public void startMacroExecution(@Nullable Object sender) {
            if (isPlaying()) {
                Logger.error(this, "Failed to play, macro is already running");
                return;
            }
            
            Logger.message(this, "Play.");
            
            var builder = new MacroProcessBuilder.Impl();
            builder.setRootType(true);
            builder.setupWithMacro(macro);
            
            try {
                var process = builder.build();
                process.addListener(this);
                process.start(null);
                runningProcess = process;
            } catch (Exception e) {
                Logger.error(this, "Failed to play macro, error: " + e);
                return;
            }
            
            view.playRecording();
        }

        @Override
        public void stopMacroExecution(@Nullable Object sender) {
            if (!isPlaying()) {
                Logger.error(this, "Failed to stop, macro is not running");
                return;
            }
            
            Logger.message(this, "Stop playing...");
            
            try {
                runningProcess.cancel();
            } catch (Exception e) {
                Logger.error(this, "Failed to stop, error: " + e);
            }
            
            view.stopRecording();
        }
        
        @Override
        public void pauseMacroExecution(@Nullable Object sender) {
            if (!isPlaying()) {
                Logger.error(this, "Failed to pause, macro is not running");
                return;
            }
            
            Logger.message(this, "Pause");
            
            try {
                runningProcess.pause();
            } catch (Exception e) {
                Logger.error(this, "Failed to pause, error: " + e);
            }
            
            view.pauseRecording();
        }
        
        @Override
        public void resumeMacroExecution(@Nullable Object sender) {
            if (!isPlaying()) {
                Logger.error(this, "Failed to resume, macro is not running");
                return;
            }
            
            Logger.message(this, "Resume");
            
            try {
                runningProcess.resume();
            } catch (Exception e) {
                Logger.error(this, "Failed to pause, error: " + e);
            }
            
            view.resumeRecording();
        }
        
        // # Looper.Subscriber
        
        @Override
        public void onLoop(double dt) {
            if (!isPlaying()) {
                return;
            }
            
            var currentTime = runningProcess.getCurrentTime();
            var progress = runningProcess.getProgressPercentage();
            view.setProgressBarValue(progress);
            
            var dataSource = view.getDataSource();
            
            if (dataSource != null) {
                var selection = dataSource.getSelectIndexForTime(currentTime);
                view.setSelectedIndex(selection);
            }
        }
        
        // # MacroProcess.Listener
        
        @Override
        public void onStart() {
            
        }
        
        @Override
        public void onNextCommand(@NotNull Command.Protocol command) {
            
        }
        
        @Override
        public void onEnd(boolean cancelled) {
            runningProcess = null;
            
            if (cancelled) {
                Logger.message(this, "Cancelled");
            } else {
                Logger.message(this, "End");
            }
            
            view.stopRecording();
        }
        
        // # HotkeyMonitor.Listener
        
        @Override
        public void onHotkeyEvent(@NotNull InputKeystroke.Protocol key, @NotNull KeyEventKind kind) {
            if (!kind.isReleaseOrTap()) {
                return;
            }
            
            var values = preferences.getValues();
            
            if (values.playMacroHotkey.get().equals(key) && !isPlaying()) {
                startMacroExecution(this);
            } else if (values.stopMacroHotkey.get().equals(key) && isPlaying()) {
                stopMacroExecution(this);
            } else if (values.pauseMacroHotkey.get().equals(key) && isPlaying()) {
                pauseMacroExecution(this);
            }
        }

        // # Private
        
        private @NotNull List<InputKeystroke.Protocol> getActionHotkeys() {
            var values = preferences.getValues();
            return Arrays.asList(values.playMacroHotkey.get(),
                    values.pauseMacroHotkey.get(),
                    values.stopMacroHotkey.get());
        }

        private void displayPlayingStartedNotification() {
            if (!preferences.getValues().startNotification.get()) {
                return;
            }
            
            var hotkey = actionHotkeyMonitor.getHotkey();
            
            if (hotkey == null) {
                return;
            }

            var macroName = macro.getSummary().name;
            var macroHotkey = hotkey.toString();

            var title = TextValue.getText(TextValue.Play_NotificationStartTitle);
            var message = TextValue.getText(TextValue.Play_NotificationStartMessage, macroName, macroHotkey);

            DeviceNotifications.getShared().displayGlobalNotification(title, message);
        }

        private void displayPlayingFinishedNotification() {
            if (!preferences.getValues().stopNotification.get()) {
                return;
            }

            var macroName = macro.getSummary().name;

            var title = TextValue.getText(TextValue.Play_NotificationFinishTitle);
            var message = TextValue.getText(TextValue.Play_NotificationFinishMessage, macroName);

            DeviceNotifications.getShared().displayGlobalNotification(title, message);
        }
    }
}
