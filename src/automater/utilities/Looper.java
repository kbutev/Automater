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
import java.util.function.Function;

/**
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
    
    public void queueCallback(final Function<Void, Boolean> callback)
    {
        _callbacksManager.queueCallback(callback);
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
        private ArrayList<Function<Void, Boolean>> _callbacks = new ArrayList();
        
        private final Object _mainLock = new Object();
        
        private void queueCallback(Function<Void, Boolean> callback)
        {
            synchronized (_mainLock)
            {
                _callbacks.add(callback);
            }
        }
        
        private void queueCallbacks(ArrayList<Function<Void, Boolean>> callbacks)
        {
            synchronized (_mainLock)
            {
                _callbacks.addAll(callbacks);
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
            ArrayList<Function<Void, Boolean>> repeatCallbacks = new ArrayList();
            Collection<Function<Void, Boolean>> callbacks;

            synchronized (_mainLock)
            {
                callbacks = Collections.unmodifiableCollection(_callbacks);
            }
            
            for (Function<Void, Boolean> callback : callbacks)
            {
                if (callback.apply(null))
                {
                    repeatCallbacks.add(callback);
                }
            }
            
            clearCallbacks();
            queueCallbacks(repeatCallbacks);
        }
    }
}
