/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * Use recordAndTranslate() to not just convert, but record the key, which later
 * you can check.
 *
 * @author Byti
 */
public interface BaseRecorderKeyboardTranslator {
    public @Nullable InputKey translate(@NotNull NativeKeyEvent keyEvent);
    public @Nullable InputKey recordAndTranslate(@NotNull NativeKeyEvent keyEvent, boolean press);
    
    public @NotNull List<InputKeyValue> getCurrentlyPressedKeys();
    public @NotNull InputKeyModifiers getCurrentlyPressedModifiers();
}
