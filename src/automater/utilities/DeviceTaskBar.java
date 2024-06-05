/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.JFrame;
import org.bridj.Pointer;
import org.bridj.cpp.com.COMRuntime;
import org.bridj.cpp.com.shell.ITaskbarList3;
import org.bridj.jawt.JAWTUtils;

/**
 * Gives you access to some of the OS task bar functionality.
 *
 * @author Byti
 */
public class DeviceTaskBar {

    public static int MAX_PROGRESS_VALUE = 100;

    private static DeviceTaskBar singleton;

    private final @NotNull Object _lock = new Object();

    private final boolean _active;
    private @Nullable ITaskbarList3 _item;
    private @Nullable Pointer<?> _progressPointer;

    private DeviceTaskBar() {
        if (DeviceOS.isWindows()) {
            try {
                _item = COMRuntime.newInstance(ITaskbarList3.class);
            } catch (Exception e) {
                _item = null;
            }
        }

        _active = (_item != null);
    }

    synchronized public static @NotNull DeviceTaskBar getShared() {
        if (singleton == null) {
            singleton = new DeviceTaskBar();
        }

        return singleton;
    }

    public void setAppTaskBarProgress(@NotNull JFrame frame, double progress) {
        if (!_active) {
            return;
        }

        int value = (int) progress;

        if (value < 0) {
            value = 0;
        }

        if (value > MAX_PROGRESS_VALUE) {
            value = MAX_PROGRESS_VALUE;
        }

        synchronized (_lock) {
            long hwndVal = JAWTUtils.getNativePeerHandle(frame);
            _progressPointer = Pointer.pointerToAddress(hwndVal);
            _item.SetProgressValue((Pointer) _progressPointer, value, MAX_PROGRESS_VALUE);
        }
    }

    public void resetAppTaskBarProgress(@NotNull JFrame frame) {
        if (!_active) {
            return;
        }

        synchronized (_lock) {
            long hwndVal = JAWTUtils.getNativePeerHandle(frame);
            _progressPointer = Pointer.pointerToAddress(hwndVal);
            _item.SetProgressValue((Pointer) _progressPointer, 0, MAX_PROGRESS_VALUE);
        }
    }
}
