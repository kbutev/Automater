/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.presenter.OpenMacroPresenter;
import automater.presenter.PlayMacroPresenter;
import automater.presenter.RecordMacroPresenter;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.work.Macro;

/**
 *
 * @author Bytevi
 */
public class PrimaryViewContoller implements RootViewController {
    private BaseViewController _currentViewController;
    
    private OpenMacroViewController _openMacroViewController;
    private RecordMacroViewController _recordMacroViewController;
    private PlayMacroViewController _playMacroViewController;
    
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
        Logger.messageEvent(this, "Navigating to record macro screen.");
        
        switchScreenToRecord();
    }
    
    @Override
    public void navigateToOpenScreen()
    {
        Logger.messageEvent(this, "Navigating to open macro screen.");
        
        switchScreenToOpen();
    }
    
    @Override
    public void navigateToPlayScreen(automater.work.Macro macro)
    {
        Logger.messageEvent(this, "Navigating to play macro screen.");
        
        switchScreenToPlay(macro);
    }
    
    // # Private
    
    private void switchScreenToOpen()
    {
        boolean playViewControllerStart = _openMacroViewController == null;
        OpenMacroPresenter presenter = null;
        
        if (playViewControllerStart)
        {
            presenter = new OpenMacroPresenter(this);
            OpenMacroViewController vc = new OpenMacroViewController(presenter);
            _openMacroViewController = vc;
            presenter.setDelegate(vc);
        }
        
        if (_currentViewController != null)
        {
            _currentViewController.suspend();
        }
        
        _currentViewController = _openMacroViewController;
        
        if (playViewControllerStart)
        {
            _currentViewController.start();
            presenter.start();
        }
        else
        {
            _currentViewController.resume();
        }
    }
    
    private void switchScreenToRecord()
    {
        boolean recordViewControllerStart = _recordMacroViewController == null;
        RecordMacroPresenter presenter = null;
        
        if (recordViewControllerStart)
        {
            presenter = new RecordMacroPresenter(this);
            RecordMacroViewController vc = new RecordMacroViewController(presenter);
            _recordMacroViewController = vc;
            presenter.setDelegate(vc);
        }
        
        if (_currentViewController != null)
        {
            _currentViewController.suspend();
        }
        
        _currentViewController = _recordMacroViewController;
        
        if (recordViewControllerStart)
        {
            _currentViewController.start();
            presenter.start();
        }
        else
        {
            _currentViewController.resume();
        }
    }
    
    private void switchScreenToPlay(Macro macro)
    {
        boolean playViewControllerStart = _playMacroViewController == null;
        PlayMacroPresenter presenter = null;
        
        if (playViewControllerStart)
        {
            presenter = new PlayMacroPresenter(this, macro);
            PlayMacroViewController vc = new PlayMacroViewController(presenter);
            _playMacroViewController = vc;
            presenter.setDelegate(vc);
        }
        
        if (_currentViewController != null)
        {
            _currentViewController.suspend();
        }
        
        _currentViewController = _playMacroViewController;
        
        if (playViewControllerStart)
        {
            _currentViewController.start();
            presenter.start();
        }
        else
        {
            _currentViewController.resume();
        }
    }
}
