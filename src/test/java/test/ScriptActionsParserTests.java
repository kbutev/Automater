/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package test.java.test;

import automater.di.DI;
import automater.di.DISetup;
import automater.model.KeyEventKind;
import automater.model.InputKeystroke;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import automater.model.action.MacroAction;
import automater.model.action.MacroHardwareAction;
import automater.parser.MacroActionsParser;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 *
 * @author Kristiyan Butev
 */
public class ScriptActionsParserTests implements Constants {
    
    public static final int key_x = NativeKeyEvent.VC_X;
    public static final int key_z = NativeKeyEvent.VC_Z;
    
    // Immutable.
    MacroActionsParser.Protocol parser;
    
    public ScriptActionsParserTests() {
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
        parser = DI.get(MacroActionsParser.Protocol.class);
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void testOfClicksParsing() throws Exception {
        // From object to json and back
        var list = new ArrayList<MacroAction>();
        var first = new MacroHardwareAction.AWTClick(1.25, KeyEventKind.press, InputKeystroke.AWT.buildFromCode(key_x));
        var second = new MacroHardwareAction.AWTClick(2.2, KeyEventKind.release, InputKeystroke.AWT.buildFromCode(key_x));
        var third = new MacroHardwareAction.AWTClick(3, KeyEventKind.tap, InputKeystroke.AWT.buildFromCode(key_z));
        list.add(first);
        list.add(second);
        list.add(third);
        
        var json = parser.parseToJSON(list);
        
        var parsedList = (ArrayList<MacroAction>)new ArrayList(parser.parseFromJSON(json));
        assertTrue(parsedList.size() == list.size());
        
        if (parsedList.get(0) instanceof MacroHardwareAction.Click a1) {
            assertTrue(a1.timestamp == first.timestamp);
            assertTrue(a1.keystroke.equals(first.keystroke));
        } else {
            assertTrue(false);
        }
        
        if (parsedList.get(1) instanceof MacroHardwareAction.Click a2) {
            assertTrue(a2.timestamp == second.timestamp);
            assertTrue(a2.keystroke.equals(second.keystroke));
        } else {
            assertTrue(false);
        }
        
        if (parsedList.get(2) instanceof MacroHardwareAction.Click a3) {
            assertTrue(a3.timestamp == third.timestamp);
            assertTrue(a3.keystroke.equals(third.keystroke));
        } else {
            assertTrue(false);
        }
    }
    
    @Test
    public void testParsingWithBlankData() throws Exception {
        var json = new JsonObject();
        
        try {
            parser.parseFromJSON(json);
            assertTrue(false);
        } catch (Exception e) {
            // Expected behavior
        }
    }
    
    @Test
    public void testParsingWithInvalidType() throws Exception {
        var json = new JsonObject();
        json.addProperty(JSONKeys.ACTION_TYPE, "hardware.invalid");
        
        try {
            parser.parseFromJSON(json);
            assertTrue(false);
        } catch (Exception e) {
            // Expected behavior
        }
    }
}
