/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import automater.di.DI;
import automater.model.KeyEventKind;
import automater.model.KeyValue;
import automater.model.Keystroke;
import automater.model.macro.Macro;
import automater.model.action.MacroAction;
import automater.model.event.CapturedEvent;
import automater.model.event.CapturedHardwareEvent;
import automater.parser.DescriptionParser;
import automater.parser.MacroActionParser;
import automater.service.EventMonitor;
import automater.service.HotkeyMonitor;
import automater.storage.MacroStorage;
import automater.utilities.Logger;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.time.StopWatch;
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
        void onUpdateEvents(@NotNull List<String> events);
        void onRecordingSaved(boolean success);
    }

    interface Protocol extends Presenter<Delegate> {
        
        double getCurrentRecordTime();

        void beginRecording(@Nullable Object sender);
        void endRecording(@Nullable Object sender);
        void saveRecording(@NotNull String name, @NotNull String description);
    }

    class Impl implements Protocol, EventMonitor.Listener, HotkeyMonitor.Listener {

        private final MacroStorage.Protocol storage = DI.get(MacroStorage.Protocol.class);
        private final MacroActionParser.Protocol actionParser = DI.get(MacroActionParser.Protocol.class);
        private final DescriptionParser.Protocol descriptionParser = DI.get(DescriptionParser.Protocol.class);

        private @Nullable Delegate delegate;

        private final EventMonitor.Protocol recorder = new EventMonitor.Impl();
        private final HotkeyMonitor.Protocol actionHotkeyMonitor;

        private final @NotNull ArrayList<MacroAction> actions = new ArrayList<>();
        private final @NotNull ArrayList<String> descriptions = new ArrayList<>();
        
        private final @NotNull StopWatch timer = new StopWatch();

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
                timer.start();
                actionHotkeyMonitor.setListener(this);
                actionHotkeyMonitor.start();
            } catch (Exception e) {
                delegate.onError(e);
            }
        }

        @Override
        public void stop() {
            Logger.message(this, "Stop");

            try {
                timer.stop();
                actionHotkeyMonitor.stop();
            } catch (Exception e) {}
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
        public double getCurrentRecordTime() {
            return (double)timer.getTime() / 1000; // Milliseconds to seconds
        }

        @Override
        public void beginRecording(@Nullable Object sender) {
            Logger.messageEvent(this, "Begin recording");
            
            actions.clear();
            descriptions.clear();
            
            try {
                recorder.setListener(this);
                recorder.start();
            } catch (Exception e) {
                if (delegate != null) {
                    delegate.onError(e);
                }
            }
            
            if (delegate != null) {
                delegate.onUpdateEvents(descriptions);
                
                if (sender != delegate) {
                    delegate.onStartRecording(sender);
                }
            }
        }

        @Override
        public void endRecording(@Nullable Object sender) {
            Logger.messageEvent(this, "End recording");
            
            try {
                recorder.stop();
            } catch (Exception e) {
                if (delegate != null) {
                    delegate.onError(e);
                }
            }
            
            if (delegate != null && sender != delegate) {
                delegate.onEndRecording(sender);
            }
        }

        @Override
        public void saveRecording(@NotNull String name, @NotNull String description) {
            var result = false;
            
            try {
                var macro = Macro.build(name, actions);
                storage.saveMacro(macro);
                result = true;
            } catch (Exception e) {
                Logger.error(this, "Failed to save macro: " + e);
            }
            
            if (delegate != null) {
                delegate.onRecordingSaved(result);
            }
        }

        // # EventMonitor.Listener
        
        @Override
        public void onEventEmitted(@NotNull CapturedEvent event) {
            // Ignore hotkey
            if (event instanceof CapturedHardwareEvent.Click click) {
                if (click.keystroke.equals(actionHotkeyMonitor.getHotkey())) {
                    return;
                }
            }
            
            var timestamp = getCurrentRecordTime();
            
            try {
                var action = actionParser.parseFromCapturedEvent(event, timestamp);
                actions.add(action);
                
                var description = descriptionParser.parseMacroAction(action);
                descriptions.add(description.toString());
            } catch (Exception e) {
                Logger.error(this, "Parsing error: " + e);
            }
            
            if (delegate != null) {
                delegate.onUpdateEvents(descriptions);
            }
        }

        // # HotkeyMonitor.Listener
        @Override
        public void onHotkeyEvent(KeyEventKind kind) {
            if (!kind.isReleaseOrTap()) {
                return;
            }
            
            if (recorder.isRecording()) {
                endRecording(this);
            } else {
                beginRecording(this);
            }
        }
    }
}
