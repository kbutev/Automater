/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.utilities.Errors;
import java.util.ArrayList;

/**
 *
 * @author Bytevi
 */
public class Recorder {
    private static Recorder singleton;
    
    private final Object _lock = new Object();
    
    private boolean _isRecording = false;
    
    private ArrayList<RecorderUserInput> _recordedInput = new ArrayList<>();
    
    synchronized public static Recorder getDefault()
    {
        if (singleton == null)
        {
            singleton = new Recorder();
        }
        
        return singleton;
    }
    
    public boolean isRecording()
    {
        synchronized (_lock)
        {
            return _isRecording;
        }
    }
    
    public void start() throws Exception
    {
        checkIfAlreadyRecording();
        
        synchronized (_lock)
        {
            _isRecording = true;
        }
    }
    
    public RecorderResult end() throws Exception
    {
        checkIfNotRecording();
        
        synchronized (_lock)
        {
            _isRecording = false;
            ArrayList<RecorderUserInput> result = _recordedInput;
            _recordedInput = new ArrayList<>();
            return new RecorderResult(result);
        }
    }
    
    private void checkIfAlreadyRecording() throws Exception
    {
        synchronized (_lock)
        {
            if (isRecording())
            {
                Errors.throwInternalLogicError("Recorder is already recording.");
            }
        }
    }
    
    private void checkIfNotRecording() throws Exception
    {
        synchronized (_lock)
        {
            if (!isRecording())
            {
                Errors.throwInternalLogicError("Recorder is not recording.");
            }
        }
    }
}
