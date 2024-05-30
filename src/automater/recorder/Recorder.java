/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.recorder;

import automater.di.DI;
import automater.recorder.model.RecorderResult;
import automater.recorder.parser.RecorderJHookListener;
import automater.recorder.model.RecorderUserInput;
import automater.recorder.parser.RecorderMasterNativeParser;
import automater.recorder.parser.RecorderNativeParser;
import automater.recorder.parser.RecorderParserFlag;
import automater.settings.Hotkey;
import automater.utilities.DeviceScreen;
import automater.utilities.Errors;
import automater.utilities.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.Dimension;
import java.util.ArrayList;

public interface Recorder {
    interface Protocol {
        void preload();
        
        void start(@NotNull RecorderNativeParser.Protocol parser, @NotNull Model model, @NotNull Listener listener) throws Exception;
        void start(@NotNull RecorderNativeParser.Protocol parser, @NotNull Model model, @NotNull Listener listener, @Nullable Hotkey stopHotkey) throws Exception;
        void stop() throws Exception;
        void registerHotkeyListener(@NotNull HotkeyListener listener);
        void unregisterHotkeyListener(@NotNull HotkeyListener listener);
        void registerPlayStopHotkeyListener(@NotNull HotkeyListener listener);
        void unregisterPlayStopHotkeyListener();
    }
    
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
    */
    interface Model {
        int getSize();
        
        public @Nullable RecorderUserInput getFirstAddedInput();
        public @Nullable RecorderUserInput getLastAddedInput();

        public void addInput(@NotNull RecorderUserInput input) throws Exception;

        public @Nullable RecorderResult retrieveRecordedData();
    }
    
    /**
    * A listener for recorded user input actions.
    * 
    * onRecordedUserInput() indicates new input has been recorded.
    * 
    * onRecordedUserInputChanged() indicates that the recorder model has changed,
    * even when no new input object has been added. This may happen due to optimizations,
    * such as grouping multiple input objects into one.
    * 
    * onFailedRecordedUserInput() indicates failure.
    */
    interface Listener {
        void onRecordedUserInput(@NotNull RecorderUserInput input);
        void onRecordedUserInputChanged();
        void onFailedRecordedUserInput(@NotNull RecorderUserInput input);

        void onFinishedRecording(@Nullable RecorderResult result, boolean success, @Nullable Exception exception);
    }
    
    /**
    * Receives requests about a pressed keyboard key.
    * 
    * The listener may listen for a specific key or any key.
    */
    interface HotkeyListener {
        boolean isListeningForAnyHotkey();

        @Nullable Hotkey getHotkey();

        void onHotkeyPressed(@NotNull Hotkey hotkey);
    }
    
    /**
    * A service that listens to system user input and records it as RecorderResult.
    * 
    * The BaseRecorderListener delegate methods are always called on the java AWT queue.
    * 
    * @author Bytevi
    */
    class Impl implements Protocol, RecorderJHookListener.Delegate {
        private final RecorderMasterNativeParser.Protocol _masterParser = DI.get(RecorderMasterNativeParser.Protocol.class);
    
        @NotNull private final Object _lock = new Object();

        @Nullable private RecorderJHookListener.Service _nativeListener;

        private boolean _recording = false;
        @Nullable private Listener _listener;
        @Nullable private Model _recorderModel;

        // # Public

        @Override
        public void preload()
        {
            // Setup the Recorder, to make sure its up and running so when starting
            // the recording, no delay will be experienced
            startIfNotStarted();
        }

        @Override
        public void start(@NotNull RecorderNativeParser.Protocol parser, @NotNull Model model, @NotNull Listener listener) throws Exception
        {
            start(parser, model, listener, null);
        }

        @Override
        public void start(@NotNull RecorderNativeParser.Protocol parser, @NotNull Model model, @NotNull Listener listener, @Nullable Hotkey stopHotkey) throws Exception
        {
            startIfNotStarted();

            Dimension size = DeviceScreen.getPrimaryScreenSize();

            Logger.messageEvent(this, "Start recording on screen size " + size.width + "x" + size.height + "...");

            synchronized (_lock)
            {
                if (_recording)
                {
                    Errors.throwIllegalStateError("Cannot start Recorder, already started");
                }
            }

            synchronized (_lock)
            {
                _recording = true;
                _recorderModel = model;
                _listener = listener;
            }
        }

