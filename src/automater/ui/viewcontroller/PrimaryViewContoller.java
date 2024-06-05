/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.viewcontroller;

import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.work.model.Macro;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import automater.mvp.BasePresenter.*;
import automater.presenter.EditMacroPresenterStandard;
import automater.presenter.OpenMacroPresenterStandard;
import automater.presenter.PlayMacroPresenter;
import automater.presenter.RecordMacroPresenter;

/**
 * Root view controller responsible for picking which view controller will be
 * the current screen for the application.
 *
 * @author Bytevi
 */
public class PrimaryViewContoller implements RootViewController {
    @Nullable private BaseViewController _currentViewController;
    
    // Cached view controllers and their presenters
    // These view controllers are never initialized twice
    @Nullable private OpenMacroViewController _openMacroViewController;
    @Nullable private OpenMacroPresenter _openMacroPresenter;
    @Nullable private RecordMacroViewController _recordMacroViewController;
    @Nullable private RecordMacroPresenter.Protocol _recordMacroPresenter;
    
    public PrimaryViewContoller()
    {
        
    }
    
    public void start()
    {
        if (_currentViewController != null)
        {
            throw Errors.internalLogicError();
        }
        
        switchScreenToDefault();
    }
    
    // # RootViewController
    
    @Override
    public @Nullable BaseViewController getCurrentViewController()
    {
        return _currentViewController;
    }
    
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
    public void navigateToPlayScreen(@NotNull automater.work.model.Macro macro)
    {
        Logger.messageEvent(this, "Navigating to play macro screen.");
        
        switchScreenToPlay(macro);
    }
    
    @Override
    public void navigateToEditScreen(@NotNull automater.work.model.Macro macro)
    {
        Logger.messageEvent(this, "Navigating to edit macro screen.");
        
        switchScreenToEdit(macro);
    }
    
    // # Private
    
    private void switchScreenToDefault()
    {
        switchScreenToOpen();
    }
    
    private void switchScreenToOpen()
    {
        boolean playViewControllerStart = _openMacroViewController == null;
        
        if (playViewControllerStart)
        {
            _openMacroPresenter = new OpenMacroPresenterStandard(this);
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
            _recordMacroPresenter = new RecordMacroPresenter.Impl();
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
    
    private void switchScreenToPlay(@NotNull Macro macro)
    {
        var presenter = new PlayMacroPresenter.Impl(macro);
        var vc = new PlayMacroViewController(presenter);
        presenter.setDelegate(vc);
        
        if (_currentViewController != null)
        {
            _currentViewController.suspend();
        }
        
        _currentViewController = vc;
        
        _currentViewController.start();
        
        presenter.start();
    }
    
    private void switchScreenToEdit(@NotNull Macro macro)
    {
        EditMacroPresenter presenter = new EditMacroPresenterStandard(this, macro);
        EditMacroViewController vc = new EditMacroViewController(presenter);
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
