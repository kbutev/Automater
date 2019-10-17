/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.settings.Hotkey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Receives requests about a pressed keyboard key.
 * 
 * The listener may listen for a specific key or any key.
 *
 * @author Bytevi
 */
public interface RecorderHotkeyListener {
    public boolean isListeningForAnyHotkey();
    
    public @Nullable Hotkey getHotkey();
    
    public void onHotkeyPressed(@NotNull Hotkey hotkey);
}
