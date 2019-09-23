/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.TextValue;
import automater.presenter.BasePresenterDelegate;
import automater.presenter.OpenMacroPresenter;
import automater.storage.PreferencesStorageValues;
import automater.ui.view.OpenMacroForm;
import automater.ui.view.StandartDescriptionsDataSource;
import automater.utilities.AlertWindows;
import automater.utilities.Callback;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import java.awt.event.WindowEvent;
import java.util.List;
import automater.utilities.Description;

/**
 *
 * @author Bytevi
 */
public class OpenMacroViewController implements BaseViewController, BasePresenterDelegate {
    private final OpenMacroPresenter _presenter;
    
    private OpenMacroForm _form;
    
    private StandartDescriptionsDataSource _dataSource;
    
    public OpenMacroViewController(OpenMacroPresenter presenter)
    {
        _presenter = presenter;
        _form = new OpenMacroForm();
    }
    
    private void setupViewCallbacks()
    {
        _form.onSwitchToPlayButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.onSwitchToRecordScreen();
            }
        };
        
        _form.onSelectItem = new Callback<Integer>() {
            @Override
            public void perform(Integer argument) {
                onSelectItem(argument);
            }
        };
        
        _form.onDoubleClickItem = new Callback<Integer>() {
            @Override
            public void perform(Integer argument) {
                onOpenItem(argument);
            }
        };
        
        _form.onOpenItem = new Callback<Integer>() {
            @Override
            public void perform(Integer argument) {
                onOpenItem(argument);
            }
        };
        
        _form.onEditItem = new Callback<Integer>() {
            @Override
            public void perform(Integer argument) {
                onEditItem(argument);
            }
        };
        
        _form.onDeleteItem = new Callback<Integer>() {
            @Override
            public void perform(Integer argument) {
                onDeleteItem(argument);
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
        _dataSource = StandartDescriptionsDataSource.createDataSourceForStandartText(macros);
        _form.setListDataSource(_dataSource);
    }
    
    @Override
    public void onLoadedMacroFromStorage(String macroName, String macroDescription, List<Description> macroActions)
    {
        
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
        
    }
    
    @Override
    public void onEditMacroAction(automater.mutableaction.BaseMutableAction action)
    {
        
    }
    
    @Override
    public void onSaveMacroAction(automater.mutableaction.BaseMutableAction action)
    {
        
    }
    
    @Override
    public void onEditedMacroActions(List<Description> newMacroActions)
    {
        
    }
    
    @Override
    public void onClosingMacroWithoutSavingChanges()
    {
        
    }
    
    @Override
    public void onErrorEncountered(Exception e)
    {
        Logger.error(this, "Error encountered: " + e.toString());
    }
    
    // # Public
    
    public void onSelectItem(int index)
    {
        
    }
    
    public void onOpenItem(int index)
    {
        _presenter.openMacroAt(index);
    }
    
    public void onEditItem(int index)
    {
        _presenter.editMacroAt(index);
    }
    
    public void onDeleteItem(int index)
    {
        if (_dataSource == null)
        {
            return;
        }
        
        List<Description> macroDescriptions = _dataSource.data;
        
        if (index < 0 || index >= macroDescriptions.size())
        {
            return;
        }
        
        Description macro = macroDescriptions.get(index);
        
        SimpleCallback confirm = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.deleteMacroAt(index);
            }
        };
        
        String textTitle = TextValue.getText(TextValue.Dialog_ConfirmDeleteMacroTitle);
        String textMessage = TextValue.getText(TextValue.Dialog_ConfirmDeleteMacroMessage, macro.getName());
        
        AlertWindows.showConfirmationMessage(_form, textTitle, textMessage, confirm, null);
    }
}
