/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.service;

import automater.di.DI;
import automater.model.InputKeystroke;
import automater.model.OutputKeyValue;
import automater.model.Point;
import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.Robot;
import org.jetbrains.annotations.NotNull;
import automater.parser.OutputKeyValueParser;

/**
 *
 * @author Kristiyan Butev
 */
public interface HardwareInputSimulator {
    
    interface Protocol {
        
        void simulateKeyPress(@NotNull InputKeystroke key);
        void simulateKeyRelease(@NotNull InputKeystroke key);
        void simulateKeyTap(@NotNull InputKeystroke key);
        void simulateMouseMove(@NotNull Point point);
    }
    
    class Impl implements Protocol {
        
        private final OutputKeyValueParser.Protocol keyValueParser = DI.get(OutputKeyValueParser.Protocol.class);
        private final @NotNull Robot robot;
        
        public Impl(@NotNull GraphicsDevice screen) throws AWTException {
            robot = new Robot(screen);
        }
        
        @Override
        public void simulateKeyPress(@NotNull InputKeystroke key) {
            OutputKeyValue code;
            
            try {
                code = keyValueParser.parse(key.value);
            } catch (Exception e) {
                return;
            }
            
            // First, simulate the modifier keys
            for (var inputModifier : key.getModifier().getValues()) {
                try {
                    var ouputModifier = keyValueParser.parseModifier(inputModifier);
                    robot.keyPress(ouputModifier.getValue());
                } catch (Exception e) {
                    
                }
            }
            
            // And then simulate the actual key value
            if (key.isKeyboard()) {
                robot.keyPress(code.value);
            } else {
                robot.mousePress(code.value);
            }
        }
        
        @Override
        public void simulateKeyRelease(@NotNull InputKeystroke key) {
            OutputKeyValue code;
            
            try {
                code = keyValueParser.parse(key.value);
            } catch (Exception e) {
                return;
            }
            
            // First, simulate the modifier keys
            for (var inputModifier : key.getModifier().getValues()) {
                try {
                    var ouputModifier = keyValueParser.parseModifier(inputModifier);
                    robot.keyRelease(ouputModifier.getValue());
                } catch (Exception e) {
                    
                }
            }
            
            // And then simulate the actual key value
            if (key.isKeyboard()) {
                robot.keyRelease(code.value);
            } else {
                robot.mouseRelease(code.value);
            }
        }
        
        @Override
        public void simulateKeyTap(@NotNull InputKeystroke key) {
            OutputKeyValue code;
            
            try {
                code = keyValueParser.parse(key.value);
            } catch (Exception e) {
                return;
            }
            
            if (key.isKeyboard()) {
                robot.keyPress(code.value);
                robot.keyRelease(code.value);
            } else {
                robot.mousePress(code.value);
                robot.mouseRelease(code.value);
            }
        }
        
        @Override
        public void simulateMouseMove(@NotNull Point point) {
            robot.mouseMove((int)point.x, (int)point.y);
        }
    }
}
