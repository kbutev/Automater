/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.recorder.parser;

import automater.di.DI;
import automater.input.InputKey;
import automater.input.InputKeyValue;
import org.jetbrains.annotations.NotNull;
import org.jnativehook.mouse.NativeMouseEvent;

/**
 * Translates native mouse key events.
 * 
 * Does NOT support getCurrentlyPressedKeys()! Only modifier keys are recorded!
 * 
 * @author Bytevi
 */
public interface RecorderSystemMouseTranslator {
    
    interface Protocol {
        @NotNull InputKey translate(@NotNull NativeMouseEvent keyEvent);
    }
    
    /**
    * Implementation.
    * 
    * Does NOT support getCurrentlyPressedKeys()! Only modifier keys are recorded!
    */
    class Impl implements Protocol {
        private final RecorderSystemKeyboardTranslator.Protocol keyboardTranslator = DI.get(RecorderSystemKeyboardTranslator.Protocol.class);

        @Override
        public @NotNull InputKey translate(@NotNull NativeMouseEvent keyEvent)
        {
            int mouseKeyValue = keyEvent.getButton();

            InputKeyValue key = InputKeyValue.UNKNOWN;

            switch(mouseKeyValue)
            {
                case 1:
                    key = InputKeyValue._MOUSE_LEFT_CLICK;
                    break;
                case 2:
                    key = InputKeyValue._MOUSE_RIGHT_CLICK;
                    break;
                case 3:
                    key = InputKeyValue._MOUSE_MIDDLE_CLICK;
                    break;
                case 4:
                    key = InputKeyValue._MOUSE_4_CLICK;
                    break;
                case 5:
                    key = InputKeyValue._MOUSE_5_CLICK;
                    break;
            }

            return new InputKey(key, keyboardTranslator.getCurrentlyPressedModifiers());
        }
    }
}

