/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.storage;

import automater.Strings;
import automater.input.InputKeyValue;
import automater.settings.Hotkey;
import automater.work.model.MacroParameters;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;

/**
 * Holds raw storage values.
 *
 * @author Bytevi
 */
public class PreferencesStorageValues implements Serializable {

    public Hotkey recordOrStopHotkey = getDefaultRecordOrStopHotkey();
    public Hotkey playOrStopHotkey = getDefaultPlayOrStopHotkey();

    public boolean displayStartNotification = false;
    public boolean displayStopNotification = false;
    public boolean displayRepeatNotification = false;
    public MacroParameters macroParameters = new MacroParameters();

    public @NotNull PreferencesStorageValues copy() {
        PreferencesStorageValues copied = new PreferencesStorageValues();
        copied.displayStartNotification = displayStartNotification;
        copied.displayStopNotification = displayStopNotification;
        copied.displayRepeatNotification = displayRepeatNotification;
        copied.macroParameters = macroParameters;
        return copied;
    }

    public static @NotNull Hotkey getDefaultRecordOrStopHotkey() {
        InputKeyValue key = InputKeyValue.fromString(Strings.DEFAULT_RECORD_OR_STOP_HOTKEY);

        return new Hotkey(key);
    }

    public static @NotNull Hotkey getDefaultPlayOrStopHotkey() {
        InputKeyValue key = InputKeyValue.fromString(Strings.DEFAULT_PLAY_OR_STOP_HOTKEY);

        return new Hotkey(key);
    }
}
