/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.viewcontroller;

import automater.ui.text.TextValue;
import automater.presenter.PlayMacroPresenter;
import automater.storage.PreferencesStorage;
import automater.ui.view.PlayMacroForm;
import automater.ui.view.PlayMacroOptionsDialog;
import automater.datasource.StandardDescriptionDataSource;
import automater.utilities.AlertWindows;
import automater.utilities.Description;
import automater.utilities.DeviceTaskBar;
import automater.utilities.Logger;
import automater.utilities.Callback;
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

    private @Nullable StandardDescriptionDataSource _dataSource;

    private @NotNull PreferencesStorage.Values _currentPreferences = new PreferencesStorage.Values();

    public PlayMacroViewController(PlayMacroPresenter.Protocol presenter) {
        _presenter = presenter;
        _form = new PlayMacroForm();
    }

    private void setupViewCallbacks() {
        var self = this;

        _form.onBackButtonCallback = (Callback.Blank) () -> {
            _presenter.navigateBack();
        };

        _form.onPlayButtonCallback = (Callback.Blank) () -> {
            _presenter.playMacro(self);
        };

        _form.onStopButtonCallback = (Callback.Blank) () -> {
            _presenter.stopMacro(self);
        };

        _form.onOptionsButtonCallback = (Callback.Blank) () -> {
            displayOptionsWindow();
        };
    }

    // # BaseViewController
    @Override
    public void start() {
        setupViewCallbacks();

        _form.setVisible(true);
    }

    @Override
    public void resume() {
        _form.setVisible(true);

        _presenter.reloadData();
    }

    @Override
    public void suspend() {
        _form.setVisible(false);
    }

    @Override
    public void terminate() {
        _form.dispatchEvent(new WindowEvent(_form, WindowEvent.WINDOW_CLOSING));
    }

    // # PlayMacroPresenterDelegate
    @Override
    public void onError(@NotNull Exception e) {
        Logger.error(this, "Error encountered: " + e.toString());

        _form.stopRecording();

        // Show message alert
        String textTitle = TextValue.getText(TextValue.Play_DialogErrorTitle);
        String textMessage = e.toString();
        String ok = TextValue.getText(TextValue.Dialog_OK);

        AlertWindows.showErrorMessage(_form, textTitle, textMessage, ok);
    }

    @Override
    public void onLoadedPreferencesFromStorage(@NotNull automater.storage.PreferencesStorage.Values values) {
        _currentPreferences = values;

        setMacroParametersDescription(new MacroParameters());

        _form.setHotkeyText(values.playMacroHotkey.toString(), values.stopMacroHotkey.toString());
    }

    @Override
    public void onLoadedMacroFromStorage(@NotNull String macroName, @NotNull String macroDescription, @NotNull List<Description> macroActions) {
        //_dataSource = StandardDescriptionsDataSource.createDataSourceForStandartText(macroActions);
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
            _form.stopRecording();

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

        _optionsDialog.onCancelButtonCallback = new Callback.Blank() {
            @Override
            public void perform() {
                _optionsDialog.setVisible(false);
            }
        };

        _optionsDialog.onSaveButtonCallback = new Callback.Blank() {
            @Override
            public void perform() {
                PreferencesStorage.Values values = getPreferenceValuesFromOptionsDialog();
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
    PreferencesStorage.Values getPreferenceValuesFromOptionsDialog() {
        PreferencesStorage.Values values = new PreferencesStorage.Values();

        values.startNotification = _optionsDialog.isNotificationStartChecked();
        values.stopNotification = _optionsDialog.isNotificationStopChecked();

        double playSpeed = _optionsDialog.getPlaySpeedValue();

        if (playSpeed < 0 || playSpeed > 10) {
            playSpeed = 1;
        }

        int repeatTimes = _optionsDialog.getRepeatValue();
        boolean repeatForever = _optionsDialog.isRepeatForeverChecked();

        MacroParameters parameters = new MacroParameters(playSpeed, repeatTimes, repeatForever);
        //values.macroParameters = parameters;

        return values;
    }

    private void saveOptionValues(@NotNull PreferencesStorage.Values values) {
        if (_optionsDialog != null) {
            _optionsDialog.setupWithStorageValues(values);
        }

        _currentPreferences = values;

        setMacroParametersDescription(new MacroParameters());
    }
}
