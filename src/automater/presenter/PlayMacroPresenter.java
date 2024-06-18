/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.datasource.StandardDescriptionDataSource;
import automater.ui.text.TextValue;
import automater.di.DI;
import automater.execution.Command;
import automater.execution.MacroProcess;
import automater.execution.MacroProcessBuilder;
import automater.model.KeyEventKind;
import automater.model.KeyValue;
import automater.model.Keystroke;
import automater.model.macro.Macro;
import automater.parser.DescriptionParser;
import automater.service.HotkeyMonitor;
import automater.storage.PreferencesStorage;
import automater.ui.view.PlayMacroFrame;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.utilities.DeviceNotifications;
import java.util.List;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Presenter for the play macro screen.
 *
 * @author Bytevi
 */
public interface PlayMacroPresenter {

    interface Delegate {

    }

    interface Protocol extends PresenterWithDelegate<Delegate> {

        boolean isPlaying();
        
        void startMacroExecution(@Nullable Object sender);
        void stopMacroExecution(@Nullable Object sender);
        void pauseMacroExecution(@Nullable Object sender);
        void resumeMacroExecution(@Nullable Object sender);
        void navigateBack();
    }

    class Impl implements Protocol, HotkeyMonitor.Listener, MacroProcess.Listener {

        private final PreferencesStorage.Protocol preferences = DI.get(PreferencesStorage.Protocol.class);
        private final DescriptionParser.Protocol descriptionParser = DI.get(DescriptionParser.Protocol.class);

        private final @NotNull PlayMacroFrame view;
        private @Nullable Delegate delegate;

        private final HotkeyMonitor.Protocol actionHotkeyMonitor;

        private final @NotNull Macro.Protocol macro;
        private final @NotNull List<String> actionDescriptions;
        
        private @Nullable MacroProcess.Protocol runningProcess;

        public Impl(@NotNull PlayMacroFrame view, @NotNull Macro.Protocol macro) {
            this.view = view;
            this.macro = macro;
            actionDescriptions = new ArrayList<>();

            actionHotkeyMonitor = HotkeyMonitor.build(Keystroke.build(KeyValue.F4));
            
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
            
            for (var action : macro.getActions()) {
                try {
                    var description = descriptionParser.parseMacroAction(action);
                    actionDescriptions.add(description.toString());
                } catch (Exception e) {}
            }
        }

        // # BasePresenter
        @Override
        public void start() {
            if (delegate == null) {
                throw Errors.delegateNotSet();
            }

            actionHotkeyMonitor.setListener(this);
            
            try {
                actionHotkeyMonitor.start();
            } catch (Exception e) {}

            Logger.message(this, "Start.");

            reloadData();
        }

        @Override
        public void stop() {
            try {
                actionHotkeyMonitor.stop();
            } catch (Exception e) {}
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
            
            var dataSource = StandardDescriptionDataSource.createDataSource(actionDescriptions);
            view.setDataSource(dataSource);
            
            //view.setHotkeys(play, stop, pause, resume);
        }
        
        // # PlayMacroPresenter
        
        @Override
        public void navigateBack() {
            Logger.messageEvent(this, "Navigate back.");

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
            Logger.messageEvent(this, "Play.");
            
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
            Logger.messageEvent(this, "Stop playing...");
            
            if (!isPlaying()) {
                Logger.error(this, "Failed to stop, macro is not running");
                return;
            }
            
            try {
                runningProcess.cancel();
            } catch (Exception e) {
                Logger.error(this, "Failed to stop, error: " + e);
            }
            
            view.stopRecording();
        }
        
        @Override
        public void pauseMacroExecution(@Nullable Object sender) {
            Logger.messageEvent(this, "Pause");
            
            view.pauseRecording();
        }
        
        @Override
        public void resumeMacroExecution(@Nullable Object sender) {
            Logger.messageEvent(this, "Resume");
            
            view.resumeRecording();
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
            
        }
        
        // # HotkeyMonitor.Listener
        
        @Override
        public void onHotkeyEvent(@NotNull KeyEventKind kind) {
            if (!kind.isReleaseOrTap()) {
                return;
            }
            
            if (isPlaying()) {
                stopMacroExecution(this);
            } else {
                startMacroExecution(this);
            }
        }

        // # Private
        
        private void updatePlayStatus() {
            
        }

        private void displayPlayingStartedNotification() {
            if (!preferences.getValues().startNotification) {
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
            if (!preferences.getValues().stopNotification) {
                return;
            }

            var macroName = macro.getSummary().name;

            var title = TextValue.getText(TextValue.Play_NotificationFinishTitle);
            var message = TextValue.getText(TextValue.Play_NotificationFinishMessage, macroName);

            DeviceNotifications.getShared().displayGlobalNotification(title, message);
        }
    }
}
