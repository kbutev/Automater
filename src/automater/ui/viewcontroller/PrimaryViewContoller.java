/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.presenter.PlayPresenter;
import automater.presenter.RecordPresenter;
import automater.utilities.Errors;
import automater.utilities.Logger;

/**
 *
 * @author Bytevi
 */
public class PrimaryViewContoller implements RootViewController {
    private BaseViewController _currentViewController;
    
    private PlayViewController _playViewController;
    private RecordViewController _recordViewController;
    
    public PrimaryViewContoller()
    {
        
    }
    
    public BaseViewController getCurrentViewController()
    {
        return _currentViewController;
    }
    
    public void start()
    {
        if (_currentViewController != null)
        {
            Errors.throwInternalLogicError("PrimaryViewController already started");
        }
        
        // By default, we start with the record screen opened
        switchScreenToRecord();
    }
    
    // # RootViewController
    
    @Override
    public void navigateToRecordScreen()
    {
        Logger.messageEvent(this, "Navigating to record screen.");
        
        switchScreenToRecord();
    }
    
    @Override
    public void navigateToPlayScreen()
    {
        Logger.messageEvent(this, "Navigating to play screen.");
        
        switchScreenToPlay();
    }
    
    private void switchScreenToPlay()
    {
        if (_playViewController == null)
        {
            PlayPresenter presenter = new PlayPresenter(this);
            PlayViewController vc = new PlayViewController(presenter);
            _playViewController = vc;
            presenter.setDelegate(vc);
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
            RecordPresenter presenter = new RecordPresenter(this);
            RecordViewController vc = new RecordViewController(presenter);
            _recordViewController = vc;
            presenter.setDelegate(vc);
        }
        
        if (_currentViewController != null)
        {
            _currentViewController.suspend();
        }
        
        _currentViewController = _recordViewController;
        _currentViewController.start();
    }
}
