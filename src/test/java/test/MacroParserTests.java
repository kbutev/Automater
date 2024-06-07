/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package test.java.test;

import automater.di.DI;
import automater.di.DISetup;
import automater.model.KeyEventKind;
import automater.model.KeyValue;
import automater.model.Keystroke;
import automater.model.macro.Macro;
import automater.model.action.MacroAction;
import automater.model.action.MacroHardwareAction;
import automater.parser.MacroParser;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Kristiyan Butev
 */
public class MacroParserTests implements Constants {
    
    // Immutable.
    MacroParser.Protocol parser;
    
    public MacroParserTests() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
        DISetup.setup();
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
        parser = DI.get(MacroParser.Protocol.class);
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void testStandard() throws Exception {
        // From object to json and back
        var actions = new ArrayList<MacroAction>();
        var first = new MacroHardwareAction.Click(1.25, KeyEventKind.press, new Keystroke(KeyValue._X));
        var second = new MacroHardwareAction.Click(2.2, KeyEventKind.release, new Keystroke(KeyValue._X));
        var third = new MacroHardwareAction.Click(3, KeyEventKind.tap, new Keystroke(KeyValue._Z));
        actions.add(first);
        actions.add(second);
        actions.add(third);
        
        var json = parser.parseToJSON(Macro.build("Macro test", actions));
        
        var macro = parser.parseFromJSON(json);
        var parsedActions = macro.getActions();
        
        assertTrue(parsedActions.size() == actions.size());
        
        if (parsedActions.get(0) instanceof MacroHardwareAction.Click a1) {
            assertTrue(a1.timestamp == first.timestamp);
            assertTrue(a1.keystroke.equals(first.keystroke));
        } else {
            assertTrue(false);
        }
        
        if (parsedActions.get(1) instanceof MacroHardwareAction.Click a2) {
            assertTrue(a2.timestamp == second.timestamp);
            assertTrue(a2.keystroke.equals(second.keystroke));
        } else {
            assertTrue(false);
        }
        
        if (parsedActions.get(2) instanceof MacroHardwareAction.Click a3) {
            assertTrue(a3.timestamp == third.timestamp);
            assertTrue(a3.keystroke.equals(third.keystroke));
        } else {
            assertTrue(false);
        }
    }
}

