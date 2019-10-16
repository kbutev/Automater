/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.parser;

import automater.recorder.model.RecorderUserInput;
import com.sun.istack.internal.NotNull;

/**
 * Forwards requests for parsed input objects.
 * 
 * Call onParseInput() when new input is forwarded.
 * Call onInputDataChange() when input data has changed without new input being delivered.
 * 
 * @author Bytevi
 */
public interface RecorderJHookListenerDelegate {
    public void onParseInput(@NotNull RecorderUserInput input);
    public void onInputDataChange();
}
