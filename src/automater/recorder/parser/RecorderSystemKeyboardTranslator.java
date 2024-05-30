/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.recorder.parser;

import automater.input.InputKey;
import automater.input.InputKeyModifiers;
import automater.input.InputKeyModifierValue;
import automater.input.InputKeyValue;
import automater.utilities.Logger;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.List;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Describes a keyboard object, that translates keys and memorizes the pressed keys.
 * 
 * Use translate(NativeKeyEvent) to simply convert NativeKeyEvent to InputKey.
 * Use recordAndTranslate() to convert and record the key.
 * 
 * @author Bytevi
 */
public interface RecorderSystemKeyboardTranslator {
    
    interface Protocol {
        @Nullable InputKey translate(@NotNull NativeKeyEvent keyEvent);
        @Nullable InputKey recordAndTranslate(@NotNull NativeKeyEvent keyEvent, boolean press);

        @NotNull List<InputKeyValue> getCurrentlyPressedKeys();
        @NotNull InputKeyModifiers getCurrentlyPressedModifiers();
    }
    
    /*
     * Implementation.
     * Does NOT support getCurrentlyPressedKeys()! Only modifier keys are recorded!
     */
    class Impl implements Protocol {
        @NotNull private static HashMap<Integer, InputKeyValue> _keyMapping = new HashMap<>();

        @NotNull private InputKeyModifiers _modifiers = InputKeyModifiers.none();
        
        @Override
        public @Nullable InputKey translate(@NotNull NativeKeyEvent keyEvent)
        {
            return recordAndTranslate(false, keyEvent, true);
        }

        @Override
        public @Nullable InputKey recordAndTranslate(@NotNull NativeKeyEvent keyEvent, boolean press)
        {
            return recordAndTranslate(true, keyEvent, press);
        }

        private @Nullable InputKey recordAndTranslate(boolean recordKeystroke, @NotNull NativeKeyEvent keyEvent, boolean press)
        {
            int keyCode = keyEvent.getKeyCode();

            InputKeyValue keyValue = keyValueForJNativeHookKey(keyCode);

            if (keyValue == InputKeyValue.UNKNOWN)
            {
                Logger.error(this, "Unrecognized key value " + String.valueOf(keyCode));
                return null;
            }

            if (recordKeystroke && keyValue.isModifier())
            {
                handleModifierPressedKey(keyValue, press);
                return null;
            }

            return new InputKey(keyValue, _modifiers);
        }

        @Override
        public @NotNull List<InputKeyValue> getCurrentlyPressedKeys()
        {
            // Not supported
            return new ArrayList<>();
        }

        @Override
        public @NotNull InputKeyModifiers getCurrentlyPressedModifiers()
        {
            return _modifiers;
        }

        private void handleModifierPressedKey(@NotNull InputKeyValue keyValue, boolean press)
        {
            InputKeyModifierValue modifier = modifierValueForKey(keyValue);

            if (press)
            {
                _modifiers = _modifiers.createWithNewAddedModifier(modifier);
            }
            else
            {
                _modifiers = _modifiers.createWithRemovedModifier(modifier);
            }
        }

        private InputKeyValue keyValueForJNativeHookKey(int keyCode)
        {
            InputKeyValue result = getKeyMapping().get(keyCode);

            if (result == null)
            {
                return InputKeyValue.UNKNOWN;
            }

            return result;
        }

        private @NotNull InputKeyModifierValue modifierValueForKey(InputKeyValue keyValue)
        {
            if (keyValue == InputKeyValue._SHIFT)
            {
                return InputKeyModifierValue.SHIFT;
            }

            if (keyValue == InputKeyValue._CONTROL)
            {
                return InputKeyModifierValue.CTRL;
            }

            if (keyValue == InputKeyValue._ALT)
            {
                return InputKeyModifierValue.ALT;
            }

            return InputKeyModifierValue.NONE;
        }

