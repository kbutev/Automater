/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

/**
 *
 * @author Bytevi
 */
public class Looper {
    public static int LOOPER_INTERVAL_MSEC = 100;

    private static Looper singleton;
    
    private final ClientsManager _clientsManager = new ClientsManager();
    
    private final Runnable runnable = new Runnable() {
        public void run()
        {
            
        }
    };
    
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

    private void loop()
    {
        _clientsManager.loop();
    }

    private void loopAgain()
    {
        //_mainHandler.postDelayed(_loopingRunnable, LOOPER_INTERVAL_MSEC);
    }
    
    class ClientsManager 
    {
        private ArrayList<LooperClient> _clients = new ArrayList<>();
        
        private final Object mainLock = new Object();
        
        void subscribe(final LooperClient client)
        {
            synchronized (mainLock)
            {
                if (!_clients.contains(client))
                {
                    _clients.add(client);
                }
            }
        }

        void unsubscribe(final LooperClient client)
        {
            synchronized (mainLock)
            {
                _clients.remove(client);
            }
        }
        
        void loop()
        {
            // This must be called on the main thread
            Collection<LooperClient> clients;

            synchronized (mainLock)
            {
                clients = Collections.unmodifiableCollection(_clients);
            }
            
            for (LooperClient client : clients)
            {
                client.loop();
            }
        }
    }
}
