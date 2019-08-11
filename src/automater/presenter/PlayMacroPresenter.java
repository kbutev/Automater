/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.presenter;

import automater.storage.GeneralStorage;
import automater.storage.MacroStorage;
import automater.ui.viewcontroller.RootViewController;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.work.Macro;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bytevi
 */
public class PlayMacroPresenter implements BasePresenter {
    private final RootViewController _rootViewController;
    private BasePresenterDelegate _delegate;
    
    private final Macro _macro;
    private List<Description> _macroActions;
    
    private MacroStorage _macrosStorage = GeneralStorage.getDefault().getMacrosStorage();
    
    public PlayMacroPresenter(RootViewController rootViewController, Macro macro)
    {
        _rootViewController = rootViewController;
        _macro = macro;
        _macroActions = macro.r.getUserInputAsDescriptions();
    }
    
    // # BasePresenter
    
    @Override
    public void start()
    {
        if (_delegate == null)
        {
            Errors.throwInternalLogicError("RecordPresenter delegate is not set before starting");
        }
        
        Logger.message(this, "Start.");
        
        _delegate.onLoadedMacroFromStorage(_macro.name, _macro.getDescription(), _macroActions);
    }
    
    @Override
    public void setDelegate(BasePresenterDelegate delegate)
    {
        if (_delegate != null)
        {
            Errors.throwInternalLogicError("RecordPresenter delegate is already set");
        }
        
        _delegate = delegate;
    }
    
    @Override
    public void requestDataUpdate()
    {
        
    }
    
    // # Public
    
    public void play()
    {
        Logger.messageEvent(this, "Play.");
        
        _macro.incrementNumberOfTimesPlayed();
        
        try {
            _macrosStorage.updateMacroInStorage(_macro);
        } catch (Exception e) {
            Logger.error(this, "Failed to update macro in storage: " + e.toString());
        }
    }
    
    public void stop()
    {
        Logger.messageEvent(this, "Stop.");
    }
    
    public void setOptions()
    {
        
    }
    
    public void resetOptions()
    {
        
    }
    
    public void navigateBack()
    {
        Logger.messageEvent(this, "Navigate back.");
        
        _rootViewController.navigateToOpenScreen();
    }
}
