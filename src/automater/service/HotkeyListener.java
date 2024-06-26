/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.service;

import automater.model.KeyEventKind;
import automater.model.InputKeystroke;
import automater.model.event.CapturedEvent;
import automater.model.event.CapturedHardwareEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface HotkeyListener {
    
    interface Delegate {
        
        boolean shouldStop();
        
        void onKeyPressed(@NotNull InputKeystroke.Protocol key);
        void onExit();
    }
    
    interface Protocol {
        @Nullable Delegate getDelegate();
        void setDelegate(@NotNull Delegate delegate);
        
        void start();
        void stop();
    }
    
    class Impl implements Protocol, EventMonitor.Listener {
        
        private final EventMonitor.Protocol monitor = new EventMonitor.Impl();
        
        private @Nullable Delegate delegate;
        
        @Override
        public Delegate getDelegate() {
            return delegate;
        }
        
        @Override
        public void setDelegate(@NotNull Delegate delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public void start() {
            assert delegate != null;
            
            monitor.setListener(this);
            
            try {
                monitor.start();
            } catch (Exception e) {
                delegate.onExit();
            }
        }
        
        @Override
        public void stop() {
            assert monitor.isRecording();
            
            try {
                monitor.stop(); 
            } catch (Exception e) {
                return;
            }
            
            delegate.onExit();
        }
        
        // # EventMonitor.Listener
        
        @Override
        public void onEventEmitted(@NotNull CapturedEvent event) {
            if (event instanceof CapturedHardwareEvent.Click click) {
                if (click.keystroke.isKeyboard() && click.kind == KeyEventKind.release) {
                    delegate.onKeyPressed(click.keystroke);
                    
                    if (delegate.shouldStop()) {
                        stop();
                    }
                }
            }
        }
    }
}
