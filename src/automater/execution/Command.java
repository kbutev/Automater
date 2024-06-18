/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.execution;

import automater.model.action.MacroHardwareAction;
import automater.utilities.CollectionUtilities;
import automater.utilities.Logger;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface Command {
    
    enum Status {
        idle, running, finished
    }
    
    interface Protocol {
        
        @NotNull Status getStatus();
        double getTimestamp();
        
        void update(@NotNull Context.Protocol context);
    }
    
    class Base implements Protocol {
        
        private final double time;
        
        private @NotNull Status status = Status.idle;
        
        public Base(double time) {
            this.time = time;
        }
        
        @Override
        public @NotNull Status getStatus() {
            return status;
        }
        
        @Override
        public double getTimestamp() {
            return time;
        }
        
        @Override
        public void update(@NotNull Context.Protocol context) {
            Logger.message(this, "update");
            
            if (getStatus() == Status.finished) {
                return;
            }
            
            if (getStatus() == Status.idle) {
                onStart(context);
                status = Status.running;
            }
            
            onUpdate(context);
            
            if (getStatus() == Status.finished) {
                onEnd(context);
            }
        }
        
        void onStart(@NotNull Context.Protocol context) {
            // Override me
        }
        
        void onUpdate(@NotNull Context.Protocol context) {
            // Override me
            status = Status.finished;
        }
        
        void onEnd(@NotNull Context.Protocol context) {
            // Override me
        }
    }
    
    class HardwareEvents extends Base {
        
        private final @NotNull List<MacroHardwareAction.Generic> actions;
        
        public HardwareEvents(@NotNull List<MacroHardwareAction.Generic> actions) {
            super(!actions.isEmpty() ? actions.get(actions.size()).timestamp : 0);
            this.actions = CollectionUtilities.copy(actions);
        }
        
        @Override
        void onStart(@NotNull Context.Protocol context) {
            
        }
        
        @Override
        void onUpdate(@NotNull Context.Protocol context) {
            
        }
        
        @Override
        void onEnd(@NotNull Context.Protocol context) {
            
        }
    }
}
