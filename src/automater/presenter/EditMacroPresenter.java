/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.presenter;

import automater.recorder.Recorder;
import automater.recorder.RecorderHotkeyListener;
import automater.settings.Hotkey;
import automater.storage.GeneralStorage;
import automater.storage.MacroStorage;
import automater.ui.viewcontroller.RootViewController;
import automater.utilities.Callback;
import automater.utilities.Description;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.work.Action;
import automater.work.BaseAction;
import automater.work.model.Macro;
import java.util.List;
import automater.work.model.BaseEditableAction;
import automater.work.model.StandartEditableAction;
import automater.work.model.StandartEditableActionTemplates;
import automater.work.model.StandartEditableActionConstants;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author Bytevi
 */
public class EditMacroPresenter implements BasePresenter, RecorderHotkeyListener {
    public final static int CREATE_ACTION_DEFAULT_TYPE_VALUE = 0;
    
    private final RootViewController _rootViewController;
    private BasePresenterDelegate _delegate;
    
    private final Macro _originalMacro;
    private ArrayList<BaseAction> _macroActions;
    private ArrayList<Description> _macroActionDescriptions;
    
    private boolean _isEditingOrCreatingAction = false;
    private boolean _isCreatingAction = false;
    
    private int _actionBeingEditedIndex;
    private int _actionTypeSelectedIndex = 0;
    private BaseEditableAction _actionBeingEdited;
    
    private boolean _recording = false;
    private final Recorder _recorder = Recorder.getDefault();
    private Callback _onKeystrokeEnteredCallback = Callback.createDoNothing();
    private Hotkey _hotkeyRecorded;
    
