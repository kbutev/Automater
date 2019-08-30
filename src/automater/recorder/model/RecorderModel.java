/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

import automater.recorder.BaseRecorderModel;
import automater.recorder.model.RecorderUserInput;
import automater.utilities.Errors;
import automater.utilities.Logger;
import java.util.ArrayList;

/**
 * Holds recording data; stores data received from the parser.
 * 
 * @author Bytevi
 */
public class RecorderModel implements BaseRecorderModel {
    private final ArrayList<RecorderUserInput> _recordedInput = new ArrayList<>();
    private RecorderUserInput _firstInput;
    private RecorderUserInput _lastInput;
    
    public RecorderModel()
    {
        
    }
    
    @Override
    public int getSize()
    {
        return _recordedInput.size();
    }
    
    @Override
    public boolean allowsDuplicateInputs()
    {
        return false;
    }
    
    @Override
    public RecorderUserInput getFirstAddedInput()
    {
        return _firstInput;
    }
    
    @Override
    public RecorderUserInput getLastAddedInput()
    {
        return _lastInput;
    }
    
    @Override
    public boolean addInput(RecorderUserInput input) throws Exception
    {
        if (input == null)
        {
            return false;
        }
        
        if (_lastInput == input)
        {
            return false;
        }
        
        Logger.message(this, "add input " + input.toString());
        
        if (_recordedInput.contains(input))
        {
            return false;
        }
        
        _recordedInput.add(input);
        
        if (_firstInput == null)
        {
            _firstInput = input;
        }
        
        _lastInput = input;
        
        return true;
    }
    
    @Override
    public RecorderResult retrieveRecordedData()
    {
        return new RecorderResult(_recordedInput);
    }
}
