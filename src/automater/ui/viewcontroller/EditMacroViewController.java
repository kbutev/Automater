/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.presenter.BasePresenterDelegate;
import automater.presenter.EditMacroPresenter;
import automater.storage.PreferencesStorageValues;
import automater.ui.view.EditMacroActionDialog;
import automater.ui.view.EditMacroForm;
import automater.ui.view.StandartDescriptionsDataSource;
import automater.utilities.Callback;
import automater.utilities.Description;
import automater.utilities.SimpleCallback;
import java.awt.event.WindowEvent;
import java.util.List;

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
        
        _form.onEditButtonCallback = new Callback<Integer>() {
            @Override
            public void perform(Integer argument) {
                _presenter.onEditMacroAt(argument);
            }
        };
        
        _form.onDoubleClickItem = new Callback<Integer>() {
            @Override
            public void perform(Integer argument) {
                _presenter.onEditMacroAt(argument);
            }
        };
        
        _form.onSaveButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                
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
        _dataSource = new StandartDescriptionsDataSource(macroActions);
        _form.setMacroInfo(macroName, macroDescription, _dataSource);
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
    public void onEditMacroAction(automater.work.model.BaseEditableAction action)
    {
        startEditingMacroAction(action);
    }

    @Override
    public void onErrorEncountered(Exception e)
    {
        
    }
    
    // # Private
    
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
                closeEditMacroActionDialog();
            }
        };
        
        _editActionDialog.onSaveButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                //_presenter.saveMacro();
                closeEditMacroActionDialog();
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
                    endHotkeyListening();
                }
            }
        };
    }
    
    private void startEditingMacroAction(automater.work.model.BaseEditableAction action)
    {
        initEditMacroActionDialog();
        
        // Setup
        setupEditingMacroActionDialog(action);
        
        // Make sure that this the last statement: show dialog
        _editActionDialog.setVisible(true);
    }
    
    private void setupEditingMacroActionDialog(automater.work.model.BaseEditableAction action)
    {
        // Selectable types
        StandartDescriptionsDataSource actionTypes;
        actionTypes = new StandartDescriptionsDataSource(_presenter.getActionTypes());
        
        _editActionDialog.setTypesDropdownModel(actionTypes);
        
        // Action type
        _editActionDialog.selectDropdownType(_presenter.getActionTypeSelectedIndex());
        
        // Set editable action
        _editActionDialog.setEditableAction(action);
    }
    
    private void closeEditMacroActionDialog()
    {
        if (_editActionDialog == null)
        {
            return;
        }
        
        _editActionDialog.setVisible(false);
        
        // Delete
        _editActionDialog = null;
    }
    
    private void startHotkeyListening()
    {
        _isHotkeyRecording = true;
        _editActionDialog.startHotkeyListening();
    }
    
    private void endHotkeyListening()
    {
        _isHotkeyRecording = false;
        _editActionDialog.endHotkeyListening();
    }
}
