/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Contains utilities for performing callbacks, and updating objects every
 * interval.
 *
 * LooperClients are updated by one single specific background thread. Do not
 * block it!
 *
 * The performSyncCallback blocks the caller thread until the given callback is
 * performed on a background thread (same background thread as the
 * LooperClients).
 *
 * @author Bytevi
 */
public class Looper {

    public static int LOOPER_INTERVAL_MSEC = 50;

    private static Looper singleton;

    private final @NotNull ClientsManager _clientsManager = new ClientsManager();
    private final @NotNull CallbacksManager _callbacksManager = new CallbacksManager();

    private final @NotNull Runnable _looperRunnable = () -> {
        loop();
        loopAgain();
    };

    private final @NotNull ScheduledThreadPoolExecutor _looperExecutor = new ScheduledThreadPoolExecutor(1);

    private final @NotNull Object _syncWaitLock = new Object();
    private final @NotNull ArrayList<Thread> _syncWaitingThreads = new ArrayList<>();

    private Looper() {
        loopAgain();
    }

    // # Public
    synchronized public static @NotNull Looper getShared() {
        if (singleton == null) {
            singleton = new Looper();
        }

        return singleton;
    }

    public void subscribe(@NotNull final LooperClient client, int delay) {
        _clientsManager.subscribe(client, delay);
    }

    public void subscribe(@NotNull final LooperClient client) {
        subscribe(client, 0);
    }

    public void unsubscribe(@NotNull final LooperClient client) {
        _clientsManager.unsubscribe(client);
    }

    public void performSyncCallbackInBackground(@NotNull final Callback.Blank callback) {
        _callbacksManager.queueCallback(callback);
        enterSyncWaitLock();
    }

    public <T> void performSyncCallbackInBackground(@NotNull final Callback.WithParameter<T> callback, @Nullable final T parameter) {
        _callbacksManager.queueCallback(callback, parameter);
        enterSyncWaitLock();
    }

    public void performSyncCallbackOnAWTQueue(@NotNull final Callback.Blank callback) {
        if (java.awt.EventQueue.isDispatchThread()) {
            callback.perform();
            return;
        }

        try {
            java.awt.EventQueue.invokeAndWait(() -> {
                callback.perform();
            });
        } catch (Exception e) {

        }
    }

    public <T> void performSyncCallbackOnAWTQueue(@NotNull final Callback.WithParameter<T> callback, @Nullable final T parameter) {
        if (java.awt.EventQueue.isDispatchThread()) {
            callback.perform(parameter);
            return;
        }

        try {
            java.awt.EventQueue.invokeAndWait(() -> {
                callback.perform(parameter);
            });
        } catch (Exception e) {

        }
    }

    public void performAsyncCallbackInBackground(@NotNull final Callback.Blank callback) {
        _callbacksManager.queueCallback(callback);
    }

    public <T> void performAsyncCallbackInBackground(@NotNull final Callback.WithParameter<T> callback, @Nullable final T parameter) {
        _callbacksManager.queueCallback(callback, parameter);
    }

    public void performAsyncCallbackOnAWTQueue(@NotNull final Callback.Blank callback) {
        java.awt.EventQueue.invokeLater(() -> {
            callback.perform();
        });
    }

    public <T> void performAsyncCallbackOnAWTQueue(@NotNull final Callback.WithParameter<T> callback, @Nullable final T parameter) {
        java.awt.EventQueue.invokeLater(() -> {
            callback.perform(parameter);
        });
    }

    // # Private
    private void loop() {
        _clientsManager.loop();
        _callbacksManager.loop();
    }

    private void loopAgain() {
        releaseAllSyncWaitLocks();

        _looperExecutor.schedule(_looperRunnable, LOOPER_INTERVAL_MSEC, TimeUnit.MILLISECONDS);
    }

    private void enterSyncWaitLock() {
        synchronized (_syncWaitLock) {
            _syncWaitingThreads.add(Thread.currentThread());
        }

        try {
            Thread.currentThread().wait();
        } catch (Exception e) {

        }

        // No need to remove itself from the array, its removed by the releaser
    }

