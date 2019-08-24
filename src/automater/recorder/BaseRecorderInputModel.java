/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.recorder.model.RecorderUserInput;

/**
 * Describes a RecorderUserInput model.
 * 
 * The receiver is to process and store given objects so
 * they can later be retrieved by retrieveRecordedData().
 * 
 * The method onParseResult(RecorderUserInput) takes an input object and
 * can throw an exception indicating that parsing should stop.
 * 
 * The method retrieveRecordedData() gets the processed data by the parser.
 * 
 * @author Bytevi
 */
public interface BaseRecorderInputModel {
    public void addInput(RecorderUserInput input) throws Exception;
    
    public RecorderResult retrieveRecordedData();
}
