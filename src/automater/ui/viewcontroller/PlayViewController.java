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
import java.awt.event.WindowEvent;
import java.util.List;
import automater.ui.view.BaseView;
import automater.utilities.Description;

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
        setupViewCallbacks();
        
        _form.setVisible(true);
        _form.onViewStart();
    }
    
    @Override
    public void resume()
    {
        _form.setVisible(true);
        _form.onViewResume();
    }
    
    @Override
    public void suspend()
    {
        _form.setVisible(false);
        _form.onViewSuspended();
    }
    
    @Override
    public void terminate()
    {
        _form.onViewTerminate();
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
    public void onActionsRecordedChange(List<Description> actions)
    {
    
    }
    
    @Override
    public void recordingSavedSuccessfully(String name)
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
        Logger.error(this, "Error encountered: " + e.toString());
    }
}
