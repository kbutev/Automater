/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.mvp.BasePresenter.PlayMacroPresenter;
import automater.TextValue;
import automater.di.DI;
import automater.mvp.BasePresenterDelegate.PlayMacroPresenterDelegate;
import automater.recorder.Recorder;
import automater.settings.Hotkey;
import automater.storage.GeneralStorage;
import automater.storage.PreferencesStorageValues;
import automater.ui.viewcontroller.RootViewController;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Date;

/**
 * Presenter for the play macro screen.
 *
 * @author Bytevi
 */
public class PlayMacroPresenterStandard implements PlayMacroPresenter, Executor.Listener, Recorder.HotkeyListener {
    
    private final GeneralStorage.Protocol storage = DI.get(GeneralStorage.Protocol.class);
    private final Recorder.Protocol recorder = DI.get(Recorder.Protocol.class);
    private final Executor.Protocol _executor = DI.get(Executor.Protocol.class);
    
    @NotNull private final RootViewController _rootViewController;
    @Nullable private PlayMacroPresenterDelegate _delegate;
    
    private boolean _started = false;
    
    @NotNull private final Macro _macro;
    @NotNull private final List<Description> _macroActionDescriptions;
    @Nullable private ExecutorProcess.Protocol _ongoingExecution;
    
    @Nullable private Hotkey _playOrStopHotkey;
    
    @NotNull private PreferencesStorageValues _options = new PreferencesStorageValues();
    
    public PlayMacroPresenterStandard(@NotNull RootViewController rootViewController, @NotNull Macro macro)
    {
        _rootViewController = rootViewController;
        
        _macro = macro;
        _macroActionDescriptions = macro.actionDescriptions;
        
        _playOrStopHotkey = _options.playOrStopHotkey;
    }
    
    // # BasePresenter
    
    @Override
    public void start()
    {
        if (_delegate == null)
        {
            Errors.throwInternalLogicError("PlayMacroPresenter delegate is not set before starting");
        }
        
        recorder.registerHotkeyListener(this);
        
        if (_started)
        {
            return;
        }
        
        _started = true;
        
        Logger.message(this, "Start.");
        
        _delegate.onLoadedMacroFromStorage(_macro.name, _macro.getDescription(), _macroActionDescriptions);
        
        _options = storage.getPreferencesStorage().getValues();
        _playOrStopHotkey = _options.playOrStopHotkey;
        _delegate.onLoadedPreferencesFromStorage(_options);
    }
    
    @Override
    public void setDelegate(@NotNull PlayMacroPresenterDelegate delegate)
    {
        if (_delegate != null)
        {
            Errors.throwInternalLogicError("PlayMacroPresenter delegate is already set");
        }
        
        _delegate = delegate;
    }
    
    @Override
    public void requestDataUpdate()
    {
        
    }
    
    // # BaseExecutorListener
    
    @Override
    public void onStart(int numberOfActions)
    {
        displayPlayingStartedNotification();
    }
    
    @Override
    public void onActionExecute(@NotNull BaseAction action)
    {
        updatePlayStatus();
    }
    
    @Override
    public void onActionUpdate(@NotNull BaseAction action)
    {
        updatePlayStatus();
    }
    
    @Override
    public void onActionFinish(@NotNull BaseAction action)
    {
        updatePlayStatus();
    }
    
    @Override
    public void onWait()
    {
        updatePlayStatus();
    }
    
    @Override
    public void onRepeat(int numberOfTimesPlayed, int numberOfTimesToPlay)
    {
        displayPlayingRepeatNotification(numberOfTimesPlayed, numberOfTimesToPlay);
    }
    
    @Override
    public void onCancel()
    {
        Logger.message(this, "Playing was cancelled!");
        
        _delegate.stopPlaying(true);
    }
    
    @Override
    public void onFinish()
    {
        Logger.message(this, "Successfully finished playing!");
        
        displayPlayingFinishedNotification();
        
        _ongoingExecution = null;
        
        _delegate.stopPlaying(false);
    }
    
    // # RecorderHotkeyListener
    
    @Override
    public boolean isListeningForAnyHotkey()
    {
        return false;
    }
    
    @Override
    public @Nullable Hotkey getHotkey()
    {
        return _playOrStopHotkey;
    }
    
