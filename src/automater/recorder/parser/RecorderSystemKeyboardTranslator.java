/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.parser;

import automater.recorder.model.RecorderUserInputKey;
import automater.recorder.model.RecorderUserInputKeyMask;
import automater.recorder.model.RecorderUserInputKeyMaskValue;
import automater.recorder.model.RecorderUserInputKeyValue;
import automater.utilities.Logger;
import java.util.HashMap;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Translates native keyboard key events.
 * 
 * @author Bytevi
 */
public class RecorderSystemKeyboardTranslator {
    private static HashMap<Integer, RecorderUserInputKeyValue> _keyMapping = new HashMap<>();
    
    private RecorderUserInputKeyMask _mask = RecorderUserInputKeyMask.none();
    
    public RecorderSystemKeyboardTranslator()
    {
        
    }
    
    public RecorderUserInputKeyMask getCurrentMask()
    {
        return _mask;
    }
    
    public RecorderUserInputKey translate(NativeKeyEvent keyEvent)
    {
        return translate(false, keyEvent, true);
    }
    
    public RecorderUserInputKey translate(boolean recordKeystroke, NativeKeyEvent keyEvent, boolean press)
    {
        int keyCode = keyEvent.getKeyCode();
        
        RecorderUserInputKeyValue keyValue = keyValueForJNativeHookKey(keyCode);
        
        if (keyValue == RecorderUserInputKeyValue.UNKNOWN)
        {
            Logger.error(this, "Unrecognized key value " + String.valueOf(keyCode));
            return null;
        }
        
        if (recordKeystroke && keyValue.isModifier())
        {
            handleMaskKey(keyValue, press);
            return null;
        }
        
        return new RecorderUserInputKey(keyValue , _mask);
    }
    
    private void handleMaskKey(RecorderUserInputKeyValue keyValue, boolean press)
    {
        RecorderUserInputKeyMaskValue mask = maskValueFoKey(keyValue);
        
        if (press)
        {
            _mask = _mask.createWithNewAddedFlag(mask);
        }
        else
        {
            _mask = _mask.createWithRemovedFlag(mask);
        }
    }
    
