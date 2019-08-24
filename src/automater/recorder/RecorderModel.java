/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.recorder.model.RecorderUserInput;
import java.util.ArrayList;

/**
 * Holds recording data; stores data received from the parser.
 * 
 * @author Bytevi
 */
public class RecorderModel implements BaseRecorderInputModel {
    private final ArrayList<RecorderUserInput> recordedInput = new ArrayList<>();
    
    public RecorderModel()
    {
        
    }
    
    @Override
    public void addInput(RecorderUserInput input)
    {
        if (input != null)
        {
            recordedInput.add(input);
        }
    }
    
    @Override
    public RecorderResult retrieveRecordedData()
    {
        return new RecorderResult(recordedInput);
    }
}
