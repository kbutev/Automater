/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.TextValue;
import automater.presenter.BasePresenterDelegate;
import automater.presenter.EditMacroPresenter;
import automater.settings.Hotkey;
import automater.storage.PreferencesStorageValues;
import automater.ui.view.EditMacroActionDialog;
import automater.ui.view.EditMacroForm;
import automater.ui.view.StandartDescriptionsDataSource;
import automater.utilities.AlertWindows;
import automater.utilities.Callback;
import automater.utilities.Description;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import java.awt.event.WindowEvent;
import java.util.List;
import automater.mutableaction.BaseMutableAction;

/**
 *
 * @author Bytevi
 */
public class EditMacroViewController implements BaseViewController, BasePresenterDelegate {
    private final EditMacroPresenter _presenter;
    
    private final EditMacroForm _form;
    private EditMacroActionDialog _editActionDialog;
    
    private StandartDescriptionsDataSource _dataSource;
    
    private boolean _isHotkeyRecording;
    
    public EditMacroViewController(EditMacroPresenter presenter)
    {
        _presenter = presenter;
        _form = new EditMacroForm();
    }
    
    private void setupViewCallbacks()
    {
        _form.onBackButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.navigateBack();
            }
        };
        
        _form.onDeleteButtonCallback = new Callback<Integer>() {
            @Override
            public void perform(Integer argument) {
                _presenter.onDeleteMacroActionAt(argument);
                _form.deselectAll();
            }
        };
        
        _form.onDoubleClickItem = new Callback<Integer>() {
            @Override
            public void perform(Integer argument) {
                _presenter.onStartEditMacroActionAt(argument);
            }
        };
        
        _form.onSaveButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                onSaveMacro();
            }
        };
        
        _form.onEditButtonCallback = new Callback<Integer>() {
            @Override
            public void perform(Integer argument) {
                _presenter.onStartEditMacroActionAt(argument);
            }
        };
        
        _form.onInsertCallback = new Callback<Integer>() {
            @Override
            public void perform(Integer argument) {
                // -1 is used to express that no selection has been made
                // We need to pass a non-negative value to the presenter
                if (argument < 0)
                {
                    argument = 0;
                }
                
                _presenter.onStartCreatingMacroActionAt(argument);
            }
        };
    }
    
    // # BaseViewController

    @Override
    public void start()
    {
        setupViewCallbacks();
        
        _form.setVisible(true);
        _form.onViewStart();
    }
    
    @Override
    public void resume()
    {
        _form.setVisible(true);
        _form.onViewResume();
        
        _presenter.requestDataUpdate();
    }
    
    @Override
    public void suspend()
    {
        _form.setVisible(false);
        _form.onViewSuspended();
    }
    
    @Override
    public void terminate()
    {
        _form.onViewTerminate();
        _form.dispatchEvent(new WindowEvent(_form, WindowEvent.WINDOW_CLOSING));
    }
    
    // # BasePresenterDelegate

    @Override
    public void startRecording()
    {
        
    }

    @Override
    public void stopRecording() 
    {
        
    }

    @Override
    public void onActionsRecordedChange(List<Description> actions) 
    {
        
    }

    @Override
    public void onRecordingSaved(String name, boolean success)
    {
        
    }

    @Override
    public void onLoadedMacrosFromStorage(List<Description> macros)
    {
        
    }

    @Override
    public void onLoadedMacroFromStorage(String macroName, String macroDescription, List<Description> macroActions)
    {
        _dataSource = StandartDescriptionsDataSource.createDataSourceForVerboseText(macroActions);
        _form.setMacroInfo(macroName, macroDescription);
        _form.setMacroDataSource(_dataSource);
    }
    
    @Override
    public void startPlaying()
    {
        
    }

    @Override
    public void updatePlayStatus(automater.work.model.ExecutorProgress progress)
    {
        
    }

    @Override
    public void cancelPlaying() 
    {
        
    }

    @Override
    public void finishPlaying()
    {
        
    }

    @Override
    public void onLoadedPreferencesFromStorage(PreferencesStorageValues values)
    {
        
    }
    
    @Override
    public void onCreateMacroAction(automater.mutableaction.BaseMutableAction action)
    {
        startCreatingMacroAction(action);
    }
    
    @Override
    public void onEditMacroAction(automater.mutableaction.BaseMutableAction action)
    {
        startEditingMacroAction(action);
    }
    
    @Override
    public void onSaveMacroAction(automater.mutableaction.BaseMutableAction action)
    {
        
    }
    
    @Override
    public void onEditedMacroActions(List<Description> newMacroActions)
    {
        _dataSource = StandartDescriptionsDataSource.createDataSourceForVerboseText(newMacroActions);
        _form.setMacroDataSource(_dataSource);
    }

    @Override
    public void onErrorEncountered(Exception e)
    {
        Logger.error(this, "Error encountered: " + e.toString());
        
        if (_editActionDialog != null)
        {
            _editActionDialog.displayError(e.getMessage());
            return;
        }
        
        // Show message alert
        String textTitle = TextValue.getText(TextValue.Error_Generic);
        String textMessage = e.getMessage();
        String ok = TextValue.getText(TextValue.Dialog_OK);
        
        AlertWindows.showErrorMessage(_form, textTitle, textMessage, ok);
    }
    
    // # Private
    
    private void onSaveMacro()
    {
        String name = _form.getMacroName();
        String description = _form.getMacroDescription();
        _presenter.onSaveMacro(name, description);
    }
    
    private void initEditMacroActionDialog()
    {
        if (_editActionDialog != null)
        {
            return;
        }
        
        _editActionDialog = new EditMacroActionDialog(_form, true);
        
        _editActionDialog.onCancelButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                cancelEditMacroActionDialog();
            }
        };
        
        _editActionDialog.onSaveButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                tryToSaveAndCloseEditMacroActionDialog();
            }
        };
        
        _editActionDialog.onTypeChangedCallback = new Callback<Integer>() {
            @Override
            public void perform(Integer argument) {
                changeEditMacroActionTypeForTypeIndex(argument);
            }
        };
        
        _editActionDialog.onHotkeyButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                if (!_isHotkeyRecording)
                {
                    startHotkeyListening();
                } 
                else 
                {
                    endHotkeyListeningWithoutAnyKeyEntered();
                }
            }
        };
    }
    
    private void startCreatingMacroAction(automater.mutableaction.BaseMutableAction action)
    {
        initEditMacroActionDialog();
        
        // Setup
        setupEditingMacroActionDialog(action);
        
        // Make sure that this the last statement: show dialog
        _editActionDialog.setVisible(true);
    }
    
    private void startEditingMacroAction(automater.mutableaction.BaseMutableAction action)
    {
        initEditMacroActionDialog();
        
        // Setup
        setupEditingMacroActionDialog(action);
        
        // Make sure that this the last statement: show dialog
        _editActionDialog.setVisible(true);
    }
    
    private void setupEditingMacroActionDialog(automater.mutableaction.BaseMutableAction action)
    {
        // Selectable types
        StandartDescriptionsDataSource actionTypes;
        actionTypes = StandartDescriptionsDataSource.createDataSourceForVerboseText(_presenter.getActionTypes());
        
        _editActionDialog.setTypesDropdownModel(actionTypes);
        
        // Action type
        _editActionDialog.selectDropdownType(_presenter.getActionTypeSelectedIndex());
        
        // Set mutable action
        _editActionDialog.setMutableAction(action);
    }
    
    private void closeEditMacroActionDialog()
    {
        if (_editActionDialog == null)
        {
            return;
        }
        
        if (_isHotkeyRecording)
        {
            _editActionDialog.endHotkeyListeningWithoutAnyKeyEntered();
        }
        
        _editActionDialog.setVisible(false);
        
        // Delete
        _editActionDialog = null;
    }
    
    private void tryToSaveAndCloseEditMacroActionDialog()
    {
        Exception exception = _presenter.canSuccessfullyEndEditMacroAction();
        
        if (exception == null)
        {
            closeEditMacroActionDialog();
            _presenter.onEndCreateOrEditMacroAction(true);
        }
        else
        {
            onErrorEncountered(exception);
        }
    }
    
    private void cancelEditMacroActionDialog()
    {
        closeEditMacroActionDialog();
        
        // Alert presenter
        _presenter.onEndCreateOrEditMacroAction(false);
    }
    
    private void changeEditMacroActionTypeForTypeIndex(int index)
    {
        if (_isHotkeyRecording)
        {
            return;
        }
        
        if (_editActionDialog == null)
        {
            return;
        }
        
        BaseMutableAction a;
        a = _presenter.changeEditMacroActionTypeForTypeIndex(index);
        
        if (a != null)
        {
            _editActionDialog.selectDropdownType(index);
            _editActionDialog.setMutableAction(a);
        }
    }
    
    private void startHotkeyListening()
    {
        if (_isHotkeyRecording)
        {
            return;
        }
        
        _isHotkeyRecording = true;
        _editActionDialog.startHotkeyListening();
        
        Callback onHotkeyEnteredCallback = new Callback<Hotkey>() {
            @Override
            public void perform(Hotkey argument) {
                endHotkeyListening(argument);
            }
        };
        
        _presenter.startListeningForKeystrokes(onHotkeyEnteredCallback);
    }
    
    private void endHotkeyListeningWithoutAnyKeyEntered()
    {
        if (!_isHotkeyRecording)
        {
            return;
        }
        
        _isHotkeyRecording = false;
        _editActionDialog.endHotkeyListeningWithoutAnyKeyEntered();
        
        _presenter.endListeningForKeystrokes();
    }
    
    private void endHotkeyListening(Hotkey hotkeyEntered)
    {
        if (!_isHotkeyRecording)
        {
            return;
        }
        
        _isHotkeyRecording = false;
        _editActionDialog.endHotkeyListening(hotkeyEntered.key.toString());
        
        _presenter.endListeningForKeystrokes();
    }
}
