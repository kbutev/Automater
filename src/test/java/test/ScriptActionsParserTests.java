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
import automater.model.action.ScriptAction;
import automater.model.action.ScriptHardwareAction;
import automater.parser.ScriptActionsParser;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Kristiyan Butev
 */
public class ScriptActionsParserTests implements Constants {
    
    // Immutable.
    ScriptActionsParser.Protocol parser;
    
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
        parser = DI.get(ScriptActionsParser.Protocol.class);
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void testOfClicksParsing() throws Exception {
        // From object to json and back
        var list = new ArrayList<ScriptAction>();
        var first = new ScriptHardwareAction.Click(1.25, KeyEventKind.press, new Keystroke(KeyValue._X));
        var second = new ScriptHardwareAction.Click(2.2, KeyEventKind.release, new Keystroke(KeyValue._X));
        var third = new ScriptHardwareAction.Click(3, KeyEventKind.tap, new Keystroke(KeyValue._Z));
        list.add(first);
        list.add(second);
        list.add(third);
        
        var json = parser.parseToJSON(list);
        
        list = (ArrayList<ScriptAction>)new ArrayList(parser.parseFromJSON(json));
        assertTrue(list.size() == 3);
        
        if (list.get(0) instanceof ScriptHardwareAction.Click a1) {
            assertTrue(a1.timestamp == first.timestamp);
            assertTrue(a1.keystroke.equals(first.keystroke));
        } else {
            assertTrue(false);
        }
        
        if (list.get(1) instanceof ScriptHardwareAction.Click a2) {
            assertTrue(a2.timestamp == second.timestamp);
            assertTrue(a2.keystroke.equals(second.keystroke));
        } else {
            assertTrue(false);
        }
        
        if (list.get(2) instanceof ScriptHardwareAction.Click a3) {
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