    private void releaseAllSyncWaitLocks() {
        synchronized (_syncWaitLock) {
            for (Thread t : _syncWaitingThreads) {
                t.interrupt();
            }

            _syncWaitingThreads.clear();
        }
    }

    class ClientsManager {

        private @NotNull HashSet<LooperClientSubscription> _subscriptions = new HashSet<>();

        private final @NotNull Object _mainLock = new Object();

        private void subscribe(@NotNull final LooperClient client, int delay) {
            synchronized (_mainLock) {
                _subscriptions.add(new LooperClientSubscription(client, delay));
            }
        }

        private void unsubscribe(@NotNull final LooperClient client) {
            synchronized (_mainLock) {
                LooperClientSubscription subToRemove = null;

                for (LooperClientSubscription sub : _subscriptions) {
                    if (sub.client == client) {
                        subToRemove = sub;
                        break;
                    }
                }

                _subscriptions.remove(subToRemove);
            }
        }

        private void loop() {
            Collection<LooperClientSubscription> subscriptions;

            synchronized (_mainLock) {
                subscriptions = CollectionUtilities.copyAsImmutable(_subscriptions);
            }

            for (LooperClientSubscription sub : subscriptions) {
                try {
                    sub.loop(LOOPER_INTERVAL_MSEC);
                } catch (Exception e) {
                    Logger.error(this, "Uncaught exception in client loop: " + e.toString());
                    e.printStackTrace(System.out);
                }
            }
        }
    }

    class CallbacksManager {

        private final @NotNull Object _mainLock = new Object();

        private @NotNull ArrayList<LooperCallback> _callbacks = new ArrayList();

        private void queueCallback(@NotNull Callback.Blank callback) {
            synchronized (_mainLock) {
                _callbacks.add(new LooperCallback(callback));
            }
        }

        private <T> void queueCallback(@NotNull Callback.WithParameter<T> callback, @Nullable T parameter) {
            synchronized (_mainLock) {
                _callbacks.add(new LooperCallback(callback, parameter));
            }
        }

        private void clearCallbacks() {
            synchronized (_mainLock) {
                _callbacks.clear();
            }
        }

        private void loop() {
            Collection<LooperCallback> callbacks;

            synchronized (_mainLock) {
                callbacks = CollectionUtilities.copyAsImmutable(_callbacks);
            }

            for (LooperCallback callback : callbacks) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            callback.perform();
                        } catch (Exception e) {
                            Logger.error(this, "Uncaught exception in callback: " + e.toString());
                            e.printStackTrace(System.out);
                        }
                    }
                };

                new Thread(runnable).start();
            }

            clearCallbacks();
        }
    }
}

class LooperClientSubscription {

    public final @NotNull LooperClient client;

    private final int _timer;
    private int _currentTimer;

    LooperClientSubscription(@NotNull LooperClient client, int timer) {
        this.client = client;
        this._timer = timer;
        this._currentTimer = timer;
    }

    public void loop(int dt) {
        // Loop only every specific interval
        if (_timer != 0) {
            _currentTimer -= dt;

            if (_currentTimer > 0) {
                return;
            }

            // Reset timer to original value
            // Overkill time value is also added to the result
            _currentTimer += _timer;
        }

        // Loop
        this.client.loop();
    }
}

class LooperCallback<T> {

    public final @NotNull Callback.Blank callback;
    public final @NotNull Callback.WithParameter<T> callbackWithParameter;
    public final @Nullable T parameter;

    LooperCallback(@NotNull Callback.Blank callback) {
        this.callback = callback;
        this.callbackWithParameter = null;
        this.parameter = null;
    }

    LooperCallback(@NotNull Callback.WithParameter<T> callback, @Nullable T parameter) {
        this.callback = null;
        this.callbackWithParameter = callback;
        this.parameter = parameter;
    }

    public void perform() {
        if (callback != null) {
            this.callback.perform();
        } else {
            this.callbackWithParameter.perform(parameter);
        }
    }
}
