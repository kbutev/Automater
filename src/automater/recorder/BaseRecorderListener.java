/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.recorder.model.RecorderUserInput;

/**
 * A listener for recorded user input actions.
 * You can modify the user input by returning a different value (return NULL
 * to reject the recorded input).
 *
 * @author Bytevi
 */
public interface BaseRecorderListener {
    public RecorderUserInput onRecordedUserInput(RecorderUserInput input);
    
    public void onFinishedRecording(RecorderResult result, boolean success, Exception exception);
}
