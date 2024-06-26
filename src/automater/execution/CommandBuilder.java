/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.execution;

import automater.di.DI;
import automater.model.action.MacroAction;
import automater.model.action.MacroHardwareAction;
import automater.service.HardwareInputSimulator;
import automater.utilities.Errors;
import automater.utilities.Size;
import java.awt.GraphicsDevice;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface CommandBuilder {
    
    interface Protocol {
        
        void setMacrosActions(@NotNull List<MacroAction> actions);
        @Nullable HardwareInputSimulator.Protocol getHardwareInputSimulator();
        void setHardwareInputSimulator(@NotNull HardwareInputSimulator.Protocol simulator);
        
        @NotNull List<Command.Protocol> build() throws Exception;
    }
    
    class Impl implements Protocol {
        
        private final GraphicsDevice screen = DI.get(GraphicsDevice.class);
        
        private @Nullable List<MacroAction> actions;
        private @Nullable HardwareInputSimulator.Protocol simulator;
        
        public Impl(@NotNull Size referenceScreen) {
            try {
                simulator = new HardwareInputSimulator.AWTImpl(screen, referenceScreen);
            } catch (Exception e) {
                
            }
        }
        
        // # Protocol
        
        @Override
        public void setMacrosActions(@NotNull List<MacroAction> actions) {
            this.actions = actions;
        }
        
        @Override
        public @Nullable HardwareInputSimulator.Protocol getHardwareInputSimulator() {
            return simulator;
        }
        
        @Override
        public void setHardwareInputSimulator(@NotNull HardwareInputSimulator.Protocol simulator) {
            this.simulator = simulator;
        }
        
        @Override
        public @NotNull List<Command.Protocol> build() throws Exception {
            if (simulator == null || actions == null) {
                throw Errors.illegalStateError();
            }
            
            return buildFromActions(actions);
        }
        
        private @NotNull List<Command.Protocol> buildFromActions(@NotNull List<MacroAction> actions) throws Exception {
            if (!(simulator instanceof HardwareInputSimulator.AWTProtocol awtSimulator)) {
                throw Errors.parsing();
            }
            
            
            var result = new ArrayList<Command.Protocol>();
            
            var currentHardwareActions = new ArrayList<MacroHardwareAction.Generic>();
            
            for (var action : actions) {
                if (action instanceof MacroHardwareAction.Generic hardwareAction) {
                    currentHardwareActions.add(hardwareAction);
                } else {
                    result.add(new Command.AWTHardwareEvents(currentHardwareActions, awtSimulator));
                    currentHardwareActions.clear();
                    
                    // TODO: handle other types of actions
                }
            }
            
            if (!currentHardwareActions.isEmpty()) {
                result.add(new Command.AWTHardwareEvents(currentHardwareActions, awtSimulator));
            }
            
            return result;
        }
    }
}
