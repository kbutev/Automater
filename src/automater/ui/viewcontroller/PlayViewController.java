/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.presenter.BasePresenterDelegate;
import automater.presenter.PlayPresenter;
import automater.ui.view.PlayForm;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import com.sun.istack.internal.Nullable;
import java.awt.event.WindowEvent;

/**
 *
 * @author Bytevi
 */
public class PlayViewController implements BaseViewController, BasePresenterDelegate {
    private final PlayPresenter _presenter;
    
    private PlayForm _form;
    
    public PlayViewController(PlayPresenter presenter)
    {
        _presenter = presenter;
        _form = new PlayForm();
    }
    
    private void setupViewCallbacks()
    {
        _form.onSwitchToPlayButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.onSwitchToRecordScreen();
            }
        };
    }
    
    // # BaseViewController
    
    @Override
    public void start()
    {
        _form.setVisible(true);
        
        setupViewCallbacks();
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
        
    }
    
    @Override
    public void stopRecording()
    {
        
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
        Logger.error(this, e.toString());
    }
}
