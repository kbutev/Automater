/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.di.DI;
import automater.model.KeyEventKind;
import automater.model.KeyValue;
import automater.model.Keystroke;
import automater.mvp.BasePresenter.EditMacroPresenter;
import automater.settings.Hotkey;
import automater.storage.GeneralStorage;
import automater.storage.LegacyMacroStorage;
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
import automater.mvp.BasePresenterDelegate.EditMacroPresenterDelegate;
import automater.service.HotkeyMonitor;
import java.util.ArrayList;
import java.util.Comparator;
import automater.work.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Presenter for the edit macro screen.
 *
 * @author Bytevi
 */
public class EditMacroPresenterStandard implements EditMacroPresenter, HotkeyMonitor.Listener {

    public final static int CREATE_ACTION_DEFAULT_TYPE = 0;
    public final static int CREATE_NEW_ACTION_TIMESTAMP_OFFSET = 1;

    private final GeneralStorage.Protocol storage = DI.get(GeneralStorage.Protocol.class);

    @NotNull private final RootViewController _rootViewController;
    @Nullable private EditMacroPresenterDelegate _delegate;

    private final HotkeyMonitor.Protocol hotkeyMonitor;

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
    @NotNull private Callback _onKeystrokeEnteredCallback = Callback.createDoNothing();
    @Nullable private Hotkey _hotkeyRecorded;

    // Edit detection
    private boolean _wasEdited = false;

    public EditMacroPresenterStandard(@NotNull RootViewController rootViewController, @NotNull Macro macro) {
        _rootViewController = rootViewController;

        _originalMacro = macro;

        _name = macro.getName();
        _description = macro.getDescription();

        _macroActions = new ArrayList<>();
        _macroActions.addAll(macro.actions);
        _macroActionDescriptions = new ArrayList<>();
        _macroActionDescriptions.addAll(macro.actionDescriptions);

        hotkeyMonitor = HotkeyMonitor.build(Keystroke.build(KeyValue._F4));
        hotkeyMonitor.setName("Play/stop");
    }

    // # BasePresenter
    @Override
    public void start() {
        if (_delegate == null) {
            throw Errors.internalLogicError();
        }

        Logger.message(this, "Start.");

        _delegate.onLoadedMacroFromStorage(_originalMacro.name, _originalMacro.getDescription(), _macroActionDescriptions);

        _wasEdited = false;
    }

    @Override
    public void setDelegate(@NotNull EditMacroPresenterDelegate delegate) {
        if (_delegate != null) {
            throw Errors.internalLogicError();
        }

        _delegate = delegate;
    }

    @Override
    public void requestDataUpdate() {

    }

    // # HotkeyMonitor.Listener
    @Override
    public void onHotkeyEvent(@NotNull KeyEventKind kind) {

    }

    // # EditMacroPresenter
    @Override
    public @NotNull
    List<Description> getActionTypes() {
        return StandardMutableActionConstants.getActionTypes();
    }

    @Override
    public int getActionTypeSelectedIndex() {
        return _actionTypeSelectedIndex;
    }

    @Override
    public void navigateBack() {
        Logger.messageEvent(this, "Navigate back.");

        if (_recording) {
            endListeningForKeystrokes();
        }

        _rootViewController.navigateToOpenScreen();
    }

    @Override
    public void onCloseMacroWithoutSaving() {
        Logger.messageEvent(this, "Close without saving changes.");

        if (wasEdited()) {
            _delegate.onClosingMacroWithoutSavingChanges();
            return;
        }

        navigateBack();
    }

    @Override
    public void onSaveMacro() {
        Logger.messageEvent(this, "Save macro and go back.");

        String name = _name;
        String description = _description;

        var macroStorage = new LegacyMacroStorage();

        Macro macro = new Macro(name, _macroActions, _originalMacro.dateCreated, _originalMacro.getLastTimePlayedDate(), _originalMacro.screenSize);
        macro.setNumberOfTimesPlayed(_originalMacro.getNumberOfTimesPlayed());
        macro.setDescription(description);

        boolean nameChanged = !_originalMacro.name.equals(name);

        if (nameChanged) {
            Exception exception = macroStorage.getSaveMacroNameError(name, nameChanged);

            if (exception != null) {
                Logger.error(this, "Failed to save macro: " + exception.toString());
                _delegate.onErrorEncountered(exception);
                return;
            }
        }

        Exception exception = macroStorage.getSaveMacroError(macro);

        if (exception != null) {
            Logger.error(this, "Failed to save macro: " + exception.toString());
            _delegate.onErrorEncountered(exception);
            return;
        }

        try {
            if (!nameChanged) {
                macroStorage.updateMacroInStorage(macro);
                Logger.messageEvent(this, "Successfully updated macro '" + macro.name + "'!");
            } else {
                macroStorage.saveMacroToStorage(macro);
                Logger.messageEvent(this, "Successfully saved new macro '" + macro.name + "'! Old macro will be deleted.");
            }
        } catch (Exception e) {
            Logger.error(this, "Failed to save macro: " + e.toString());
            _delegate.onErrorEncountered(e);
            return;
        }

        // Delete old macro
        if (nameChanged) {
            try {
                macroStorage.deleteMacro(_originalMacro);
            } catch (Exception e) {

            }
        }

        navigateBack();
    }

