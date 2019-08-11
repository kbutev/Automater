/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.recorder.parser.RecorderInputParser;
import automater.recorder.parser.RecorderParserFlag;
import automater.utilities.Errors;
import automater.utilities.Logger;
import java.util.ArrayList;

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
    private final RecorderModel _model = new RecorderModel();
    private final Object _lock = new Object();
    private final RecorderNativeListener _listener;
    
    private Recorder()
    {
        RecorderInputParser parser = new RecorderInputParser(defaults.getDefaultRecordSettings(), _model);
        this._listener = new RecorderNativeListener(parser);
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
    
    public void start(BaseRecorderListener listener) throws Exception
    {
        Logger.messageEvent(this, "Start...");
        
        synchronized (_lock)
        {
            _model.listener = listener;
            _state.start();
            _listener.start();
        }
    }
    
    public RecorderResult stop() throws Exception
    {
        Logger.messageEvent(this, "Stop!");
        
        synchronized (_lock)
        {
            _model.listener = null;
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
    
    // Default values
    private class Defaults
    {
        private ArrayList<RecorderParserFlag> getDefaultRecordSettings()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            //settings.add(RecorderParserFlag.RECORD_MOUSE_MOTION);
            //settings.add(RecorderParserFlag.RECORD_MOUSE_WHEEL);
            //settings.add(RecorderParserFlag.RECORD_WINDOW_EVENTS);
            settings.add(RecorderParserFlag.LOG_EVENTS);
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
