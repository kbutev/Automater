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
import automater.utilities.Looper;
import automater.utilities.LooperSwing;
import automater.utilities.SimpleCallback;
import automater.work.model.Macro;
import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the open macro screen.
 *
 * @author Bytevi
 */
public class OpenMacroPresenter implements BasePresenter {
    private final RootViewController _rootViewController;
    private BasePresenterDelegate _delegate;
    
    private final MacroStorage _macrosStorage = GeneralStorage.getDefault().getMacrosStorage();
    
    private List<Macro> _macros = new ArrayList<>();
    private final List<Description> _macrosAsDescriptions = new ArrayList<>();
    
    public OpenMacroPresenter(RootViewController rootViewController)
    {
        _rootViewController = rootViewController;
    }
    
    // # BasePresenter
    
    @Override
    public void start()
    {
        if (_delegate == null)
        {
            Errors.throwInternalLogicError("OpenMacroPresenter delegate is not set before starting");
        }
        
        Logger.message(this, "Start.");
        
        updateMacroData();
    }
    
    @Override
    public void setDelegate(BasePresenterDelegate delegate)
    {
        if (_delegate != null)
        {
            Errors.throwInternalLogicError("OpenMacroPresenter delegate is already set");
        }
        
        _delegate = delegate;
    }
    
    @Override
    public void requestDataUpdate()
    {
        Logger.message(this, "Data update requested.");
        
        updateMacroData();
    }
    
    // # Public functionality
    
    public void onSwitchToRecordScreen()
    {
        _rootViewController.navigateToRecordScreen();
    }
    
    public void openMacroAt(int index)
    {
        Logger.messageEvent(this, "Open macro at " + String.valueOf(index));
        
        if (index < 0 || index >= _macros.size())
        {
            Logger.error(this, "Cannot open macro at " + String.valueOf(index) + ", invalid index");
            return;
        }
        
        Macro macro = _macros.get(index);
        
        _rootViewController.navigateToPlayScreen(macro);
    }
    
    public void editMacroAt(int index)
    {
        Logger.messageEvent(this, "Edit macro at " + String.valueOf(index));
        
        if (index < 0 || index >= _macros.size())
        {
            Logger.error(this, "Cannot open macro at " + String.valueOf(index) + ", invalid index");
            return;
        }
        
        Macro macro = _macros.get(index);
        
        _rootViewController.navigateToEditScreen(macro);
    }
    
    public void deleteMacroAt(int index)
    {
        Logger.messageEvent(this, "Delete macro at " + String.valueOf(index));
        
        if (index < 0 || index >= _macros.size())
        {
            Logger.error(this, "Cannot delete macro at " + String.valueOf(index) + ", invalid index");
            return;
        }
        
        Macro macro = _macros.get(index);
        
        try {
            _macrosStorage.deleteMacro(macro);
        } catch (Exception e) {
            
        }
        
        updateMacroData();
    }
    
    // # Private
    
    private void updateMacroData()
    {
        final OpenMacroPresenter presenter = this;
        
        Looper.getShared().performAsyncCallbackInBackground(new SimpleCallback() {
            @Override
            public void perform() {
                final List<Macro> macros = _macrosStorage.getMacros();
                
                LooperSwing.getShared().performCallback(new SimpleCallback() {
                    @Override
                    public void perform() {
                        Logger.message(this, "Performing data update: " + macros.size() + " macros");
                        
                        presenter.setMacroData(macros);
                        
                        _delegate.onLoadedMacrosFromStorage(presenter._macrosAsDescriptions);
                    }
                });
            }
        });
    }
    
    private void setMacroData(List<Macro> data)
    {
        _macros = data;
        
        _macrosAsDescriptions.clear();
        _macrosAsDescriptions.addAll(data);
    }
}
