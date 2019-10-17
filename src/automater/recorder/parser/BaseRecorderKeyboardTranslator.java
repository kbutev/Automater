/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.parser;

import automater.input.InputKey;
import automater.input.InputKeyModifierValue;
import automater.input.InputKeyValue;
import automater.input.InputKeyModifiers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 *
 * @author Byti
 */
public interface BaseRecorderKeyboardTranslator {
    public @NotNull InputKeyModifiers getCurrentMask();
    
    public @Nullable InputKey translate(@NotNull NativeKeyEvent keyEvent);
    
    public @Nullable InputKey translate(boolean recordKeystroke, @NotNull NativeKeyEvent keyEvent, boolean press);
    
    public InputKeyValue keyValueForJNativeHookKey(int keyCode);
    
    public @NotNull InputKeyModifierValue maskValueFoKey(InputKeyValue keyValue);
}
