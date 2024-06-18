/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.execution;

import automater.model.macro.Macro;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface MacroProcessBuilder {
    
    interface Protocol {
        
        void setupWithMacro(@NotNull Macro.Protocol macro);
        
        void setRootType(boolean value);
        
        @NotNull MacroProcess.Protocol build() throws Exception;
    }
    
    class Impl implements Protocol {
        
        private boolean rootType = true;
        private @Nullable Macro.Protocol macro;
        
        public Impl() {
            
        }
        
        @Override
        public void setupWithMacro(@NotNull Macro.Protocol macro) {
            this.macro = macro;
        }
        
        @Override
        public void setRootType(boolean value) {
            rootType = value;
        }
        
        @Override
        public @NotNull MacroProcess.Protocol build() throws Exception {
            var commandsBuild = new CommandBuilder.Impl();
            commandsBuild.setMacrosActions(macro.getActions());
            var commands = commandsBuild.build();
            var result = rootType ? new MacroProcess.Root(commands) : new MacroProcess.Child(commands);
            return result;
        }
    }
}
