/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.parser;

import automater.input.InputKey;
import automater.input.InputKeyModifierValue;
import automater.input.InputKeyModifiers;
import automater.input.InputKeyValue;
import automater.utilities.DeviceOS;
import automater.utilities.Errors;
import automater.work.model.ActionSystemKey;
import automater.work.model.ActionSystemKeyModifierValue;
import automater.work.model.ActionSystemKeyModifiers;
import com.sun.istack.internal.NotNull;
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
    @NotNull private static final HashMap<InputKeyValue, Integer> _keyboardMapping = new HashMap<>();
    @NotNull private static final HashMap<InputKeyValue, Integer> _mouseKeyMapping = new HashMap<>();
    
    public static ActionSystemKey translateKeystroke(@NotNull InputKey key) throws Exception
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
    
    public static ActionSystemKeyModifiers translateModifiers(@NotNull InputKey key)
    {
        InputKeyModifiers modifiers = key.modifiers;
        Set<InputKeyModifierValue> values = modifiers.getValues();
        
        if (values.isEmpty())
        {
            return ActionSystemKeyModifiers.none();
        }
        
        HashSet<ActionSystemKeyModifierValue> modifierValues = new HashSet<>();
        
        for (InputKeyModifierValue modifier : values)
        {
            if (modifier == InputKeyModifierValue.CTRL)
            {
                modifierValues.add(ActionSystemKeyModifierValue.CTRL);
            }
            
            if (modifier == InputKeyModifierValue.ALT)
            {
                modifierValues.add(ActionSystemKeyModifierValue.ALT);
            }
            
            if (modifier == InputKeyModifierValue.SHIFT)
            {
                modifierValues.add(ActionSystemKeyModifierValue.SHIFT);
            }
        }
        
        return ActionSystemKeyModifiers.createModifierValues(modifierValues);
    }
    
    public static boolean isKeyboardRecorderUserInputKey(@NotNull InputKey key)
    {
        return key.isKeyboardKey();
    }
    
    public static boolean isMouseRecorderUserInputKey(@NotNull InputKey key)
    {
        return key.isMouseKey();
    }
    
    public static int translateKeyboardKey(@NotNull InputKey key) throws Exception
    {
        HashMap<InputKeyValue, Integer> keyMapping = getKeyboardMapping();
        
        if (keyMapping.containsKey(key.value))
        {
            return keyMapping.get(key.value);
        }
        
        return 0;
    }
    
    public static int translateMouseKey(@NotNull InputKey key) throws Exception
    {
        HashMap<InputKeyValue, Integer> keyMapping = getMouseKeyMapping();
        
        if (keyMapping.containsKey(key.value))
        {
            return keyMapping.get(key.value);
        }
        
        return 0;
    }
    
    public static synchronized @NotNull HashMap<InputKeyValue, Integer> getKeyboardMapping()
    {
        if (_keyboardMapping.size() > 0)
        {
            return _keyboardMapping;
        }
        
        _keyboardMapping.put(InputKeyValue._0, KeyEvent.VK_0);
        _keyboardMapping.put(InputKeyValue._1, KeyEvent.VK_1);
        _keyboardMapping.put(InputKeyValue._2, KeyEvent.VK_2);
        _keyboardMapping.put(InputKeyValue._3, KeyEvent.VK_3);
        _keyboardMapping.put(InputKeyValue._4, KeyEvent.VK_4);
        _keyboardMapping.put(InputKeyValue._5, KeyEvent.VK_5);
        _keyboardMapping.put(InputKeyValue._6, KeyEvent.VK_6);
        _keyboardMapping.put(InputKeyValue._7, KeyEvent.VK_7);
        _keyboardMapping.put(InputKeyValue._8, KeyEvent.VK_8);
        _keyboardMapping.put(InputKeyValue._9, KeyEvent.VK_9);
        
        _keyboardMapping.put(InputKeyValue._A, KeyEvent.VK_A);
        _keyboardMapping.put(InputKeyValue._B, KeyEvent.VK_B);
        _keyboardMapping.put(InputKeyValue._C, KeyEvent.VK_C);
        _keyboardMapping.put(InputKeyValue._D, KeyEvent.VK_D);
        _keyboardMapping.put(InputKeyValue._E, KeyEvent.VK_E);
        _keyboardMapping.put(InputKeyValue._F, KeyEvent.VK_F);
        _keyboardMapping.put(InputKeyValue._G, KeyEvent.VK_G);
        _keyboardMapping.put(InputKeyValue._H, KeyEvent.VK_H);
        _keyboardMapping.put(InputKeyValue._I, KeyEvent.VK_I);
        _keyboardMapping.put(InputKeyValue._J, KeyEvent.VK_J);
        _keyboardMapping.put(InputKeyValue._K, KeyEvent.VK_K);
        _keyboardMapping.put(InputKeyValue._L, KeyEvent.VK_L);
        _keyboardMapping.put(InputKeyValue._M, KeyEvent.VK_M);
        _keyboardMapping.put(InputKeyValue._N, KeyEvent.VK_N);
        _keyboardMapping.put(InputKeyValue._O, KeyEvent.VK_O);
        _keyboardMapping.put(InputKeyValue._P, KeyEvent.VK_P);
        _keyboardMapping.put(InputKeyValue._Q, KeyEvent.VK_Q);
        _keyboardMapping.put(InputKeyValue._R, KeyEvent.VK_R);
        _keyboardMapping.put(InputKeyValue._S, KeyEvent.VK_S);
        _keyboardMapping.put(InputKeyValue._T, KeyEvent.VK_T);
        _keyboardMapping.put(InputKeyValue._U, KeyEvent.VK_U);
        _keyboardMapping.put(InputKeyValue._V, KeyEvent.VK_V);
        _keyboardMapping.put(InputKeyValue._W, KeyEvent.VK_W);
        _keyboardMapping.put(InputKeyValue._X, KeyEvent.VK_X);
        _keyboardMapping.put(InputKeyValue._Y, KeyEvent.VK_Y);
        _keyboardMapping.put(InputKeyValue._Z, KeyEvent.VK_Z);
        
        _keyboardMapping.put(InputKeyValue._SPACE, KeyEvent.VK_SPACE);
        _keyboardMapping.put(InputKeyValue._ENTER, KeyEvent.VK_ENTER);
        _keyboardMapping.put(InputKeyValue._ESCAPE, KeyEvent.VK_ESCAPE);
        _keyboardMapping.put(InputKeyValue._BACKSPACE, KeyEvent.VK_BACK_SPACE);
        _keyboardMapping.put(InputKeyValue._MINUS, KeyEvent.VK_MINUS);
        _keyboardMapping.put(InputKeyValue._EQUALS, KeyEvent.VK_EQUALS);
        _keyboardMapping.put(InputKeyValue._TAB, KeyEvent.VK_TAB);
        _keyboardMapping.put(InputKeyValue._CAPS_LOCK, KeyEvent.VK_CAPS_LOCK);
        
        _keyboardMapping.put(InputKeyValue._F1, KeyEvent.VK_F1);
        _keyboardMapping.put(InputKeyValue._F2, KeyEvent.VK_F2);
        _keyboardMapping.put(InputKeyValue._F3, KeyEvent.VK_F3);
        _keyboardMapping.put(InputKeyValue._F4, KeyEvent.VK_F4);
        _keyboardMapping.put(InputKeyValue._F5, KeyEvent.VK_F5);
        _keyboardMapping.put(InputKeyValue._F6, KeyEvent.VK_F6);
        _keyboardMapping.put(InputKeyValue._F7, KeyEvent.VK_F7);
        _keyboardMapping.put(InputKeyValue._F8, KeyEvent.VK_F8);
        _keyboardMapping.put(InputKeyValue._F9, KeyEvent.VK_F9);
        _keyboardMapping.put(InputKeyValue._F10, KeyEvent.VK_F10);
        _keyboardMapping.put(InputKeyValue._F11, KeyEvent.VK_F11);
        _keyboardMapping.put(InputKeyValue._F12, KeyEvent.VK_F12);
        
        _keyboardMapping.put(InputKeyValue._BACKQUOTE, KeyEvent.VK_BACK_QUOTE);
        _keyboardMapping.put(InputKeyValue._OPEN_BRACKET, KeyEvent.VK_OPEN_BRACKET);
        _keyboardMapping.put(InputKeyValue._CLOSE_BRACKET, KeyEvent.VK_CLOSE_BRACKET);
        _keyboardMapping.put(InputKeyValue._BACK_SLASH, KeyEvent.VK_BACK_SLASH);
        _keyboardMapping.put(InputKeyValue._SEMICOLON, KeyEvent.VK_SEMICOLON);
        _keyboardMapping.put(InputKeyValue._QUOTE, KeyEvent.VK_QUOTE);
        _keyboardMapping.put(InputKeyValue._COMMA, KeyEvent.VK_COMMA);
        _keyboardMapping.put(InputKeyValue._PERIOD, KeyEvent.VK_PERIOD);
        _keyboardMapping.put(InputKeyValue._SLASH, KeyEvent.VK_SLASH);
        _keyboardMapping.put(InputKeyValue._PRINTSCREEN, KeyEvent.VK_PRINTSCREEN);
        _keyboardMapping.put(InputKeyValue._SCROLL_LOCK, KeyEvent.VK_SCROLL_LOCK);
        _keyboardMapping.put(InputKeyValue._PAUSE, KeyEvent.VK_PAUSE);
        _keyboardMapping.put(InputKeyValue._INSERT, KeyEvent.VK_INSERT);
        _keyboardMapping.put(InputKeyValue._DELETE, KeyEvent.VK_DELETE);
        _keyboardMapping.put(InputKeyValue._HOME, KeyEvent.VK_HOME);
        _keyboardMapping.put(InputKeyValue._END, KeyEvent.VK_END);
        _keyboardMapping.put(InputKeyValue._PAGE_UP, KeyEvent.VK_PAGE_UP);
        _keyboardMapping.put(InputKeyValue._PAGE_DOWN, KeyEvent.VK_PAGE_DOWN);
        _keyboardMapping.put(InputKeyValue._UP, KeyEvent.VK_UP);
        _keyboardMapping.put(InputKeyValue._LEFT, KeyEvent.VK_LEFT);
        _keyboardMapping.put(InputKeyValue._RIGHT, KeyEvent.VK_RIGHT);
        _keyboardMapping.put(InputKeyValue._DOWN, KeyEvent.VK_DOWN);
        _keyboardMapping.put(InputKeyValue._NUM_LOCK, KeyEvent.VK_NUM_LOCK);
        _keyboardMapping.put(InputKeyValue._SEPARATOR, KeyEvent.VK_SEPARATOR);
        _keyboardMapping.put(InputKeyValue._CONTEXT_MENU, KeyEvent.VK_CONTEXT_MENU);
        
        if (DeviceOS.isWindows())
        {
            _keyboardMapping.put(InputKeyValue._META, KeyEvent.VK_WINDOWS);
        }
        else
        {
            _keyboardMapping.put(InputKeyValue._META, KeyEvent.VK_META);
        }
        
        return _keyboardMapping;
    }
    
    public static synchronized @NotNull HashMap<InputKeyValue, Integer> getMouseKeyMapping()
    {
        if (_mouseKeyMapping.size() > 0)
        {
            return _mouseKeyMapping;
        }
        
        _mouseKeyMapping.put(InputKeyValue._MOUSE_LEFT_CLICK, InputEvent.BUTTON1_DOWN_MASK);
        _mouseKeyMapping.put(InputKeyValue._MOUSE_MIDDLE_CLICK, InputEvent.BUTTON2_DOWN_MASK);
        _mouseKeyMapping.put(InputKeyValue._MOUSE_RIGHT_CLICK, InputEvent.BUTTON3_DOWN_MASK);
        
        return _mouseKeyMapping;
    }
}
