/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.TextValue;
import automater.mvp.BasePresenterDelegate;
import automater.mvp.BasePresenter.RecordMacroPresenter;
import automater.storage.PreferencesStorageValues;
import automater.ui.view.RecordMacroForm;
import automater.ui.view.StandardDescriptionsDataSource;
import automater.utilities.AlertWindows;
import automater.utilities.Callback;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import java.awt.event.WindowEvent;
import java.util.List;
import automater.utilities.Description;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Record macro screen.
 * 
 * @author Bytevi
 */
public class RecordMacroViewController implements BaseViewController, BasePresenterDelegate {
    @NotNull private final RecordMacroPresenter _presenter;
    
    @NotNull private final RecordMacroForm _form;
    
    @Nullable private StandardDescriptionsDataSource _dataSource;
    
    public RecordMacroViewController(RecordMacroPresenter presenter)
    {
        _presenter = presenter;
        _form = new RecordMacroForm();
    }
    
    private void setupViewCallbacks()
    {
        _form.onSwitchToPlayButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.onSwitchToPlayScreen();
            }
        };
        
        _form.onRecordMacroButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.onStartRecording();
            }
        };
        
        _form.onStopRecordMacroButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.onStopRecording();
            }
        };
        
        _form.onSaveMacroButtonCallback = new Callback<String>() {
            @Override
            public void perform(String argument) {
                _presenter.onSaveRecording(argument, _form.getMacroDescription());
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
        _form.beginRecording();
    }
    
    @Override
    public void stopRecording()
    {
        _form.endRecording();
    }
    
    @Override
    public void onActionsRecordedChange(@NotNull List<Description> actions)
    {
        _dataSource = StandardDescriptionsDataSource.createDataSourceForStandartText(actions);
        _form.setListDataSource(_dataSource);
    }
    
    @Override
    public void onRecordingSaved(@NotNull String name, boolean success)
    {
        if (!success) {
            return;
        }
        
        // Show message alert
        String textTitle = TextValue.getText(TextValue.Dialog_SavedRecordingTitle);
        String textMessage = TextValue.getText(TextValue.Dialog_SavedRecordingMessage);
        String ok = TextValue.getText(TextValue.Dialog_OK);
        
        AlertWindows.showMessage(_form, textTitle, textMessage, ok, new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.onRecordingSavedSuccessufllyClosed();
            }
        });
    }
    
    @Override
    public void onLoadedMacrosFromStorage(@NotNull List<Description> macros)
    {
        
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
        _form.setRecordOrStopHotkeyText(values.recordOrStopHotkey);
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
        
        // Show message alert
        String textTitle = TextValue.getText(TextValue.Dialog_SaveRecordingFailedTitle);
        String textMessage = TextValue.getText(TextValue.Dialog_SaveRecordingFailedMessage, e.getMessage());
        String ok = TextValue.getText(TextValue.Dialog_OK);
        
        AlertWindows.showErrorMessage(_form, textTitle, textMessage, ok);
    }
}
