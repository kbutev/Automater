/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.recorder.parser;

import automater.input.InputKey;
import automater.input.InputKeyModifiers;
import automater.input.InputKeyValue;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * Describes a keyboard object, that translates keys and memorizes the pressed keys.
 * 
 * Use translate(NativeKeyEvent) to simply convert NativeKeyEvent to InputKey.
 * Use recordAndTranslate() to convert and record the key.
 *
 * @author Byti
 */
public interface BaseRecorderKeyboardTranslator {
    public @Nullable InputKey translate(@NotNull NativeKeyEvent keyEvent);
    public @Nullable InputKey recordAndTranslate(@NotNull NativeKeyEvent keyEvent, boolean press);
    
    public @NotNull List<InputKeyValue> getCurrentlyPressedKeys();
    public @NotNull InputKeyModifiers getCurrentlyPressedModifiers();
}
