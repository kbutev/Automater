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
    private static HashMap<Integer, RecorderUserInputKeyValue> keyMapping = new HashMap<>();
    
    private RecorderUserInputKeyMask _mask = RecorderUserInputKeyMask.empty();
    
    public RecorderSystemKeyboardTranslator()
    {
        
    }
    
    public RecorderUserInputKeyMask getCurrentMask()
    {
        return _mask;
    }
    
    public RecorderUserInputKey translate(NativeKeyEvent keyEvent, boolean press)
    {
        int keyCode = keyEvent.getKeyCode();
        
        RecorderUserInputKeyValue keyValue = keyValueForJNativeHookKey(keyCode);
        
        if (keyValue == RecorderUserInputKeyValue.UNKNOWN)
        {
            Logger.error(this, "Unrecognized key value " + String.valueOf(keyCode));
            return null;
        }
        
        if (keyValue.isModifier())
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
        
        Logger.messageEvent(this, "Modifier key " + mask.toString() + " " + (press ? "pressed" : "released"));
    }
    
    public static synchronized HashMap<Integer, RecorderUserInputKeyValue> getKeyMapping()
    {
        if (keyMapping.size() > 0)
        {
            return keyMapping;
        }
        
        keyMapping.put(NativeKeyEvent.VC_0, RecorderUserInputKeyValue._0);
        keyMapping.put(NativeKeyEvent.VC_1, RecorderUserInputKeyValue._1);
        keyMapping.put(NativeKeyEvent.VC_2, RecorderUserInputKeyValue._2);
        keyMapping.put(NativeKeyEvent.VC_3, RecorderUserInputKeyValue._3);
        keyMapping.put(NativeKeyEvent.VC_4, RecorderUserInputKeyValue._4);
        keyMapping.put(NativeKeyEvent.VC_5, RecorderUserInputKeyValue._5);
        keyMapping.put(NativeKeyEvent.VC_6, RecorderUserInputKeyValue._6);
        keyMapping.put(NativeKeyEvent.VC_7, RecorderUserInputKeyValue._7);
        keyMapping.put(NativeKeyEvent.VC_8, RecorderUserInputKeyValue._8);
        keyMapping.put(NativeKeyEvent.VC_9, RecorderUserInputKeyValue._9);
        
        keyMapping.put(NativeKeyEvent.VC_A, RecorderUserInputKeyValue._A);
        keyMapping.put(NativeKeyEvent.VC_B, RecorderUserInputKeyValue._B);
        keyMapping.put(NativeKeyEvent.VC_C, RecorderUserInputKeyValue._C);
        keyMapping.put(NativeKeyEvent.VC_D, RecorderUserInputKeyValue._D);
        keyMapping.put(NativeKeyEvent.VC_E, RecorderUserInputKeyValue._E);
        keyMapping.put(NativeKeyEvent.VC_F, RecorderUserInputKeyValue._F);
        keyMapping.put(NativeKeyEvent.VC_G, RecorderUserInputKeyValue._G);
        keyMapping.put(NativeKeyEvent.VC_H, RecorderUserInputKeyValue._H);
        keyMapping.put(NativeKeyEvent.VC_I, RecorderUserInputKeyValue._I);
        keyMapping.put(NativeKeyEvent.VC_J, RecorderUserInputKeyValue._J);
        keyMapping.put(NativeKeyEvent.VC_K, RecorderUserInputKeyValue._K);
        keyMapping.put(NativeKeyEvent.VC_L, RecorderUserInputKeyValue._L);
        keyMapping.put(NativeKeyEvent.VC_M, RecorderUserInputKeyValue._M);
        keyMapping.put(NativeKeyEvent.VC_N, RecorderUserInputKeyValue._N);
        keyMapping.put(NativeKeyEvent.VC_0, RecorderUserInputKeyValue._O);
        keyMapping.put(NativeKeyEvent.VC_P, RecorderUserInputKeyValue._P);
        keyMapping.put(NativeKeyEvent.VC_Q, RecorderUserInputKeyValue._Q);
        keyMapping.put(NativeKeyEvent.VC_R, RecorderUserInputKeyValue._R);
        keyMapping.put(NativeKeyEvent.VC_S, RecorderUserInputKeyValue._S);
        keyMapping.put(NativeKeyEvent.VC_T, RecorderUserInputKeyValue._T);
        keyMapping.put(NativeKeyEvent.VC_U, RecorderUserInputKeyValue._U);
        keyMapping.put(NativeKeyEvent.VC_V, RecorderUserInputKeyValue._V);
        keyMapping.put(NativeKeyEvent.VC_W, RecorderUserInputKeyValue._W);
        keyMapping.put(NativeKeyEvent.VC_X, RecorderUserInputKeyValue._X);
        keyMapping.put(NativeKeyEvent.VC_Y, RecorderUserInputKeyValue._Y);
        keyMapping.put(NativeKeyEvent.VC_Z, RecorderUserInputKeyValue._Z);
        
        keyMapping.put(NativeKeyEvent.VC_SHIFT, RecorderUserInputKeyValue._SHIFT);
        keyMapping.put(NativeKeyEvent.VC_CONTROL, RecorderUserInputKeyValue._CONTROL);
        keyMapping.put(NativeKeyEvent.VC_ALT, RecorderUserInputKeyValue._ALT);
        
        keyMapping.put(NativeKeyEvent.VC_SPACE, RecorderUserInputKeyValue._SPACE);
        keyMapping.put(NativeKeyEvent.VC_ENTER, RecorderUserInputKeyValue._ENTER);
        keyMapping.put(NativeKeyEvent.VC_ESCAPE, RecorderUserInputKeyValue._ESCAPE);
        keyMapping.put(NativeKeyEvent.VC_BACKSPACE, RecorderUserInputKeyValue._BACKSPACE);
        keyMapping.put(NativeKeyEvent.VC_MINUS, RecorderUserInputKeyValue._MINUS);
        keyMapping.put(NativeKeyEvent.VC_EQUALS, RecorderUserInputKeyValue._EQUALS);
        keyMapping.put(NativeKeyEvent.VC_TAB, RecorderUserInputKeyValue._TAB);
        keyMapping.put(NativeKeyEvent.VC_CAPS_LOCK, RecorderUserInputKeyValue._CAPS_LOCK);
        
        keyMapping.put(NativeKeyEvent.VC_F1, RecorderUserInputKeyValue._F1);
        keyMapping.put(NativeKeyEvent.VC_F2, RecorderUserInputKeyValue._F2);
        keyMapping.put(NativeKeyEvent.VC_F3, RecorderUserInputKeyValue._F3);
        keyMapping.put(NativeKeyEvent.VC_F4, RecorderUserInputKeyValue._F4);
        keyMapping.put(NativeKeyEvent.VC_F5, RecorderUserInputKeyValue._F5);
        keyMapping.put(NativeKeyEvent.VC_F6, RecorderUserInputKeyValue._F6);
        keyMapping.put(NativeKeyEvent.VC_F7, RecorderUserInputKeyValue._F7);
        keyMapping.put(NativeKeyEvent.VC_F8, RecorderUserInputKeyValue._F8);
        keyMapping.put(NativeKeyEvent.VC_F9, RecorderUserInputKeyValue._F9);
        keyMapping.put(NativeKeyEvent.VC_F10, RecorderUserInputKeyValue._F10);
        keyMapping.put(NativeKeyEvent.VC_F11, RecorderUserInputKeyValue._F11);
        keyMapping.put(NativeKeyEvent.VC_F12, RecorderUserInputKeyValue._F12);
        
        keyMapping.put(NativeKeyEvent.VC_BACKQUOTE, RecorderUserInputKeyValue._BACKQUOTE);
        keyMapping.put(NativeKeyEvent.VC_OPEN_BRACKET, RecorderUserInputKeyValue._OPEN_BRACKET);
        keyMapping.put(NativeKeyEvent.VC_CLOSE_BRACKET, RecorderUserInputKeyValue._CLOSE_BRACKET);
        keyMapping.put(NativeKeyEvent.VC_BACK_SLASH, RecorderUserInputKeyValue._BACK_SLASH);
        keyMapping.put(NativeKeyEvent.VC_SEMICOLON, RecorderUserInputKeyValue._SEMICOLON);
        keyMapping.put(NativeKeyEvent.VC_QUOTE, RecorderUserInputKeyValue._QUOTE);
        keyMapping.put(NativeKeyEvent.VC_COMMA, RecorderUserInputKeyValue._COMMA);
        keyMapping.put(NativeKeyEvent.VC_PERIOD, RecorderUserInputKeyValue._PERIOD);
        keyMapping.put(NativeKeyEvent.VC_SLASH, RecorderUserInputKeyValue._SLASH);
        keyMapping.put(NativeKeyEvent.VC_PRINTSCREEN, RecorderUserInputKeyValue._PRINTSCREEN);
        keyMapping.put(NativeKeyEvent.VC_SCROLL_LOCK, RecorderUserInputKeyValue._SCROLL_LOCK);
        keyMapping.put(NativeKeyEvent.VC_PAUSE, RecorderUserInputKeyValue._PAUSE);
        keyMapping.put(NativeKeyEvent.VC_INSERT, RecorderUserInputKeyValue._INSERT);
        keyMapping.put(NativeKeyEvent.VC_DELETE, RecorderUserInputKeyValue._DELETE);
        keyMapping.put(NativeKeyEvent.VC_HOME, RecorderUserInputKeyValue._HOME);
        keyMapping.put(NativeKeyEvent.VC_END, RecorderUserInputKeyValue._END);
        keyMapping.put(NativeKeyEvent.VC_PAGE_UP, RecorderUserInputKeyValue._PAGE_UP);
        keyMapping.put(NativeKeyEvent.VC_PAGE_DOWN, RecorderUserInputKeyValue._PAGE_DOWN);
        keyMapping.put(NativeKeyEvent.VC_UP, RecorderUserInputKeyValue._UP);
        keyMapping.put(NativeKeyEvent.VC_LEFT, RecorderUserInputKeyValue._LEFT);
        keyMapping.put(NativeKeyEvent.VC_CLEAR, RecorderUserInputKeyValue._CLEAR);
        keyMapping.put(NativeKeyEvent.VC_RIGHT, RecorderUserInputKeyValue._RIGHT);
        keyMapping.put(NativeKeyEvent.VC_DOWN, RecorderUserInputKeyValue._DOWN);
        keyMapping.put(NativeKeyEvent.VC_NUM_LOCK, RecorderUserInputKeyValue._NUM_LOCK);
        keyMapping.put(NativeKeyEvent.VC_SEPARATOR, RecorderUserInputKeyValue._SEPARATOR);
        keyMapping.put(NativeKeyEvent.VC_META, RecorderUserInputKeyValue._META);
        keyMapping.put(NativeKeyEvent.VC_CONTEXT_MENU, RecorderUserInputKeyValue._CONTEXT_MENU);
        keyMapping.put(NativeKeyEvent.VC_POWER, RecorderUserInputKeyValue._POWER);
        keyMapping.put(NativeKeyEvent.VC_SLEEP, RecorderUserInputKeyValue._SLEEP);
        keyMapping.put(NativeKeyEvent.VC_WAKE, RecorderUserInputKeyValue._WAKE);
        keyMapping.put(NativeKeyEvent.VC_MEDIA_PLAY, RecorderUserInputKeyValue._MEDIA_PLAY);
        keyMapping.put(NativeKeyEvent.VC_MEDIA_STOP, RecorderUserInputKeyValue._MEDIA_STOP);
        keyMapping.put(NativeKeyEvent.VC_MEDIA_PREVIOUS, RecorderUserInputKeyValue._MEDIA_PREVIOUS);
        keyMapping.put(NativeKeyEvent.VC_MEDIA_NEXT, RecorderUserInputKeyValue._MEDIA_NEXT);
        keyMapping.put(NativeKeyEvent.VC_MEDIA_SELECT, RecorderUserInputKeyValue._MEDIA_SELECT);
        keyMapping.put(NativeKeyEvent.VC_MEDIA_EJECT, RecorderUserInputKeyValue._MEDIA_EJECT);
        keyMapping.put(NativeKeyEvent.VC_VOLUME_MUTE, RecorderUserInputKeyValue._VOLUME_MUTE);
        keyMapping.put(NativeKeyEvent.VC_VOLUME_UP, RecorderUserInputKeyValue._VOLUME_UP);
        keyMapping.put(NativeKeyEvent.VC_VOLUME_DOWN, RecorderUserInputKeyValue._VOLUME_DOWN);
        
        return keyMapping;
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
