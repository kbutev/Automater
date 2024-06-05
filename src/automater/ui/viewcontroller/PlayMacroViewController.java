/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.viewcontroller;

import automater.TextValue;
import automater.presenter.PlayMacroPresenter;
import automater.storage.PreferencesStorageValues;
import automater.ui.view.PlayMacroForm;
import automater.ui.view.PlayMacroOptionsDialog;
import automater.ui.view.StandardDescriptionsDataSource;
import automater.utilities.AlertWindows;
import automater.utilities.Description;
import automater.utilities.DeviceTaskBar;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import automater.work.model.MacroParameters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Play macro screen.
 *
 * @author Bytevi
 */
public class PlayMacroViewController implements BaseViewController, PlayMacroPresenter.Delegate {

    private final @NotNull PlayMacroPresenter.Protocol _presenter;

    private final @NotNull PlayMacroForm _form;
    private @Nullable PlayMacroOptionsDialog _optionsDialog;

    private @Nullable StandardDescriptionsDataSource _dataSource;

    private @NotNull PreferencesStorageValues _currentPreferences = new PreferencesStorageValues();

    public PlayMacroViewController(PlayMacroPresenter.Protocol presenter) {
        _presenter = presenter;
        _form = new PlayMacroForm();
    }

    private void setupViewCallbacks() {
        var self = this;

        _form.onBackButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.navigateBack();
            }
        };

        _form.onPlayButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.playMacro(self);
            }
        };

        _form.onStopButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.stopMacro(self);
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
    public void start() {
        setupViewCallbacks();

        _form.setVisible(true);
        _form.onViewStart();
    }

    @Override
    public void resume() {
        _form.setVisible(true);
        _form.onViewResume();

        _presenter.reloadData();
    }

    @Override
    public void suspend() {
        _form.setVisible(false);
        _form.onViewSuspended();
    }

    @Override
    public void terminate() {
        _form.onViewTerminate();
        _form.dispatchEvent(new WindowEvent(_form, WindowEvent.WINDOW_CLOSING));
    }

    // # PlayMacroPresenterDelegate
    @Override
    public void onError(@NotNull Exception e) {
        Logger.error(this, "Error encountered: " + e.toString());

        _form.cancelRecording();

        // Show message alert
        String textTitle = TextValue.getText(TextValue.Play_DialogErrorTitle);
        String textMessage = e.toString();
        String ok = TextValue.getText(TextValue.Dialog_OK);

        AlertWindows.showErrorMessage(_form, textTitle, textMessage, ok);
    }

    @Override
    public void onLoadedPreferencesFromStorage(@NotNull automater.storage.PreferencesStorageValues values) {
        _currentPreferences = values;

        setMacroParametersDescription(values.macroParameters);

        _form.setPlayOrStopHotkeyText(values.playOrStopHotkey);
    }

    @Override
    public void onLoadedMacroFromStorage(@NotNull String macroName, @NotNull String macroDescription, @NotNull List<Description> macroActions) {
        _dataSource = StandardDescriptionsDataSource.createDataSourceForStandartText(macroActions);
        _form.setMacroInfo(macroName, macroDescription, _dataSource);
    }

    @Override
    public void startPlaying() {
        _form.playRecording();

        DeviceTaskBar.getShared().resetAppTaskBarProgress(_form);
    }

    @Override
    public void stopPlaying(boolean wasCancelled) {
        if (!wasCancelled) {
            _form.finishRecording();

            DeviceTaskBar.getShared().resetAppTaskBarProgress(_form);
        } else {
            _form.cancelRecording();

            DeviceTaskBar.getShared().resetAppTaskBarProgress(_form);
        }
    }

    @Override
    public void updatePlayStatus(@NotNull automater.work.model.ExecutorProgress progress) {
        _form.setProgressBarValue(progress.getPercentageDone());
        _form.setStatus(progress.getCurrentStatus());
        _form.setSelectedIndex(progress.getCurrentActionIndex());

        DeviceTaskBar.getShared().setAppTaskBarProgress(_form, progress.getPercentageDone() * 100);
    }

    // # Private
    private void setMacroParametersDescription(@NotNull MacroParameters parameters) {
        _form.setMacroParametersDescription(parameters.toString());
    }

    private void initOptionsWindow() {
        if (_optionsDialog != null) {
            return;
        }

        _optionsDialog = new PlayMacroOptionsDialog(_form, true);

        _optionsDialog.onCancelButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _optionsDialog.setVisible(false);
            }
        };

        _optionsDialog.onSaveButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                PreferencesStorageValues values = getPreferenceValuesFromOptionsDialog();
                saveOptionValues(values);
                _optionsDialog.setVisible(false);
            }
        };
    }

    private void displayOptionsWindow() {
        initOptionsWindow();

        // Setup default values
        _optionsDialog.setupWithStorageValues(_currentPreferences);

        // Make sure that this the last statement: show dialog
        _optionsDialog.setVisible(true);
    }

    private @NotNull
    PreferencesStorageValues getPreferenceValuesFromOptionsDialog() {
        PreferencesStorageValues values = new PreferencesStorageValues();

        values.displayStartNotification = _optionsDialog.isNotificationStartChecked();
        values.displayStopNotification = _optionsDialog.isNotificationStopChecked();
        values.displayRepeatNotification = _optionsDialog.isNotificationRepeatChecked();

        double playSpeed = _optionsDialog.getPlaySpeedValue();

        if (playSpeed < 0 || playSpeed > 10) {
            playSpeed = 1;
        }

        int repeatTimes = _optionsDialog.getRepeatValue();
        boolean repeatForever = _optionsDialog.isRepeatForeverChecked();

        MacroParameters parameters = new MacroParameters(playSpeed, repeatTimes, repeatForever);
        values.macroParameters = parameters;

        return values;
    }

    private void saveOptionValues(@NotNull PreferencesStorageValues values) {
        if (_optionsDialog != null) {
            _optionsDialog.setupWithStorageValues(values);
        }

        _currentPreferences = values;

        _presenter.setOptionValues(values);

        setMacroParametersDescription(values.macroParameters);
    }
}
