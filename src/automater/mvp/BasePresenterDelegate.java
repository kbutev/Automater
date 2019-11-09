/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.mvp;

import java.util.List;
import automater.storage.PreferencesStorageValues;
import automater.utilities.Description;
import org.jetbrains.annotations.NotNull;

/**
 * Presenter's delegate.
 * 
 * @author Bytevi
 */
public interface BasePresenterDelegate {
    public void startRecording();
    public void stopRecording();
    public void onActionsRecordedChange(@NotNull List<Description> actions);
    public void onRecordingSaved(@NotNull String name, boolean success);
    
    public void onLoadedMacrosFromStorage(@NotNull List<Description> macros);
    
    public void onLoadedMacroFromStorage(@NotNull String macroName, @NotNull String macroDescription, @NotNull List<Description> macroActions);
    
    public void startPlaying();
    public void updatePlayStatus(@NotNull automater.work.model.ExecutorProgress progress);
    public void cancelPlaying();
    public void finishPlaying();
    
    public void onLoadedPreferencesFromStorage(@NotNull PreferencesStorageValues values);
    
    public void onCreateMacroAction(@NotNull automater.mutableaction.BaseMutableAction action);
    public void onEditMacroAction(@NotNull automater.mutableaction.BaseMutableAction action);
    public void onSaveMacroAction(@NotNull automater.mutableaction.BaseMutableAction action);
    public void onEditedMacroActions(@NotNull List<Description> newMacroActions);
    public void onClosingMacroWithoutSavingChanges();
    
    public void onErrorEncountered(@NotNull Exception e);
}
