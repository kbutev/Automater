/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.ui.view.FormActionDelegate;
import automater.ui.view.RecordForm;
import java.awt.event.WindowEvent;

/**
 *
 * @author Bytevi
 */
public class RecordViewController implements ViewController, FormActionDelegate {
    private FormActionDelegate _delegate;
    
    private RecordForm _form;
    
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
        _form.delegate = delegate;
    }
    
    @Override
    public void onSwitchWindow()
    {
        if (_delegate != null)
        {
            _delegate.onSwitchWindow();
        }
    }
}