    @Override
    public void onHotkeyPressed(@NotNull Hotkey hotkey)
    {
        Logger.message(this, "Play hotkey tapped!");
        
        if (_ongoingExecution == null)
        {
            play();
        }
        else
        {
            stop();
        }
    }
    
    // # PlayMacroPresenter
    
    @Override
    public void navigateBack()
    {
        Logger.messageEvent(this, "Navigate back.");
        
        recorder.unregisterHotkeyListener(this);
        
        _rootViewController.navigateToOpenScreen();
    }
    
    @Override
    public void play()
    {
        Logger.messageEvent(this, "Play.");
        
        try {
            _ongoingExecution = _executor.performMacro(_macro, _options.macroParameters, this);
        } catch (Exception e) {
            Logger.error(this, "Failed to start executor: " + e.toString());
            e.printStackTrace(System.out);
            _delegate.onErrorEncountered(e);
            return;
        }
        
        _macro.incrementNumberOfTimesPlayed();
        _macro.setLastTimePlayedDate(new Date());
        
        try {
            storage.getMacrosStorage().updateMacroInStorage(_macro);
        } catch (Exception e) {
            Logger.error(this, "Failed to update macro in storage: " + e.toString());
        }
        
        _delegate.startPlaying();
    }
    
    @Override
    public void stop()
    {
        if (_ongoingExecution == null)
        {
            Logger.messageEvent(this, "No need to stop, already idle.");
            return;
        }
        
        Logger.messageEvent(this, "Stop playing...");
        
        try {
            _ongoingExecution.stop();
            Logger.messageEvent(this, "Stopped ongoing execution process.");
            _ongoingExecution = null;
        } catch (Exception e) {
            Logger.error(this, "Failed to stop execution process: " + e.toString());
        }
        
        // Do not alert the presenter delegate here, as a BaseExecutorListener we should
        // be alerted by the execution that the process stopped
    }
    
    @Override
    public void setOptionValues(@NotNull PreferencesStorageValues values)
    {
        Logger.messageEvent(this, "Play parameters changed: " + values.macroParameters.toString());
        
        _options = values;
        
        // Save the option values to storage
        storage.getPreferencesStorage().saveValues(values);
    }
    
    // # Private
    
    private void updatePlayStatus()
    {
        if (_ongoingExecution == null)
        {
            return;
        }
        
        ExecutorProgress progress = _ongoingExecution.getProgress();
        
        _delegate.updatePlayStatus(progress);
    }
    
    private void displayPlayingStartedNotification()
    {
        if (!_options.displayStartNotification)
        {
            return;
        }
        
        String macroName = _macro.name;
        String macroHotkey = _playOrStopHotkey.key.toString();
        
        String title = TextValue.getText(TextValue.Play_NotificationStartTitle);
        String message = TextValue.getText(TextValue.Play_NotificationStartMessage, macroName, macroHotkey);
        
        DeviceNotifications.getShared().displayGlobalNotification(title, message);
    }
    
    private void displayPlayingFinishedNotification()
    {
        if (!_options.displayStopNotification)
        {
            return;
        }
        
        String macroName = _macro.name;
        
        String title = TextValue.getText(TextValue.Play_NotificationFinishTitle);
        String message = TextValue.getText(TextValue.Play_NotificationFinishMessage, macroName);
        
        DeviceNotifications.getShared().displayGlobalNotification(title, message);
    }
    
    private void displayPlayingRepeatNotification(int numberOfTimesPlayed, int numberOfTimesToPlay)
    {
        if (!_options.displayRepeatNotification)
        {
            return;
        }
        
        String macroName = _macro.name;
        String timesPlayed = String.valueOf(numberOfTimesPlayed) + "/" + String.valueOf(numberOfTimesToPlay);
        
        if (_options.macroParameters.repeatForever)
        {
            timesPlayed = TextValue.getText(TextValue.Play_NotificationRepeatForever);
        }
        
        String title = TextValue.getText(TextValue.Play_NotificationRepeatTitle);
        String message = TextValue.getText(TextValue.Play_NotificationRepeatMessage, macroName, timesPlayed);
        
        DeviceNotifications.getShared().displayGlobalNotification(title, message);
    }
}
