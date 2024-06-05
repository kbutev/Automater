/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.service;

import automater.model.KeyEventKind;
import automater.model.Keystroke;
import automater.model.event.CapturedEvent;
import automater.model.event.CapturedHardwareEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface HotkeyMonitor {

    public static @NotNull Protocol build(@NotNull Keystroke hotkey) {
        return new Impl(hotkey);
    }

    interface Protocol {

        @Nullable Listener getListener();
        void setListener(@NotNull Listener listener);
        @Nullable Keystroke getHotkey();
        void setHotkey(@NotNull Keystroke hotkey);

        void start() throws Exception;
        void stop() throws Exception;
    }

    interface Listener {

        void onHotkeyEvent(@NotNull KeyEventKind kind);
    }

    class Impl implements Protocol, NativeEventMonitor.Listener {

        private final @NotNull NativeEventMonitor.Protocol monitor = NativeEventMonitor.build();
        private @Nullable Listener listener;
        private @Nullable Keystroke hotkey;

        public Impl(@NotNull Keystroke hotkey) {
            this.hotkey = hotkey;
        }

        @Override
        public @Nullable
        Listener getListener() {
            return this.listener;
        }

        @Override
        public void setListener(@NotNull Listener listener) {
            assert listener != null;
            this.listener = listener;
        }

        @Override
        public @Nullable
        Keystroke getHotkey() {
            return hotkey;
        }

        @Override
        public void setHotkey(@NotNull Keystroke hotkey) {
            assert hotkey != null;
            this.hotkey = hotkey;
        }

        @Override
        public void start() throws Exception {
            monitor.setListener(this);
            monitor.start();
        }

        @Override
        public void stop() throws Exception {
            monitor.stop();
        }

        // # NativeEventMonitor.Listener
        @Override
        public void onParseEvent(CapturedEvent event) {
            if (listener == null || hotkey == null) {
                return;
            }

            if (event instanceof CapturedHardwareEvent.Click click) {
                if (hotkey.equals(click.keystroke)) {
                    listener.onHotkeyEvent(click.kind);
                }
            }
        }

        @Override
        public void onInputDataChange() {

        }

        @Override
        public void onParseError(@NotNull Exception e) {

        }
    }
}
