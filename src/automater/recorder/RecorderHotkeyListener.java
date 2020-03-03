/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
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
