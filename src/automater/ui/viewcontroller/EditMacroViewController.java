/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.viewcontroller;

import automater.TextValue;
import automater.mvp.BasePresenter.EditMacroPresenter;
import automater.settings.Hotkey;
import automater.ui.view.EditMacroActionDialog;
import automater.ui.view.EditMacroForm;
import automater.ui.view.StandardDescriptionDataSource;
import automater.utilities.AlertWindows;
import automater.utilities.Callback;
import automater.utilities.Description;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import java.awt.event.WindowEvent;
import java.util.List;
import automater.mutableaction.BaseMutableAction;
import automater.mvp.BasePresenterDelegate.EditMacroPresenterDelegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Edit macro screen: modify name, description or actions.
 *
 * @author Bytevi
 */
public class EditMacroViewController implements BaseViewController, EditMacroPresenterDelegate {

    @NotNull private final EditMacroPresenter _presenter;

    @NotNull private final EditMacroForm _form;
    @Nullable private EditMacroActionDialog _editActionDialog;

    @Nullable private StandardDescriptionDataSource _dataSource;

    private boolean _isHotkeyRecording;

    public EditMacroViewController(@NotNull EditMacroPresenter presenter) {
        _presenter = presenter;
        _form = new EditMacroForm();
    }

    private void setupViewCallbacks() {
        _form.onBackButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.onCloseMacroWithoutSaving();
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
                if (argument < 0) {
                    argument = 0;
                }

                _presenter.onStartCreatingMacroActionAt(argument);
            }
        };

        _form.onNameChangedCallback = new Callback<String>() {
            @Override
            public void perform(String argument) {
                _presenter.onMacroNameChanged(argument);
            }
        };

        _form.onDescriptionChangedCallback = new Callback<String>() {
            @Override
            public void perform(String argument) {
                _presenter.onMacroDescriptionChanged(argument);
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

        _presenter.requestDataUpdate();
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

    // # EditMacroPresenterDelegate
    @Override
    public void onErrorEncountered(@NotNull Exception e) {
        Logger.error(this, "Error encountered: " + e.toString());

        if (_editActionDialog != null) {
            _editActionDialog.displayError(e.getMessage());
            return;
        }

        // Show message alert
        String textTitle = TextValue.getText(TextValue.Play_DialogErrorTitle);
        String textMessage = e.getMessage();
        String ok = TextValue.getText(TextValue.Dialog_OK);

        AlertWindows.showErrorMessage(_form, textTitle, textMessage, ok);
    }

    @Override
    public void onLoadedMacroFromStorage(@NotNull String macroName, @NotNull String macroDescription, @NotNull List<Description> macroActions) {
        //_dataSource = StandardDescriptionsDataSource.createDataSource(macroActions);
        _form.setMacroInfo(macroName, macroDescription);
        _form.setMacroDataSource(_dataSource);
    }

    @Override
    public void onBeginCreateMacroAction(@NotNull automater.mutableaction.BaseMutableAction action) {
        startCreatingMacroAction(action);
    }

    @Override
    public void onBeginEditMacroAction(@NotNull automater.mutableaction.BaseMutableAction action) {
        startEditingMacroAction(action);
    }

    @Override
    public void onEditedMacroActions(@NotNull List<Description> newMacroActions) {
        //_dataSource = StandardDescriptionsDataSource.createDataSourceForVerboseText(newMacroActions);
        _form.setMacroDataSource(_dataSource);
    }

    @Override
    public void onClosingMacroWithoutSavingChanges() {
        // Show confirm alert
        String textTitle = TextValue.getText(TextValue.Edit_CloseWithoutSavingTitle);
        String textMessage = TextValue.getText(TextValue.Edit_CloseWithoutSavingMessage);

        AlertWindows.showConfirmationMessage(_form, textTitle, textMessage, new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.navigateBack();
            }
        }, null);
    }

    // # Private
    private void onSaveMacro() {
        _presenter.onSaveMacro();
    }

    private void initEditMacroActionDialog() {
        if (_editActionDialog != null) {
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
                if (!_isHotkeyRecording) {
                    startHotkeyListening();
                } else {
                    endHotkeyListeningWithoutAnyKeyEntered();
                }
            }
        };
    }

    private void startCreatingMacroAction(@NotNull automater.mutableaction.BaseMutableAction action) {
        initEditMacroActionDialog();

        // Setup
        setupEditingMacroActionDialog(action);

        // Make sure that this the last statement: show dialog
        _editActionDialog.setVisible(true);
    }

    private void startEditingMacroAction(@NotNull automater.mutableaction.BaseMutableAction action) {
        initEditMacroActionDialog();

        // Setup
        setupEditingMacroActionDialog(action);

        // Make sure that this the last statement: show dialog
        _editActionDialog.setVisible(true);
    }

    private void setupEditingMacroActionDialog(@NotNull automater.mutableaction.BaseMutableAction action) {
        // Selectable types
        StandardDescriptionDataSource actionTypes;
        //actionTypes = StandardDescriptionsDataSource.createDataSourceForVerboseText(_presenter.getActionTypes());

        //_editActionDialog.setTypesDropdownModel(actionTypes);

        // Action type
        _editActionDialog.selectDropdownType(_presenter.getActionTypeSelectedIndex());

        // Set mutable action
        _editActionDialog.setMutableAction(action);
    }

    private void closeEditMacroActionDialog() {
        if (_editActionDialog == null) {
            return;
        }

        if (_isHotkeyRecording) {
            _editActionDialog.endHotkeyListeningWithoutAnyKeyEntered();
        }

        _editActionDialog.setVisible(false);

        // Delete
        _editActionDialog = null;
    }

    private void tryToSaveAndCloseEditMacroActionDialog() {
        Exception exception = _presenter.canSuccessfullyEndEditMacroAction();

        if (exception == null) {
            closeEditMacroActionDialog();
            _presenter.onEndCreateOrEditMacroAction(true);
        } else {
            onErrorEncountered(exception);
        }
    }

    private void cancelEditMacroActionDialog() {
        closeEditMacroActionDialog();

        // Alert presenter
        _presenter.onEndCreateOrEditMacroAction(false);
    }

    private void changeEditMacroActionTypeForTypeIndex(int index) {
        if (_isHotkeyRecording) {
            return;
        }

        if (_editActionDialog == null) {
            return;
        }

        BaseMutableAction a;
        a = _presenter.changeEditMacroActionTypeForTypeIndex(index);

        if (a != null) {
            _editActionDialog.selectDropdownType(index);
            _editActionDialog.setMutableAction(a);
        }
    }

    private void startHotkeyListening() {
        if (_isHotkeyRecording) {
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

    private void endHotkeyListeningWithoutAnyKeyEntered() {
        if (!_isHotkeyRecording) {
            return;
        }

        _isHotkeyRecording = false;
        _editActionDialog.endHotkeyListeningWithoutAnyKeyEntered();

        _presenter.endListeningForKeystrokes();
    }

    private void endHotkeyListening(@NotNull Hotkey hotkeyEntered) {
        if (!_isHotkeyRecording) {
            return;
        }

        _isHotkeyRecording = false;
        _editActionDialog.endHotkeyListening(hotkeyEntered.key.toString());

        _presenter.endListeningForKeystrokes();
    }
}
