/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package test.java.test;

import automater.di.DI;
import automater.di.DISetup;
import automater.model.KeyEventKind;
import automater.model.InputKeystroke;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import automater.model.action.MacroHardwareAction;
import automater.parser.MacroActionParser;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 *
 * @author Kristiyan Butev
 */
public class KeystrokeTests {
    
    public static final int key_x = NativeKeyEvent.VC_X;
    public static final int key_z = NativeKeyEvent.VC_Z;
    
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
            var t1 = InputKeystroke.AWT.buildFromCode(NativeKeyEvent.VC_X);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testParsing() throws Exception {
        var keystroke = InputKeystroke.AWT.buildFromCode(key_x);
        var a = new MacroHardwareAction.AWTClick(1.25, KeyEventKind.press, keystroke);
        
        var json = parser.parseToJSON(a);
        
        if (parser.parseFromJSON(json) instanceof MacroHardwareAction.AWTClick result) {
            assertTrue(result.keystroke.equals(keystroke));
            assertTrue(result.keystroke.value.code == key_x);
            assertFalse(result.keystroke.value.code == key_z);
        } else {
            assertTrue(true);
        }
    }
}
