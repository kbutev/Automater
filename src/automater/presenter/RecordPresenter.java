/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.presenter;

import automater.recorder.Recorder;
import automater.recorder.RecorderResult;
import automater.storage.GeneralStorage;
import automater.storage.MacroStorage;
import automater.ui.viewcontroller.RootViewController;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.work.Macro;
import com.sun.istack.internal.Nullable;

/**
 *
 * @author Bytevi
 */
public class RecordPresenter implements BasePresenter {
    private final RootViewController _rootViewController;
    private BasePresenterDelegate _delegate;
    
    private final MacroStorage _storage = GeneralStorage.getDefault().getMacrosStorage();
    
    private final Recorder _recorder = Recorder.getDefault();
    private RecorderResult _recordedResult;
    
    public RecordPresenter(RootViewController rootViewController)
    {
        _rootViewController = rootViewController;
    }
    
    @Override
    public void start()
    {
        if (_delegate == null)
        {
            Errors.throwInternalLogicError("RecordPresenter delegate is not set before starting");
        }
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
    
    public void onSwitchToPlayScreen()
    {
        _rootViewController.navigateToPlayScreen();
    }
    
    public void onStartRecording()
    {
        if (_recorder.isRecording())
        {
            _delegate.onErrorEncountered(new Exception("Cannot start when already recording!"));
            return;
        }
        
        try {
            _recorder.start();
        } catch (Exception e) {
            _delegate.onErrorEncountered(e);
            return;
        }
        
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
    
    public void onSaveRecording(String name)
    {
        RecorderResult result = _recordedResult;
        
        Logger.message(this, "Recorded " + String.valueOf(result.userInputs.size()) + " events");
        
        Macro macro = new Macro(name, _recordedResult);
        
        // Error checking
        Exception canSaveRec = _storage.getSaveMacroError(macro);
        
        if (canSaveRec != null)
        {
            _delegate.onErrorEncountered(canSaveRec);
            return;
        }
        
        // Wipe recorded result
        _recordedResult = null;
        
        // Save operation
        try {
            _storage.addMacroToStorage(macro);
        } catch (Exception e) {
            _delegate.onErrorEncountered(e);
            return;
        }
        
        // Alert delegate that operation was successful
        _delegate.recordingSavedSuccessfully(name);
    }
    
    public void onRecordingSavedSuccessufllyClosed()
    {
        _rootViewController.navigateToPlayScreen();
    }
}
