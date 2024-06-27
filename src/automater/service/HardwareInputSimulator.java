/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.service;

import automater.di.DI;
import automater.model.OutputKeyValue;
import automater.provider.ScreenProvider;
import automater.utilities.Point;
import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.Robot;
import org.jetbrains.annotations.NotNull;
import automater.utilities.Logger;
import automater.utilities.Size;
import java.util.ArrayList;

/**
 *
 * @author Kristiyan Butev
 */
public interface HardwareInputSimulator {
    
    interface Protocol {
        
        void end();
    }
    
    interface AWTProtocol extends Protocol {
        
        void simulateKeyPress(@NotNull OutputKeyValue.AWT key);
        void simulateKeyRelease(@NotNull OutputKeyValue.AWT key);
        void simulateKeyTap(@NotNull OutputKeyValue.AWT key);
        void simulateMouseMove(@NotNull Point point);
        void simulateMouseScroll(@NotNull Point scroll);
    }
    
    class AWTImpl implements AWTProtocol {
        
        private final ScreenProvider.AWTGraphicsDeviceProtocol robotScreen = DI.get(ScreenProvider.AWTGraphicsDeviceProtocol.class);
        private final ScreenProvider.AWTSimulatorScreen simulatorScreen = DI.get(ScreenProvider.AWTSimulatorScreen.class);
        
        private final @NotNull Robot robot;
        private final @NotNull Size currentScreen;
        private final @NotNull Size referenceScreen;
        
        private final @NotNull ArrayList<Integer> pressedKeyboardKeys = new ArrayList<>();
        private final @NotNull ArrayList<Integer> pressedMouseKeys = new ArrayList<>();
        
        public AWTImpl(@NotNull Size referenceScreen) throws AWTException {
            robot = new Robot(robotScreen.getGraphicsDevice());
            currentScreen = simulatorScreen.getScreen().size;
            this.referenceScreen = referenceScreen;
        }
        
        @Override
        public void simulateKeyPress(@NotNull OutputKeyValue.AWT key) {
            int code = key.value;
            
            Logger.messageVerbose(this, "press " + key.toString() + " code = " + code);
            
            // Modifiers
//            var modifier = key.getModifier().getValue();
//            
//            if (modifier != null) {
//                var modifierCode = modifier.getKeyEventValue();
//                
//                Logger.messageVerbose(this, "press modifier " + modifier.toString() + " code = " + modifierCode);
//                
//                pressKey(modifierCode);
//            }
            
            // And then simulate the actual key value
            if (key.isKeyboard()) {
                pressKey(code);
            } else {
                pressMouseKey(code);
            }
        }
        
        @Override
        public void simulateKeyRelease(@NotNull OutputKeyValue.AWT key) {
            int code = key.value;
            
            Logger.messageVerbose(this, "release " + key.toString() + " code = " + code);
            
            // And then simulate the actual key value
            if (key.isKeyboard()) {
                releaseKey(code);
            } else {
                releaseMouseKey(code);
            }
            
            // Modifiers
//            var modifier = key.getModifier().getValue();
//            
//            if (modifier != null) {
//                var modifierCode = modifier.getKeyEventValue();
//                
//                Logger.messageVerbose(this, "release modifier " + modifier.toString() + " code = " + modifierCode);
//                
//                releaseKey(modifierCode);
//            }
        }
        
        @Override
        public void simulateKeyTap(@NotNull OutputKeyValue.AWT key) {
            simulateKeyPress(key);
            simulateKeyRelease(key);
        }
        
        @Override
        public void simulateMouseMove(@NotNull Point point) {
            double screenScaleX = referenceScreen.width / currentScreen.width;
            double screenScaleY = referenceScreen.height / currentScreen.height;

            var result = Point.make(point.x / screenScaleX, point.y / screenScaleY);
            
            Logger.messageVerbose(this, "move mouse to  " + result.toString());
            
            robot.mouseMove((int)result.x, (int)result.y);
        }
        
        @Override
        public void simulateMouseScroll(@NotNull Point scroll) {
            Logger.messageVerbose(this, "move scroll by  " + scroll.toString());
            
            robot.mouseWheel((int)scroll.y);
        }
        
        @Override
        public void end() {
            // Release all pressed keys
            
            for (var key : pressedKeyboardKeys) {
                releaseKey(key);
            }
            
            for (var key : pressedMouseKeys) {
                releaseMouseKey(key);
            }
            
            pressedMouseKeys.clear();
            pressedKeyboardKeys.clear();
        }
        
        private void pressKey(int code) {
            try { robot.keyPress(code); } catch(Exception e) {}
            
            if (!pressedKeyboardKeys.contains(code)) {
                pressedKeyboardKeys.add(code);
            }
        }
        
        private void releaseKey(int code) {
            try { robot.keyRelease(code); } catch(Exception e) {}
            
            if (pressedKeyboardKeys.contains(code)) {
                pressedKeyboardKeys.remove((Object)code);
            }
        }
        
        private void pressMouseKey(int code) {
            try { robot.mousePress(code); } catch(Exception e) {}
            
            if (!pressedMouseKeys.contains(code)) {
                pressedMouseKeys.add(code);
            }
        }
        
        private void releaseMouseKey(int code) {
            try { robot.mouseRelease(code); } catch(Exception e) {}
            
            if (pressedMouseKeys.contains(code)) {
                pressedMouseKeys.remove((Object)code);
            }
        }
    }
}