        public static synchronized HashMap<Integer, InputKeyValue> getKeyMapping()
        {
            if (_keyMapping.size() > 0)
            {
                return _keyMapping;
            }

            _keyMapping.put(NativeKeyEvent.VC_0, InputKeyValue._0);
            _keyMapping.put(NativeKeyEvent.VC_1, InputKeyValue._1);
            _keyMapping.put(NativeKeyEvent.VC_2, InputKeyValue._2);
            _keyMapping.put(NativeKeyEvent.VC_3, InputKeyValue._3);
            _keyMapping.put(NativeKeyEvent.VC_4, InputKeyValue._4);
            _keyMapping.put(NativeKeyEvent.VC_5, InputKeyValue._5);
            _keyMapping.put(NativeKeyEvent.VC_6, InputKeyValue._6);
            _keyMapping.put(NativeKeyEvent.VC_7, InputKeyValue._7);
            _keyMapping.put(NativeKeyEvent.VC_8, InputKeyValue._8);
            _keyMapping.put(NativeKeyEvent.VC_9, InputKeyValue._9);

            _keyMapping.put(NativeKeyEvent.VC_A, InputKeyValue._A);
            _keyMapping.put(NativeKeyEvent.VC_B, InputKeyValue._B);
            _keyMapping.put(NativeKeyEvent.VC_C, InputKeyValue._C);
            _keyMapping.put(NativeKeyEvent.VC_D, InputKeyValue._D);
            _keyMapping.put(NativeKeyEvent.VC_E, InputKeyValue._E);
            _keyMapping.put(NativeKeyEvent.VC_F, InputKeyValue._F);
            _keyMapping.put(NativeKeyEvent.VC_G, InputKeyValue._G);
            _keyMapping.put(NativeKeyEvent.VC_H, InputKeyValue._H);
            _keyMapping.put(NativeKeyEvent.VC_I, InputKeyValue._I);
            _keyMapping.put(NativeKeyEvent.VC_J, InputKeyValue._J);
            _keyMapping.put(NativeKeyEvent.VC_K, InputKeyValue._K);
            _keyMapping.put(NativeKeyEvent.VC_L, InputKeyValue._L);
            _keyMapping.put(NativeKeyEvent.VC_M, InputKeyValue._M);
            _keyMapping.put(NativeKeyEvent.VC_N, InputKeyValue._N);
            _keyMapping.put(NativeKeyEvent.VC_O, InputKeyValue._O);
            _keyMapping.put(NativeKeyEvent.VC_P, InputKeyValue._P);
            _keyMapping.put(NativeKeyEvent.VC_Q, InputKeyValue._Q);
            _keyMapping.put(NativeKeyEvent.VC_R, InputKeyValue._R);
            _keyMapping.put(NativeKeyEvent.VC_S, InputKeyValue._S);
            _keyMapping.put(NativeKeyEvent.VC_T, InputKeyValue._T);
            _keyMapping.put(NativeKeyEvent.VC_U, InputKeyValue._U);
            _keyMapping.put(NativeKeyEvent.VC_V, InputKeyValue._V);
            _keyMapping.put(NativeKeyEvent.VC_W, InputKeyValue._W);
            _keyMapping.put(NativeKeyEvent.VC_X, InputKeyValue._X);
            _keyMapping.put(NativeKeyEvent.VC_Y, InputKeyValue._Y);
            _keyMapping.put(NativeKeyEvent.VC_Z, InputKeyValue._Z);

            _keyMapping.put(NativeKeyEvent.VC_SHIFT, InputKeyValue._SHIFT);
            _keyMapping.put(NativeKeyEvent.VC_CONTROL, InputKeyValue._CONTROL);
            _keyMapping.put(NativeKeyEvent.VC_ALT, InputKeyValue._ALT);

            _keyMapping.put(NativeKeyEvent.VC_SPACE, InputKeyValue._SPACE);
            _keyMapping.put(NativeKeyEvent.VC_ENTER, InputKeyValue._ENTER);
            _keyMapping.put(NativeKeyEvent.VC_ESCAPE, InputKeyValue._ESCAPE);
            _keyMapping.put(NativeKeyEvent.VC_BACKSPACE, InputKeyValue._BACKSPACE);
            _keyMapping.put(NativeKeyEvent.VC_MINUS, InputKeyValue._MINUS);
            _keyMapping.put(NativeKeyEvent.VC_EQUALS, InputKeyValue._EQUALS);
            _keyMapping.put(NativeKeyEvent.VC_TAB, InputKeyValue._TAB);
            _keyMapping.put(NativeKeyEvent.VC_CAPS_LOCK, InputKeyValue._CAPS_LOCK);

            _keyMapping.put(NativeKeyEvent.VC_F1, InputKeyValue._F1);
            _keyMapping.put(NativeKeyEvent.VC_F2, InputKeyValue._F2);
            _keyMapping.put(NativeKeyEvent.VC_F3, InputKeyValue._F3);
            _keyMapping.put(NativeKeyEvent.VC_F4, InputKeyValue._F4);
            _keyMapping.put(NativeKeyEvent.VC_F5, InputKeyValue._F5);
            _keyMapping.put(NativeKeyEvent.VC_F6, InputKeyValue._F6);
            _keyMapping.put(NativeKeyEvent.VC_F7, InputKeyValue._F7);
            _keyMapping.put(NativeKeyEvent.VC_F8, InputKeyValue._F8);
            _keyMapping.put(NativeKeyEvent.VC_F9, InputKeyValue._F9);
            _keyMapping.put(NativeKeyEvent.VC_F10, InputKeyValue._F10);
            _keyMapping.put(NativeKeyEvent.VC_F11, InputKeyValue._F11);
            _keyMapping.put(NativeKeyEvent.VC_F12, InputKeyValue._F12);

            _keyMapping.put(NativeKeyEvent.VC_BACKQUOTE, InputKeyValue._BACKQUOTE);
            _keyMapping.put(NativeKeyEvent.VC_OPEN_BRACKET, InputKeyValue._OPEN_BRACKET);
            _keyMapping.put(NativeKeyEvent.VC_CLOSE_BRACKET, InputKeyValue._CLOSE_BRACKET);
            _keyMapping.put(NativeKeyEvent.VC_BACK_SLASH, InputKeyValue._BACK_SLASH);
            _keyMapping.put(NativeKeyEvent.VC_SEMICOLON, InputKeyValue._SEMICOLON);
            _keyMapping.put(NativeKeyEvent.VC_QUOTE, InputKeyValue._QUOTE);
            _keyMapping.put(NativeKeyEvent.VC_COMMA, InputKeyValue._COMMA);
            _keyMapping.put(NativeKeyEvent.VC_PERIOD, InputKeyValue._PERIOD);
            _keyMapping.put(NativeKeyEvent.VC_SLASH, InputKeyValue._SLASH);
            _keyMapping.put(NativeKeyEvent.VC_PRINTSCREEN, InputKeyValue._PRINTSCREEN);
            _keyMapping.put(NativeKeyEvent.VC_SCROLL_LOCK, InputKeyValue._SCROLL_LOCK);
            _keyMapping.put(NativeKeyEvent.VC_PAUSE, InputKeyValue._PAUSE);
            _keyMapping.put(NativeKeyEvent.VC_INSERT, InputKeyValue._INSERT);
            _keyMapping.put(NativeKeyEvent.VC_DELETE, InputKeyValue._DELETE);
            _keyMapping.put(NativeKeyEvent.VC_HOME, InputKeyValue._HOME);
            _keyMapping.put(NativeKeyEvent.VC_END, InputKeyValue._END);
            _keyMapping.put(NativeKeyEvent.VC_PAGE_UP, InputKeyValue._PAGE_UP);
            _keyMapping.put(NativeKeyEvent.VC_PAGE_DOWN, InputKeyValue._PAGE_DOWN);
            _keyMapping.put(NativeKeyEvent.VC_UP, InputKeyValue._UP);
            _keyMapping.put(NativeKeyEvent.VC_LEFT, InputKeyValue._LEFT);
            _keyMapping.put(NativeKeyEvent.VC_CLEAR, InputKeyValue._CLEAR);
            _keyMapping.put(NativeKeyEvent.VC_RIGHT, InputKeyValue._RIGHT);
            _keyMapping.put(NativeKeyEvent.VC_DOWN, InputKeyValue._DOWN);
            _keyMapping.put(NativeKeyEvent.VC_NUM_LOCK, InputKeyValue._NUM_LOCK);
            _keyMapping.put(NativeKeyEvent.VC_SEPARATOR, InputKeyValue._SEPARATOR);
            _keyMapping.put(NativeKeyEvent.VC_META, InputKeyValue._META);
            _keyMapping.put(NativeKeyEvent.VC_CONTEXT_MENU, InputKeyValue._CONTEXT_MENU);
            _keyMapping.put(NativeKeyEvent.VC_POWER, InputKeyValue._POWER);
            _keyMapping.put(NativeKeyEvent.VC_SLEEP, InputKeyValue._SLEEP);
            _keyMapping.put(NativeKeyEvent.VC_WAKE, InputKeyValue._WAKE);
            _keyMapping.put(NativeKeyEvent.VC_MEDIA_PLAY, InputKeyValue._MEDIA_PLAY);
            _keyMapping.put(NativeKeyEvent.VC_MEDIA_STOP, InputKeyValue._MEDIA_STOP);
            _keyMapping.put(NativeKeyEvent.VC_MEDIA_PREVIOUS, InputKeyValue._MEDIA_PREVIOUS);
            _keyMapping.put(NativeKeyEvent.VC_MEDIA_NEXT, InputKeyValue._MEDIA_NEXT);
            _keyMapping.put(NativeKeyEvent.VC_MEDIA_SELECT, InputKeyValue._MEDIA_SELECT);
            _keyMapping.put(NativeKeyEvent.VC_MEDIA_EJECT, InputKeyValue._MEDIA_EJECT);
            _keyMapping.put(NativeKeyEvent.VC_VOLUME_MUTE, InputKeyValue._VOLUME_MUTE);
            _keyMapping.put(NativeKeyEvent.VC_VOLUME_UP, InputKeyValue._VOLUME_UP);
            _keyMapping.put(NativeKeyEvent.VC_VOLUME_DOWN, InputKeyValue._VOLUME_DOWN);

            return _keyMapping;
        }
    }
}
