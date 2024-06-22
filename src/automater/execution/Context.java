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
        private final double currentTime;
        
        public Process(@NotNull Owner owner, double time) {
            parent = owner;
            root = null;
            currentTime = time;
        }
        
        public Process(@NotNull Owner owner, @NotNull Owner root, double time) {
            parent = owner;
            this.root = root;
            currentTime = time;
        }
        
        public Process(@NotNull Context.Protocol context, double time) {
            parent = context.getParentOwner();
            root = context.getRootOwner();
            currentTime = time;
        }
        
        @Override
        public double getTime() {
            return currentTime;
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
