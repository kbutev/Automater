/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.presenter;

import automater.recorder.BaseRecorderListener;
import automater.recorder.Recorder;
import automater.recorder.RecorderResult;
import automater.recorder.model.RecorderUserInput;
import automater.storage.GeneralStorage;
import automater.storage.MacroStorage;
import automater.ui.viewcontroller.RootViewController;
import automater.utilities.CollectionUtilities;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.work.Macro;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bytevi
 */
public class RecordMacroPresenter implements BasePresenter, BaseRecorderListener {
    private final RootViewController _rootViewController;
    private BasePresenterDelegate _delegate;
    
    private final MacroStorage _storage = GeneralStorage.getDefault().getMacrosStorage();
    
    private final Recorder _recorder = Recorder.getDefault();
    private RecorderResult _recordedResult;
    
    private ArrayList<Description> _macroActionDescriptionsList = new ArrayList<>();
    
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
            Errors.throwInternalLogicError("RecordPresenter delegate is not set before starting");
        }
        
        Logger.message(this, "Start.");
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
    
    // # BaseRecorderListener
    
    @Override
    public void onRecordedUserInput(RecorderUserInput input)
    {
        Logger.messageEvent(this, "Captured user input " + input.toString());
        
        _macroActionDescriptionsList.add(input);
        
        _delegate.onActionsRecordedChange(getActionStringsData());
    }
    
    // # Public functionality
    
    public void onSwitchToPlayScreen()
    {
        _rootViewController.navigateToOpenScreen();
    }
    
    public void onStartRecording()
    {
        if (_recorder.isRecording())
        {
            _delegate.onErrorEncountered(new Exception("Cannot start when already recording!"));
            return;
        }
        
        try {
            _recorder.start(this);
        } catch (Exception e) {
            _delegate.onErrorEncountered(e);
            return;
        }
        
        clearData();
        
        Logger.messageEvent(this, "Macro recording has started....");
        
        _delegate.startRecording();
    }
    
    public void onStopRecording()
    {
        if (!_recorder.isRecording())
        {
            _delegate.onErrorEncountered(new Exception("Cannot stop when not recording!"));
            return;
        }
        
        _recordedResult = null;
        
        try {
            _recordedResult = _recorder.stop();
        } catch (Exception e) {
            _delegate.onErrorEncountered(e);
        }
        
        Logger.messageEvent(this, "Macro recording has ended!");
        
        _delegate.stopRecording();
    }
    
    public void onSaveRecording(String name, String description)
    {
        RecorderResult result = _recordedResult;
        
        Logger.message(this, "Recorded " + String.valueOf(result.userInputs.size()) + " events");
        
        Macro macro = new Macro(name, _recordedResult);
        Logger.message(this, "test " + description);
        macro.setDescription(description);
        
        // Simple error checking - for name taken, actions empty etc...
        Exception canSaveRec = _storage.getSaveMacroError(macro);
        
        if (canSaveRec != null)
        {
            // When a simple error is encountered, do not wipe out here
            _delegate.onErrorEncountered(canSaveRec);
            return;
        }
        
        // Wipe recorded result, regardless of save operation result
        _recordedResult = null;
        
        // Save operation
        try {
            _storage.saveMacroToStorage(macro);
        } catch (Exception e) {
            _delegate.onRecordingSaved(name, false);
            _delegate.onErrorEncountered(e);
            return;
        }
        
        // Alert delegate that operation was successful
        _delegate.onRecordingSaved(name, true);
    }
    
    public void onRecordingSavedSuccessufllyClosed()
    {
        clearData();
        
        _rootViewController.navigateToOpenScreen();
    }
    
    // # Private
    
    private void clearData()
    {
        _macroActionDescriptionsList.clear();
        
        if (_delegate != null)
        {
            _delegate.onActionsRecordedChange(getActionStringsData());
        }
    }
    
    private List<Description> getActionStringsData()
    {
        return CollectionUtilities.copyAsReversed(_macroActionDescriptionsList);
    }
}
