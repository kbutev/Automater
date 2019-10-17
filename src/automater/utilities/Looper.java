/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * Contains utilities for performing callbacks, and updating objects every interval.
 * 
 * LooperClients are updated by one single specific background thread. Do not block it!
 * 
 * The performSyncCallback blocks the caller thread until the given callback is performed on
 * a background thread (same background thread as the LooperClients).
 *
 * @author Bytevi
 */
public class Looper {
    public static int LOOPER_INTERVAL_MSEC = 50;

    private static Looper singleton;
    
    @NotNull private final ClientsManager _clientsManager = new ClientsManager();
    @NotNull private final CallbacksManager _callbacksManager = new CallbacksManager();
    
    @NotNull private final Runnable _looperRunnable = new Runnable() {
        public void run()
        {
            loop();
            loopAgain();
        }
    };
    
    @NotNull private final ScheduledThreadPoolExecutor _looperExecutor = new ScheduledThreadPoolExecutor(1);
    
    @NotNull private final Object _syncWaitLock = new Object();
    @NotNull private final ArrayList<Thread> _syncWaitingThreads = new ArrayList<>();
    
    private Looper()
    {
        loopAgain();
    }
    
    // # Public

    synchronized public static @NotNull Looper getShared()
    {
        if (singleton == null)
        {
            singleton = new Looper();
        }

        return singleton;
    }
    
    public void subscribe(@NotNull final LooperClient client, int delay)
    {
        _clientsManager.subscribe(client, delay);
    }

    public void subscribe(@NotNull final LooperClient client)
    {
        subscribe(client, 0);
    }

    public void unsubscribe(@NotNull final LooperClient client)
    {
        _clientsManager.unsubscribe(client);
    }
    
    public void performSyncCallbackInBackground(@NotNull final SimpleCallback callback)
    {
        _callbacksManager.queueCallback(callback);
        enterSyncWaitLock();
    }
    
    public <T> void performSyncCallbackInBackground(@NotNull final Callback<T> callback, @Nullable final T parameter)
    {
        _callbacksManager.queueCallback(callback, parameter);
        enterSyncWaitLock();
    }
    
    public void performSyncCallbackOnAWTQueue(@NotNull final SimpleCallback callback)
    {
        if (java.awt.EventQueue.isDispatchThread())
        {
            callback.perform();
            return;
        }
        
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    callback.perform();
                }
            });
        } catch (Exception e) {
            
        }
    }
    
    public <T> void performSyncCallbackOnAWTQueue(@NotNull final Callback<T> callback, @Nullable final T parameter)
    {
        if (java.awt.EventQueue.isDispatchThread())
        {
            callback.perform(parameter);
            return;
        }
        
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    callback.perform(parameter);
                }
            });
        } catch (Exception e) {
            
        }
    }
    
    public void performAsyncCallbackInBackground(@NotNull final SimpleCallback callback)
    {
        _callbacksManager.queueCallback(callback);
    }
    
    public <T> void performAsyncCallbackInBackground(@NotNull final Callback<T> callback, @Nullable final T parameter)
    {
        _callbacksManager.queueCallback(callback, parameter);
    }
    
    public void performAsyncCallbackOnAWTQueue(@NotNull final SimpleCallback callback)
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                callback.perform();
            }
        });
    }
    
    public <T> void performAsyncCallbackOnAWTQueue(@NotNull final Callback<T> callback, @Nullable final T parameter)
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                callback.perform(parameter);
            }
        });
    }
    
    // # Private
    
    private void loop()
    {
        _clientsManager.loop();
        _callbacksManager.loop();
    }
    
    private void loopAgain()
    {
        releaseAllSyncWaitLocks();
        
        _looperExecutor.schedule(_looperRunnable, LOOPER_INTERVAL_MSEC, TimeUnit.MILLISECONDS);
    }
    
    private void enterSyncWaitLock()
    {
        synchronized(_syncWaitLock)
        {
            _syncWaitingThreads.add(Thread.currentThread());
        }
        
        try {
            Thread.currentThread().wait();
        } catch (Exception e) {
            
        }
        
        // No need to remove itself from the array, its removed by the releaser
    }
    
    private void releaseAllSyncWaitLocks()
    {
        synchronized(_syncWaitLock)
        {
            for (Thread t : _syncWaitingThreads)
            {
                t.interrupt();
            }
            
            _syncWaitingThreads.clear();
        }
    }
    
    class ClientsManager 
    {
        @NotNull private HashSet<LooperClientSubscription> _subscriptions = new HashSet<>();
        
        @NotNull private final Object _mainLock = new Object();
        
        private void subscribe(@NotNull final LooperClient client, int delay)
        {
            synchronized (_mainLock)
            {
                _subscriptions.add(new LooperClientSubscription(client, delay));
            }
        }

        private void unsubscribe(@NotNull final LooperClient client)
        {
            synchronized (_mainLock)
            {
                LooperClientSubscription subToRemove = null;
                
                for (LooperClientSubscription sub : _subscriptions)
                {
                    if (sub.client == client)
                    {
                        subToRemove = sub;
                        break;
                    }
                }
                
                _subscriptions.remove(subToRemove);
            }
        }
        
       private void loop()
       {
            Collection<LooperClientSubscription> subscriptions;

            synchronized (_mainLock)
            {
                subscriptions = CollectionUtilities.copyAsImmutable(_subscriptions);
            }
            
            for (LooperClientSubscription sub : subscriptions)
            {
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
        @NotNull private final Object _mainLock = new Object();
        
        @NotNull private ArrayList<LooperCallback> _callbacks = new ArrayList();
        
        private void queueCallback(@NotNull SimpleCallback callback)
        {
            synchronized (_mainLock)
            {
                _callbacks.add(new LooperCallback(callback));
            }
        }
        
        private <T> void queueCallback(@NotNull Callback<T> callback, @Nullable T parameter)
        {
            synchronized (_mainLock)
            {
                _callbacks.add(new LooperCallback(callback, parameter));
            }
        }
        
        private void clearCallbacks()
        {
            synchronized (_mainLock)
            {
                _callbacks.clear();
            }
        }
        
        private void loop()
        {
            Collection<LooperCallback> callbacks;

            synchronized (_mainLock)
            {
                callbacks = CollectionUtilities.copyAsImmutable(_callbacks);
            }
            
            for (LooperCallback callback : callbacks)
            {
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
    @NotNull public final LooperClient client;
    
    @NotNull private final int _timer;
    private int _currentTimer;
    
    LooperClientSubscription(@NotNull LooperClient client, int timer)
    {
        this.client = client;
        this._timer = timer;
        this._currentTimer = timer;
    }
    
    public void loop(int dt)
    {
        // Loop only every specific interval
        if (_timer != 0)
        {
            _currentTimer -= dt;
            
            if (_currentTimer > 0)
            {
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

class LooperCallback <T> {
    @NotNull public final SimpleCallback callback;
    @NotNull public final Callback<T> callbackWithParameter;
    @Nullable public final T parameter;
        
    LooperCallback(@NotNull SimpleCallback callback)
    {
        this.callback = callback;
        this.callbackWithParameter = null;
        this.parameter = null;
    }
    
    LooperCallback(@NotNull Callback<T> callback, @Nullable T parameter)
    {
        this.callback = null;
        this.callbackWithParameter = callback;
        this.parameter = parameter;
    }
    
    public void perform()
    {
        if (callback != null)
        {
            this.callback.perform();
        }
        else
        {
            this.callbackWithParameter.perform(parameter);
        }
    }
}
