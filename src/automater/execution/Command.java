/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.execution;

import automater.model.KeyEventKind;
import automater.model.action.MacroHardwareAction;
import automater.model.macro.Macro;
import automater.service.HardwareInputSimulator;
import automater.utilities.CollectionUtilities;
import automater.utilities.Logger;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            //Logger.message(this, "update");
            
            if (getStatus() == Status.finished) {
                return;
            }
            
            if (getStatus() == Status.idle) {
                onStart(context);
                status = Status.running;
            }
            
            status = onUpdate(context);
            
            if (getStatus() == Status.finished) {
                onEnd(context);
            }
        }
        
        void onStart(@NotNull Context.Protocol context) {
            // Override me
        }
        
        @NotNull Status onUpdate(@NotNull Context.Protocol context) {
            return Status.finished;
        }
        
        void onEnd(@NotNull Context.Protocol context) {
            // Override me
        }
    }
    
    class HardwareEvents extends Base {
        
        private final @NotNull List<MacroHardwareAction.Generic> actions;
        private final @NotNull HardwareInputSimulator.Protocol simulator;
        
        public HardwareEvents(@NotNull List<MacroHardwareAction.Generic> actions,
                @NotNull HardwareInputSimulator.Protocol simulator) {
            super(!actions.isEmpty() ? actions.get(actions.size()-1).timestamp : 0);
            this.actions = CollectionUtilities.copy(actions);
            this.simulator = simulator;
        }
        
        @Override
        void onStart(@NotNull Context.Protocol context) {
            
        }
        
        @Override
        @NotNull Status onUpdate(@NotNull Context.Protocol context) {
            while (!actions.isEmpty()) {
                var nextAction = actions.get(0);
                
                if (context.getTime() >= nextAction.getTimestamp()) {
                    performAction(nextAction);
                    actions.remove(0);
                } else {
                    return Status.running;
                }
            }
            
            return Status.finished;
        }
        
        @Override
        void onEnd(@NotNull Context.Protocol context) {
            
        }
        
        private void performAction(@NotNull MacroHardwareAction.Generic action) {
            if (action instanceof MacroHardwareAction.Click click) {
                if (click.kind == KeyEventKind.press) {
                    simulator.simulateKeyPress(click.keystroke);
                } else if (click.kind == KeyEventKind.release) {
                    simulator.simulateKeyRelease(click.keystroke);
                } else if (click.kind == KeyEventKind.tap) {
                    simulator.simulateKeyTap(click.keystroke);
                }
            } else {
                // TODO
            }
        }
    }
    
    class ExecuteMacro extends Base implements MacroProcess.Listener {
        
        private final @NotNull Macro.Protocol macro;
        private @Nullable MacroProcess.Protocol runningProcess;
        
        public ExecuteMacro(@NotNull Macro.Protocol macro) {
            super(0);
            this.macro = macro;
        }
        
        @Override
        void onStart(@NotNull Context.Protocol context) {
            var builder = new MacroProcessBuilder.Impl();
            builder.setRootType(true);
            builder.setupWithMacro(macro);
            
            try {
                var process = builder.build();
                process.addListener(this);
                process.start(context);
                runningProcess = process;
            } catch (Exception e) {
                Logger.error(this, "Failed to play macro, error: " + e);
            }
        }
        
        @Override
        @NotNull Status onUpdate(@NotNull Context.Protocol context) {
            // TODO
            return Status.finished;
        }
        
        @Override
        void onEnd(@NotNull Context.Protocol context) {
            // TODO
        }
        
        // # MacroProcess.Listener
        
        @Override
        public void onStart() {
            
        }
        
        @Override
        public void onNextCommand(Protocol command) {
            
        }
        
        @Override
        public void onEnd(boolean cancelled) {
            
        }
    }
}
