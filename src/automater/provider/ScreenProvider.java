/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.provider;

import automater.model.Screen;
import automater.utilities.Size;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import org.jetbrains.annotations.NotNull;

/**
 * Provides screens.
 * @author Kristiyan Butev
 */
public interface ScreenProvider {
    
    interface Protocol {
        
        @NotNull Screen getScreen();
    }
    
    interface AWTGraphicsDeviceProtocol extends Protocol {
        
        @NotNull GraphicsDevice getGraphicsDevice();
    }
    
    class AWTGraphicsDevice implements AWTGraphicsDeviceProtocol {
        
        private final @NotNull GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        @Override
        public @NotNull Screen getScreen() {
            var display = graphicsDevice.getDisplayMode();
            return new Screen(Size.make(display.getWidth(), display.getHeight()));
        }
        
        @Override
        public @NotNull GraphicsDevice getGraphicsDevice() {
            return graphicsDevice;
        }
    }
    
    interface AWTSimulatorScreenProtocol extends Protocol {
        
    }
    
    class AWTSimulatorScreen implements AWTSimulatorScreenProtocol {
        
        @Override
        public @NotNull Screen getScreen() {
            return new Screen(Size.make(Toolkit.getDefaultToolkit().getScreenSize()));
        }
    }
}
