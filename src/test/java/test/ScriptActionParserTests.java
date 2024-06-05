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
import automater.model.Point;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import automater.model.action.MacroAction;
import automater.model.action.MacroHardwareAction;
import automater.parser.MacroActionParser;

/**
 *
 * @author Kristiyan Butev
 */
public class ScriptActionParserTests implements Constants {
    
    // Immutable.
    MacroActionParser.Protocol parser;
    
    public ScriptActionParserTests() {
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
        parser = DI.get(MacroActionParser.Protocol.class);
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void testClickRawParsing() throws Exception {
        // From raw json to object
        var rawJSON = new JsonObject();
        rawJSON.addProperty(JSONKeys.ACTION_TYPE, JSONValues.CLICK_TYPE);
        rawJSON.addProperty(JSONKeys.ACTION_TIME, "1.0");
        rawJSON.addProperty(JSONKeys.ACTION_KEY, "release");
        var keystrokeJSON = new JsonObject();
        keystrokeJSON.addProperty(JSONKeys.KEYSTROKE_V, KeyValue._X.name());
        rawJSON.add(JSONKeys.ACTION_VALUE, keystrokeJSON);
        
        var result = parser.parseFromJSON(rawJSON);
        
        if (result instanceof MacroHardwareAction.Click action) {
            assertTrue(action.actionType.equals("hardware.c"));
            assertTrue(action.timestamp == 1.0);
            assertTrue(action.kind == KeyEventKind.release);
        } else {
            assertTrue(false);
        }
    }
    
    @Test
    public void testClickParsing() throws Exception {
        // From object to json and back
        var action = new MacroHardwareAction.Click(0, KeyEventKind.press, new Keystroke(KeyValue._X));
        
        if (!(parser.parseToJSON(action) instanceof JsonObject parsedJSON)) {
            throw new Exception("Invalid json");
        }
        
        assertTrue(parsedJSON.get(JSONKeys.ACTION_TYPE).getAsString().equals(JSONValues.CLICK_TYPE));
        assertTrue(parsedJSON.get(JSONKeys.ACTION_TIME).getAsDouble() == action.timestamp);
        assertTrue(parsedJSON.get(JSONKeys.ACTION_KEY).getAsString().equals(action.kind.name()));
        
        var keystrokeJSON = parsedJSON.get(JSONKeys.ACTION_VALUE).getAsJsonObject();
        assertTrue(keystrokeJSON != null);
        
        var result = parser.parseFromJSON(parsedJSON);
        
        if (result instanceof MacroHardwareAction.Click parsedAction) {
            assertTrue(parsedAction.actionType.equals(action.actionType));
            assertTrue(parsedAction.timestamp == action.timestamp);
            assertTrue(parsedAction.kind == action.kind);
        } else {
            assertTrue(false);
        }
    }
    
    @Test
    public void testMouseMoveRawParsing() throws Exception {
        // From raw json to object
        var json = new JsonObject();
        json.addProperty(JSONKeys.ACTION_TYPE, JSONValues.MOUSE_MOVE_TYPE);
        json.addProperty(JSONKeys.ACTION_TIME, "2.0");
        
        var point = new JsonObject();
        point.addProperty(JSONKeys.X, "100");
        point.addProperty(JSONKeys.Y, "200");
        json.add("p", point);
        
        var result = parser.parseFromJSON(json);
        
        if (result instanceof MacroHardwareAction.MouseMove action) {
            assertTrue(action.actionType.equals("hardware.mm"));
            assertTrue(action.timestamp == 2.0);
            assertTrue(action.point.x == 100);
            assertTrue(action.point.y == 200);
        } else {
            assertTrue(false);
        }
    }
    
    @Test
    public void testMouseMoveParsing() throws Exception {
        // From object to json and back
        var action = new MacroHardwareAction.MouseMove(0, Point.make(10, 15));
        
        if (!(parser.parseToJSON(action) instanceof JsonObject parsedJSON)) {
            throw new Exception("Invalid json");
        }
        
        assertTrue(parsedJSON.get(JSONKeys.ACTION_TYPE).getAsString().equals(JSONValues.MOUSE_MOVE_TYPE));
        assertTrue(parsedJSON.get(JSONKeys.ACTION_TIME).getAsDouble() == action.timestamp);
        var pointJSON = parsedJSON.get(JSONKeys.ACTION_P).getAsJsonObject();
        assertTrue(pointJSON.get(JSONKeys.X).getAsDouble() == action.point.x);
        assertTrue(pointJSON.get(JSONKeys.Y).getAsDouble() == action.point.y);
        
        var result = parser.parseFromJSON(parsedJSON);
        
        if (result instanceof MacroHardwareAction.MouseMove parsedAction) {
            assertTrue(parsedAction.actionType.equals(action.actionType));
            assertTrue(parsedAction.timestamp == action.timestamp);
            assertTrue(parsedAction.point.x == action.point.x);
            assertTrue(parsedAction.point.y == action.point.y);
        } else {
            assertTrue(false);
        }
    }
    
    @Test
    public void testMouseScrollRawParsing() throws Exception {
        // From raw json to object
        var json = new JsonObject();
        json.addProperty(JSONKeys.ACTION_TYPE, JSONValues.MOUSE_SCROLL_TYPE);
        json.addProperty(JSONKeys.ACTION_TIME, "3.0");
        
        var point = new JsonObject();
        point.addProperty(JSONKeys.X, "200");
        point.addProperty(JSONKeys.Y, "200");
        json.add(JSONKeys.ACTION_P1, point);
        
        var scroll = new JsonObject();
        scroll.addProperty(JSONKeys.X, "0");
        scroll.addProperty(JSONKeys.Y, "5");
        json.add(JSONKeys.ACTION_P2, scroll);
        
        var result = parser.parseFromJSON(json);
        
        if (result instanceof MacroHardwareAction.MouseScroll action) {
            assertTrue(action.actionType.equals(JSONValues.MOUSE_SCROLL_TYPE));
            assertTrue(action.timestamp == 3.0);
            assertTrue(action.point.x == 200);
            assertTrue(action.point.y == 200);
            assertTrue(action.scroll.x == 0);
            assertTrue(action.scroll.y == 5);
        } else {
            assertTrue(false);
        }
    }
    
    @Test
    public void testMouseScrollParsing() throws Exception {
        // From object to json and back
        var action = new MacroHardwareAction.MouseScroll(0, Point.make(10, 15), Point.make(0, 0.5f));
        
        if (!(parser.parseToJSON(action) instanceof JsonObject parsedJSON)) {
            throw new Exception("Invalid json");
        }
        
        assertTrue(parsedJSON.get(JSONKeys.ACTION_TYPE).getAsString().equals(JSONValues.MOUSE_SCROLL_TYPE));
        assertTrue(parsedJSON.get(JSONKeys.ACTION_TIME).getAsDouble() == action.timestamp);
        var point1JSON = parsedJSON.get(JSONKeys.ACTION_P1).getAsJsonObject();
        assertTrue(point1JSON.get(JSONKeys.X).getAsDouble() == action.point.x);
        assertTrue(point1JSON.get(JSONKeys.Y).getAsDouble() == action.point.y);
        var point2JSON = parsedJSON.get(JSONKeys.ACTION_P2).getAsJsonObject();
        assertTrue(point2JSON.get(JSONKeys.X).getAsDouble() == action.scroll.x);
        assertTrue(point2JSON.get(JSONKeys.Y).getAsDouble() == action.scroll.y);
        
        var result = parser.parseFromJSON(parsedJSON);
        
        if (result instanceof MacroHardwareAction.MouseScroll parsedAction) {
            assertTrue(parsedAction.actionType.equals(action.actionType));
            assertTrue(parsedAction.timestamp == action.timestamp);
            assertTrue(parsedAction.point.x == action.point.x);
            assertTrue(parsedAction.point.y == action.point.y);
            assertTrue(parsedAction.scroll.x == action.scroll.x);
            assertTrue(parsedAction.scroll.y == action.scroll.y);
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
    
    @Test
    public void testClickParsingWithMissingFieldEnum() throws Exception {
        var json = new JsonObject();
        json.addProperty(JSONKeys.ACTION_TYPE, JSONValues.CLICK_TYPE);
        json.addProperty(JSONKeys.ACTION_TIME, "1.0");
        // KEY_KEY enum is missing
        var keystrokeJSON = new JsonObject();
        keystrokeJSON.addProperty(JSONKeys.KEYSTROKE_V, KeyValue._X.name());
        json.add(JSONKeys.ACTION_VALUE, keystrokeJSON);
        
        try {
            parser.parseFromJSON(json);
            assertTrue(false);
        } catch (Exception e) {
            // Expected behavior
        }
    }
    
    @Test
    public void testClickParsingWithInvalidFieldEnum() throws Exception {
        var json = new JsonObject();
        json.addProperty(JSONKeys.ACTION_TYPE, JSONValues.CLICK_TYPE);
        json.addProperty(JSONKeys.ACTION_TIME, "1.0");
        json.addProperty(JSONKeys.ACTION_KEY, "zzzz"); // Invalid enum value
        var keystrokeJSON = new JsonObject();
        keystrokeJSON.addProperty(JSONKeys.KEYSTROKE_V, KeyValue._X.name());
        json.add(JSONKeys.ACTION_VALUE, keystrokeJSON);
        
        try {
            parser.parseFromJSON(json);
            assertTrue(false);
        } catch (Exception e) {
            // Expected behavior
        }
    }
    
    @Test
    public void testClickParsingWithMissingFieldObject() throws Exception {
        var json = new JsonObject();
        json.addProperty(JSONKeys.ACTION_TYPE, JSONValues.CLICK_TYPE);
        // KEY_TIME is missing
        json.addProperty(JSONKeys.ACTION_KEY, "click");
        var keystrokeJSON = new JsonObject();
        keystrokeJSON.addProperty(JSONKeys.KEYSTROKE_V, KeyValue._X.name());
        json.add(JSONKeys.ACTION_VALUE, keystrokeJSON);
        
        try {
            parser.parseFromJSON(json);
            assertTrue(false);
        } catch (Exception e) {
            // Expected behavior
        }
    }
    
    @Test
    public void testMouseMoveParsingWithMissingSubfield() throws Exception {
        var json = new JsonObject();
        json.addProperty(JSONKeys.ACTION_TYPE, JSONValues.MOUSE_MOVE_TYPE);
        json.addProperty(JSONKeys.ACTION_TIME, "2.0");
        
        var point = new JsonObject();
        point.addProperty(JSONKeys.X, "100");
        // Missing KEY_Y
        json.add(JSONKeys.ACTION_P, point);
        
        try {
            parser.parseFromJSON(json);
            assertTrue(false);
        } catch (Exception e) {
            // Expected behavior
        }
    }
}
