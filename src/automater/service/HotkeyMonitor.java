/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.service;

import automater.di.DI;
import automater.model.KeyEventKind;
import automater.model.InputKeystroke;
import automater.model.event.CapturedEvent;
import automater.model.event.CapturedHardwareEvent;
import automater.utilities.Errors;
import automater.utilities.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface HotkeyMonitor {

    public static @NotNull Protocol build(@NotNull InputKeystroke.Protocol hotkey) {
        return new Impl(hotkey);
    }
    
    public static @NotNull Protocol build(@NotNull InputKeystroke.Protocol hotkey, @NotNull String name) {
        var result = new Impl(hotkey);
        result.setName(name);
        return result;
    }

    interface Protocol {

        @Nullable Listener getListener();
        void setListener(@NotNull Listener listener);
        @Nullable InputKeystroke.Protocol getHotkey();
        void setHotkey(@NotNull InputKeystroke.Protocol hotkey);
        @NotNull String getName();
        void setName(@NotNull String name);

        void start() throws Exception;
        void stop() throws Exception;
    }

    interface Listener {

        void onHotkeyEvent(@NotNull KeyEventKind kind);
    }

    class Impl implements Protocol, NativeEventMonitor.Listener {

        private final NativeEventMonitor.Protocol monitor = DI.get(NativeEventMonitor.Protocol.class);
        private @Nullable Listener listener;
        private @Nullable InputKeystroke.Protocol hotkey;
        private @NotNull String name = "";

        public Impl(@NotNull InputKeystroke.Protocol hotkey) {
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
        public @Nullable InputKeystroke.Protocol getHotkey() {
            return hotkey;
        }

        @Override
        public void setHotkey(@NotNull InputKeystroke.Protocol hotkey) {
            assert hotkey != null;
            this.hotkey = hotkey;
        }

        @Override
        public @NotNull String getName() {
            return name;
        }
        
        @Override
        public void setName(@NotNull String name) {
            this.name = name;
        }
        
        @Override
        public void start() throws Exception {
            if (listener == null) {
                throw Errors.delegateNotSet();
            }
            
            monitor.addListener(this);
            
            Logger.message(this, "start '" + name + "'");
        }

        @Override
        public void stop() throws Exception {
            monitor.removeListener(this);
            
            Logger.message(this, "stop '" + name + "'");
        }

        // # NativeEventMonitor.Listener
        @Override
        public void onParseEvent(CapturedEvent event) {
            if (listener == null || hotkey == null) {
                return;
            }

            if (event instanceof CapturedHardwareEvent.Click click) {
                Logger.messageVerbose(this, "onParseEvent " + click.toString());
                
                if (hotkey.equalsIgnoreModifier(click.keystroke)) {
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
