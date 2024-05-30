/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.mvp.BasePresenter.RecordMacroPresenter;
import automater.TextValue;
import automater.di.DI;
import automater.mvp.BasePresenterDelegate.RecordMacroPresenterDelegate;
import automater.recorder.Recorder;
import automater.recorder.model.RecorderModelStandard;
import automater.recorder.model.RecorderResult;
import automater.recorder.model.RecorderUserInput;
import automater.recorder.parser.RecorderNativeParser;
import automater.recorder.parser.RecorderNativeParserSmart;
import automater.settings.Hotkey;
import automater.storage.GeneralStorage;
import automater.storage.PreferencesStorageValues;
import automater.ui.viewcontroller.RootViewController;
import automater.utilities.CollectionUtilities;
import automater.utilities.Description;
import automater.utilities.DeviceScreen;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.utilities.DeviceNotifications;
import automater.work.BaseAction;
import automater.work.model.Macro;
import automater.work.parser.ActionsFromMacroInputsParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Presenter for the record macro screen.
 *
 * @author Bytevi
 */
public class RecordMacroPresenterStandard implements RecordMacroPresenter, Recorder.Listener, Recorder.HotkeyListener {
    
    private final GeneralStorage.Protocol storage = DI.get(GeneralStorage.Protocol.class);
    private final Recorder.Protocol recorder = DI.get(Recorder.Protocol.class);
    private final ActionsFromMacroInputsParser.Protocol _parser = DI.get(ActionsFromMacroInputsParser.Protocol.class);
    
    @NotNull private final RootViewController _rootViewController;
    @Nullable private RecordMacroPresenterDelegate _delegate;
    
    @NotNull private Recorder.Model _recorderModel = new RecorderModelStandard();
    @Nullable private RecorderNativeParser.Impl _recorderMacroParser;
    private boolean _hasStartedMacroRecording = false;
    @Nullable private RecorderResult _recordedResult;
    
    @NotNull private final ArrayList<Description> _macroActionDescriptionsList = new ArrayList<>();
    
    @Nullable private Hotkey _recordOrStopHotkey;
    
    @NotNull private final ActionsParsing _actionsParsing = new ActionsParsing();
    
    public RecordMacroPresenterStandard(@NotNull RootViewController rootViewController)
    {
        _rootViewController = rootViewController;
        _recordOrStopHotkey = storage.getPreferencesStorage().getValues().recordOrStopHotkey;
    }
    
    // # BasePresenter
    
    @Override
    public void start()
    {
        if (_delegate == null)
        {
            Errors.throwInternalLogicError("RecordMacroPresenter delegate is not set before starting");
        }
        
        Logger.message(this, "Start.");
        
        recorder.registerPlayStopHotkeyListener(this);
        
        // Always start with one "do nothing" action, so user can save the macro even
        // without recording a single action
        setupDefaultRecordedInputData();
        updateDelegateActionsData();
        
        // Load preferences, just once, for the hotkey
        PreferencesStorageValues preferences = storage.getPreferencesStorage().getValues();
        _recordOrStopHotkey = preferences.recordOrStopHotkey;
        _delegate.onLoadedPreferencesFromStorage(preferences);
    }
    
    @Override
    public void setDelegate(@NotNull RecordMacroPresenterDelegate delegate)
    {
        if (_delegate != null)
        {
            Errors.throwInternalLogicError("RecordMacroPresenter delegate is already set");
        }
        
        _delegate = delegate;
    }
    
    @Override
    public void requestDataUpdate()
    {
        updateDelegateActionsData();
    }
    
    // # BaseRecorderListener
    
    @Override
    public void onRecordedUserInput(@NotNull RecorderUserInput input)
    {
        Logger.messageEvent(this, "Captured user input " + input.toString());
        
        _macroActionDescriptionsList.add(input);
        
        _delegate.onActionsRecordedChange(getActionStringsData());
    }
    
    @Override
    public void onRecordedUserInputChanged()
    {
        _delegate.onActionsRecordedChange(getActionStringsData());
    }
    
    @Override
    public void onFailedRecordedUserInput(@NotNull RecorderUserInput input)
    {
        _delegate.onActionsRecordedChange(getActionStringsData());
    }
    
    @Override
    public void onFinishedRecording(@Nullable RecorderResult result, boolean success, @Nullable Exception exception)
    {
        Logger.messageEvent(this, "Recording was stopped.");
        
        _recordedResult = result;
        
        _delegate.stopRecording();
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
        return _recordOrStopHotkey;
    }
    
    @Override
    public void onHotkeyPressed(@NotNull Hotkey hotkey)
    {
        Logger.message(this, "Record hotkey tapped!");
        
        if (!_hasStartedMacroRecording)
        {
            displayRecordingStartedNotification();
            
            onStartRecording();
        }
        else
        {
            displayRecordingStoppedNotification();
            
            onStopRecording();
        }
    }
    
    // # RecordMacroPresenter
    
    @Override
    public void onSwitchToPlayScreen()
    {
        recorder.unregisterPlayStopHotkeyListener();
        
        _rootViewController.navigateToOpenScreen();
    }
    
    @Override
    public void onStartRecording()
    {
        if (_hasStartedMacroRecording)
        {
            _delegate.onErrorEncountered(new Exception("Cannot start when already recording!"));
            return;
        }
        
        clearData();
        updateDelegateActionsData();
        
        try {
            _recorderMacroParser = new RecorderNativeParserSmart();
            recorder.start(_recorderMacroParser, _recorderModel, this);
        } catch (Exception e) {
            _delegate.onErrorEncountered(e);
            return;
        }
        
        _hasStartedMacroRecording = true;
        
        Logger.messageEvent(this, "Starting recording...");
        
        _delegate.startRecording();
    }
    
