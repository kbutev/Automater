/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.ui.view.FormActionDelegate;
import automater.ui.view.PlayForm;
import automater.utilities.Logger;
import java.awt.event.WindowEvent;

/**
 *
 * @author Bytevi
 */
public class PlayViewController implements ViewController, FormActionDelegate {
    private FormActionDelegate _delegate;
    
    private PlayForm _form;
    
    public PlayViewController()
    {
        _form = new PlayForm();
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
        
    }
    
    @Override
    public void onEndRecord()
    {
        
    }
    
    @Override
    public void onSaveRecording(String name)
    {
        
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
