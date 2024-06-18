/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Performs callbacks and loops.
 *
 * @author Bytevi
 */
public interface Looper {
    
    public static int LOOPER_INTERVAL_MSEC = 50;
    
    interface Subscriber {
        
        void onLoop(double dt);
    }
    
    interface Protocol {
        
        void subscribe(@NotNull Subscriber subscriber);
        void subscribe(@NotNull Subscriber subscriber, int delayInMS);
        void unsubscribe(@NotNull Subscriber subscriber);
        void performAsync(final @NotNull Callback.Blank callback);
        
        void start();
    }
    
    interface Main extends Protocol {
        
        void performSync(final @NotNull Callback.Blank callback);
    }
    
    interface Background extends Protocol {
        
    }
    
    class Base implements Protocol {
        
        private final @NotNull Object lock = new Object();
        private final boolean isBackground;
        private final @NotNull ScheduledThreadPoolExecutor looperExecutor = new ScheduledThreadPoolExecutor(1);
        private final BackgroundCallbacksManager backgroundCallbacksManager;
        
        private boolean isStarted = false;
        private @NotNull ArrayList<SubscriberEntry> subscriptions = new ArrayList<>();
        
        Base(boolean isBackground) {
            this.isBackground = isBackground;
            backgroundCallbacksManager = isBackground ? new BackgroundCallbacksManager() : null;
        }
        
        // # Main
        
        @Override
        public void subscribe(@NotNull Subscriber subscriber) {
            subscribe(subscriber, LOOPER_INTERVAL_MSEC);
        }
        
        @Override
        public void subscribe(@NotNull Subscriber subscriber, int delayInMS) {
            synchronized (lock) {
                for (var sub : subscriptions) {
                    if (sub.sub == subscriber) {
                        return;
                    }
                }
                
                subscriptions.add(new SubscriberEntry(subscriber, delayInMS));
            }
        }
        
        @Override
        public void unsubscribe(@NotNull Subscriber subscriber) {
            synchronized (lock) {
                SubscriberEntry subToRemove = null;
                
                for (var sub : subscriptions) {
                    if (sub.sub == subscriber) {
                        subToRemove = sub;
                        break;
                    }
                }
                
                subscriptions.remove(subToRemove);
            }
        }
        
        public void performSync(final @NotNull Callback.Blank callback) {
            if (isBackground) {
                callback.perform();
            } else {
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
        }
        
        @Override
        public void performAsync(final @NotNull Callback.Blank callback) {
            if (isBackground) {
                backgroundCallbacksManager.queueCallback(callback);
            } else {
                java.awt.EventQueue.invokeLater(() -> {
                    callback.perform();
                });
            } 
        }
        
        @Override
        public void start() {
            assert !isStarted;
            isStarted = true;
            loopAgain();
        }
        
        // # Private
        
        private void loop() {
            Collection<SubscriberEntry> subscriptions;

            synchronized (lock) {
                subscriptions = CollectionUtilities.copyAsImmutable(this.subscriptions);
            }

            for (var sub : subscriptions) {
                try {
                    sub.loop(LOOPER_INTERVAL_MSEC);
                } catch (Exception e) {
                    Logger.error(this, "Uncaught exception in client loop: " + e.toString());
                    e.printStackTrace(System.out);
                }
            }
            
            if (isBackground) {
                backgroundCallbacksManager.loop();
            }
        }
        
        private void loopAgain() {
            looperExecutor.schedule(() -> {
                loop();
                loopAgain();
            },
            LOOPER_INTERVAL_MSEC, TimeUnit.MILLISECONDS);
        }
    }
    
    class SubscriberEntry {
        final @NotNull Subscriber sub;
        final int delayInMS;
        final double delayInSeconds;
        private int currentTimer;

        SubscriberEntry(@NotNull Subscriber sub, int delayInMS) {
            this.sub = sub;
            this.delayInMS = delayInMS;
            delayInSeconds = delayInMS / 1000;
            currentTimer = delayInMS;
        }
        
        public void loop(int dt) {
            // Loop only every specific interval
            if (delayInMS != 0) {
                currentTimer -= dt;

                if (currentTimer > 0) {
                    return;
                }

                // Reset timer to original value
                // Overkill time value is also added to the result
                currentTimer += delayInMS;
            }

            // Loop
            sub.onLoop(delayInSeconds);
        }
    }
    
    class MainImpl extends Base implements Main {
        
        public MainImpl() {
            super(false);
        }
    }
    
    class BackgroundImpl extends Base implements Background {
        
        public BackgroundImpl() {
            super(true);
        }
    }
    
    class BackgroundCallbacksManager {

        private final @NotNull Object lock = new Object();

        private @NotNull ArrayList<Callback.Blank> callbacks = new ArrayList();

        private void queueCallback(@NotNull Callback.Blank callback) {
            synchronized (lock) {
                callbacks.add(callback);
            }
        }

        private void clearCallbacks() {
            synchronized (lock) {
                callbacks.clear();
            }
        }

        private void loop() {
            Collection<Callback.Blank> callbacks;

            synchronized (lock) {
                callbacks = CollectionUtilities.copyAsImmutable(this.callbacks);
            }

            for (var callback : callbacks) {
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
