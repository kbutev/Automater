/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package test.java.test;

import automater.di.DI;
import automater.di.DISetup;
import automater.model.KeyEventKind;
import automater.model.KeyModifier;
import automater.model.KeyModifierValue;
import automater.model.KeyValue;
import automater.model.Keystroke;
import automater.model.action.ScriptHardwareAction;
import automater.parser.ScriptActionParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Kristiyan Butev
 */
public class KeystrokeTests {
    
    // Immutable.
    ScriptActionParser.Protocol parser;
    
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
        parser = DI.get(ScriptActionParser.Protocol.class);
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testConstruction() throws Exception {
        try {
            var t1 = new Keystroke(KeyValue._X);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testParsing() throws Exception {
        var keystroke = new Keystroke(KeyValue._X, new KeyModifier(KeyModifierValue.ALT));
        var a = new ScriptHardwareAction.Click(1.25, KeyEventKind.press, keystroke);
        
        var json = parser.parseToJSON(a);
        
        if (parser.parseFromJSON(json) instanceof ScriptHardwareAction.Click result) {
            assertTrue(result.keystroke.equals(keystroke));
            assertTrue(result.keystroke.value.equals(KeyValue._X));
            assertFalse(result.keystroke.equals(new Keystroke(KeyValue._Z)));
            assertTrue(result.keystroke.getModifier().contains(KeyModifierValue.ALT));
            assertFalse(result.keystroke.getModifier().contains(KeyModifierValue.CTRL));
        } else {
            assertTrue(true);
        }
    }
}
