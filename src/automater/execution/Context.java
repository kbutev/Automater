/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.execution;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface Context {
    
    interface Protocol {
        
        double getTime();
        @NotNull Owner getParentOwner();
        @NotNull Owner getRootOwner();
    }
    
    interface Owner {
        
    }
    
    class Process implements Protocol {
        
        private final @Nullable Owner parent;
        private final @Nullable Owner root;
        
        public Process(@NotNull Owner owner) {
            parent = owner;
            root = null;
        }
        
        public Process(@NotNull Context.Protocol context, @NotNull Owner owner) {
            parent = owner;
            root = context.getParentOwner();
        }
        
        @Override
        public double getTime() {
            return 0;
        }
        
        @Override
        public @NotNull Owner getParentOwner() {
            return parent;
        }
        
        @Override
        public @NotNull Owner getRootOwner() {
            return root != null ? root : parent;
        }
    }
}
