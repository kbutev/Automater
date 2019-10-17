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
import automater.work.BaseAction;
import automater.work.model.Macro;
import java.util.List;
import automater.mutableaction.StandardMutableAction;
import automater.mutableaction.StandardMutableActionTemplates;
import automater.mutableaction.StandardMutableActionConstants;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import automater.mutableaction.BaseMutableAction;
import automater.work.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Presenter for the edit macro screen.
 *
 * @author Bytevi
 */
public class EditMacroPresenter implements BasePresenter, RecorderHotkeyListener {
    public final static int CREATE_ACTION_DEFAULT_TYPE = 0;
    public final static int CREATE_NEW_ACTION_TIMESTAMP_OFFSET = 1;
    
    @NotNull private final RootViewController _rootViewController;
    @Nullable private BasePresenterDelegate _delegate;
    
    @NotNull private final Macro _originalMacro;
    
    // New values
    @NotNull private String _name;
    @NotNull private String _description;
    @NotNull private final ArrayList<BaseAction> _macroActions;
    @NotNull private final ArrayList<Description> _macroActionDescriptions;
    
    // Edit action
    private boolean _isEditingOrCreatingAction = false;
    private boolean _isCreatingAction = false;
    
    private int _actionBeingEditedIndex;
    private int _actionTypeSelectedIndex = 0;
    @Nullable private StandardMutableAction _actionBeingEdited;
    
    // Hotkey recording
    private boolean _recording = false;
    @NotNull private final Recorder _recorder = Recorder.getDefault();
    @NotNull private Callback _onKeystrokeEnteredCallback = Callback.createDoNothing();
    @Nullable private Hotkey _hotkeyRecorded;
    
    // Edit detection
    private boolean _wasEdited = false;
    
    public EditMacroPresenter(@NotNull RootViewController rootViewController, @NotNull Macro macro)
    {
        _rootViewController = rootViewController;
        
        _originalMacro = macro;
        
        _name = macro.getName();
        _description = macro.getDescription();
        
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
        
        _delegate.onLoadedMacroFromStorage(_originalMacro.name, _originalMacro.getDescription(), _macroActionDescriptions);
        
        _wasEdited = false;
    }

    @Override
    public void setDelegate(@NotNull BasePresenterDelegate delegate)
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
    public @Nullable Hotkey getHotkey()
    {
        return null;
    }
    
    @Override
    public void onHotkeyPressed(@NotNull Hotkey hotkey)
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
    
