/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.viewcontroller;

import automater.TextValue;
import automater.presenter.RecordMacroPresenter;
import automater.ui.view.RecordMacroForm;
import automater.ui.view.StandardDescriptionsDataSource;
import automater.utilities.AlertWindows;
import automater.utilities.Callback;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import java.awt.event.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Record macro screen.
 * 
 * @author Bytevi
 */
public class RecordMacroViewController implements BaseViewController, RecordMacroPresenter.Delegate {
    private final @NotNull RecordMacroPresenter.Protocol _presenter;
    
    private final @NotNull RecordMacroForm _form;
    
    private @Nullable StandardDescriptionsDataSource _dataSource;
    
    public RecordMacroViewController(@NotNull RecordMacroPresenter.Protocol presenter)
    {
        _presenter = presenter;
        _form = new RecordMacroForm();
    }
    
    private void setupViewCallbacks()
    {
        var self = this;
        
        _form.onSwitchToPlayButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.stop();
            }
        };
        
        _form.onRecordMacroButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.beginRecording(self);
            }
        };
        
        _form.onStopRecordMacroButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.endRecording(self);
            }
        };
        
        _form.onSaveMacroButtonCallback = new Callback<String>() {
            @Override
            public void perform(String argument) {
                _presenter.saveRecording(argument, _form.getMacroDescription());
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
    
    // # RecordMacroPresenter.Delegate
    
    @Override
    public void onError(@NotNull Exception e)
    {
        Logger.error(this, "Error encountered: " + e.toString());
        
        // Show message alert
        String textTitle = TextValue.getText(TextValue.Dialog_SaveRecordingFailedTitle);
        String textMessage = TextValue.getText(TextValue.Dialog_SaveRecordingFailedMessage, e.getMessage());
        String ok = TextValue.getText(TextValue.Dialog_OK);
        
        AlertWindows.showErrorMessage(_form, textTitle, textMessage, ok);
    }
    
    @Override
    public void onLoadedPreferencesFromStorage(@NotNull automater.storage.PreferencesStorageValues values)
    {
        _form.setRecordOrStopHotkeyText(values.recordOrStopHotkey);
    }
    
    @Override
    public void onStartRecording(@Nullable Object sender)
    {
        _form.beginRecording(sender);
    }
    
    @Override
    public void onEndRecording(@Nullable Object sender)
    {
        _form.endRecording(sender);
    }
    
    @Override
    public void onRecordingSaved(boolean success)
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
                //_presenter.onRecordingSavedSuccessufllyClosed();
            }
        });
    }
}
