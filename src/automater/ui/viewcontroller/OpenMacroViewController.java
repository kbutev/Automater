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
import automater.ui.view.StandardDescriptionsDataSource;
import automater.utilities.AlertWindows;
import automater.utilities.Callback;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import java.awt.event.WindowEvent;
import java.util.List;
import automater.utilities.Description;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * Open macro screen.
 *
 * @author Bytevi
 */
public class OpenMacroViewController implements BaseViewController, BasePresenterDelegate {
    @NotNull private final OpenMacroPresenter _presenter;
    
    @NotNull private final OpenMacroForm _form;
    
    @Nullable private StandardDescriptionsDataSource _dataSource;
    
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
    public void onActionsRecordedChange(@NotNull List<Description> actions)
    {
    
    }
    
    @Override
    public void onRecordingSaved(@NotNull String name, boolean success)
    {
        
    }
    
    @Override
    public void onLoadedMacrosFromStorage(@NotNull List<Description> macros)
    {
        _dataSource = StandardDescriptionsDataSource.createDataSourceForStandartText(macros);
        _form.setListDataSource(_dataSource);
    }
    
    @Override
    public void onLoadedMacroFromStorage(@NotNull String macroName, @NotNull String macroDescription, @NotNull List<Description> macroActions)
    {
        
    }
    
    @Override
    public void startPlaying()
    {
        
    }
    
    @Override
    public void updatePlayStatus(@NotNull automater.work.model.ExecutorProgress progress)
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
    public void onLoadedPreferencesFromStorage(@NotNull PreferencesStorageValues values)
    {
        
    }
    
    @Override
    public void onCreateMacroAction(@NotNull automater.mutableaction.BaseMutableAction action)
    {
        
    }
    
    @Override
    public void onEditMacroAction(@NotNull automater.mutableaction.BaseMutableAction action)
    {
        
    }
    
    @Override
    public void onSaveMacroAction(@NotNull automater.mutableaction.BaseMutableAction action)
    {
        
    }
    
    @Override
    public void onEditedMacroActions(@NotNull List<Description> newMacroActions)
    {
        
    }
    
    @Override
    public void onClosingMacroWithoutSavingChanges()
    {
        
    }
    
    @Override
    public void onErrorEncountered(@NotNull Exception e)
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
