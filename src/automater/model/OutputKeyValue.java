/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface OutputKeyValue {
    
    interface Protocol {
        
        boolean isKeyboard();
        boolean isMouse();
    }
    
    class AWT implements Protocol {
        public final int value;
        public final boolean keyboard;
        
        public static @NotNull AWT buildKeyboard(int value) {
            return new AWT(value, true);
        }
        
        public static @NotNull AWT buildMouse(int value) {
            return new AWT(value, false);
        }
    
        AWT(int value, boolean keyboard) {
            this.value = value;
            this.keyboard = keyboard;
        }
        
        @Override
        public boolean isKeyboard() {
            return keyboard;
        }
        
        @Override
        public boolean isMouse() {
            return !keyboard;
        }
    }
}
