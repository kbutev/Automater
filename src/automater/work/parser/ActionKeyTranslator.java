/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.parser;

import automater.recorder.model.RecorderUserInputKey;
import automater.recorder.model.RecorderUserInputKeyModifierValue;
import automater.recorder.model.RecorderUserInputKeyModifiers;
import automater.recorder.model.RecorderUserInputKeyValue;
import automater.utilities.DeviceOS;
import automater.utilities.Errors;
import automater.work.model.ActionSystemKey;
import automater.work.model.ActionSystemKeyModifierValue;
import automater.work.model.ActionSystemKeyModifiers;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Translates RecorderUserInputKey(s) to ActionSystemKey(s).
 * 
 * @author Bytevi
 */
public class ActionKeyTranslator {
    private static final HashMap<RecorderUserInputKeyValue, Integer> _keyboardMapping = new HashMap<>();
    private static final HashMap<RecorderUserInputKeyValue, Integer> _mouseKeyMapping = new HashMap<>();
    
    public static ActionSystemKey translateKeystroke(RecorderUserInputKey key) throws Exception
    {
        if (isKeyboardRecorderUserInputKey(key))
        {
            return ActionSystemKey.createKeyboardKey(translateKeyboardKey(key));
        }
        
        if (isMouseRecorderUserInputKey(key))
        {
            return ActionSystemKey.createMouseKey(translateMouseKey(key));
        }
        
        Errors.throwInvalidArgument("ActionKeyTranslator cannot translate key '" + key.toString() + "': cannot recognize type");
        return null;
    }
    
    public static ActionSystemKeyModifiers translateModifiers(RecorderUserInputKey key)
    {
        RecorderUserInputKeyModifiers modifiers = key.modifiers;
        Set<RecorderUserInputKeyModifierValue> values = modifiers.getValues();
        
        if (values.isEmpty())
        {
            return ActionSystemKeyModifiers.none();
        }
        
        HashSet<ActionSystemKeyModifierValue> modifierValues = new HashSet<>();
        
        for (RecorderUserInputKeyModifierValue modifier : values)
        {
            if (modifier == RecorderUserInputKeyModifierValue.CTRL)
            {
                modifierValues.add(ActionSystemKeyModifierValue.CTRL);
            }
            
            if (modifier == RecorderUserInputKeyModifierValue.ALT)
            {
                modifierValues.add(ActionSystemKeyModifierValue.ALT);
            }
            
            if (modifier == RecorderUserInputKeyModifierValue.SHIFT)
            {
                modifierValues.add(ActionSystemKeyModifierValue.SHIFT);
            }
        }
        
        return ActionSystemKeyModifiers.createModifierValues(modifierValues);
    }
    
    public static boolean isKeyboardRecorderUserInputKey(RecorderUserInputKey key)
    {
        return key.isKeyboardKey();
    }
    
    public static boolean isMouseRecorderUserInputKey(RecorderUserInputKey key)
    {
        return key.isMouseKey();
    }
    
    public static int translateKeyboardKey(RecorderUserInputKey key) throws Exception
    {
        HashMap<RecorderUserInputKeyValue, Integer> keyMapping = getKeyboardMapping();
        
        if (keyMapping.containsKey(key.value))
        {
            return keyMapping.get(key.value);
        }
        
        return 0;
    }
    
    public static int translateMouseKey(RecorderUserInputKey key) throws Exception
    {
        HashMap<RecorderUserInputKeyValue, Integer> keyMapping = getMouseKeyMapping();
        
        if (keyMapping.containsKey(key.value))
        {
            return keyMapping.get(key.value);
        }
        
        return 0;
    }
    
