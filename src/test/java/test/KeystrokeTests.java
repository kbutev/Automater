/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package test.java.test;

import automater.di.DI;
import automater.di.DISetup;
import automater.model.KeyEventKind;
import automater.model.InputKeyModifier;
import automater.model.InputKeyModifierValue;
import automater.model.InputKeyValue;
import automater.model.InputKeystroke;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import automater.model.action.MacroHardwareAction;
import automater.parser.MacroActionParser;

/**
 *
 * @author Kristiyan Butev
 */
public class KeystrokeTests {
    
    // Immutable.
    MacroActionParser.Protocol parser;
    
    public KeystrokeTests() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        DISetup.setup();
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        parser = DI.get(MacroActionParser.Protocol.class);
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testConstruction() throws Exception {
        try {
            var t1 = new InputKeystroke(InputKeyValue.X);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testParsing() throws Exception {
        var keystroke = new InputKeystroke(InputKeyValue.X, new InputKeyModifier(InputKeyModifierValue.ALT));
        var a = new MacroHardwareAction.Click(1.25, KeyEventKind.press, keystroke);
        
        var json = parser.parseToJSON(a);
        
        if (parser.parseFromJSON(json) instanceof MacroHardwareAction.Click result) {
            assertTrue(result.keystroke.equals(keystroke));
            assertTrue(result.keystroke.value.equals(InputKeyValue.X));
            assertFalse(result.keystroke.equals(new InputKeystroke(InputKeyValue.Z)));
            assertTrue(result.keystroke.getModifier().contains(InputKeyModifierValue.ALT));
            assertFalse(result.keystroke.getModifier().contains(InputKeyModifierValue.CTRL));
        } else {
            assertTrue(true);
        }
    }
}
