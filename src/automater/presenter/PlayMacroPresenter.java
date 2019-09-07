/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.presenter;

import automater.TextValue;
import automater.recorder.Recorder;
import automater.recorder.RecorderHotkeyListener;
import automater.settings.Hotkey;
import automater.storage.GeneralStorage;
import automater.storage.MacroStorage;
import automater.storage.PreferencesStorageValues;
import automater.ui.viewcontroller.RootViewController;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.utilities.OSUIEffects;
import automater.work.BaseAction;
import automater.work.BaseExecutorProcess;
import automater.work.Executor;
import automater.work.model.Macro;
import java.util.List;
import automater.work.ExecutorListener;
import automater.work.model.ExecutorProgress;
import java.util.Date;

/**
 *
 * @author Bytevi
 */
public class PlayMacroPresenter implements BasePresenter, ExecutorListener, RecorderHotkeyListener {
    private final RootViewController _rootViewController;
    private BasePresenterDelegate _delegate;
    
    private final Executor _executor = Executor.getDefault();
    private final MacroStorage _macrosStorage = GeneralStorage.getDefault().getMacrosStorage();
    
    private boolean _started = false;
    
    private final Macro _macro;
    private final List<Description> _macroActionDescriptions;
    private BaseExecutorProcess _ongoingExecution;
    
    private final Recorder _recorder = Recorder.getDefault();
    private Hotkey _playOrStopHotkey;
    
    private PreferencesStorageValues _options = PreferencesStorageValues.defaultValues();
    
    public PlayMacroPresenter(RootViewController rootViewController, Macro macro)
    {
        _rootViewController = rootViewController;
        
        _macro = macro;
        _macroActionDescriptions = macro.actionDescriptions;
    }
    
    // # BasePresenter
    
    @Override
    public void start()
    {
        if (_delegate == null)
        {
            Errors.throwInternalLogicError("RecordPresenter delegate is not set before starting");
        }
        
        _recorder.registerHotkeyListener(this);
        
        if (_started)
        {
            return;
        }
        
        _started = true;
        
        Logger.message(this, "Start.");
        
        _delegate.onLoadedMacroFromStorage(_macro.name, _macro.getDescription(), _macroActionDescriptions);
        _delegate.onLoadedPreferencesFromStorage(GeneralStorage.getDefault().getPreferencesStorage().getValues());
    }
    
    @Override
    public void setDelegate(BasePresenterDelegate delegate)
    {
        if (_delegate != null)
        {
            Errors.throwInternalLogicError("RecordPresenter delegate is already set");
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
    public void onActionExecute(BaseAction action)
    {
        updatePlayStatus();
    }
    
    @Override
    public void onActionUpdate(BaseAction action)
    {
        updatePlayStatus();
    }
    
    @Override
    public void onActionFinish(BaseAction action)
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
        _delegate.finishPlaying();
    }
    
    @Override
    public void onFinish()
    {
        displayPlayingFinishedNotification();
        
        _ongoingExecution = null;
        
        _delegate.finishPlaying();
    }
    
    // # RecorderHotkeyListener
    
    @Override
    public Hotkey getHotkey()
    {
        return _playOrStopHotkey;
    }
    
    @Override
    public void onHotkeyPressed()
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
    
    // # Public
    
    public boolean isPlaying()
    {
        return _executor.isPerforming();
    }
    
    public void play()
    {
        Logger.messageEvent(this, "Play.");
        
        try {
            _ongoingExecution = _executor.performMacro(_macro, _options.macroParameters, this);
        } catch (Exception e) {
            Logger.error(this, "Failed to start executor: " + e.toString());
            _delegate.onErrorEncountered(e);
            return;
        }
        
        _macro.incrementNumberOfTimesPlayed();
        _macro.setLastTimePlayedDate(new Date());
        
        try {
            _macrosStorage.updateMacroInStorage(_macro);
        } catch (Exception e) {
            Logger.error(this, "Failed to update macro in storage: " + e.toString());
        }
        
        _delegate.startPlaying();
    }
    
    public void stop()
    {
        if (_ongoingExecution == null)
        {
            Logger.messageEvent(this, "No need to stop, already idle.");
            return;
        }
        
        Logger.messageEvent(this, "Stop.");
        
        try {
            _ongoingExecution.stop();
            Logger.messageEvent(this, "Stopped ongoing execution process.");
            _ongoingExecution = null;
        } catch (Exception e) {
            Logger.error(this, "Failed to stop execution process: " + e.toString());
            return;
        }
        
        _delegate.stopRecording();
    }
    
    public void setOptionValues(PreferencesStorageValues values)
    {
        Logger.messageEvent(this, "Play parameters changed: " + values.macroParameters.toString());
        
        _options = values;
        
        // Save the option values to storage
        GeneralStorage.getDefault().getPreferencesStorage().saveValues(values);
    }
    
    public void navigateBack()
    {
        Logger.messageEvent(this, "Navigate back.");
        
        _recorder.unregisterHotkeyListener(this);
        
        _rootViewController.navigateToOpenScreen();
    }
    
    public void setPlayOrStopHotkey(Hotkey hotkey)
    {
        synchronized (this)
        {
            _playOrStopHotkey = hotkey;
        }
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
        String tooltip = TextValue.getText(TextValue.Play_NotificationStartTooltip, macroName);
        
        OSUIEffects.getShared().displayOSNotification(title, message, tooltip);
    }
    
    private void displayPlayingFinishedNotification()
    {
        if (!_options.displayStopNotification)
        {
            return;
        }
        
        String macroName = _macro.name;
        String macroHotkey = _playOrStopHotkey.key.toString();
        
        String title = TextValue.getText(TextValue.Play_NotificationFinishTitle);
        String message = TextValue.getText(TextValue.Play_NotificationFinishMessage, macroName);
        String tooltip = TextValue.getText(TextValue.Play_NotificationFinishTooltip, macroName);
        
        OSUIEffects.getShared().displayOSNotification(title, message, tooltip);
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
        String tooltip = TextValue.getText(TextValue.Play_NotificationRepeatTooltip, macroName, timesPlayed);
        
        OSUIEffects.getShared().displayOSNotification(title, message, tooltip);
    }
}