    @Override
    public void onStartCreatingMacroActionAt(int index) {
        if (index < 0 || index >= _macroActions.size()) {
            throw Errors.invalidArgument("index");
        }

        if (_isEditingOrCreatingAction) {
            throw Errors.illegalStateError();
        }

        long timestamp;

        if (index < _macroActions.size()) {
            timestamp = _macroActions.get(index).getPerformTime();
        } else {
            timestamp = _macroActions.get(_macroActions.size() - 1).getPerformTime();
        }

        timestamp += CREATE_NEW_ACTION_TIMESTAMP_OFFSET;

        _isEditingOrCreatingAction = true;
        _isCreatingAction = true;

        _actionBeingEditedIndex = index;
        _actionTypeSelectedIndex = CREATE_ACTION_DEFAULT_TYPE;

        _actionBeingEdited = StandardMutableActionTemplates.buildTemplateFromTypeIndex(_actionTypeSelectedIndex, timestamp);

        Logger.messageEvent(this, "Start creating new macro action at index " + String.valueOf(index) + "");

        if (_actionBeingEdited == null) {
            _isEditingOrCreatingAction = false;
            _isCreatingAction = false;
            _actionBeingEditedIndex = 0;

            Exception e = new Exception("Failed to create macro action at index " + index + ", cannot recognize action type");
            _delegate.onErrorEncountered(e);
            return;
        }

        _delegate.onBeginCreateMacroAction(_actionBeingEdited);
    }

    @Override
    public void onStartEditMacroActionAt(int index) {
        if (index < 0 || index >= _macroActions.size()) {
            throw Errors.invalidArgument("index");
        }

        if (_isEditingOrCreatingAction) {
            throw Errors.illegalStateError();
        }

        _isEditingOrCreatingAction = true;
        _isCreatingAction = false;

        BaseAction action = _macroActions.get(index);
        _actionBeingEditedIndex = index;
        _actionTypeSelectedIndex = StandardMutableActionConstants.getActionTypeSelectedIndex(action);

        _actionBeingEdited = StandardMutableAction.createFromAction(action);

        Logger.messageEvent(this, "Start editing macro action '" + action.toString() + "' at index " + String.valueOf(index) + "");

        if (_actionBeingEdited == null) {
            _isEditingOrCreatingAction = false;
            _isCreatingAction = false;
            _actionBeingEditedIndex = 0;

            Exception e = new Exception("Failed to edit macro action at index " + index + ", cannot recognize action type");
            _delegate.onErrorEncountered(e);
            return;
        }

        _delegate.onBeginCreateMacroAction(_actionBeingEdited);
    }

    @Override
    public @Nullable
    Exception canSuccessfullyEndEditMacroAction() {
        if (!_isEditingOrCreatingAction) {
            return null;
        }

        try {
            _actionBeingEdited.buildAction();
            return null;
        } catch (Exception e) {
            return e;
        }
    }

    @Override
    public void onEndCreateOrEditMacroAction(boolean save) {
        if (!_isEditingOrCreatingAction) {
            return;
        }

        boolean isCreatingAction = _isCreatingAction;

        _isEditingOrCreatingAction = false;
        _isCreatingAction = false;

        StandardMutableAction a = _actionBeingEdited;
        int actionBeingEditedIndex = _actionBeingEditedIndex;

        _actionBeingEditedIndex = 0;
        _actionBeingEdited = null;

        if (_recording) {
            Logger.warning(this, "Trying to finish creating/editing a macro without ending the keystrokes listener first!");
            endListeningForKeystrokes();
        }

        if (!save) {
            Logger.messageEvent(this, "Cancel creating/editing action.");
            return;
        }

        if (isCreatingAction) {
            updateMacroWithNewCreatedAction(a);
            Logger.messageEvent(this, "Ending creating new action, insert it at " + String.valueOf(actionBeingEditedIndex));
        } else {
            updateMacroWithEditedAction(a, actionBeingEditedIndex);
            Logger.messageEvent(this, "Ending editing action, save it.");
        }

        _delegate.onEditedMacroActions(_macroActionDescriptions);

        _delegate.onBeginCreateMacroAction(a);
    }

