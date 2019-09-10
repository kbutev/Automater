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
import automater.utilities.Callback;
import automater.utilities.CollectionUtilities;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.work.BaseAction;
import automater.work.model.Macro;
import java.util.List;
import automater.work.model.BaseEditableAction;
import automater.work.model.StandartEditableAction;
import automater.work.model.StandartEditableActionConstants;
import java.util.ArrayList;

/**
 *
 * @author Bytevi
 */
public class EditMacroPresenter implements BasePresenter, RecorderHotkeyListener {
    private final RootViewController _rootViewController;
    private BasePresenterDelegate _delegate;
    
    private Macro _macro;
    private ArrayList<BaseAction> _macroActions;
    private ArrayList<Description> _macroActionDescriptions;
    
    private int _actionBeingEditedIndex;
    private BaseAction _actionBeingEdited;
    private BaseEditableAction _actionEditedValues;
    
    private boolean _recording = false;
    private final Recorder _recorder = Recorder.getDefault();
    private Callback _onKeystrokeEnteredCallback = Callback.createDoNothing();
    private Hotkey _hotkeyRecorded;
    
    public EditMacroPresenter(RootViewController rootViewController, Macro macro)
    {
        _rootViewController = rootViewController;
        
        _macro = macro;
        _macroActions = new ArrayList<>();
        _macroActions.addAll(macro.actions);
        _macroActionDescriptions = new ArrayList<>();
        _macroActionDescriptions.addAll(macro.actionDescriptions);
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
        
        updateDelegateWithMacroInfo();
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
        if (!_recording)
        {
            return;
        }
        
        Logger.message(this, "Hotkey pressed while listening for key strokes - " + hotkey.key.toString());
        
        _hotkeyRecorded = hotkey;
        
        endListeningForKeystrokes();
    }
    
    // # Public values
    
    public List<Description> getActionTypes()
    {
        return StandartEditableActionConstants.getActionTypes();
    }
    
    public int getActionTypeSelectedIndex()
    {
        return StandartEditableActionConstants.getActionTypeSelectedIndex(_actionBeingEdited);
    }
    
    // # Public operations
    
    public void navigateBack()
    {
        Logger.messageEvent(this, "Navigate back.");
        
        if (_recording)
        {
            endListeningForKeystrokes();
        }
        
        _rootViewController.navigateToOpenScreen();
    }
    
    public void onStartEditMacroActionAt(int index)
    {
        if (index < 0 || index >= _macroActions.size())
        {
            return;
        }
        
        _actionBeingEditedIndex = index;
        
        _actionBeingEdited = _macroActions.get(_actionBeingEditedIndex);
        
        _actionEditedValues = StandartEditableAction.create(_actionBeingEdited);
        
        Logger.messageEvent(this, "Start editing macro action '" + _actionBeingEdited.toString() + "' at index " + String.valueOf(index) + "");
        
        _delegate.onEditMacroAction(_actionEditedValues);
    }
    
    public Exception canSuccessfullyEndEditMacroAction()
    {
        if (_actionEditedValues == null)
        {
            return null;
        }
        
        try {
            _actionEditedValues.buildAction();
            return null;
        } catch (Exception e) {
            return e;
        }
    }
    
    public void onEndEditMacroAction()
    {
        if (_actionEditedValues == null)
        {
            return;
        }
        
        Logger.messageEvent(this, "Ending editing action, save it.");
        
        if (_recording)
        {
            endListeningForKeystrokes();
        }
        
        BaseEditableAction a = _actionEditedValues;
        int actionBeingEditedIndex = _actionBeingEditedIndex;
        
        _actionBeingEditedIndex = 0;
        _actionBeingEdited = null;
        _actionEditedValues = null;
        
        updateMacroWithEditedAction(a, actionBeingEditedIndex);
        
        updateDelegateWithMacroInfo();
    }
    
    public void startListeningForKeystrokes(Callback<Hotkey> onKeystrokeEnteredCallback)
    {
        if (_recording)
        {
            return;
        }
        
        _recording = true;
        
        Logger.message(this, "Start listening for key strokes");
        
        _hotkeyRecorded = null;
        
        _onKeystrokeEnteredCallback = onKeystrokeEnteredCallback;
        
        _recorder.registerHotkeyListener(this);
    }
    
    public void endListeningForKeystrokes()
    {
        if (!_recording)
        {
            return;
        }
        
        _recording = false;
        
        Logger.message(this, "Stop listening for key strokes");
        
        _onKeystrokeEnteredCallback.perform(_hotkeyRecorded);
        
        _recorder.unregisterHotkeyListener(this);
    }
    
    // # Private
    
    private void updateDelegateWithMacroInfo()
    {
        _delegate.onLoadedMacroFromStorage(_macro.name, _macro.getDescription(), _macroActionDescriptions);
    }
    
    private void updateMacroWithEditedAction(BaseEditableAction a, int actionBeingEditedIndex)
    {
        try {
            BaseAction action = a.buildAction();
            
            _macroActions.set(actionBeingEditedIndex, action);
            _macroActionDescriptions.set(actionBeingEditedIndex, action.getDescription());
            
            Logger.message(this, _macroActionDescriptions.toString());
        } 
        catch (Exception e)
        {
            
        }
    }
}
