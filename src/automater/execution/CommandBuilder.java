/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.execution;

import automater.model.action.MacroAction;
import automater.model.action.MacroHardwareAction;
import automater.utilities.Errors;
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
        
        @NotNull List<Command.Protocol> build() throws Exception;
    }
    
    class Impl implements Protocol {
        
        private @Nullable List<MacroAction> actions;
        
        public Impl() {
            
        }
        
        // # Protocol
        
        @Override
        public void setMacrosActions(@NotNull List<MacroAction> actions) {
            this.actions = actions;
        }
        
        @Override
        public @NotNull List<Command.Protocol> build() throws Exception {
            if (actions == null) {
                throw Errors.illegalStateError();
            }
            
            return buildFromActions(actions);
        }
        
        private @NotNull List<Command.Protocol> buildFromActions(@NotNull List<MacroAction> actions) throws Exception {
            var result = new ArrayList<Command.Protocol>();
            
            var currentHardwareActions = new ArrayList<MacroHardwareAction.Generic>();
            
            for (var action : actions) {
                if (action instanceof MacroHardwareAction.Generic hardwareAction) {
                    currentHardwareActions.add(hardwareAction);
                } else {
                    result.add(new Command.HardwareEvents(currentHardwareActions));
                    currentHardwareActions.clear();
                    
                    // TODO: handle other types of actions
                }
            }
            
            if (!currentHardwareActions.isEmpty()) {
                result.add(new Command.HardwareEvents(currentHardwareActions));
            }
            
            return result;
        }
    }
}
