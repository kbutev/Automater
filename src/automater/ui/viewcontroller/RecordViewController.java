/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.recorder.Recorder;
import automater.recorder.RecorderResult;
import automater.recorder.RecorderUserInput;
import automater.storage.GeneralStorage;
import automater.ui.view.FormActionDelegate;
import automater.ui.view.RecordForm;
import automater.utilities.Logger;
import automater.work.Macro;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Bytevi
 */
public class RecordViewController implements ViewController, FormActionDelegate {
    private FormActionDelegate _delegate;
    
    private RecordForm _form;
    
    private RecorderResult _recordedResult;
    
    public RecordViewController()
    {
        _form = new RecordForm();
    }
    
    @Override
    public void start()
    {
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
    
    @Override
    public void setActionDelegate(FormActionDelegate delegate)
    {
        _delegate = delegate;
        _form.delegate = this;
    }
    
    @Override
    public void onSwitchWindow()
    {
        if (_delegate != null)
        {
            _delegate.onSwitchWindow();
        }
    }
    
    @Override
    public void onBeginRecord()
    {
        Logger.messageAction("Begin recording...");
        
        try {
            Recorder.getDefault().start();
            _form.beginRecording();
        } catch (Exception e) {
            _form.displayError("Record", "Cannot start recorder, its already recording!");
        }
    }
    
    @Override
    public void onEndRecord()
    {
        Logger.messageAction("End recording.");
        
        try {
            // Retrieve result
            _recordedResult = Recorder.getDefault().stop();
            
            Collection data = _recordedResult.userInputs;
            
            Logger.messageAction("Recorded " + String.valueOf(data.size()) + " actions! Data:\n" + data.toString());
            
        } catch (Exception e) {
            _form.displayError("Record", "Cannot end recorder, its not recording!");
        }
        
        _form.endRecording();
    }
    
    @Override
    public void onSaveRecording(String name)
    {
        Logger.messageAction("Save recording as \"" + name + "\"");
        
        _form.willSaveRecording();
        
        // Parse result to macros
        ArrayList<Macro> macros = new ArrayList<Macro>();
        macros.add(new Macro(name, _recordedResult));
        
        // Store result
        try {
            GeneralStorage.getDefault().getMacrosStorage().addMacrosToStorage(macros);
        } catch (Exception e) {
            _form.didSaveRecording(false, e.toString());
            return;
        }
        
        _form.didSaveRecording(true, null);
    }
    
    @Override
    public void onPlayMacro(String name)
    {
        
    }
    
    @Override
    public void onStopPlayingMacro()
    {
        
    }
    
    @Override
    public void onDeleteMacro(String name)
    {
        
    }
    
    @Override
    public void onChangePlayMacroParameters(String name)
    {
        
    }
}
