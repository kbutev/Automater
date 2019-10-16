/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

import automater.recorder.BaseRecorderModel;
import automater.utilities.Logger;
import java.util.ArrayList;

/**
 * Stores raw recording data.
 * 
 * @author Bytevi
 */
public class RecorderModelStandard implements BaseRecorderModel {
    private final ArrayList<RecorderUserInput> _recordedInput = new ArrayList<>();
    private RecorderUserInput _firstInput;
    private RecorderUserInput _lastInput;
    
    public RecorderModelStandard()
    {
        
    }
    
    @Override
    public int getSize()
    {
        return _recordedInput.size();
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
    public void addInput(RecorderUserInput input) throws Exception
    {
        if (input == null)
        {
            return;
        }
        
        Logger.message(this, "Add input " + input.toString());
        
        _recordedInput.add(input);
        
        if (_firstInput == null)
        {
            _firstInput = input;
        }
        
        _lastInput = input;
    }
    
    @Override
    public RecorderResult retrieveRecordedData()
    {
        return new RecorderResult(_recordedInput);
    }
}
