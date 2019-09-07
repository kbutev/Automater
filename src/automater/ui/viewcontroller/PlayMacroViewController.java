/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.TextValue;
import automater.presenter.BasePresenterDelegate;
import automater.presenter.PlayMacroPresenter;
import automater.storage.PreferencesStorageValues;
import automater.ui.view.PlayMacroForm;
import automater.ui.view.PlayMacroOptionsDialog;
import automater.ui.view.StandartDescriptionsDataSource;
import automater.utilities.AlertWindows;
import automater.utilities.Description;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import automater.work.model.ExecutorProgress;
import automater.work.model.MacroParameters;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 *
 * @author Bytevi
 */
public class PlayMacroViewController implements BaseViewController, BasePresenterDelegate {
    private final PlayMacroPresenter _presenter;
    
    private PlayMacroForm _form;
    private PlayMacroOptionsDialog _optionsDialog;
    
    private StandartDescriptionsDataSource _dataSource;
    
    private PreferencesStorageValues _defaultPreferences = PreferencesStorageValues.defaultValues();
    
    public PlayMacroViewController(PlayMacroPresenter presenter)
    {
        _presenter = presenter;
        _form = new PlayMacroForm();
    }
    
    private void setupViewCallbacks()
    {
        _form.onBackButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.navigateBack();
            }
        };
        
        _form.onPlayButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.play();
            }
        };
        
        _form.onStopButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.stop();
            }
        };
        
        _form.onOptionsButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                displayOptionsWindow();
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
        _form.play();
    }
    
    @Override
    public void updatePlayStatus(ExecutorProgress progress)
    {
        _form.setProgressBarValue(progress.getPercentageDone());
        _form.setStatus(progress.getCurrentStatus());
        _form.setSelectedIndex(progress.getCurrentActionIndex());
    }

    @Override
    public void cancelPlaying()
    {
        _form.cancel();
    }
    
    @Override
    public void finishPlaying()
    {
        _form.finish();
    }
    
    @Override
    public void onLoadedPreferencesFromStorage(PreferencesStorageValues values)
    {
        _defaultPreferences = values;
    }

    @Override
    public void onErrorEncountered(Exception e)
    {
        Logger.error(this, "Error encountered: " + e.toString());
        
        _form.cancel();
        
        // Show message alert
        String textTitle = TextValue.getText(TextValue.Play_DialogErrorTitle);
        String textMessage = e.toString();
        String ok = TextValue.getText(TextValue.Dialog_OK);
        
        AlertWindows.showErrorMessage(_form, textTitle, textMessage, ok);
    }
    
    // # Private
    
    private void setMacroParametersDescription(MacroParameters parameters)
    {
        _form.setMacroParametersValue(parameters.toString());
    }
    
    private void initOptionsWindow()
    {
        if (_optionsDialog != null)
        {
            return;
        }
        
        Frame frame = _form.getFrames()[0];
        
        _optionsDialog = new PlayMacroOptionsDialog(frame, true);
        
        _optionsDialog.onCancelButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _optionsDialog.setVisible(false);
            }
        };
        
        _optionsDialog.onOKButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                PreferencesStorageValues values = getPreferenceValuesFromOptionsDialog();
                setOptionValues(values);
                _optionsDialog.setVisible(false);
            }
        };
    }
    
    private void displayOptionsWindow()
    {
        initOptionsWindow();
        
        // Setup default values
        _optionsDialog.setupWithStorageValues(_defaultPreferences);
        
        // Display
        _optionsDialog.setVisible(true);
    }
    
    private PreferencesStorageValues getPreferenceValuesFromOptionsDialog()
    {
        PreferencesStorageValues values = PreferencesStorageValues.defaultValues();
        
        values.displayStartNotification = _optionsDialog.isNotificationStartChecked();
        values.displayStopNotification = _optionsDialog.isNotificationStopChecked();
        values.displayRepeatNotification = _optionsDialog.isNotificationRepeatChecked();
        
        double playSpeed = _optionsDialog.getPlaySpeedValue();
        
        if (playSpeed < 0 || playSpeed > 10)
        {
            playSpeed = 1;
        }
        
        int repeatTimes = _optionsDialog.getRepeatValue();
        boolean repeatForever = _optionsDialog.isRepeatForeverChecked();
        
        MacroParameters parameters = new MacroParameters(playSpeed, repeatTimes, repeatForever);
        values.macroParameters = parameters;
        
        return values;
    }
    
    private void setOptionValues(PreferencesStorageValues values)
    {
        if (_optionsDialog != null)
        {
            _optionsDialog.setupWithStorageValues(values);
        }
        
        _presenter.setOptionValues(values);
    }
}