    public @NotNull List<Description> getActionTypes()
    {
        return StandardMutableActionConstants.getActionTypes();
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
    
    public void onCloseMacroWithoutSaving()
    {
        Logger.messageEvent(this, "Close without saving changes.");
        
        if (wasEdited())
        {
            _delegate.onClosingMacroWithoutSavingChanges();
            return;
        }
        
        navigateBack();
    }
    
    public void onSaveMacro()
    {
        Logger.messageEvent(this, "Save macro and go back.");
        
        String name = _name;
        String description = _description;
        
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
        if (index < 0 || index >= _macroActions.size())
        {
            Errors.throwInvalidArgument("Invalid index given to Edit macro presenter to create a new action");
            return;
        }
        
        if (_isEditingOrCreatingAction)
        {
            Errors.throwIllegalStateError("Already creating/editing a macro.");
            return;
        }
        
        long timestamp;
        
        if (index < _macroActions.size())
        {
            timestamp = _macroActions.get(index).getPerformTime();
        }
        else
        {
            timestamp = _macroActions.get(_macroActions.size()-1).getPerformTime();
        }
        
        timestamp += CREATE_NEW_ACTION_TIMESTAMP_OFFSET;
        
        _isEditingOrCreatingAction = true;
        _isCreatingAction = true;
        
        _actionBeingEditedIndex = index;
        _actionTypeSelectedIndex = CREATE_ACTION_DEFAULT_TYPE;
        
        _actionBeingEdited = StandardMutableActionTemplates.buildTemplateFromTypeIndex(_actionTypeSelectedIndex, timestamp);
        
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
            Errors.throwInvalidArgument("Invalid index given to Edit macro presenter to edit action");
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
        _actionTypeSelectedIndex = StandardMutableActionConstants.getActionTypeSelectedIndex(action);
        
        _actionBeingEdited = StandardMutableAction.createFromAction(action);
        
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
    
    public @Nullable Exception canSuccessfullyEndEditMacroAction()
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
        
        StandardMutableAction a = _actionBeingEdited;
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
            updateMacroWithNewCreatedAction(a);
            Logger.messageEvent(this, "Ending creating new action, insert it at " + String.valueOf(actionBeingEditedIndex));
        }
        else
        {
            updateMacroWithEditedAction(a, actionBeingEditedIndex);
            Logger.messageEvent(this, "Ending editing action, save it.");
        }
        
        _delegate.onEditedMacroActions(_macroActionDescriptions);
        
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
        
        _wasEdited = true;
        
        BaseAction action = _macroActions.get(_actionBeingEditedIndex);
        
        Logger.messageEvent(this, "Delete macro action '" + action.toString() + "' at index " + String.valueOf(index) + "");
        
        _macroActions.remove(index);
        _macroActionDescriptions.remove(index);
        
        // Check if no actions are left
        // So that we can add a do nothing action always to prevent empty actions
        if (_macroActions.isEmpty())
        {
            BaseAction doNothing = Action.createDoNothing(0);
            _macroActions.add(doNothing);
            _macroActionDescriptions.add(doNothing);
        }
        
        // Update delegate
        _delegate.onEditedMacroActions(_macroActionDescriptions);
    }
    
    public @Nullable BaseMutableAction changeEditMacroActionTypeForTypeIndex(int index)
    {
        if (!_isEditingOrCreatingAction)
        {
            Errors.throwIllegalStateError("Cannot change macro type without already creating/editing a macro.");
            return null;
        }
        
        long timestamp = _actionBeingEdited.getTimestamp();
        
        StandardMutableAction a;
        a = StandardMutableActionTemplates.buildTemplateFromTypeIndex(index, timestamp);
        
        if (a == null)
        {
            Errors.throwInvalidArgument("Failed to change edit macro action, invalid type index " + index);
            return null;
        }
        
        Description description = a.getDescription();
        
         Logger.messageEvent(this, "Change macro action type to " + description.getName());
        
        _actionTypeSelectedIndex = index;
        
        _actionBeingEdited = a;
        
        return a;
    }
    
    public void startListeningForKeystrokes(@NotNull Callback<Hotkey> onKeystrokeEnteredCallback)
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
    
    public void onMacroNameChanged(@NotNull String name)
    {
        _wasEdited = true;
        
        _name = name;
    }
    
    public void onMacroDescriptionChanged(@NotNull String description)
    {
        _wasEdited = true;
        
        _description = description;
    }
    
    // # Private
    
    private boolean wasEdited()
    {
        return _wasEdited;
    }
    
    private void updateMacroWithNewCreatedAction(@NotNull StandardMutableAction a)
    {
        _wasEdited = true;
        
        try {
            BaseAction action = a.buildAction();
            
            _macroActions.add(action);
            _macroActionDescriptions.add(action);
            
            sortActions();
        } 
        catch (Exception e)
        {
            Logger.error(this, "Failed to update macro with new created action: " + e.toString());
        }
    }
    
    private void updateMacroWithEditedAction(@NotNull StandardMutableAction a, int actionBeingEditedIndex)
    {
        _wasEdited = true;
        
        try {
            BaseAction action = a.buildAction();
            
            long originalTime = _macroActions.get(actionBeingEditedIndex).getPerformTime();
            long newTime = a.getTimestamp();
            
            _macroActions.set(actionBeingEditedIndex, action);
            _macroActionDescriptions.set(actionBeingEditedIndex, action);
            
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
