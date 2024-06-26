/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package test.java.test;

/**
 *
 * @author Kristiyan Butev
 */
public interface Constants {
    
    class JSONKeys {
        public static String ACTION_TYPE = "a";
        public static String ACTION_TIME = "t";
        public static String ACTION_KEY_KIND = "k";
        public static String ACTION_KEYSTROKE = "v";
        public static String ACTION_P = "p";
        public static String ACTION_P1 = "p1";
        public static String ACTION_P2 = "p2";
        
        public static String KEYSTROKE_VALUE = "v";
        public static String KEYSTROKE_MODIFIER = "m";
        
        public static String KEY_VALUE_CODE = "c";
        public static String KEY_VALUE_TYPE = "t";
        
        public static String X = "x";
        public static String Y = "y";
    }
    
    class JSONValues {
        public static String CLICK_TYPE = "hardware.c";
        public static String MOUSE_MOVE_TYPE = "hardware.mm";
        public static String MOUSE_SCROLL_TYPE = "hardware.scrl";
    }
}
