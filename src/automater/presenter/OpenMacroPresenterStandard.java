/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.mvp.BasePresenter.OpenMacroPresenter;
import automater.mvp.BasePresenterDelegate.OpenMacroPresenterDelegate;
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
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the open macro screen.
 *
 * @author Bytevi
 */
public class OpenMacroPresenterStandard implements OpenMacroPresenter {
    @NotNull private final RootViewController _rootViewController;
    @NotNull private OpenMacroPresenterDelegate _delegate;
    
    @NotNull private final MacroStorage _macrosStorage = GeneralStorage.getDefault().getMacrosStorage();
    
    @NotNull private List<Macro> _macros = new ArrayList<>();
    @NotNull private final List<Description> _macrosAsDescriptions = new ArrayList<>();
    
    public OpenMacroPresenterStandard(@NotNull RootViewController rootViewController)
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
    public void setDelegate(@NotNull OpenMacroPresenterDelegate delegate)
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
    
    // # OpenMacroPresenter
    
    @Override
    public void onSwitchToRecordScreen()
    {
        _rootViewController.navigateToRecordScreen();
    }
    
    @Override
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
    
    @Override
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
    
    @Override
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
        final OpenMacroPresenterStandard presenter = this;
        
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
    
    private void setMacroData(@NotNull List<Macro> data)
    {
        _macros = data;
        
        _macrosAsDescriptions.clear();
        _macrosAsDescriptions.addAll(data);
    }
}