        @Override
        public void stop() throws Exception
        {
            Logger.messageEvent(this, "Stop!");

            boolean isRecording;

            synchronized (_lock)
            {
                if (!_recording)
                {
                    Errors.throwIllegalStateError("Cannot stop Recorder, must be recording first");
                }

                isRecording = _recording;
            }

            if (isRecording)
            {
                cancel(false, null);
            }
        }

        @Override
        public void registerHotkeyListener(@NotNull HotkeyListener listener)
        {
            startIfNotStarted();

            _masterParser.registerHotkeyListener(listener);
        }

        @Override
        public void unregisterHotkeyListener(@NotNull HotkeyListener listener)
        {
            _masterParser.unregisterHotkeyListener(listener);
        }

        @Override
        public void registerPlayStopHotkeyListener(@NotNull HotkeyListener listener)
        {
            startIfNotStarted();

            Logger.messageEvent(this, "Registering a play/stop hotkey listener for client " + listener.toString());
            _masterParser.setPlayStopHotkeyListener(listener);
        }

        @Override
        public void unregisterPlayStopHotkeyListener()
        {
            var l = _masterParser.getPlayStopHotkeyListener();

            if (l != null)
            {
                Logger.messageEvent(this, "Unregistering a play/stop hotkey listener for client " + l.toString());
            }

            _masterParser.setPlayStopHotkeyListener(null);
        }

        // # RecorderJHookListenerDelegate

        @Override
        public void onParseInput(@NotNull RecorderUserInput input)
        {
            // If not recording, do nothing
            if (!_recording)
            {
                return;
            }

            // Add the parsed input to the model
            try {
                _recorderModel.addInput(input);
            } catch (Exception e) {
                try {
                    Logger.messageEvent(this, "Cancel! Exception encountered: " + e.toString());
                    e.printStackTrace(System.out);
                    _listener.onFailedRecordedUserInput(input);
                    cancel(false, e);
                } catch (Exception e2) {

                }

                return;
            }

            // Alert listener
            if (_listener != null)
            {
                _listener.onRecordedUserInput(input);
            }
        }

        @Override
        public void onInputDataChange()
        {
            // If not recording, do nothing
            if (!_recording)
            {
                return;
            }

            // Alert listener
            if (_listener != null)
            {
                _listener.onRecordedUserInputChanged();
            }
        }

        // # Private
        
        private ArrayList<RecorderParserFlag> getDefaultFlags()
        {
            return DI.get(Recorder.Defaults.class).getDefaultRecordFlags();
        }

        private void startIfNotStarted()
        {
            if (_nativeListener != null)
            {
                return;
            }

            var parser = _masterParser.getSubparser();
            parser.setFlags(getDefaultFlags());
            _nativeListener = new RecorderJHookListener.Service(_masterParser.getSubparser(), this);

            try {
                _nativeListener.start();
            } catch (Exception e) {

            }
        }

        private void cancel(boolean success, @Nullable Exception exception) throws Exception
        {
            synchronized (_lock)
            {
                if (!_recording)
                {
                    return;
                }

                _recording = false;

                Listener listener = _listener;

                _listener = null;

                RecorderResult result = _recorderModel.retrieveRecordedData();

                listener.onFinishedRecording(result, success, exception);
            }
        }
    }
    
    // Default values
    class Defaults
    {
        public @NotNull ArrayList<RecorderParserFlag> getDefaultRecordFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_MOTION);
            settings.add(RecorderParserFlag.RECORD_MOUSE_WHEEL);
            settings.add(RecorderParserFlag.RECORD_WINDOW_EVENTS);
            //settings.add(RecorderParserFlag.LOG_EVENTS);
            return settings;
        }

        public @NotNull ArrayList<RecorderParserFlag> getRecordOnlyKeyClicksAndMouseMotionFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_MOTION);
            settings.add(RecorderParserFlag.LOG_EVENTS);
            return settings;
        }

        public @NotNull ArrayList<RecorderParserFlag> getRecordOnlyKeyClicksFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            settings.add(RecorderParserFlag.LOG_EVENTS);
            return settings;
        }

        public @NotNull ArrayList<RecorderParserFlag> getRecordOnlyKeyClicksAndMouseMotionSilentlyFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_MOTION);
            return settings;
        }

        public @NotNull ArrayList<RecorderParserFlag> getRecordOnlyKeyClicksSilentlyFlags()
        {
            ArrayList<RecorderParserFlag> settings;
            settings = new ArrayList();
            settings.add(RecorderParserFlag.RECORD_KEYBOARD_EVENTS);
            settings.add(RecorderParserFlag.RECORD_MOUSE_CLICKS);
            return settings;
        }
    }
}
