/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.recorder.parser;

import automater.recorder.model.RecorderUserInput;
import automater.utilities.Errors;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import org.jetbrains.annotations.NotNull;
import org.jnativehook.GlobalScreen;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

/**
 * Wrapper of NativeHookListener.
 * 
 * Requires a parser to translate the jnativehook events to Automater friendly
 * objects: RecorderUserInput.
 * 
 * @author Bytevi
 */
public interface RecorderJHookListener {
    
    
    /**
    * Forwards requests for parsed input objects.
    * 
    * Call onParseInput() when new input is forwarded.
    * Call onInputDataChange() when input data has changed without new input being delivered.
    * 
    * @author Bytevi
    */
    public interface Delegate {
        public void onParseInput(@NotNull RecorderUserInput input);
        public void onInputDataChange();
    }
    
    class Service {
        private static final Object _registerLock = new Object();
        private static SwingDispatchService _swingDispatchService = null;

        private NativeHookListener _nativeHookListener;
        private final RecorderNativeParser.Protocol _parser;
        private final Delegate _delegate;

        public Service(RecorderNativeParser.Protocol parser, Delegate delegate)
        {
            this._parser = parser;
            this._delegate = delegate;
        }

        public boolean isRunning()
        {
            return _nativeHookListener != null;
        }

        public void start() throws Exception
        {
            if (isRunning())
            {
                Errors.throwInternalLogicError("RecorderListener cannot start, it's already running");
            }

            _nativeHookListener = new NativeHookListener();
            _nativeHookListener.parser = _parser;
            _nativeHookListener.delegate = _delegate;

            registerEventDispatcherOnce();
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(_nativeHookListener);
            GlobalScreen.addNativeMouseListener(_nativeHookListener);
            GlobalScreen.addNativeMouseMotionListener(_nativeHookListener);
            GlobalScreen.addNativeMouseWheelListener(_nativeHookListener);
        }

        public void stop() throws Exception
        {
            if (!isRunning())
            {
                Errors.throwInternalLogicError("RecorderListener cannot stop, it's not running at all");
            }

            GlobalScreen.unregisterNativeHook();
            GlobalScreen.removeNativeMouseListener(_nativeHookListener);
            GlobalScreen.removeNativeMouseListener(_nativeHookListener);
            GlobalScreen.removeNativeMouseListener(_nativeHookListener);
            GlobalScreen.removeNativeMouseListener(_nativeHookListener);

            _nativeHookListener = null;
        }

        // # Private

        private void registerEventDispatcherOnce()
        {
            synchronized (_registerLock)
            {
                if (_swingDispatchService == null)
                {
                    _swingDispatchService = new SwingDispatchService();
                    GlobalScreen.setEventDispatcher(_swingDispatchService);
                }
            }
        }
    }
    
    // Hook listener
    class NativeHookListener implements ActionListener, ItemListener, NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener, WindowListener
    {
        private RecorderNativeParser.Protocol parser;
        private Delegate delegate;
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            
        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent nke) {
            
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent nke) {
            RecorderUserInput input = parser.evaluatePress(nke);
            
            if (input != null)
            {
                delegate.onParseInput(input);
            }
            else
            {
                delegate.onInputDataChange();
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nke) {
            RecorderUserInput input = parser.evaluateRelease(nke);
            
            if (input != null)
            {
                delegate.onParseInput(input);
            }
            else
            {
                delegate.onInputDataChange();
            }
        }

        @Override
        public void nativeMouseClicked(NativeMouseEvent nme) {
            
        }

        @Override
        public void nativeMousePressed(NativeMouseEvent nme) {
            RecorderUserInput input = parser.evaluatePress(nme);
            
            if (input != null)
            {
                delegate.onParseInput(input);
            }
            else
            {
                delegate.onInputDataChange();
            }
        }

        @Override
        public void nativeMouseReleased(NativeMouseEvent nme) {
            RecorderUserInput input = parser.evaluateRelease(nme);
            
            if (input != null)
            {
                delegate.onParseInput(input);
            }
            else
            {
                delegate.onInputDataChange();
            }
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent nme) {
            RecorderUserInput input = parser.evaluateMouseMove(nme);
            
            if (input != null)
            {
                delegate.onParseInput(input);
            }
            else
            {
                delegate.onInputDataChange();
            }
        }

        @Override
        public void nativeMouseDragged(NativeMouseEvent nme) {
            RecorderUserInput input = parser.evaluateMouseMove(nme);
            
            if (input != null)
            {
                delegate.onParseInput(input);
            }
            else
            {
                delegate.onInputDataChange();
            }
        }

        @Override
        public void nativeMouseWheelMoved(NativeMouseWheelEvent nmwe) {
            RecorderUserInput input = parser.evaluateMouseWheel(nmwe);
            
            if (input != null)
            {
                delegate.onParseInput(input);
            }
            else
            {
                delegate.onInputDataChange();
            }
        }

        @Override
        public void windowOpened(WindowEvent e) {
            
        }

        @Override
        public void windowClosing(WindowEvent e) {
            
        }

        @Override
        public void windowClosed(WindowEvent e) {
            
        }

        @Override
        public void windowIconified(WindowEvent e) {
            
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            
        }

        @Override
        public void windowActivated(WindowEvent e) {
            
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            
        }
    }
}