    @Override
    public void onDeleteMacroActionAt(int index) {
        if (index < 0 || index >= _macroActions.size()) {
            return;
        }

        if (_isEditingOrCreatingAction) {
            throw Errors.illegalStateError();
        }

        _wasEdited = true;

        BaseAction action = _macroActions.get(_actionBeingEditedIndex);

        Logger.messageEvent(this, "Delete macro action '" + action.toString() + "' at index " + String.valueOf(index) + "");

        _macroActions.remove(index);
        _macroActionDescriptions.remove(index);

        // Check if no actions are left
        // So that we can add a do nothing action always to prevent empty actions
        if (_macroActions.isEmpty()) {
            BaseAction doNothing = Action.createDoNothing(0);
            _macroActions.add(doNothing);
            _macroActionDescriptions.add(doNothing);
        }

        // Update delegate
        _delegate.onEditedMacroActions(_macroActionDescriptions);
    }

    @Override
    public @Nullable
    automater.mutableaction.BaseMutableAction changeEditMacroActionTypeForTypeIndex(int index) {
        if (!_isEditingOrCreatingAction) {
            throw Errors.illegalStateError();
        }

        long timestamp = _actionBeingEdited.getTimestamp();

        StandardMutableAction a;
        a = StandardMutableActionTemplates.buildTemplateFromTypeIndex(index, timestamp);

        if (a == null) {
            throw Errors.invalidArgument("index");
        }

        Description description = a.getDescription();

        Logger.messageEvent(this, "Change macro action type to " + description.getName());

        _actionTypeSelectedIndex = index;

        _actionBeingEdited = a;

        return a;
    }

    @Override
    public void onMacroNameChanged(@NotNull String name) {
        _wasEdited = true;

        _name = name;
    }

    @Override
    public void onMacroDescriptionChanged(@NotNull String description) {
        _wasEdited = true;

        _description = description;
    }

    @Override
    public void startListeningForKeystrokes(@NotNull Callback<automater.settings.Hotkey> onKeystrokeEnteredCallback) {
        if (_recording) {
            return;
        }

        _recording = true;

        Logger.message(this, "Start listening for key strokes");

        _hotkeyRecorded = null;

        _onKeystrokeEnteredCallback = onKeystrokeEnteredCallback;

        // TODO
    }

    @Override
    public void endListeningForKeystrokes() {
        if (!_recording) {
            return;
        }

        _recording = false;

        Logger.message(this, "Stop listening for key strokes");

        _onKeystrokeEnteredCallback.perform(_hotkeyRecorded);

        // TODO
    }

    // # Private
    private boolean wasEdited() {
        return _wasEdited;
    }

    private void updateMacroWithNewCreatedAction(@NotNull StandardMutableAction a) {
        _wasEdited = true;

        try {
            BaseAction action = a.buildAction();

            _macroActions.add(action);
            _macroActionDescriptions.add(action);

            sortActions();
        } catch (Exception e) {
            Logger.error(this, "Failed to update macro with new created action: " + e.toString());
        }
    }

    private void updateMacroWithEditedAction(@NotNull StandardMutableAction a, int actionBeingEditedIndex) {
        _wasEdited = true;

        try {
            BaseAction action = a.buildAction();

            long originalTime = _macroActions.get(actionBeingEditedIndex).getPerformTime();
            long newTime = a.getTimestamp();

            _macroActions.set(actionBeingEditedIndex, action);
            _macroActionDescriptions.set(actionBeingEditedIndex, action);

            if (originalTime != newTime) {
                Logger.message(this, "Action's timestamp was edited, must fully sort the actions array by time");
                sortActions();
            }
        } catch (Exception e) {
            Logger.error(this, "Failed to update macro with edited action: " + e.toString());
        }
    }

    private void sortActions() {
        Comparator comparator = (Comparator<BaseAction>) (BaseAction a, BaseAction b) -> {
            int result = (int) (a.getPerformTime() - b.getPerformTime());
            return result;
        };

        _macroActions.sort(comparator);
        _macroActionDescriptions.sort(comparator);
    }
}
