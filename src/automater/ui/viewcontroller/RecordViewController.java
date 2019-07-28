/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.presenter.BasePresenterDelegate;
import automater.presenter.RecordPresenter;
import automater.ui.view.RecordForm;
import automater.utilities.AlertWindows;
import automater.utilities.Callback;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import java.awt.event.WindowEvent;

/**
 *
 * @author Bytevi
 */
public class RecordViewController implements BaseViewController, BasePresenterDelegate {
    private final RecordPresenter _presenter;
    
    private RecordForm _form;
    
    public RecordViewController(RecordPresenter presenter)
    {
        _presenter = presenter;
        _form = new RecordForm();
    }
    
    private void setupViewCallbacks()
    {
        _form.onSwitchToPlayButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.onSwitchToPlayScreen();
            }
        };
        
        _form.onRecordMacroButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.onStartRecording();
            }
        };
        
        _form.onStopRecordMacroButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.onStopRecording();
            }
        };
        
        _form.onSaveMacroButtonCallback = new Callback<String>() {
            @Override
            public void perform(String argument) {
                _presenter.onSaveRecording(argument);
            }
        };
    }
    
    // # BaseViewController
    
    @Override
    public void start()
    {
        setupViewCallbacks();
        
        _form.setVisible(true);
    }
    
    @Override
    public void suspend()
    {
        _form.setVisible(false);
    }
    
    @Override
    public void terminate()
    {
        _form.dispatchEvent(new WindowEvent(_form, WindowEvent.WINDOW_CLOSING));
    }
    
    // # BasePresenterDelegate
    
    @Override
    public void startRecording()
    {
        _form.beginRecording();
    }
    
    @Override
    public void stopRecording()
    {
        _form.endRecording();
    }
    
    @Override
    public void recordingSavedSuccessfully(String name)
    {
        AlertWindows.showMessage(_form, "Save Macro", "Recording '" + name + "' saved successfully!", "Ok", new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.onRecordingSavedSuccessufllyClosed();
            }
        });
    }
    
    @Override
    public void startPlaying()
    {
        
    }
        
    @Override    
    public void stopPlaying()
    {
        
    }
    
    @Override
    public void onErrorEncountered(Exception e)
    {
        Logger.error(this, "Error encountered: " + e.toString());
        
        AlertWindows.showErrorMessage(_form, "Save Macro", "Could not save: " + e.getMessage(), "Ok");
    }
}
