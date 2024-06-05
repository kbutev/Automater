/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.di.DI;
import automater.model.KeyEventKind;
import automater.model.KeyValue;
import automater.model.Keystroke;
import automater.model.event.CapturedEvent;
import automater.model.event.EventDescription;
import automater.parser.CapturedEventParser;
import automater.service.EventMonitor;
import automater.service.HotkeyMonitor;
import automater.storage.GeneralStorage;
import automater.utilities.Logger;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Presenter for the record macro screen.
 *
 * @author Bytevi
 */
public interface RecordMacroPresenter {

    interface Delegate {

        void onError(@NotNull Exception e);

        void onLoadedPreferencesFromStorage(@NotNull automater.storage.PreferencesStorageValues values);

        void onStartRecording(@Nullable Object sender);
        void onEndRecording(@Nullable Object sender);
        void onRecordingSaved(boolean success);
    }

    interface Protocol extends Presenter<Delegate> {

        void beginRecording(@Nullable Object sender);
        void endRecording(@Nullable Object sender);
        void saveRecording(@NotNull String name, @NotNull String description);
    }

    class Impl implements Protocol, EventMonitor.Listener, HotkeyMonitor.Listener {

        private final GeneralStorage.Protocol storage = DI.get(GeneralStorage.Protocol.class);
        private final CapturedEventParser.Protocol parser = DI.get(CapturedEventParser.Protocol.class);

        private @Nullable Delegate delegate;

        private final EventMonitor.Protocol recorder = new EventMonitor.Impl();
        private final HotkeyMonitor.Protocol actionHotkeyMonitor;

        private final @NotNull ArrayList<EventDescription> events = new ArrayList<>();

        public Impl() {
            actionHotkeyMonitor = HotkeyMonitor.build(Keystroke.build(KeyValue._F4));
        }

        // # BasePresenter
        @Override
        public void start() {
            if (delegate == null) {
                assert false;
            }

            Logger.message(this, "Start");

            try {
                actionHotkeyMonitor.start();
            } catch (Exception e) {
                delegate.onError(e);
            }
        }

        @Override
        public void stop() {
            Logger.message(this, "Stop");

            try {
                actionHotkeyMonitor.stop();
            } catch (Exception e) {
                delegate.onError(e);
            }
        }

        @Override
        public @Nullable Delegate getDelegate() {
            return delegate;
        }

        @Override
        public void setDelegate(@NotNull Delegate delegate) {
            assert delegate != null;
            this.delegate = delegate;
        }

        @Override
        public void reloadData() {

        }

        @Override
        public void beginRecording(@Nullable Object sender) {
            Logger.messageEvent(this, "Starting recording...");

            if (delegate != null) {
                delegate.onStartRecording(sender);
            }
        }

        @Override
        public void endRecording(@Nullable Object sender) {
            if (delegate != null) {
                delegate.onEndRecording(sender);
            }
        }

        @Override
        public void saveRecording(@NotNull String name, @NotNull String description) {
            if (delegate != null) {
                delegate.onRecordingSaved(true);
            }
        }

        // # EventMonitor.Listener
        @Override
        public void onEventEmitted(@NotNull CapturedEvent event) {
            try {
                var description = parser.parseToDescription(event);
                events.add(description);
            } catch (Exception e) {

            }
        }

        // # HotkeyMonitor.Listener
        @Override
        public void onHotkeyEvent(KeyEventKind kind) {
            if (recorder.isRecording()) {
                endRecording(this);
            } else {
                beginRecording(this);
            }
        }
    }
}