    @Override
    public void onStopRecording()
    {
        if (!_hasStartedMacroRecording)
        {
            _delegate.onErrorEncountered(new Exception("Cannot stop when not recording!"));
            return;
        }
        
        _hasStartedMacroRecording = false;
        
        Logger.messageEvent(this, "Stopping recording...");
        
        try {
            recorder.stop();
            _delegate.stopRecording();
            Logger.messageEvent(this, "Recording has ended!");
        } catch (Exception e) {
            _delegate.onErrorEncountered(e);
            Logger.error(this, "Failed to stop recording: " + e.toString());
            e.printStackTrace(System.out);
        }
    }
    
    @Override
    public void onSaveRecording(@NotNull String name, @NotNull String description)
    {
        if (_recordedResult == null)
        {
            Logger.error(this, "Cannot save yet, nothing has been recorded!");
            _delegate.onErrorEncountered(new Exception("Cannot save, nothing has been recorded!"));
            return;
        }
        
        // Store copy of data
        RecorderResult result = _recordedResult;
        
        Logger.message(this, "Parsing " + String.valueOf(result.userInputs.size()) + " recorded inputs...");
        
        List<BaseAction> actions;
        
        try {
            actions = _actionsParsing.parseUserInputs(result.userInputs, _parser);
        } catch (Exception e) {
            Logger.error(this, "Failed to parse recorded inputs! Reason: " + e.getMessage());
            _delegate.onErrorEncountered(new Exception("Failed to save the recorded inputs: " + e.getMessage()));
            return;
        }
        
        Logger.message(this, "Finished parsing the macro inputs!");
        Logger.message(this, "Attempting to save macro to storage...");
        
        Dimension currentScreenSize = DeviceScreen.getPrimaryScreenSize();
        
        Macro macro = new Macro(name, actions, new Date(), currentScreenSize);
        
        macro.setDescription(description);
        
        // Simple error checking - for name taken, actions empty etc...
        Exception canSaveRec = storage.getMacrosStorage().getSaveMacroError(macro);
        
        if (canSaveRec != null)
        {
            Logger.error(this, "Failed to save macro to storage! Reason: " + canSaveRec.getMessage());
            
            // When a simple error is encountered, do not wipe out here
            _delegate.onErrorEncountered(canSaveRec);
            return;
        }
        
        // Save to storage
        try {
            storage.getMacrosStorage().saveMacroToStorage(macro);
        } catch (Exception e) {
            Logger.error(this, "Failed to save macro to storage! Reason: " + e.getMessage());
            _delegate.onRecordingSaved(name, false);
            _delegate.onErrorEncountered(e);
            return;
        }
        
        // Reset the model to its default values
        clearData();
        setupDefaultRecordedInputData();
        
        // Alert delegate that operation was successful
        Logger.message(this, "Successfully saved macro to storage!");
        _delegate.onRecordingSaved(name, true);
    }
    
    @Override
    public void onRecordingSavedSuccessufllyClosed()
    {
        clearData();
        
        updateDelegateActionsData();
        
        onSwitchToPlayScreen();
    }
    
    // # Private
    
    private void setupDefaultRecordedInputData()
    {
        ArrayList<RecorderUserInput> userInputs;
        userInputs = new ArrayList<>();
        userInputs.add(RecorderUserInput.createDoNothing(0));
        _recorderModel = new RecorderModelStandard();
        _recordedResult = new RecorderResult(userInputs);
        
        _macroActionDescriptionsList.clear();
        _macroActionDescriptionsList.add(userInputs.get(0));
    }
    
    private void clearData()
    {
        _recorderModel = new RecorderModelStandard();
        _recordedResult = null;
        
        _macroActionDescriptionsList.clear();
    }
    
    private void updateDelegateActionsData()
    {
        if (_delegate != null)
        {
            _delegate.onActionsRecordedChange(getActionStringsData());
        }
    }
    
    private @NotNull List<Description> getActionStringsData()
    {
        return CollectionUtilities.copy(_macroActionDescriptionsList);
    }
    
    private void displayRecordingStartedNotification()
    {
        String macroHotkey = _recordOrStopHotkey.key.toString();
        
        String title = TextValue.getText(TextValue.Record_NotificationStartTitle);
        String message = TextValue.getText(TextValue.Record_NotificationStartMessage, macroHotkey);
        
        DeviceNotifications.getShared().displayGlobalNotification(title, message);
    }
    
    private void displayRecordingStoppedNotification()
    {
        String macroHotkey = _recordOrStopHotkey.key.toString();
        
        String title = TextValue.getText(TextValue.Record_NotificationStopTitle);
        String message = TextValue.getText(TextValue.Record_NotificationStopMessage, macroHotkey);
        
        DeviceNotifications.getShared().displayGlobalNotification(title, message);
    }
    
    // Parsing
    class ActionsParsing {
        public List<BaseAction> parseUserInputs(@NotNull Collection<RecorderUserInput> userInputs, @NotNull ActionsFromMacroInputsParser.Protocol actionParser) throws Exception
        {
            actionParser.onBeginParsing();
            
            Iterator<RecorderUserInput> it = userInputs.iterator();
            
            while (it.hasNext())
            {
                actionParser.onParseInput(it.next());
            }
            return actionParser.onFinishParsingMacro();
        }
    }
}
