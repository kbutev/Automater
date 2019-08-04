/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.parser;

import automater.recorder.model.RecorderUserInput;

/**
 * Interface for a receiver that handles the result of a parsed user input.
 * 
 * @author Bytevi
 */
public interface RecorderInputParserDelegate {
    void onParseResult(RecorderUserInput input);
}
