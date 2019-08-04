/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.presenter;

import automater.ui.viewcontroller.RootViewController;
import automater.utilities.Errors;
import automater.ui.view.BaseView;

/**
 *
 * @author Bytevi
 */
public class PlayPresenter implements BasePresenter {
    private final RootViewController _rootViewController;
    private BasePresenterDelegate _delegate;
    
    public PlayPresenter(RootViewController rootViewController)
    {
        _rootViewController = rootViewController;
    }
    
    @Override
    public void start()
    {
        if (_delegate == null)
        {
            Errors.throwInternalLogicError("PlayPresenter delegate is not set before starting");
        }
    }
    
    @Override
    public void setDelegate(BasePresenterDelegate delegate)
    {
        if (_delegate != null)
        {
            Errors.throwInternalLogicError("PlayPresenter delegate is already set");
        }
        
        _delegate = delegate;
    }
    
    public void onSwitchToRecordScreen()
    {
        _rootViewController.navigateToRecordScreen();
    }
}
