/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.presenter;

import automater.recorder.Recorder;
import automater.recorder.RecorderHotkeyListener;
import automater.settings.Hotkey;
import automater.ui.viewcontroller.RootViewController;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.work.BaseAction;
import automater.work.model.Macro;
import java.util.List;
import automater.work.model.BaseEditableAction;
import automater.work.model.StandartEditableAction;
import automater.work.model.StandartEditableActionConstants;

/**
 *
 * @author Bytevi
 */
public class EditMacroPresenter implements BasePresenter, RecorderHotkeyListener {
    private final RootViewController _rootViewController;
    private BasePresenterDelegate _delegate;
    
    private final Macro _macro;
    private final List<BaseAction> _macroActions;
    private final List<Description> _macroActionDescriptions;
    
    private BaseAction _actionBeingEdited;
    private BaseEditableAction _actionEditedValues;
    
    private final Recorder _recorder = Recorder.getDefault();
    
    public EditMacroPresenter(RootViewController rootViewController, Macro macro)
    {
        _rootViewController = rootViewController;
        
        _macro = macro;
        _macroActions = _macro.actions;
        _macroActionDescriptions = macro.actionDescriptions;
    }
    
    // # BasePresenter

    @Override
    public void start() 
    {
        if (_delegate == null)
        {
            Errors.throwInternalLogicError("EditMacroPresenter delegate is not set before starting");
        }
        
        Logger.message(this, "Start.");
        
        _delegate.onLoadedMacroFromStorage(_macro.name, _macro.getDescription(), _macroActionDescriptions);
    }

    @Override
    public void setDelegate(BasePresenterDelegate delegate)
    {
        if (_delegate != null)
        {
            Errors.throwInternalLogicError("EditMacroPresenter delegate is already set");
        }
        
        _delegate = delegate;
    }

    @Override
    public void requestDataUpdate() 
    {
        
    }
    
    // # RecorderHotkeyListener
    
    @Override
    public boolean isListeningForAnyHotkey()
    {
        return true;
    }
    
    @Override
    public Hotkey getHotkey()
    {
        return null;
    }
    
    @Override
    public void onHotkeyPressed(Hotkey hotkey)
    {
        
    }
    
    // # Public
    
    public void navigateBack()
    {
        Logger.messageEvent(this, "Navigate back.");
        
        _rootViewController.navigateToOpenScreen();
    }
    
    public void onEditMacroAt(int index)
    {
        if (index < 0 || index >= _macroActions.size())
        {
            return;
        }
        
        _actionBeingEdited = _macroActions.get(index);
        
        _actionEditedValues = StandartEditableAction.create(_actionBeingEdited);
        
        Logger.messageEvent(this, "Edit macro '" + _actionBeingEdited.toString() + "' at index " + String.valueOf(index) + "");
        
        _delegate.onEditMacroAction(_actionEditedValues);
    }
    
    public List<Description> getActionTypes()
    {
        return StandartEditableActionConstants.getActionTypes();
    }
    
    public int getActionTypeSelectedIndex()
    {
        return StandartEditableActionConstants.getActionTypeSelectedIndex(_actionBeingEdited);
    }
    
    public void startListeningForHotkey()
    {
        //_recorder.registerHotkeyListener(this);
    }
    
    public void stopListeningForHotkey()
    {
        
    }
}
