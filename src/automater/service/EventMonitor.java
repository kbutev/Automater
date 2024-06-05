/*
 * Created by Kristiyan Butev.
 * Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.service;

import automater.model.EventFilter;
import automater.model.event.CapturedEvent;
import automater.utilities.CollectionUtilities;
import automater.utilities.Logger;
import automater.utilities.RunState;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EventMonitor {

    interface Protocol {

        @Nullable Listener getListener();
        void setListener(@NotNull Listener listener);
        @Nullable EventFilter getFilter();
        void setFilter(@NotNull EventFilter filter);

        boolean isRecording();
        @NotNull List<CapturedEvent> getCapturedEvents();

        void start() throws Exception;
        void stop() throws Exception;
    }

    interface Listener {

        void onEventEmitted(@NotNull CapturedEvent event);
    }

    class Impl implements Protocol, NativeEventMonitor.Listener {

        private final NativeEventMonitor.Protocol nativeMonitor;
        private @Nullable Listener listener;
        private final ArrayList<CapturedEvent> capturedEvents = new ArrayList<>();

        private final RunState state = new RunState();

        public Impl() {
            nativeMonitor = NativeEventMonitor.build();
        }

        @Override
        public Listener getListener() {
            return listener;
        }

        @Override
        public void setListener(@NotNull Listener listener) {
            this.listener = listener;
        }

        @Override
        public @Nullable
        EventFilter getFilter() {
            return nativeMonitor.getFilter();
        }

        @Override
        public void setFilter(@NotNull EventFilter filter) {
            this.nativeMonitor.setFilter(filter);
        }

        @Override
        public boolean isRecording() {
            return state.isStarted();
        }

        @Override
        public @NotNull
        List<CapturedEvent> getCapturedEvents() {
            return CollectionUtilities.copy(capturedEvents);
        }

        @Override
        public void start() throws Exception {
            capturedEvents.clear();
            nativeMonitor.start();
        }

        @Override
        public void stop() throws Exception {
            nativeMonitor.stop();
        }

        // # NativeEventMonitor.Listener
        @Override
        public void onParseEvent(@NotNull CapturedEvent event) {
            capturedEvents.add(event);

            if (listener != null) {
                listener.onEventEmitted(event);
            }
        }

        @Override
        public void onInputDataChange() {

        }

        @Override
        public void onParseError(Exception e) {
            Logger.error(this, "Captured event error: " + e);
        }
    }
}
