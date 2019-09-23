/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.presenter;

import java.util.List;
import automater.storage.PreferencesStorageValues;
import automater.utilities.Description;

/**
 * Presenters delegates.
 * 
 * @author Bytevi
 */
public interface BasePresenterDelegate {
    public void startRecording();
    public void stopRecording();
    public void onActionsRecordedChange(List<Description> actions);
    public void onRecordingSaved(String name, boolean success);
    
    public void onLoadedMacrosFromStorage(List<Description> macros);
    
    public void onLoadedMacroFromStorage(String macroName, String macroDescription, List<Description> macroActions);
    
    public void startPlaying();
    public void updatePlayStatus(automater.work.model.ExecutorProgress progress);
    public void cancelPlaying();
    public void finishPlaying();
    
    public void onLoadedPreferencesFromStorage(PreferencesStorageValues values);
    
    public void onCreateMacroAction(automater.mutableaction.BaseMutableAction action);
    public void onEditMacroAction(automater.mutableaction.BaseMutableAction action);
    public void onSaveMacroAction(automater.mutableaction.BaseMutableAction action);
    public void onEditedMacroActions(List<Description> newMacroActions);
    public void onClosingMacroWithoutSavingChanges();
    
    public void onErrorEncountered(Exception e);
}
