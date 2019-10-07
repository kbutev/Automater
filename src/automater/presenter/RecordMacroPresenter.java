/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.presenter;

import automater.TextValue;
import automater.recorder.BaseRecorderListener;
import automater.recorder.Recorder;
import automater.recorder.RecorderHotkeyListener;
import automater.recorder.model.RecorderModel;
import automater.recorder.model.RecorderResult;
import automater.recorder.model.RecorderUserInput;
import automater.recorder.parser.RecorderNativeParser;
import automater.recorder.parser.RecorderNativeParserSmart;
import automater.recorder.parser.RecorderParserFlag;
import automater.settings.Hotkey;
import automater.storage.GeneralStorage;
import automater.storage.MacroStorage;
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
import automater.work.parser.BaseActionsParser;
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
public class RecordMacroPresenter implements BasePresenter, BaseRecorderListener, RecorderHotkeyListener {
    private final RootViewController _rootViewController;
    private BasePresenterDelegate _delegate;
    
    private final MacroStorage _storage = GeneralStorage.getDefault().getMacrosStorage();
    
    private final Recorder _recorder = Recorder.getDefault();
    private final List<RecorderParserFlag> _recordFlags = _recorder.defaults.getDefaultRecordFlags();
    private RecorderModel _recorderModel = new RecorderModel();
    private RecorderNativeParser _recorderMacroParser;
    private boolean _hasStartedMacroRecording = false;
    private RecorderResult _recordedResult;
    
    private final BaseActionsParser _parser = new ActionsFromMacroInputsParser();
    
    private final ArrayList<Description> _macroActionDescriptionsList = new ArrayList<>();
    
    private Hotkey _recordOrStopHotkey;
    
    private final ActionsParsing _actionsParsing = new ActionsParsing();
    
    public RecordMacroPresenter(RootViewController rootViewController)
    {
        _rootViewController = rootViewController;
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
        
        _recorder.registerPlayStopHotkeyListener(this);
        
        // Always start with one "do nothing" action, so user can save the macro even
        // without recording a single action
        setupDefaultRecordedInputData();
        updateDelegateActionsData();
    }
    
    @Override
    public void setDelegate(BasePresenterDelegate delegate)
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
    public void onRecordedUserInput(RecorderUserInput input)
    {
        if (input == null)
        {
            return;
        }
        
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
    public void onFailedRecordedUserInput(RecorderUserInput input)
    {
        _delegate.onActionsRecordedChange(getActionStringsData());
    }
    
    @Override
    public void onFinishedRecording(RecorderResult result, boolean success, Exception exception)
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
    public Hotkey getHotkey()
    {
        return _recordOrStopHotkey;
    }
    
    @Override
    public void onHotkeyPressed(Hotkey hotkey)
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
    
    // # Public
    
    public void onSwitchToPlayScreen()
    {
        _recorder.unregisterPlayStopHotkeyListener();
        
        _rootViewController.navigateToOpenScreen();
    }
    
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
            _recorderMacroParser = new RecorderNativeParserSmart(_recordFlags);
            _recorder.start(_recorderMacroParser, _recorderModel, this);
        } catch (Exception e) {
            _delegate.onErrorEncountered(e);
            return;
        }
        
        _hasStartedMacroRecording = true;
        
        Logger.messageEvent(this, "Starting recording...");
        
        _delegate.startRecording();
    }
    
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
            _recorder.stop();
            _delegate.stopRecording();
            Logger.messageEvent(this, "Recording has ended!");
        } catch (Exception e) {
            _delegate.onErrorEncountered(e);
            Logger.error(this, "Failed to stop recording: " + e.toString());
            e.printStackTrace(System.out);
        }
    }
    
    public void onSaveRecording(String name, String description)
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
        Exception canSaveRec = _storage.getSaveMacroError(macro);
        
        if (canSaveRec != null)
        {
            Logger.error(this, "Failed to save macro to storage! Reason: " + canSaveRec.getMessage());
            
            // When a simple error is encountered, do not wipe out here
            _delegate.onErrorEncountered(canSaveRec);
            return;
        }
        
        // Save to storage
        try {
            _storage.saveMacroToStorage(macro);
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
    
    public void onRecordingSavedSuccessufllyClosed()
    {
        clearData();
        
        updateDelegateActionsData();
        
        onSwitchToPlayScreen();
    }
    
    public void setPlayOrStopHotkey(Hotkey hotkey)
    {
        synchronized (this)
        {
            _recordOrStopHotkey = hotkey;
        }
    }
    
    // # Private
    
    private void setupDefaultRecordedInputData()
    {
        ArrayList<RecorderUserInput> userInputs;
        userInputs = new ArrayList<>();
        userInputs.add(RecorderUserInput.createDoNothing(0));
        _recorderModel = new RecorderModel();
        _recordedResult = new RecorderResult(userInputs);
        
        _macroActionDescriptionsList.clear();
        _macroActionDescriptionsList.add(userInputs.get(0));
    }
    
    private void clearData()
    {
        _recorderModel = new RecorderModel();
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
    
    private List<Description> getActionStringsData()
    {
        return CollectionUtilities.copyAsReversed(_macroActionDescriptionsList);
    }
    
    private void displayRecordingStartedNotification()
    {
        String macroHotkey = _recordOrStopHotkey.key.toString();
        
        String title = TextValue.getText(TextValue.Record_NotificationStartTitle);
        String message = TextValue.getText(TextValue.Record_NotificationStartMessage, macroHotkey);
        String tooltip = TextValue.getText(TextValue.Record_NotificationStartTooltip);
        
        DeviceNotifications.getShared().displayOSNotification(title, message, tooltip);
    }
    
    private void displayRecordingStoppedNotification()
    {
        String macroHotkey = _recordOrStopHotkey.key.toString();
        
        String title = TextValue.getText(TextValue.Record_NotificationStopTitle);
        String message = TextValue.getText(TextValue.Record_NotificationStopMessage, macroHotkey);
        String tooltip = TextValue.getText(TextValue.Record_NotificationStopTooltip);
        
        DeviceNotifications.getShared().displayOSNotification(title, message, tooltip);
    }
    
    // Parsing
    class ActionsParsing {
        public List<BaseAction> parseUserInputs(Collection<RecorderUserInput> userInputs, BaseActionsParser actionParser) throws Exception
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
