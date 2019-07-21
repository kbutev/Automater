/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

/**
 * Handles the result of a parsed system user input.
 * 
 * @author Bytevi
 */
public interface RecorderInputParserDelegate {
    void onParseResult(RecorderUserInput input);
}
