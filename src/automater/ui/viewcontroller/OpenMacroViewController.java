/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.viewcontroller;

import automater.TextValue;
import automater.model.macro.MacroSummaryDescription;
import automater.presenter.OpenMacroPresenter;
import automater.ui.view.OpenMacroForm;
import automater.ui.view.StandardDescriptionDataSource;
import automater.utilities.AlertWindows;
import automater.utilities.Logger;
import automater.utilities.SimpleCallback;
import java.awt.event.WindowEvent;
import java.util.List;
import automater.utilities.Description;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Open macro screen.
 *
 * @author Bytevi
 */
public class OpenMacroViewController implements BaseViewController, OpenMacroPresenter.Delegate {

    private final @NotNull OpenMacroPresenter.Protocol _presenter;

    private final @NotNull OpenMacroForm _form;

    private @Nullable StandardDescriptionDataSource _dataSource;

    public OpenMacroViewController(OpenMacroPresenter.Protocol presenter) {
        _presenter = presenter;
        _form = new OpenMacroForm();
    }

    private void setupViewCallbacks() {
        _form.onSwitchToPlayButtonCallback = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.onSwitchToRecordScreen();
            }
        };

        _form.onSelectItem = (Integer argument) -> {
            onSelectItem(argument);
        };

        _form.onDoubleClickItem = (Integer argument) -> {
            onOpenItem(argument);
        };

        _form.onOpenItem = (Integer argument) -> {
            onOpenItem(argument);
        };

        _form.onEditItem = (Integer argument) -> {
            onEditItem(argument);
        };

        _form.onDeleteItem = (Integer argument) -> {
            onDeleteItem(argument);
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

    // # OpenMacroPresenterDelegate
    @Override
    public void onError(@NotNull Exception e) {
        Logger.error(this, "Error encountered: " + e.toString());
    }

    @Override
    public void onLoadedMacrosFromStorage(@NotNull List<String> macros) {
        _dataSource = StandardDescriptionDataSource.createDataSource(macros);
        _form.setListDataSource(_dataSource);
    }

    // # Public
    public void onSelectItem(int index) {

    }

    public void onOpenItem(int index) {
        _presenter.openMacroAt(index);
    }

    public void onEditItem(int index) {
        _presenter.editMacroAt(index);
    }

    public void onDeleteItem(int index) {
        if (_dataSource == null) {
            return;
        }

        var macroDescriptions = _dataSource.data;

        if (index < 0 || index >= macroDescriptions.size()) {
            return;
        }

        var macro = macroDescriptions.get(index);

        var confirm = new SimpleCallback() {
            @Override
            public void perform() {
                _presenter.deleteMacroAt(index);
            }
        };

        var textTitle = TextValue.getText(TextValue.Dialog_ConfirmDeleteMacroTitle);
        var textMessage = TextValue.getText(TextValue.Dialog_ConfirmDeleteMacroMessage, "macro name");

        AlertWindows.showConfirmationMessage(_form, textTitle, textMessage, confirm, null);
    }
}
