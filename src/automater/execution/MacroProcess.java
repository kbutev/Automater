/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.execution;

import automater.di.DI;
import automater.utilities.Callback;
import automater.utilities.CollectionUtilities;
import automater.utilities.Errors;
import automater.utilities.Logger;
import automater.utilities.Looper;
import automater.utilities.LooperClient;
import automater.utilities.RunState;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface MacroProcess {
    
    static final int LOOP_DELAY = 10;
    
    interface Listener {
        
        void onStart();
        void onNextCommand(@NotNull Command.Protocol command);
        void onEnd(boolean cancelled);
    }
    
    interface Protocol {
        
        boolean isRunning();
        double getCurrentTime();
        double getDuration();
        
        void addListener(@NotNull Listener listener);
        void removeListener(@NotNull Listener listener);
        
        void start(@Nullable Context.Protocol context) throws Exception;
        void pause() throws Exception;
        void resume() throws Exception;
        void cancel() throws Exception;
    }
    
    class Child implements Protocol, Context.Owner {
        
        final Looper.Protocol looper = DI.get(Looper.Background.class);
        
        private final @NotNull Object lock = new Object();
        
        private final @NotNull List<Command.Protocol> commands;
        private final double duration;
        
        private final @NotNull RunState state = new RunState();
        private @Nullable Context.Protocol context;
        
        private final @NotNull ArrayList<Listener> listeners;
        
        private final @NotNull StopWatch timer = new StopWatch();
        
        public Child(@NotNull List<Command.Protocol> commands) {
            this.commands = CollectionUtilities.copy(commands);
            duration = !commands.isEmpty() ? commands.get(commands.size()).getTimestamp() : 0;
            listeners = new ArrayList<>();
        }
        
        // # Protocol
        
        @Override
        public boolean isRunning() {
            synchronized (listeners) {
                return state.isStarted();
            }
        }
        
        @Override
        public double getCurrentTime() {
            synchronized (listeners) {
                return timer.getTime(TimeUnit.MILLISECONDS) * 1000;
            }
        }
        
        @Override
        public double getDuration() {
            return duration;
        }
        
        @Override
        public void addListener(@NotNull Listener listener) {
            synchronized (listeners) {
                if (!listeners.contains(listener)) {
                    listeners.add(listener);
                }
            }
        }
        
        @Override
        public void removeListener(@NotNull Listener listener) {
            synchronized (listeners) {
                if (listeners.contains(listener)) {
                    listeners.remove(listener);
                }
            }
        }
        
        @Override
        public void start(@Nullable Context.Protocol context) throws Exception {
            synchronized (lock) {
                state.start();
                
                this.context = context != null ? context : new Context.Process(this);
                
                if (!commands.isEmpty()) {
                    var firstCommand = commands.get(0);

                    alertListeners((Listener listener) -> {
                        listener.onNextCommand(firstCommand);
                    });
                } else {
                    onFinalCommand();
                }
                
                timer.start();
            }
        }
        
        @Override
        public void pause() throws Exception {
            synchronized (lock) {
                if (!isRunning()) {
                    throw Errors.illegalStateError();
                }
                
                timer.suspend();
            }
        }
        
        @Override
        public void resume() throws Exception {
            synchronized (lock) {
                if (!isRunning()) {
                    throw Errors.illegalStateError();
                }
                
                timer.resume();
            }
        }
        
        @Override
        public void cancel() throws Exception {
            synchronized (lock) {
                stop();

                alertListeners((Listener listener) -> {
                    listener.onEnd(true);
                });
            }
        }
        
        // # Loop
        
        void update() {
            synchronized (lock) {
                if (commands.isEmpty()) {
                    return;
                }

                Logger.message(this, "update");

                var next = commands.get(0);

                while (true) {
                    next.update(context);

                    if (next.getStatus() == Command.Status.finished) {
                        commands.remove(0);

                        if (!commands.isEmpty()) {
                            next = commands.get(0);
                            onNext(next);
                        } else {
                            onFinalCommand();
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        
        // # Private
        
        protected void stop() throws Exception {
            // assume in synchronized (lock)
            
            state.stop();
            listeners.clear();
            timer.stop();
            timer.reset();
        }
        
        protected void onNext(@NotNull Command.Protocol command) {
            // assume in synchronized (lock)
            
            alertListeners((Listener listener) -> {
                listener.onNextCommand(command);
            });
        }
        
        protected void onFinalCommand() {
            // assume in synchronized (lock)
            
            try {
                stop();
                
                alertListeners((Listener listener) -> {
                    listener.onEnd(false);
                });
            } catch (Exception e) {

            }
        }
        
        private void alertListeners(Callback.WithParameter<Listener> callback) {
            // assume in synchronized (lock)
            
            var listeners = CollectionUtilities.copy(this.listeners);
            
            // Always delegate on main thread
            looper.performAsync(() -> {
                for (var listener : listeners) {
                    callback.perform(listener);
                }
            });
        }
    }
    
    class Root extends Child implements Looper.Subscriber {
        
        
        public Root(@NotNull List<Command.Protocol> commands) {
            super(commands);
        }
        
        // # Protocol
        
        @Override
        public void start(@Nullable Context.Protocol context) throws Exception {
            context = context != null ? context : new Context.Process(this);
            
            super.start(context);
            
            looper.subscribe(this, LOOP_DELAY);
        }
        
        // # LooperClient
        
        @Override
        public void onLoop(double dt) {
            update();
        }
        
        // # Private
        
        @Override
        protected void stop() throws Exception {
            super.stop();
            
            looper.unsubscribe(this);
        }
    }
}
