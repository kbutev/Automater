/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.presenter.OpenMacroPresenter;
import automater.presenter.PlayMacroPresenter;
import automater.presenter.RecordMacroPresenter;
import automater.recorder.model.RecorderUserInputKey;
import automater.recorder.model.RecorderUserInputKeyMask;
import automater.recorder.model.RecorderUserInputKeyValue;
import automater.settings.Hotkey;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.work.model.Macro;

/**
 *
 * @author Bytevi
 */
public class PrimaryViewContoller implements RootViewController {
    private BaseViewController _currentViewController;
    
    private OpenMacroViewController _openMacroViewController;
    private OpenMacroPresenter _openMacroPresenter;
    private RecordMacroViewController _recordMacroViewController;
    private RecordMacroPresenter _recordMacroPresenter;
    
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
    public void navigateToPlayScreen(automater.work.model.Macro macro)
    {
        Logger.messageEvent(this, "Navigating to play macro screen.");
        
        switchScreenToPlay(macro);
    }
    
    // # Private
    
    private void switchScreenToOpen()
    {
        boolean playViewControllerStart = _openMacroViewController == null;
        
        if (playViewControllerStart)
        {
            _openMacroPresenter = new OpenMacroPresenter(this);
            OpenMacroViewController vc = new OpenMacroViewController(_openMacroPresenter);
            _openMacroViewController = vc;
            _openMacroPresenter.setDelegate(vc);
        }
        
        if (_currentViewController != null)
        {
            _currentViewController.suspend();
        }
        
        _currentViewController = _openMacroViewController;
        
        if (playViewControllerStart)
        {
            _currentViewController.start();
        }
        else
        {
            _currentViewController.resume();
        }
        
        _openMacroPresenter.start();
    }
    
    private void switchScreenToRecord()
    {
        boolean recordViewControllerStart = _recordMacroViewController == null;
        
        if (recordViewControllerStart)
        {
            Hotkey playOrStopHotkey;
            playOrStopHotkey = new Hotkey(RecorderUserInputKeyValue._F4);
            _recordMacroPresenter = new RecordMacroPresenter(this);
            _recordMacroPresenter.setPlayOrStopHotkey(playOrStopHotkey);
            RecordMacroViewController vc = new RecordMacroViewController(_recordMacroPresenter);
            _recordMacroViewController = vc;
            _recordMacroPresenter.setDelegate(vc);
        }
        
        if (_currentViewController != null)
        {
            _currentViewController.suspend();
        }
        
        _currentViewController = _recordMacroViewController;
        
        if (recordViewControllerStart)
        {
            _currentViewController.start();
        }
        else
        {
            _currentViewController.resume();
        }
        
        _recordMacroPresenter.start();
    }
    
    private void switchScreenToPlay(Macro macro)
    {
        Hotkey playOrStopHotkey;
        playOrStopHotkey = new Hotkey(RecorderUserInputKeyValue._F4);
        
        PlayMacroPresenter presenter = new PlayMacroPresenter(this, macro);
        presenter.setPlayOrStopHotkey(playOrStopHotkey);
        PlayMacroViewController vc = new PlayMacroViewController(presenter);
        presenter.setDelegate(vc);
        
        if (_currentViewController != null)
        {
            _currentViewController.suspend();
        }
        
        _currentViewController = vc;
        
        _currentViewController.start();
        
        presenter.start();
    }
}
