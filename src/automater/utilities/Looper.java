/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

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
 * All callbacks are done on new separate background threads, they can be blocked w/o any worries.
 * 
 * The performSyncCallback blocks the caller thread until the given callback is performed on
 * a background thread (same background thread as the LooperClients).
 *
 * @author Bytevi
 */
public class Looper {
    public static int LOOPER_INTERVAL_MSEC = 50;

    private static Looper singleton;
    
    private final ClientsManager _clientsManager = new ClientsManager();
    private final CallbacksManager _callbacksManager = new CallbacksManager();
    
    private final Runnable _looperRunnable = new Runnable() {
        public void run()
        {
            loop();
            loopAgain();
        }
    };
    
    private final ScheduledThreadPoolExecutor _looperExecutor = new ScheduledThreadPoolExecutor(1);
    
    private final Object _syncWaitLock = new Object();
    private final ArrayList<Thread> _syncWaitingThreads = new ArrayList<>();
    
    private Looper()
    {
        loopAgain();
    }

    synchronized public static Looper getShared()
    {
        if (singleton == null)
        {
            singleton = new Looper();
        }

        return singleton;
    }
    
    public void subscribe(final LooperClient client, int delay)
    {
        _clientsManager.subscribe(client, delay);
    }

    public void subscribe(final LooperClient client)
    {
        subscribe(client, 0);
    }

    public void unsubscribe(final LooperClient client)
    {
        _clientsManager.unsubscribe(client);
    }
    
    public void performSyncCallback(final SimpleCallback callback)
    {
        _callbacksManager.queueCallback(callback);
        enterSyncWaitLock();
    }
    
    public void performAsyncCallback(final SimpleCallback callback)
    {
        _callbacksManager.queueCallback(callback);
    }
    
    public <T> void performSyncCallback(final Callback<T> callback, final T parameter)
    {
        _callbacksManager.queueCallback(callback, parameter);
        enterSyncWaitLock();
    }

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
        private HashSet<LooperClientSubscription> _subscriptions = new HashSet<>();
        
        private final Object _mainLock = new Object();
        
        private void subscribe(final LooperClient client, int delay)
        {
            synchronized (_mainLock)
            {
                _subscriptions.add(new LooperClientSubscription(client, delay));
            }
        }

        private void unsubscribe(final LooperClient client)
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
        private ArrayList<LooperCallback> _callbacks = new ArrayList();
        
        private final Object _mainLock = new Object();
        
        private void queueCallback(SimpleCallback callback)
        {
            synchronized (_mainLock)
            {
                _callbacks.add(new LooperCallback(callback));
            }
        }
        
        private <T> void queueCallback(Callback<T> callback, T parameter)
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
    public final LooperClient client;
    
    private final int _timer;
    private int _currentTimer;
    
    LooperClientSubscription(LooperClient client, int timer)
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
    public final SimpleCallback callback;
    public final Callback<T> callbackWithParameter;
    public final T parameter;
        
    LooperCallback(SimpleCallback callback)
    {
        this.callback = callback;
        this.callbackWithParameter = null;
        this.parameter = null;
    }
    
    LooperCallback(Callback<T> callback, T parameter)
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
