/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.utilities.Errors;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;
import automater.recorder.parser.BaseRecorderInputParser;

/**
 * Wrapper of NativeHookListener.
 * 
 * Requires a parser to translate user commands from jnativehook events to
 * RecorderUserInput objects.
 * 
 * @author Bytevi
 */
public class RecorderNativeListener {
    private static final SwingDispatchService _swingDispatchService = new SwingDispatchService();
    
    private NativeHookListener _nativeHookListener;
    private final BaseRecorderInputParser _parser;
    
    public RecorderNativeListener(BaseRecorderInputParser parser)
    {
        this._parser = parser;
        
        GlobalScreen.setEventDispatcher(_swingDispatchService);
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
    
    private class NativeHookListener implements ActionListener, ItemListener, NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener, WindowListener
    {
        public NativeHookListener()
        {
            
        }
        
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
            _parser.evaluatePress(nke);
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nke) {
            _parser.evaluateRelease(nke);
        }

        @Override
        public void nativeMouseClicked(NativeMouseEvent nme) {
            
        }

        @Override
        public void nativeMousePressed(NativeMouseEvent nme) {
            _parser.evaluatePress(nme);
        }

        @Override
        public void nativeMouseReleased(NativeMouseEvent nme) {
            _parser.evaluateRelease(nme);
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent nme) {
            _parser.evaluate(nme);
        }

        @Override
        public void nativeMouseDragged(NativeMouseEvent nme) {
            
        }

        @Override
        public void nativeMouseWheelMoved(NativeMouseWheelEvent nmwe) {
            _parser.evaluate(nmwe);
        }

        @Override
        public void windowOpened(WindowEvent e) {
            _parser.evaluate(e);
        }

        @Override
        public void windowClosing(WindowEvent e) {
            _parser.evaluate(e);
        }

        @Override
        public void windowClosed(WindowEvent e) {
            _parser.evaluate(e);
        }

        @Override
        public void windowIconified(WindowEvent e) {
            
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            
        }

        @Override
        public void windowActivated(WindowEvent e) {
            _parser.evaluate(e);
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            _parser.evaluate(e);
        }
    }
}
