/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.presenter;

import java.util.List;
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
    public void recordingSavedSuccessfully(String name);
    
    public void startPlaying();
    public void stopPlaying();
    
    public void onErrorEncountered(Exception e);
}
