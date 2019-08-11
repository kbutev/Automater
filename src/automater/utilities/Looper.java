/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Contains utilities for performing callbacks, and updating objects every interval.
 *
 * @author Bytevi
 */
public class Looper {
    public static int LOOPER_INTERVAL_MSEC = 100;

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

    public void subscribe(final LooperClient client)
    {
        _clientsManager.subscribe(client);
    }

    public void unsubscribe(final LooperClient client)
    {
        _clientsManager.unsubscribe(client);
    }
    
    public void performCallback(final SimpleCallback callback)
    {
        _callbacksManager.queueCallback(callback);
    }
    
    public <T> void performCallback(final Callback<T> callback, final T parameter)
    {
        _callbacksManager.queueCallback(callback, parameter);
    }

    private void loop()
    {
        _clientsManager.loop();
        _callbacksManager.loop();
    }
    
    private void loopAgain()
    {
        _looperExecutor.schedule(_looperRunnable, LOOPER_INTERVAL_MSEC, TimeUnit.MILLISECONDS);
    }
    
    class ClientsManager 
    {
        private ArrayList<LooperClient> _clients = new ArrayList<>();
        
        private final Object _mainLock = new Object();
        
        private void subscribe(final LooperClient client)
        {
            synchronized (_mainLock)
            {
                if (!_clients.contains(client))
                {
                    _clients.add(client);
                }
            }
        }

        private void unsubscribe(final LooperClient client)
        {
            synchronized (_mainLock)
            {
                _clients.remove(client);
            }
        }
        
       private void loop()
       {
            Collection<LooperClient> clients;

            synchronized (_mainLock)
            {
                clients = Collections.unmodifiableCollection(_clients);
            }
            
            for (LooperClient client : clients)
            {
                client.loop();
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
                callbacks = Collections.unmodifiableCollection(_callbacks);
            }
            
            for (LooperCallback callback : callbacks)
            {
                callback.perform();
            }
            
            clearCallbacks();
        }
    }
    
    class LooperCallback <T> {
        public final SimpleCallback callback;
        public final Callback<T> callbackWithParameter;
        public final T parameter;
        
        private LooperCallback(SimpleCallback callback)
        {
            this.callback = callback;
            this.callbackWithParameter = null;
            this.parameter = null;
        }
        
        private LooperCallback(Callback<T> callback, T parameter)
        {
            this.callback = null;
            this.callbackWithParameter = callback;
            this.parameter = parameter;
        }
        
        private void perform()
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
}
