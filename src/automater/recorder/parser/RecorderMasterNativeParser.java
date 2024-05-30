/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.recorder.parser;

import automater.di.DI;
import automater.input.InputKey;
import automater.recorder.Recorder;
import automater.recorder.model.RecorderUserInput;
import automater.settings.Hotkey;
import automater.utilities.CollectionUtilities;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelEvent;

public interface RecorderMasterNativeParser {
    
    interface Protocol {
        RecorderNativeParser.Protocol getSubparser();
        void registerHotkeyListener(@NotNull Recorder.HotkeyListener listener);
        void unregisterHotkeyListener(@NotNull Recorder.HotkeyListener listener);
        @Nullable Recorder.HotkeyListener getPlayStopHotkeyListener();
        void setPlayStopHotkeyListener(@Nullable Recorder.HotkeyListener listener);
    }
    
    class Impl implements Protocol, RecorderNativeParser.Protocol
    {
        private final RecorderNativeParser.Protocol _subParser = DI.get(RecorderNativeParser.Protocol.class);
        private final RecorderSystemKeyboardTranslator.Protocol _keyboardTranslator = DI.get(RecorderSystemKeyboardTranslator.Protocol.class);
        
        @NotNull private final Object _lock = new Object();

        @NotNull private final HashSet<Recorder.HotkeyListener> _hotkeyListeners = new HashSet<>();
        @Nullable private Recorder.HotkeyListener _playStopHotkeyListener;

        @Override
        public RecorderNativeParser.Protocol getSubparser()
        {
            return this;
        }
        
        @Override
        public @NotNull List<RecorderParserFlag> getFlags()
        {
            return _subParser.getFlags();
        }
        
        @Override
        public void setFlags(@NotNull List<RecorderParserFlag> flags)
        {
            _subParser.setFlags(flags);
        }
        
        @Override
        public void registerHotkeyListener(@NotNull Recorder.HotkeyListener listener)
        {
            synchronized (_lock)
            {
                _hotkeyListeners.add(listener);
            }
        }

        @Override
        public void unregisterHotkeyListener(@NotNull Recorder.HotkeyListener listener)
        {
            synchronized (_lock)
            {
                _hotkeyListeners.remove(listener);
            }
        }

        @Override
        public @Nullable Recorder.HotkeyListener getPlayStopHotkeyListener()
        {
            synchronized (_lock)
            {
                return _playStopHotkeyListener;
            }
        }

        @Override
        public void setPlayStopHotkeyListener(@Nullable Recorder.HotkeyListener listener)
        {
            synchronized (_lock)
            {
                if (listener != null)
                {
                    _playStopHotkeyListener = listener;
                    _hotkeyListeners.add(listener);
                }
                else if (_playStopHotkeyListener != null)
                {
                    _hotkeyListeners.remove(_playStopHotkeyListener);
                    _playStopHotkeyListener = null;
                }
            }
        }

        @Override
        public @Nullable RecorderUserInput evaluatePress(@NotNull NativeKeyEvent keyboardEvent) {
            // Hotkey listeners update & alert
            InputKey translatedKey = _keyboardTranslator.recordAndTranslate(keyboardEvent, true);

            if (translatedKey != null)
            {
                // Never parse the play/stop keystroke
                if (isPlayStopHotkeyMatchingInputKey(translatedKey))
                {
                    return null;
                }
            }

            // Subparser delegation
            return _subParser.evaluatePress(keyboardEvent);
        }

        @Override
        public @Nullable RecorderUserInput evaluateRelease(@NotNull NativeKeyEvent keyboardEvent) {
            // Hotkey listeners update
            InputKey translatedKey = _keyboardTranslator.recordAndTranslate(keyboardEvent, false);

            if (translatedKey != null)
            {
                // Play/stop hotkey and the other hotkey listeners are alerted only when
                // releasing the keyboard key
                updateHotkeyListeners(translatedKey);

                // Never parse the play/stop keystroke
                if (isPlayStopHotkeyMatchingInputKey(translatedKey))
                {
                    return null;
                }
            }

            // Subparser delegation
            return _subParser.evaluateRelease(keyboardEvent);
        }

        @Override
        public @Nullable RecorderUserInput evaluatePress(@NotNull NativeMouseEvent mouseEvent) {
            return _subParser.evaluatePress(mouseEvent);
        }

        @Override
        public @Nullable RecorderUserInput evaluateRelease(@NotNull NativeMouseEvent mouseEvent) {
            return _subParser.evaluateRelease(mouseEvent);
        }

        @Override
        public @Nullable RecorderUserInput evaluateMouseMove(@NotNull NativeMouseEvent mouseMoveEvent) {
            return _subParser.evaluateMouseMove(mouseMoveEvent);
        }

        @Override
        public @Nullable RecorderUserInput evaluateMouseWheel(@NotNull NativeMouseWheelEvent mouseWheelEvent) {
            return _subParser.evaluateMouseWheel(mouseWheelEvent);
        }

        @Override
        public @Nullable RecorderUserInput evaluateWindowEvent(@NotNull WindowEvent windowEvent) {
            return _subParser.evaluateWindowEvent(windowEvent);
        }

        private void updateHotkeyListeners(@NotNull InputKey translatedKey)
        {
            Collection<Recorder.HotkeyListener> listeners;

            synchronized (_lock)
            {
                listeners = CollectionUtilities.copyAsImmutable(_hotkeyListeners);
            }

            for (Recorder.HotkeyListener l : listeners)
            {
                if (hotkeyListenerIsListeningForKeystroke(l, translatedKey))
                {
                    Hotkey hotkey = new Hotkey(translatedKey);
                    l.onHotkeyPressed(hotkey);
                }
            }
        }

        private boolean hotkeyListenerIsListeningForKeystroke(@NotNull Recorder.HotkeyListener l, @NotNull InputKey translatedKey)
        {
            return l.isListeningForAnyHotkey() || isHotkeyMatchingInputKey(l.getHotkey(), translatedKey);
        }

        private boolean isHotkeyMatchingInputKey(@Nullable Hotkey hotkey, @NotNull InputKey translatedKey)
        {
            if (hotkey == null)
            {
                return false;
            }

            return hotkey.isEqualTo(translatedKey);
        }

        private boolean isPlayStopHotkeyMatchingInputKey(@NotNull InputKey translatedKey)
        {
            return _playStopHotkeyListener != null && isHotkeyMatchingInputKey(_playStopHotkeyListener.getHotkey(), translatedKey);
        }
    }
}


