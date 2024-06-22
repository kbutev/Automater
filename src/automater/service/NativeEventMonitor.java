/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.service;

import automater.di.DI;
import automater.model.EventFilter;
import automater.model.KeyEventKind;
import automater.model.event.CapturedEvent;
import automater.parser.CapturedEventParser;
import automater.utilities.Callback;
import automater.utilities.Logger;
import automater.utilities.RunState;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jnativehook.GlobalScreen;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

/**
 * A global monitor of mouse and keyboard events.
 * Never initialize more than one monitor at a time.
 *
 * Requires a parser to translate the jnativehook events to Automater friendly
 * objects: RecorderUserInput.
 *
 * @author Bytevi
 */
public interface NativeEventMonitor {

    public static @NotNull Protocol build() {
        return new Impl();
    }

    interface Protocol {

        void addListener(@NotNull Listener listener);
        void removeListener(@NotNull Listener listener);
        
        @Nullable EventFilter getFilter();
        void setFilter(@NotNull EventFilter filter);

        boolean isRecording();

        void start() throws Exception;
        void stop() throws Exception;
    }

    interface Listener {

        void onParseEvent(@NotNull CapturedEvent event);
        void onInputDataChange();
        void onParseError(@NotNull Exception e);
    }

    class Impl implements Protocol {

        private CapturedEventParser.Protocol parser;

        private static final Object registerLock = new Object();
        private static SwingDispatchService swingDispatchService = null;

        private final NativeHookListener nativeHookListener = new NativeHookListener();

        private EventFilter filter = new EventFilter();

        private final RunState state = new RunState();

        @Override
        public void addListener(@NotNull Listener listener) {
            if (!nativeHookListener.listeners.contains(listener)) {
                nativeHookListener.listeners.add(listener);
            }
        }
        
        @Override
        public void removeListener(@NotNull Listener listener) {
            if (nativeHookListener.listeners.contains(listener)) {
                nativeHookListener.listeners.remove(listener);
            }
        }
        
        @Override
        public @Nullable
        EventFilter getFilter() {
            return filter.copy();
        }

        @Override
        public void setFilter(@NotNull EventFilter filter) {
            this.filter = filter.copy();
        }

        @Override
        public boolean isRecording() {
            return state.isStarted();
        }

        @Override
        public void start() throws Exception {
            Logger.message(this, "start");
            
            parser = new CapturedEventParser.Impl();
            state.start();
            nativeHookListener.parser = parser;
            nativeHookListener.filter = filter;

            registerEventDispatcherOnce();
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(nativeHookListener);
            GlobalScreen.addNativeMouseListener(nativeHookListener);
            GlobalScreen.addNativeMouseMotionListener(nativeHookListener);
            GlobalScreen.addNativeMouseWheelListener(nativeHookListener);
        }

        @Override
        public void stop() throws Exception {
            Logger.message(this, "stop");
            
            state.stop();
            GlobalScreen.removeNativeMouseListener(nativeHookListener);
            GlobalScreen.removeNativeMouseListener(nativeHookListener);
            GlobalScreen.removeNativeMouseListener(nativeHookListener);
            GlobalScreen.removeNativeMouseListener(nativeHookListener);
        }

        // # Private
        private void registerEventDispatcherOnce() {
            synchronized (registerLock) {
                if (swingDispatchService == null) {
                    swingDispatchService = new SwingDispatchService();
                    GlobalScreen.setEventDispatcher(swingDispatchService);
                }
            }
        }
    }

    // Hook listener
    class NativeHookListener implements ActionListener, ItemListener, NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener, WindowListener {

        private CapturedEventParser.Protocol parser;
        private ArrayList<Listener> listeners = new ArrayList<>();
        private EventFilter filter = new EventFilter();
        
        @Override
        public void actionPerformed(ActionEvent e) {

        }

        @Override
        public void itemStateChanged(ItemEvent e) {

        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent nke) {

        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent event) {
            try {
                var result = parser.parseNativeKeyboardEvent(event, 0, KeyEventKind.press);
                
                if (result == null) {
                    return;
                }

                if (filter.filtersOut(result)) {
                    return;
                }
                
                forEachListener((var listener) -> {
                    listener.onParseEvent(result);
                    listener.onInputDataChange();
                });
            } catch (Exception e) {
                forEachListener((var listener) -> {
                    listener.onParseError(e);
                });
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent event) {
            try {
                var result = parser.parseNativeKeyboardEvent(event, 0, KeyEventKind.release);

                if (result == null) {
                    return;
                }
                
                if (filter.filtersOut(result)) {
                    return;
                }
                
                forEachListener((var listener) -> {
                    listener.onParseEvent(result);
                    listener.onInputDataChange();
                });
            } catch (Exception e) {
                forEachListener((var listener) -> {
                    listener.onParseError(e);
                });
            }
        }

        @Override
        public void nativeMouseClicked(NativeMouseEvent event) {

        }

        @Override
        public void nativeMousePressed(NativeMouseEvent event) {
            try {
                var result = parser.parseNativeMouseKeyEvent(event, 0, KeyEventKind.press);

                if (filter.filtersOut(result)) {
                    return;
                }

                forEachListener((var listener) -> {
                    listener.onParseEvent(result);
                    listener.onInputDataChange();
                });
            } catch (Exception e) {
                forEachListener((var listener) -> {
                    listener.onParseError(e);
                });
            }
        }

        @Override
        public void nativeMouseReleased(NativeMouseEvent event) {
            try {
                var result = parser.parseNativeMouseKeyEvent(event, 0, KeyEventKind.release);

                if (filter.filtersOut(result)) {
                    return;
                }

                forEachListener((var listener) -> {
                    listener.onParseEvent(result);
                    listener.onInputDataChange();
                });
            } catch (Exception e) {
                forEachListener((var listener) -> {
                    listener.onParseError(e);
                });
            }
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent event) {
            try {
                var result = parser.parseNativeEvent(event, 0);

                if (filter.filtersOut(result)) {
                    return;
                }

                forEachListener((var listener) -> {
                    listener.onParseEvent(result);
                    listener.onInputDataChange();
                });
            } catch (Exception e) {
                forEachListener((var listener) -> {
                    listener.onParseError(e);
                });
            }
        }

        @Override
        public void nativeMouseDragged(NativeMouseEvent event) {
            try {
                var result = parser.parseNativeEvent(event, 0);

                if (filter.filtersOut(result)) {
                    return;
                }

                forEachListener((var listener) -> {
                    listener.onParseEvent(result);
                    listener.onInputDataChange();
                });
            } catch (Exception e) {
                forEachListener((var listener) -> {
                    listener.onParseError(e);
                });
            }
        }

        @Override
        public void nativeMouseWheelMoved(NativeMouseWheelEvent event) {
            try {
                var result = parser.parseNativeEvent(event, 0);

                if (filter.filtersOut(result)) {
                    return;
                }

                forEachListener((var listener) -> {
                    listener.onParseEvent(result);
                    listener.onInputDataChange();
                });
            } catch (Exception e) {
                forEachListener((var listener) -> {
                    listener.onParseError(e);
                });
            }
        }

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {

        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
        
        private void forEachListener(Callback.WithParameter<Listener> callback) {
            for (var listener : listeners) {
                callback.perform(listener);
            }
        }
    }
}