    public static synchronized HashMap<Integer, RecorderUserInputKeyValue> getKeyMapping()
    {
        if (_keyMapping.size() > 0)
        {
            return _keyMapping;
        }
        
        _keyMapping.put(NativeKeyEvent.VC_0, RecorderUserInputKeyValue._0);
        _keyMapping.put(NativeKeyEvent.VC_1, RecorderUserInputKeyValue._1);
        _keyMapping.put(NativeKeyEvent.VC_2, RecorderUserInputKeyValue._2);
        _keyMapping.put(NativeKeyEvent.VC_3, RecorderUserInputKeyValue._3);
        _keyMapping.put(NativeKeyEvent.VC_4, RecorderUserInputKeyValue._4);
        _keyMapping.put(NativeKeyEvent.VC_5, RecorderUserInputKeyValue._5);
        _keyMapping.put(NativeKeyEvent.VC_6, RecorderUserInputKeyValue._6);
        _keyMapping.put(NativeKeyEvent.VC_7, RecorderUserInputKeyValue._7);
        _keyMapping.put(NativeKeyEvent.VC_8, RecorderUserInputKeyValue._8);
        _keyMapping.put(NativeKeyEvent.VC_9, RecorderUserInputKeyValue._9);
        
        _keyMapping.put(NativeKeyEvent.VC_A, RecorderUserInputKeyValue._A);
        _keyMapping.put(NativeKeyEvent.VC_B, RecorderUserInputKeyValue._B);
        _keyMapping.put(NativeKeyEvent.VC_C, RecorderUserInputKeyValue._C);
        _keyMapping.put(NativeKeyEvent.VC_D, RecorderUserInputKeyValue._D);
        _keyMapping.put(NativeKeyEvent.VC_E, RecorderUserInputKeyValue._E);
        _keyMapping.put(NativeKeyEvent.VC_F, RecorderUserInputKeyValue._F);
        _keyMapping.put(NativeKeyEvent.VC_G, RecorderUserInputKeyValue._G);
        _keyMapping.put(NativeKeyEvent.VC_H, RecorderUserInputKeyValue._H);
        _keyMapping.put(NativeKeyEvent.VC_I, RecorderUserInputKeyValue._I);
        _keyMapping.put(NativeKeyEvent.VC_J, RecorderUserInputKeyValue._J);
        _keyMapping.put(NativeKeyEvent.VC_K, RecorderUserInputKeyValue._K);
        _keyMapping.put(NativeKeyEvent.VC_L, RecorderUserInputKeyValue._L);
        _keyMapping.put(NativeKeyEvent.VC_M, RecorderUserInputKeyValue._M);
        _keyMapping.put(NativeKeyEvent.VC_N, RecorderUserInputKeyValue._N);
        _keyMapping.put(NativeKeyEvent.VC_0, RecorderUserInputKeyValue._O);
        _keyMapping.put(NativeKeyEvent.VC_P, RecorderUserInputKeyValue._P);
        _keyMapping.put(NativeKeyEvent.VC_Q, RecorderUserInputKeyValue._Q);
        _keyMapping.put(NativeKeyEvent.VC_R, RecorderUserInputKeyValue._R);
        _keyMapping.put(NativeKeyEvent.VC_S, RecorderUserInputKeyValue._S);
        _keyMapping.put(NativeKeyEvent.VC_T, RecorderUserInputKeyValue._T);
        _keyMapping.put(NativeKeyEvent.VC_U, RecorderUserInputKeyValue._U);
        _keyMapping.put(NativeKeyEvent.VC_V, RecorderUserInputKeyValue._V);
        _keyMapping.put(NativeKeyEvent.VC_W, RecorderUserInputKeyValue._W);
        _keyMapping.put(NativeKeyEvent.VC_X, RecorderUserInputKeyValue._X);
        _keyMapping.put(NativeKeyEvent.VC_Y, RecorderUserInputKeyValue._Y);
        _keyMapping.put(NativeKeyEvent.VC_Z, RecorderUserInputKeyValue._Z);
        
        _keyMapping.put(NativeKeyEvent.VC_SHIFT, RecorderUserInputKeyValue._SHIFT);
        _keyMapping.put(NativeKeyEvent.VC_CONTROL, RecorderUserInputKeyValue._CONTROL);
        _keyMapping.put(NativeKeyEvent.VC_ALT, RecorderUserInputKeyValue._ALT);
        
        _keyMapping.put(NativeKeyEvent.VC_SPACE, RecorderUserInputKeyValue._SPACE);
        _keyMapping.put(NativeKeyEvent.VC_ENTER, RecorderUserInputKeyValue._ENTER);
        _keyMapping.put(NativeKeyEvent.VC_ESCAPE, RecorderUserInputKeyValue._ESCAPE);
        _keyMapping.put(NativeKeyEvent.VC_BACKSPACE, RecorderUserInputKeyValue._BACKSPACE);
        _keyMapping.put(NativeKeyEvent.VC_MINUS, RecorderUserInputKeyValue._MINUS);
        _keyMapping.put(NativeKeyEvent.VC_EQUALS, RecorderUserInputKeyValue._EQUALS);
        _keyMapping.put(NativeKeyEvent.VC_TAB, RecorderUserInputKeyValue._TAB);
        _keyMapping.put(NativeKeyEvent.VC_CAPS_LOCK, RecorderUserInputKeyValue._CAPS_LOCK);
        
        _keyMapping.put(NativeKeyEvent.VC_F1, RecorderUserInputKeyValue._F1);
        _keyMapping.put(NativeKeyEvent.VC_F2, RecorderUserInputKeyValue._F2);
        _keyMapping.put(NativeKeyEvent.VC_F3, RecorderUserInputKeyValue._F3);
        _keyMapping.put(NativeKeyEvent.VC_F4, RecorderUserInputKeyValue._F4);
        _keyMapping.put(NativeKeyEvent.VC_F5, RecorderUserInputKeyValue._F5);
        _keyMapping.put(NativeKeyEvent.VC_F6, RecorderUserInputKeyValue._F6);
        _keyMapping.put(NativeKeyEvent.VC_F7, RecorderUserInputKeyValue._F7);
        _keyMapping.put(NativeKeyEvent.VC_F8, RecorderUserInputKeyValue._F8);
        _keyMapping.put(NativeKeyEvent.VC_F9, RecorderUserInputKeyValue._F9);
        _keyMapping.put(NativeKeyEvent.VC_F10, RecorderUserInputKeyValue._F10);
        _keyMapping.put(NativeKeyEvent.VC_F11, RecorderUserInputKeyValue._F11);
        _keyMapping.put(NativeKeyEvent.VC_F12, RecorderUserInputKeyValue._F12);
        
        _keyMapping.put(NativeKeyEvent.VC_BACKQUOTE, RecorderUserInputKeyValue._BACKQUOTE);
        _keyMapping.put(NativeKeyEvent.VC_OPEN_BRACKET, RecorderUserInputKeyValue._OPEN_BRACKET);
        _keyMapping.put(NativeKeyEvent.VC_CLOSE_BRACKET, RecorderUserInputKeyValue._CLOSE_BRACKET);
        _keyMapping.put(NativeKeyEvent.VC_BACK_SLASH, RecorderUserInputKeyValue._BACK_SLASH);
        _keyMapping.put(NativeKeyEvent.VC_SEMICOLON, RecorderUserInputKeyValue._SEMICOLON);
        _keyMapping.put(NativeKeyEvent.VC_QUOTE, RecorderUserInputKeyValue._QUOTE);
        _keyMapping.put(NativeKeyEvent.VC_COMMA, RecorderUserInputKeyValue._COMMA);
        _keyMapping.put(NativeKeyEvent.VC_PERIOD, RecorderUserInputKeyValue._PERIOD);
        _keyMapping.put(NativeKeyEvent.VC_SLASH, RecorderUserInputKeyValue._SLASH);
        _keyMapping.put(NativeKeyEvent.VC_PRINTSCREEN, RecorderUserInputKeyValue._PRINTSCREEN);
        _keyMapping.put(NativeKeyEvent.VC_SCROLL_LOCK, RecorderUserInputKeyValue._SCROLL_LOCK);
        _keyMapping.put(NativeKeyEvent.VC_PAUSE, RecorderUserInputKeyValue._PAUSE);
        _keyMapping.put(NativeKeyEvent.VC_INSERT, RecorderUserInputKeyValue._INSERT);
        _keyMapping.put(NativeKeyEvent.VC_DELETE, RecorderUserInputKeyValue._DELETE);
        _keyMapping.put(NativeKeyEvent.VC_HOME, RecorderUserInputKeyValue._HOME);
        _keyMapping.put(NativeKeyEvent.VC_END, RecorderUserInputKeyValue._END);
        _keyMapping.put(NativeKeyEvent.VC_PAGE_UP, RecorderUserInputKeyValue._PAGE_UP);
        _keyMapping.put(NativeKeyEvent.VC_PAGE_DOWN, RecorderUserInputKeyValue._PAGE_DOWN);
        _keyMapping.put(NativeKeyEvent.VC_UP, RecorderUserInputKeyValue._UP);
        _keyMapping.put(NativeKeyEvent.VC_LEFT, RecorderUserInputKeyValue._LEFT);
        _keyMapping.put(NativeKeyEvent.VC_CLEAR, RecorderUserInputKeyValue._CLEAR);
        _keyMapping.put(NativeKeyEvent.VC_RIGHT, RecorderUserInputKeyValue._RIGHT);
        _keyMapping.put(NativeKeyEvent.VC_DOWN, RecorderUserInputKeyValue._DOWN);
        _keyMapping.put(NativeKeyEvent.VC_NUM_LOCK, RecorderUserInputKeyValue._NUM_LOCK);
        _keyMapping.put(NativeKeyEvent.VC_SEPARATOR, RecorderUserInputKeyValue._SEPARATOR);
        _keyMapping.put(NativeKeyEvent.VC_META, RecorderUserInputKeyValue._META);
        _keyMapping.put(NativeKeyEvent.VC_CONTEXT_MENU, RecorderUserInputKeyValue._CONTEXT_MENU);
        _keyMapping.put(NativeKeyEvent.VC_POWER, RecorderUserInputKeyValue._POWER);
        _keyMapping.put(NativeKeyEvent.VC_SLEEP, RecorderUserInputKeyValue._SLEEP);
        _keyMapping.put(NativeKeyEvent.VC_WAKE, RecorderUserInputKeyValue._WAKE);
        _keyMapping.put(NativeKeyEvent.VC_MEDIA_PLAY, RecorderUserInputKeyValue._MEDIA_PLAY);
        _keyMapping.put(NativeKeyEvent.VC_MEDIA_STOP, RecorderUserInputKeyValue._MEDIA_STOP);
        _keyMapping.put(NativeKeyEvent.VC_MEDIA_PREVIOUS, RecorderUserInputKeyValue._MEDIA_PREVIOUS);
        _keyMapping.put(NativeKeyEvent.VC_MEDIA_NEXT, RecorderUserInputKeyValue._MEDIA_NEXT);
        _keyMapping.put(NativeKeyEvent.VC_MEDIA_SELECT, RecorderUserInputKeyValue._MEDIA_SELECT);
        _keyMapping.put(NativeKeyEvent.VC_MEDIA_EJECT, RecorderUserInputKeyValue._MEDIA_EJECT);
        _keyMapping.put(NativeKeyEvent.VC_VOLUME_MUTE, RecorderUserInputKeyValue._VOLUME_MUTE);
        _keyMapping.put(NativeKeyEvent.VC_VOLUME_UP, RecorderUserInputKeyValue._VOLUME_UP);
        _keyMapping.put(NativeKeyEvent.VC_VOLUME_DOWN, RecorderUserInputKeyValue._VOLUME_DOWN);
        
        return _keyMapping;
    }
    
    public static RecorderUserInputKeyValue keyValueForJNativeHookKey(int keyCode)
    {
        RecorderUserInputKeyValue result = getKeyMapping().get(keyCode);
        
        if (result == null)
        {
            return RecorderUserInputKeyValue.UNKNOWN;
        }
        
        return result;
    }
    
    public static RecorderUserInputKeyMaskValue maskValueFoKey(RecorderUserInputKeyValue keyValue)
    {
        if (keyValue == RecorderUserInputKeyValue._SHIFT)
        {
            return RecorderUserInputKeyMaskValue.SHIFT;
        }
        
        if (keyValue == RecorderUserInputKeyValue._CONTROL)
        {
            return RecorderUserInputKeyMaskValue.CTRL;
        }
        
        if (keyValue == RecorderUserInputKeyValue._ALT)
        {
            return RecorderUserInputKeyMaskValue.ALT;
        }
            
        return RecorderUserInputKeyMaskValue.NONE;
    }
}