    public EditMacroPresenter(RootViewController rootViewController, Macro macro)
    {
        _rootViewController = rootViewController;
        
        _originalMacro = macro;
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
        return _actionTypeSelectedIndex;
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
    
    public void onSaveMacro(String name, String description)
    {
        Logger.messageEvent(this, "Save macro and go back.");
        
        MacroStorage macroStorage = GeneralStorage.getDefault().getMacrosStorage();
        
        Macro macro = new Macro(name, _macroActions, new Date(), _originalMacro.getLastTimePlayedDate(), _originalMacro.screenSize);
        macro.setDescription(description);
        
        boolean nameChanged = !_originalMacro.name.equals(name);
        
        if (nameChanged)
        {
            Exception exception = macroStorage.getSaveMacroNameError(name, nameChanged);
            
            if (exception != null)
            {
                Logger.error(this, "Failed to save macro: " + exception.toString());
                _delegate.onErrorEncountered(exception);
                return;
            }
        }
        
        Exception exception = macroStorage.getSaveMacroError(macro);
        
        if (exception != null)
        {
            Logger.error(this, "Failed to save macro: " + exception.toString());
            _delegate.onErrorEncountered(exception);
            return;
        }
        
        try {
            if (!nameChanged)
            {
                macroStorage.updateMacroInStorage(macro);
                Logger.messageEvent(this, "Successfully updated macro '" + macro.name + "'!");
            }
            else
            {
                macroStorage.saveMacroToStorage(macro);
                Logger.messageEvent(this, "Successfully saved new macro '" + macro.name + "'! Old macro will be deleted.");
            }
        } catch (Exception e) {
            Logger.error(this, "Failed to save macro: " + e.toString());
            _delegate.onErrorEncountered(e);
            return;
        }
        
        // Delete old macro
        if (nameChanged)
        {
            try {
                macroStorage.deleteMacro(_originalMacro);
            } catch (Exception e) {
                
            }
        }
        
        navigateBack();
    }
    
    public void onStartCreatingMacroActionAt(int index)
    {
        if (index < 0 || index > _macroActions.size())
        {
            return;
        }
        
        if (_isEditingOrCreatingAction)
        {
            Errors.throwIllegalStateError("Already creating/editing a macro.");
            return;
        }
        
        long timestamp = 0;
        
        if (index < _macroActions.size())
        {
            timestamp = _macroActions.get(index).getPerformTime();
        }
        else
        {
            timestamp = _macroActions.get(_macroActions.size()-1).getPerformTime();
        }
        
        _isEditingOrCreatingAction = true;
        _isCreatingAction = true;
        
        _actionBeingEditedIndex = index;
        _actionTypeSelectedIndex = CREATE_ACTION_DEFAULT_TYPE_VALUE;
        
        _actionBeingEdited = StandartEditableActionTemplates.buildTemplateFromTypeIndex(_actionTypeSelectedIndex, timestamp);
        
        Logger.messageEvent(this, "Start creating new macro action at index " + String.valueOf(index) + "");
        
        if (_actionBeingEdited == null)
        {
            _isEditingOrCreatingAction = false;
            _isCreatingAction = false;
            _actionBeingEditedIndex = 0;
            
            Exception e = new Exception("Failed to create macro action at index " + index + ", cannot recognize action type");
            _delegate.onErrorEncountered(e);
            return;
        }
        
        _delegate.onCreateMacroAction(_actionBeingEdited);
    }
    
    public void onStartEditMacroActionAt(int index)
    {
        if (index < 0 || index >= _macroActions.size())
        {
            return;
        }
        
        if (_isEditingOrCreatingAction)
        {
            Errors.throwIllegalStateError("Already creating/editing a macro.");
            return;
        }
        
        _isEditingOrCreatingAction = true;
        _isCreatingAction = false;
        
        BaseAction action = _macroActions.get(index);
        _actionBeingEditedIndex = index;
        _actionTypeSelectedIndex = StandartEditableActionConstants.getActionTypeSelectedIndex(action);
        
        _actionBeingEdited = StandartEditableAction.create(action);
        
        Logger.messageEvent(this, "Start editing macro action '" + action.toString() + "' at index " + String.valueOf(index) + "");
        
        if (_actionBeingEdited == null)
        {
            _isEditingOrCreatingAction = false;
            _isCreatingAction = false;
            _actionBeingEditedIndex = 0;
            
            Exception e = new Exception("Failed to edit macro action at index " + index + ", cannot recognize action type");
            _delegate.onErrorEncountered(e);
            return;
        }
        
        _delegate.onCreateMacroAction(_actionBeingEdited);
    }
    
    public Exception canSuccessfullyEndEditMacroAction()
    {
        if (!_isEditingOrCreatingAction)
        {
            return null;
        }
        
        try {
            _actionBeingEdited.buildAction();
            return null;
        } catch (Exception e) {
            return e;
        }
    }
    
    public void onEndCreateOrEditMacroAction(boolean save)
    {
        if (!_isEditingOrCreatingAction)
        {
            return;
        }
        
        boolean isCreatingAction = _isCreatingAction;
        
        _isEditingOrCreatingAction = false;
        _isCreatingAction = false;
        
        BaseEditableAction a = _actionBeingEdited;
        int actionBeingEditedIndex = _actionBeingEditedIndex;
        
        _actionBeingEditedIndex = 0;
        _actionBeingEdited = null;
        
        if (_recording)
        {
            Logger.warning(this, "Trying to finish creating/editing a macro without ending the keystrokes listener first!");
            endListeningForKeystrokes();
        }
        
        if (!save)
        {
            Logger.messageEvent(this, "Cancel creating/editing action.");
            return;
        }
        
        if (isCreatingAction)
        {
            updateMacroWithNewCreatedAction(a, actionBeingEditedIndex);
            Logger.messageEvent(this, "Ending creating new action, insert it at " + String.valueOf(actionBeingEditedIndex));
        }
        else
        {
            updateMacroWithEditedAction(a, actionBeingEditedIndex);
            Logger.messageEvent(this, "Ending editing action, save it.");
        }
        
        updateDelegateWithMacroInfo();
        
        _delegate.onCreateMacroAction(a);
    }
    
    public void onDeleteMacroActionAt(int index)
    {
        if (index < 0 || index >= _macroActions.size())
        {
            return;
        }
        
        if (_isEditingOrCreatingAction)
        {
            Errors.throwIllegalStateError("Cannot delete macro while creating/editing a macro.");
            return;
        }
        
        BaseAction action = _macroActions.get(_actionBeingEditedIndex);
        
        Logger.messageEvent(this, "Delete macro action '" + action.toString() + "' at index " + String.valueOf(index) + "");
        
        _macroActions.remove(index);
        _macroActionDescriptions.remove(index);
        
        updateDelegateWithMacroInfo();
    }
    
    public BaseEditableAction changeEditMacroActionTypeForTypeIndex(int index)
    {
        if (!_isEditingOrCreatingAction)
        {
            Errors.throwIllegalStateError("Cannot change macro type without already creating/editing a macro.");
            return null;
        }
        
        long timestamp = _actionBeingEdited.getTimestamp();
        
        BaseEditableAction a;
        a = StandartEditableActionTemplates.buildTemplateFromTypeIndex(index, timestamp);
        
        if (a == null)
        {
            Errors.throwInvalidArgument("Failed to change edit macro action, invalid type index " + index);
            return null;
        }
        
        Description description = a.getDescription();
        
        if (description != null)
        {
            Logger.messageEvent(this, "Change macro action type to " + description.getName());
        }
        else
        {
            Logger.messageEvent(this, "Change macro action type");
        }
        
        _actionTypeSelectedIndex = index;
        
        _actionBeingEdited = a;
        
        return a;
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
        _delegate.onLoadedMacroFromStorage(_originalMacro.name, _originalMacro.getDescription(), _macroActionDescriptions);
    }
    
    private void updateMacroWithNewCreatedAction(BaseEditableAction a, int actionBeingEditedIndex)
    {
        try {
            BaseAction action = a.buildAction();
            
            _macroActions.add(actionBeingEditedIndex, action);
            _macroActionDescriptions.add(actionBeingEditedIndex, action.getDescription());
            
            sortActions();
        } 
        catch (Exception e)
        {
            Logger.error(this, "Failed to update macro with new created action: " + e.toString());
        }
    }
    
    private void updateMacroWithEditedAction(BaseEditableAction a, int actionBeingEditedIndex)
    {
        try {
            BaseAction action = a.buildAction();
            
            long originalTime = _originalMacro.actions.get(actionBeingEditedIndex).getPerformTime();
            long newTime = a.getTimestamp();
            
            _macroActions.set(actionBeingEditedIndex, action);
            _macroActionDescriptions.set(actionBeingEditedIndex, action.getDescription());
            
            if (originalTime != newTime)
            {
                Logger.message(this, "Action's timestamp was edited, must fully sort the actions array by time");
                sortActions();
            }
        } 
        catch (Exception e)
        {
            Logger.error(this, "Failed to update macro with edited action: " + e.toString());
        }
    }
    
    private void sortActions()
    {
        Comparator comparator = new Comparator<BaseAction>() {
            @Override
            public int compare(BaseAction a, BaseAction b) {
                int result = (int)(a.getPerformTime() - b.getPerformTime());
                return result;
        }};
        
        _macroActions.sort(comparator);
        _macroActionDescriptions.sort(comparator);
    }
}
