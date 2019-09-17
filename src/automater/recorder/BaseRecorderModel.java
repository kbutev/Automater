/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.recorder.model.RecorderResult;
import automater.recorder.model.RecorderUserInput;

/**
 * Describes a recorded user input model.
 * 
 * The receiver is to process and store given objects so
 * they can later be retrieved by retrieveRecordedData().
 * 
 * The method addInput() takes an input object and adds it to the data.
 * Method is capable of throwing exceptions indicating that parsing should stop.
 * 
 * The method retrieveRecordedData() gets the processed data by the parser.
 * 
 * @author Bytevi
 */
public interface BaseRecorderModel {
    public int getSize();
    
    public RecorderUserInput getFirstAddedInput();
    public RecorderUserInput getLastAddedInput();
    
    public void addInput(RecorderUserInput input) throws Exception;
    
    public RecorderResult retrieveRecordedData();
}
