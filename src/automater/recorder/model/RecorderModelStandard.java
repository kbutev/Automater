/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

import automater.recorder.BaseRecorderModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;

/**
 * Stores raw recording data.
 * 
 * @author Bytevi
 */
public class RecorderModelStandard implements BaseRecorderModel {
    @NotNull private final ArrayList<RecorderUserInput> _recordedInput = new ArrayList<>();
    @Nullable private RecorderUserInput _firstInput;
    @Nullable private RecorderUserInput _lastInput;
    
    public RecorderModelStandard()
    {
        
    }
    
    @Override
    public int getSize()
    {
        return _recordedInput.size();
    }
    
    @Override
    public @Nullable RecorderUserInput getFirstAddedInput()
    {
        return _firstInput;
    }
    
    @Override
    public @Nullable RecorderUserInput getLastAddedInput()
    {
        return _lastInput;
    }
    
    @Override
    public void addInput(@NotNull RecorderUserInput input) throws Exception
    {
        _recordedInput.add(input);
        
        if (_firstInput == null)
        {
            _firstInput = input;
        }
        
        _lastInput = input;
    }
    
    @Override
    public @Nullable RecorderResult retrieveRecordedData()
    {
        return new RecorderResult(_recordedInput);
    }
}
