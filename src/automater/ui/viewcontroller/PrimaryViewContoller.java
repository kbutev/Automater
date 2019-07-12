/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.ui.view.FormActionDelegate;
import automater.utilities.Errors;
import automater.utilities.Logger;

/**
 *
 * @author Bytevi
 */
public class PrimaryViewContoller implements ViewController, FormActionDelegate {
    private ViewController _currentViewController;
    
    private PlayViewController _playViewController;
    private RecordViewController _recordViewController;
    
    public PrimaryViewContoller()
    {
        
    }
    
    public ViewController getCurrentViewController()
    {
        return _currentViewController;
    }
    
    @Override
    public void start()
    {
        switchScreenToPlay();
    }
    
    @Override
    public void suspend()
    {
        Errors.throwInternalLogicError("Cannot hide primary view controller");
    }
    
    @Override
    public void terminate()
    {
        Errors.throwInternalLogicError("Cannot terminate primary view controller");
    }
    
    @Override
    public void setActionDelegate(FormActionDelegate delegate)
    {
        
    }
    
    @Override
    public void onSwitchWindow()
    {
        if (_currentViewController instanceof RecordViewController)
        {
            Logger.messageAction("Switching from Record screen to Play screen");
            switchScreenToPlay();
        }
        else
        {
            Logger.messageAction("Switching from Play screen to Record screen");
            switchScreenToRecord();
        }
    }
    
    private void switchScreenToPlay()
    {
        if (_playViewController == null)
        {
            _playViewController = new PlayViewController();
            _playViewController.setActionDelegate(this);
        }
        
        if (_currentViewController != null)
        {
            _currentViewController.suspend();
        }
        
        _currentViewController = _playViewController;
        _currentViewController.start();
    }
    
    private void switchScreenToRecord()
    {
        if (_recordViewController == null)
        {
            _recordViewController = new RecordViewController();
            _recordViewController.setActionDelegate(this);
        }
        
        if (_currentViewController != null)
        {
            _currentViewController.suspend();
        }
        
        _currentViewController = _recordViewController;
        _currentViewController.start();
    }
}
