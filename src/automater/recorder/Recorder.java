/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.utilities.Looper;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * A service that listens to system user input and records it as RecorderResult.
 * 
 * Must be started in order to function, stopped when no longer needed and in
 * order to retrieve the recorded input.
 * 
 * @author Bytevi
 */
public class Recorder {
    private static Recorder singleton;
    
    public final Defaults defaults = new Defaults();
    public final Helpers helpers = new Helpers();
    
    private final State _state = new State();
    private final Model _model = new Model();
    private final Object _lock = new Object();
    private final RecorderListener _listener;
    
    private Recorder()
    {
        RecorderInputParserStandart parser = new RecorderInputParserStandart(defaults.getDefaultRecordSettings(), _model);
        this._listener = new RecorderListener(parser);
        
        Looper.getShared().queueCallback(new Function<Void, Boolean>() {
            @Override
            public Boolean apply(Void t) {
                return true;
            }
        });
    }
    
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
            return _state.isRecording();
        }
    }
    
    public void start() throws Exception
    {
        Logger.messageEvent(this, "Start...");
        
        synchronized (_lock)
        {
            _state.start();
            _listener.start();
        }
    }
    
    public RecorderResult stop() throws Exception
    {
        Logger.messageEvent(this, "Stop!");
        
        synchronized (_lock)
        {
            _state.stop();
            _listener.stop();
            
            return _model.parseRecordedInputToRecordedResult(true);
        }
    }
    
    // Recording state
    private class State
    {
        private boolean _isRecording = false;
        
        public boolean isRecording()
        {
            return _isRecording;
        }
        
        private void start() throws Exception
        {
            helpers.checkIfAlreadyRecording();
            
            Logger.messageEvent(this, "Recorder: start");
            
            _isRecording = true;
        }
        
        private void stop() throws Exception
        {
            helpers.checkIfNotRecording();
            
            Logger.messageEvent(this, "Recorder: stop");
            
            _isRecording = false;
        }
    }
    
    // Holds recording data; stores data received from the parser
    private class Model implements RecorderInputParserDelegate
    {
        ArrayList<RecorderUserInput> recordedInput = new ArrayList<>();
        
        RecorderUserInputToResultParser inputParser = new RecorderUserInputToResultParser();
        
        public void onParseResult(RecorderUserInput input)
        {
            recordedInput.add(input);
        }
        
        private RecorderResult parseRecordedInputToRecordedResult(boolean resetOnComplete)
        {
            RecorderResult result = inputParser.parse(recordedInput);
            
            if (resetOnComplete)
            {
                reset();
            }
            
            return result;
        }
        
        private void reset()
        {
            recordedInput = new ArrayList<>();
        }
    }
    
    // Parses RecorderUserInput objects to one single RecorderResult
    private class RecorderUserInputToResultParser
    {
        private RecorderResult parse(ArrayList<RecorderUserInput> userInputs)
        {
            return new RecorderResult(userInputs);
        }
    }
    
    // Holds default values
    private class Defaults
    {
        private ArrayList<RecorderInputParserStandart.RecordSettings> getDefaultRecordSettings()
        {
            ArrayList<RecorderInputParserStandart.RecordSettings> settings;
            settings = new ArrayList();
            settings.add(RecorderInputParserStandart.RecordSettings.RECORD_KEYBOARD_EVENTS);
            //settings.add(RecorderInputParser.RecordSettings.RECORD_MOUSE_CLICKS);
            //settings.add(RecorderInputParser.RecordSettings.RECORD_MOUSE_MOTION);
            //settings.add(RecorderInputParser.RecordSettings.RECORD_MOUSE_WHEEL);
            //settings.add(RecorderInputParser.RecordSettings.RECORD_WINDOW_EVENTS);
            settings.add(RecorderInputParserStandart.RecordSettings.LOG_EVENTS);
            return settings;
        }
    }
    
    // Commonly used methods
    private class Helpers
    {
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
}
