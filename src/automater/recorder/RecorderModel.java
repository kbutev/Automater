/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.recorder.parser.RecorderInputParserDelegate;
import automater.recorder.model.RecorderUserInput;
import automater.utilities.Logger;
import java.util.ArrayList;

/**
 * Holds recording data; stores data received from the parser.
 * 
 * @author Bytevi
 */
public class RecorderModel implements RecorderInputParserDelegate {
    private ArrayList<RecorderUserInput> recordedInput = new ArrayList<>();
    public BaseRecorderListener listener;
    
    @Override
    public void onParseResult(RecorderUserInput input)
    {
        recordedInput.add(input);
        
        if (listener != null)
        {
            listener.onRecordedUserInput(input);
        }
    }
    
    public RecorderResult parseRecordedInputToRecordedResult(boolean resetOnComplete)
    {
        RecorderResult result = new RecorderResult(recordedInput);
        
        if (resetOnComplete)
        {
            reset();
        }
        
        return result;
    }
    
    public void reset()
    {
        recordedInput = new ArrayList<>();
    }
}