    public static synchronized HashMap<RecorderUserInputKeyValue, Integer> getKeyboardMapping()
    {
        if (_keyboardMapping.size() > 0)
        {
            return _keyboardMapping;
        }
        
        _keyboardMapping.put(RecorderUserInputKeyValue._0, KeyEvent.VK_0);
        _keyboardMapping.put(RecorderUserInputKeyValue._1, KeyEvent.VK_1);
        _keyboardMapping.put(RecorderUserInputKeyValue._2, KeyEvent.VK_2);
        _keyboardMapping.put(RecorderUserInputKeyValue._3, KeyEvent.VK_3);
        _keyboardMapping.put(RecorderUserInputKeyValue._4, KeyEvent.VK_4);
        _keyboardMapping.put(RecorderUserInputKeyValue._5, KeyEvent.VK_5);
        _keyboardMapping.put(RecorderUserInputKeyValue._6, KeyEvent.VK_6);
        _keyboardMapping.put(RecorderUserInputKeyValue._7, KeyEvent.VK_7);
        _keyboardMapping.put(RecorderUserInputKeyValue._8, KeyEvent.VK_8);
        _keyboardMapping.put(RecorderUserInputKeyValue._9, KeyEvent.VK_9);
        
        _keyboardMapping.put(RecorderUserInputKeyValue._A, KeyEvent.VK_A);
        _keyboardMapping.put(RecorderUserInputKeyValue._B, KeyEvent.VK_B);
        _keyboardMapping.put(RecorderUserInputKeyValue._C, KeyEvent.VK_C);
        _keyboardMapping.put(RecorderUserInputKeyValue._D, KeyEvent.VK_D);
        _keyboardMapping.put(RecorderUserInputKeyValue._E, KeyEvent.VK_E);
        _keyboardMapping.put(RecorderUserInputKeyValue._F, KeyEvent.VK_F);
        _keyboardMapping.put(RecorderUserInputKeyValue._G, KeyEvent.VK_G);
        _keyboardMapping.put(RecorderUserInputKeyValue._H, KeyEvent.VK_H);
        _keyboardMapping.put(RecorderUserInputKeyValue._I, KeyEvent.VK_I);
        _keyboardMapping.put(RecorderUserInputKeyValue._J, KeyEvent.VK_J);
        _keyboardMapping.put(RecorderUserInputKeyValue._K, KeyEvent.VK_K);
        _keyboardMapping.put(RecorderUserInputKeyValue._L, KeyEvent.VK_L);
        _keyboardMapping.put(RecorderUserInputKeyValue._M, KeyEvent.VK_M);
        _keyboardMapping.put(RecorderUserInputKeyValue._N, KeyEvent.VK_N);
        _keyboardMapping.put(RecorderUserInputKeyValue._O, KeyEvent.VK_O);
        _keyboardMapping.put(RecorderUserInputKeyValue._P, KeyEvent.VK_P);
        _keyboardMapping.put(RecorderUserInputKeyValue._Q, KeyEvent.VK_Q);
        _keyboardMapping.put(RecorderUserInputKeyValue._R, KeyEvent.VK_R);
        _keyboardMapping.put(RecorderUserInputKeyValue._S, KeyEvent.VK_S);
        _keyboardMapping.put(RecorderUserInputKeyValue._T, KeyEvent.VK_T);
        _keyboardMapping.put(RecorderUserInputKeyValue._U, KeyEvent.VK_U);
        _keyboardMapping.put(RecorderUserInputKeyValue._V, KeyEvent.VK_V);
        _keyboardMapping.put(RecorderUserInputKeyValue._W, KeyEvent.VK_W);
        _keyboardMapping.put(RecorderUserInputKeyValue._X, KeyEvent.VK_X);
        _keyboardMapping.put(RecorderUserInputKeyValue._Y, KeyEvent.VK_Y);
        _keyboardMapping.put(RecorderUserInputKeyValue._Z, KeyEvent.VK_Z);
        
        _keyboardMapping.put(RecorderUserInputKeyValue._SPACE, KeyEvent.VK_SPACE);
        _keyboardMapping.put(RecorderUserInputKeyValue._ENTER, KeyEvent.VK_ENTER);
        _keyboardMapping.put(RecorderUserInputKeyValue._ESCAPE, KeyEvent.VK_ESCAPE);
        _keyboardMapping.put(RecorderUserInputKeyValue._BACKSPACE, KeyEvent.VK_BACK_SPACE);
        _keyboardMapping.put(RecorderUserInputKeyValue._MINUS, KeyEvent.VK_MINUS);
        _keyboardMapping.put(RecorderUserInputKeyValue._EQUALS, KeyEvent.VK_EQUALS);
        _keyboardMapping.put(RecorderUserInputKeyValue._TAB, KeyEvent.VK_TAB);
        _keyboardMapping.put(RecorderUserInputKeyValue._CAPS_LOCK, KeyEvent.VK_CAPS_LOCK);
        
        _keyboardMapping.put(RecorderUserInputKeyValue._F1, KeyEvent.VK_F1);
        _keyboardMapping.put(RecorderUserInputKeyValue._F2, KeyEvent.VK_F2);
        _keyboardMapping.put(RecorderUserInputKeyValue._F3, KeyEvent.VK_F3);
        _keyboardMapping.put(RecorderUserInputKeyValue._F4, KeyEvent.VK_F4);
        _keyboardMapping.put(RecorderUserInputKeyValue._F5, KeyEvent.VK_F5);
        _keyboardMapping.put(RecorderUserInputKeyValue._F6, KeyEvent.VK_F6);
        _keyboardMapping.put(RecorderUserInputKeyValue._F7, KeyEvent.VK_F7);
        _keyboardMapping.put(RecorderUserInputKeyValue._F8, KeyEvent.VK_F8);
        _keyboardMapping.put(RecorderUserInputKeyValue._F9, KeyEvent.VK_F9);
        _keyboardMapping.put(RecorderUserInputKeyValue._F10, KeyEvent.VK_F10);
        _keyboardMapping.put(RecorderUserInputKeyValue._F11, KeyEvent.VK_F11);
        _keyboardMapping.put(RecorderUserInputKeyValue._F12, KeyEvent.VK_F12);
        
        _keyboardMapping.put(RecorderUserInputKeyValue._BACKQUOTE, KeyEvent.VK_BACK_QUOTE);
        _keyboardMapping.put(RecorderUserInputKeyValue._OPEN_BRACKET, KeyEvent.VK_OPEN_BRACKET);
        _keyboardMapping.put(RecorderUserInputKeyValue._CLOSE_BRACKET, KeyEvent.VK_CLOSE_BRACKET);
        _keyboardMapping.put(RecorderUserInputKeyValue._BACK_SLASH, KeyEvent.VK_BACK_SLASH);
        _keyboardMapping.put(RecorderUserInputKeyValue._SEMICOLON, KeyEvent.VK_SEMICOLON);
        _keyboardMapping.put(RecorderUserInputKeyValue._QUOTE, KeyEvent.VK_QUOTE);
        _keyboardMapping.put(RecorderUserInputKeyValue._COMMA, KeyEvent.VK_COMMA);
        _keyboardMapping.put(RecorderUserInputKeyValue._PERIOD, KeyEvent.VK_PERIOD);
        _keyboardMapping.put(RecorderUserInputKeyValue._SLASH, KeyEvent.VK_SLASH);
        _keyboardMapping.put(RecorderUserInputKeyValue._PRINTSCREEN, KeyEvent.VK_PRINTSCREEN);
        _keyboardMapping.put(RecorderUserInputKeyValue._SCROLL_LOCK, KeyEvent.VK_SCROLL_LOCK);
        _keyboardMapping.put(RecorderUserInputKeyValue._PAUSE, KeyEvent.VK_PAUSE);
        _keyboardMapping.put(RecorderUserInputKeyValue._INSERT, KeyEvent.VK_INSERT);
        _keyboardMapping.put(RecorderUserInputKeyValue._DELETE, KeyEvent.VK_DELETE);
        _keyboardMapping.put(RecorderUserInputKeyValue._HOME, KeyEvent.VK_HOME);
        _keyboardMapping.put(RecorderUserInputKeyValue._END, KeyEvent.VK_END);
        _keyboardMapping.put(RecorderUserInputKeyValue._PAGE_UP, KeyEvent.VK_PAGE_UP);
        _keyboardMapping.put(RecorderUserInputKeyValue._PAGE_DOWN, KeyEvent.VK_PAGE_DOWN);
        _keyboardMapping.put(RecorderUserInputKeyValue._UP, KeyEvent.VK_UP);
        _keyboardMapping.put(RecorderUserInputKeyValue._LEFT, KeyEvent.VK_LEFT);
        _keyboardMapping.put(RecorderUserInputKeyValue._RIGHT, KeyEvent.VK_RIGHT);
        _keyboardMapping.put(RecorderUserInputKeyValue._DOWN, KeyEvent.VK_DOWN);
        _keyboardMapping.put(RecorderUserInputKeyValue._NUM_LOCK, KeyEvent.VK_NUM_LOCK);
        _keyboardMapping.put(RecorderUserInputKeyValue._SEPARATOR, KeyEvent.VK_SEPARATOR);
        _keyboardMapping.put(RecorderUserInputKeyValue._CONTEXT_MENU, KeyEvent.VK_CONTEXT_MENU);
        
        if (DeviceOS.isWindows())
        {
            _keyboardMapping.put(RecorderUserInputKeyValue._META, KeyEvent.VK_WINDOWS);
        }
        else
        {
            _keyboardMapping.put(RecorderUserInputKeyValue._META, KeyEvent.VK_META);
        }
        
        return _keyboardMapping;
    }
    
    public static synchronized HashMap<RecorderUserInputKeyValue, Integer> getMouseKeyMapping()
    {
        if (_mouseKeyMapping.size() > 0)
        {
            return _mouseKeyMapping;
        }
        
        _mouseKeyMapping.put(RecorderUserInputKeyValue._MOUSE_LEFT_CLICK, InputEvent.BUTTON1_DOWN_MASK);
        _mouseKeyMapping.put(RecorderUserInputKeyValue._MOUSE_MIDDLE_CLICK, InputEvent.BUTTON2_DOWN_MASK);
        _mouseKeyMapping.put(RecorderUserInputKeyValue._MOUSE_RIGHT_CLICK, InputEvent.BUTTON3_DOWN_MASK);
        
        return _mouseKeyMapping;
    }
}
