/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.TextValue;
import automater.di.DI;
import automater.model.KeyEventKind;
import automater.model.KeyValue;
import automater.model.Keystroke;
import automater.service.HotkeyMonitor;
import automater.storage.MacroStorage;
import automater.storage.PreferencesStorage;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.utilities.DeviceNotifications;
import automater.work.BaseAction;
import automater.work.Executor;
import automater.work.ExecutorProcess;
import automater.work.model.Macro;
import java.util.List;
import automater.work.model.ExecutorProgress;
import automater.work.model.MacroParameters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Date;

/**
 * Presenter for the play macro screen.
 *
 * @author Bytevi
 */
public interface PlayMacroPresenter {

    interface Delegate {

        void onError(@NotNull Exception e);
        void onLoadedPreferencesFromStorage(@NotNull PreferencesStorage.Values values);
        void onLoadedMacroFromStorage(@NotNull String macroName, @NotNull String macroDescription, @NotNull List<Description> macroActions);
        void startPlaying();
        void stopPlaying(boolean wasCancelled);
        void updatePlayStatus(@NotNull automater.work.model.ExecutorProgress progress);
    }

    interface Protocol extends Presenter<Delegate> {

        void playMacro(@Nullable Object sender);
        void stopMacro(@Nullable Object sender);
        void navigateBack();
    }

    class Impl implements Protocol, Executor.Listener, HotkeyMonitor.Listener {

        private final MacroStorage.Protocol storage = DI.get(MacroStorage.Protocol.class);
        private final PreferencesStorage.Protocol preferences = DI.get(PreferencesStorage.Protocol.class);
        private final Executor.Protocol executor = DI.get(Executor.Protocol.class);

        private @Nullable Delegate delegate;

        private final HotkeyMonitor.Protocol actionHotkeyMonitor;

        private final @NotNull Macro macro;
        private final @NotNull List<Description> macroActionDescriptions;
        private @Nullable ExecutorProcess.Protocol ongoingExecution;

        public Impl(@NotNull Macro macro) {
            this.macro = macro;
            macroActionDescriptions = macro.actionDescriptions;

            actionHotkeyMonitor = HotkeyMonitor.build(Keystroke.build(KeyValue.F4));
        }

        // # BasePresenter
        @Override
        public void start() {
            if (delegate == null) {
                throw Errors.delegateNotSet();
            }

            actionHotkeyMonitor.setListener(this);

            Logger.message(this, "Start.");

            delegate.onLoadedMacroFromStorage(macro.name, macro.getDescription(), macroActionDescriptions);

            delegate.onLoadedPreferencesFromStorage(preferences.getValues());
        }

        @Override
        public void stop() {

        }

        @Override
        public @Nullable
        Delegate getDelegate() {
            return delegate;
        }

        @Override
        public void setDelegate(@NotNull Delegate delegate) {
            assert delegate != null;
            this.delegate = delegate;
        }

        @Override
        public void reloadData() {

        }

        // # BaseExecutorListener
        @Override
        public void onStart(int numberOfActions) {
            displayPlayingStartedNotification();
        }

        @Override
        public void onActionExecute(@NotNull BaseAction action) {
            updatePlayStatus();
        }

        @Override
        public void onActionUpdate(@NotNull BaseAction action) {
            updatePlayStatus();
        }

        @Override
        public void onActionFinish(@NotNull BaseAction action) {
            updatePlayStatus();
        }

        @Override
        public void onWait() {
            updatePlayStatus();
        }

        @Override
        public void onRepeat(int numberOfTimesPlayed, int numberOfTimesToPlay) {
            
        }

        @Override
        public void onCancel() {
            Logger.message(this, "Playing was cancelled!");

            delegate.stopPlaying(true);
        }

        @Override
        public void onFinish() {
            Logger.message(this, "Successfully finished playing!");

            displayPlayingFinishedNotification();

            ongoingExecution = null;

            delegate.stopPlaying(false);
        }

        // # HotkeyMonitor.Listener
        @Override
        public void onHotkeyEvent(@NotNull KeyEventKind kind) {

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
        public void playMacro(@Nullable Object sender) {
            Logger.messageEvent(this, "Play.");
            
            var macroParameters = new MacroParameters();

            try {
                ongoingExecution = executor.performMacro(macro, macroParameters, this);
            } catch (Exception e) {
                Logger.error(this, "Failed to start executor: " + e.toString());
                e.printStackTrace(System.out);
                delegate.onError(e);
                return;
            }

            macro.incrementNumberOfTimesPlayed();
            macro.setLastTimePlayedDate(new Date());

            try {
                //storage.getMacrosStorage().updateMacroInStorage(macro);
            } catch (Exception e) {
                Logger.error(this, "Failed to update macro in storage: " + e.toString());
            }

            delegate.startPlaying();
        }

        @Override
        public void stopMacro(@Nullable Object sender) {
            if (ongoingExecution == null) {
                Logger.messageEvent(this, "No need to stop, already idle.");
                return;
            }

            Logger.messageEvent(this, "Stop playing...");

            try {
                ongoingExecution.stop();
                Logger.messageEvent(this, "Stopped ongoing execution process.");
                ongoingExecution = null;
            } catch (Exception e) {
                Logger.error(this, "Failed to stop execution process: " + e.toString());
            }

            // Do not alert the presenter delegate here, as a BaseExecutorListener we should
            // be alerted by the execution that the process stopped
        }

        // # Private
        private void updatePlayStatus() {
            if (ongoingExecution == null) {
                return;
            }

            ExecutorProgress progress = ongoingExecution.getProgress();

            delegate.updatePlayStatus(progress);
        }

        private void displayPlayingStartedNotification() {
            if (!preferences.getValues().startNotification) {
                return;
            }

            String macroName = macro.name;
            String macroHotkey = actionHotkeyMonitor.getHotkey().toString();

            String title = TextValue.getText(TextValue.Play_NotificationStartTitle);
            String message = TextValue.getText(TextValue.Play_NotificationStartMessage, macroName, macroHotkey);

            DeviceNotifications.getShared().displayGlobalNotification(title, message);
        }

        private void displayPlayingFinishedNotification() {
            if (!preferences.getValues().stopNotification) {
                return;
            }

            String macroName = macro.name;

            String title = TextValue.getText(TextValue.Play_NotificationFinishTitle);
            String message = TextValue.getText(TextValue.Play_NotificationFinishMessage, macroName);

            DeviceNotifications.getShared().displayGlobalNotification(title, message);
        }
    }
}
