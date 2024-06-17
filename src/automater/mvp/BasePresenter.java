/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.mvp;

import automater.model.Keystroke;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import automater.utilities.Callback;
import automater.utilities.Description;
import automater.mvp.BasePresenterDelegate.*;

/**
 * Base implementation for presenters.
 * 
 * @author Bytevi
 */
public interface BasePresenter {
    public void start();
    
    public void requestDataUpdate();
    
    public interface EditMacroPresenter extends BasePresenter {
        // Properties
        public void setDelegate(@NotNull EditMacroPresenterDelegate delegate);
        public @NotNull List<Description> getActionTypes();
        public int getActionTypeSelectedIndex();
    
        // Navigation
        public void navigateBack();
        public void onCloseMacroWithoutSaving();
        public void onSaveMacro();
    
        // Macro operations
        public void onStartCreatingMacroActionAt(int index);
        public void onStartEditMacroActionAt(int index);
        public @Nullable Exception canSuccessfullyEndEditMacroAction();
        public void onEndCreateOrEditMacroAction(boolean save);
        public void onDeleteMacroActionAt(int index);
        public @Nullable automater.mutableaction.BaseMutableAction changeEditMacroActionTypeForTypeIndex(int index);
        public void onMacroNameChanged(@NotNull String name);
        public void onMacroDescriptionChanged(@NotNull String description);
    
        // Keystroke events
        public void startListeningForKeystrokes(@NotNull Callback.WithParameter<Keystroke> onKeystrokeEnteredCallback);
        public void endListeningForKeystrokes();
    }
}
