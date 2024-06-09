/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.viewcontroller;

import automater.TextValue;
import automater.presenter.RecordMacroPresenter;
import automater.ui.view.RecordMacroForm;
import automater.ui.view.StandardDescriptionDataSource;
import automater.utilities.AlertWindows;
import automater.utilities.Callback;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import java.awt.event.WindowEvent;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Record macro screen.
 *
 * @author Bytevi
 */
public class RecordMacroViewController implements BaseViewController, RecordMacroPresenter.Delegate {

    private final @NotNull RecordMacroPresenter.Protocol presenter;

    private final @NotNull RecordMacroForm form;

    public RecordMacroViewController(@NotNull RecordMacroPresenter.Protocol presenter) {
        this.presenter = presenter;
        form = new RecordMacroForm();
    }

    private void setupViewCallbacks() {
        var self = this;

        form.onSwitchToPlayButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                presenter.stop();
            }
        };

        form.onBeginRecordMacroButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                presenter.beginRecording(self);
            }
        };

        form.onStopRecordMacroButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                presenter.endRecording(self);
            }
        };

        form.onSaveMacroButtonCallback = new Callback<String>() {
            @Override
            public void perform(String argument) {
                presenter.saveRecording(argument, form.getMacroDescription());
            }
        };
    }

    // # BaseViewController
    @Override
    public void start() {
        assert presenter != null;
        
        setupViewCallbacks();

        form.setVisible(true);
        form.onViewStart();
    }

    @Override
    public void resume() {
        form.setVisible(true);
        form.onViewResume();
    }

    @Override
    public void suspend() {
        form.setVisible(false);
        form.onViewSuspended();
    }

    @Override
    public void terminate() {
        form.onViewTerminate();
        form.dispatchEvent(new WindowEvent(form, WindowEvent.WINDOW_CLOSING));
    }

    // # RecordMacroPresenter.Delegate
    @Override
    public void onError(@NotNull Exception e) {
        Logger.error(this, "Error encountered: " + e.toString());

        // Show message alert
        String textTitle = TextValue.getText(TextValue.Dialog_SaveRecordingFailedTitle);
        String textMessage = TextValue.getText(TextValue.Dialog_SaveRecordingFailedMessage, e.getMessage());
        String ok = TextValue.getText(TextValue.Dialog_OK);

        AlertWindows.showErrorMessage(form, textTitle, textMessage, ok);
    }

    @Override
    public void onLoadedPreferencesFromStorage(@NotNull automater.storage.PreferencesStorage.Values values) {
        form.setRecordOrStopHotkeyText(values.recordHotkey);
    }

    @Override
    public void onStartRecording(@Nullable Object sender) {
        assert presenter != null;
        
        if (sender == presenter) {
            form.beginRecording();
        }
    }

    @Override
    public void onEndRecording(@Nullable Object sender) {
        assert presenter != null;
        
        if (sender == presenter) {
            form.endRecording();
        }
    }
    
    @Override
    public void onUpdateEvents(@NotNull List<String> events) {
        var result = StandardDescriptionDataSource.createDataSource(events);
        form.setListDataSource(result);
    }

    @Override
    public void onRecordingSaved(boolean success) {
        if (!success) {
            return;
        }

        // Show message alert
        String textTitle = TextValue.getText(TextValue.Dialog_SavedRecordingTitle);
        String textMessage = TextValue.getText(TextValue.Dialog_SavedRecordingMessage);
        String ok = TextValue.getText(TextValue.Dialog_OK);

        AlertWindows.showMessage(form, textTitle, textMessage, ok, new SimpleCallback() {
            @Override
            public void perform() {
                //_presenter.onRecordingSavedSuccessufllyClosed();
            }
        });
    }
}
