/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.mvp;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import automater.utilities.Callback;
import automater.utilities.Description;

/**
 * Base implementation for presenters.
 * 
 * @author Bytevi
 */
public interface BasePresenter {
    public void start();
    public void setDelegate(@NotNull BasePresenterDelegate delegate);
    
    public void requestDataUpdate();
    
    public interface EditMacroPresenter extends BasePresenter {
        // Properties
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
        public void startListeningForKeystrokes(@NotNull Callback<automater.settings.Hotkey> onKeystrokeEnteredCallback);
        public void endListeningForKeystrokes();
    }
    
    public interface OpenMacroPresenter extends BasePresenter {
        // Navigation
        public void onSwitchToRecordScreen();
    
        // Macro operations
        public void openMacroAt(int index);
        public void editMacroAt(int index);
        public void deleteMacroAt(int index);
    }
    
    public interface PlayMacroPresenter extends BasePresenter {
        // Navigation
        public void navigateBack();
    
        // Play macro operations
        public void play();
        public void stop();
        public void setOptionValues(@NotNull automater.storage.PreferencesStorageValues values);
    }
    
    public interface RecordMacroPresenter extends BasePresenter {
        // Navigation
        public void onSwitchToPlayScreen();
    
        // Recording operations
        public void onStartRecording();
        public void onStopRecording();
        public void onSaveRecording(@NotNull String name, @NotNull String description);
        public void onRecordingSavedSuccessufllyClosed();
    }
}
