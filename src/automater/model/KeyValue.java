/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a keyboard or mouse key value.
 * 
 * Use toString() and fromString() to display an appropriate user readable text
 * value or to serialize the enum.
 * 
 * @author Bytevi
 */
public enum KeyValue {
    UNKNOWN,
    
    @SerializedName("ML") MOUSE_LEFT_CLICK,
    @SerializedName("MR") MOUSE_RIGHT_CLICK,
    @SerializedName("M3") MOUSE_MIDDLE_CLICK,
    @SerializedName("M4") MOUSE_4_CLICK,
    @SerializedName("M5") MOUSE_5_CLICK,

    @SerializedName("1") n1,
    @SerializedName("2") n2,
    @SerializedName("3") n3,
    @SerializedName("4") n4,
    @SerializedName("5") n5,
    @SerializedName("6") n6,
    @SerializedName("7") n7,
    @SerializedName("8") n8,
    @SerializedName("9") n9,
    @SerializedName("0") n0,
    
    A,
    B,
    C,
    D,
    E,
    F,
    G,
    H,
    I,
    J,
    K,
    L,
    M,
    N,
    O,
    P,
    Q,
    R,
    S,
    T,
    U,
    V,
    W,
    X,
    Y,
    Z,
    
    NATIVE_KEY_FIRST,
    NATIVE_KEY_LAST,
    NATIVE_KEY_TYPED,
    NATIVE_KEY_PRESSED,
    NATIVE_KEY_RELEASED,
    KEY_LOCATION_UNKNOWN,
    KEY_LOCATION_STANDARD,
    KEY_LOCATION_LEFT,
    KEY_LOCATION_RIGHT,
    KEY_LOCATION_NUMPAD,
    ESCAPE,
    F1,
    F2,
    F3,
    F4,
    F5,
    F6,
    F7,
    F8,
    F9,
    F10,
    F11,
    F12,
    F13,
    F14,
    F15,
    F16,
    F17,
    F18,
    F19,
    F20,
    F21,
    F22,
    F23,
    F24,
    BACKQUOTE,
    MINUS,
    EQUALS,
    BACKSPACE,
    TAB,
    CAPS_LOCK,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    BACK_SLASH,
    SEMICOLON,
    QUOTE,
    ENTER,
    COMMA,
    PERIOD,
    SLASH,
    SPACE,
    PRINTSCREEN,
    SCROLL_LOCK,
    PAUSE,
    INSERT,
    DELETE,
    HOME,
    END,
    PAGE_UP,
    PAGE_DOWN,
    UP,
    LEFT,
    CLEAR,
    RIGHT,
    DOWN,
    NUM_LOCK,
    SEPARATOR,
    SHIFT,
    CONTROL,
    ALT,
    META,
    CONTEXT_MENU,
    POWER,
    SLEEP,
    WAKE,
    MEDIA_PLAY,
    MEDIA_STOP,
    MEDIA_PREVIOUS,
    MEDIA_NEXT,
    MEDIA_SELECT,
    MEDIA_EJECT,
    VOLUME_MUTE,
    VOLUME_UP,
    VOLUME_DOWN,
    APP_MAIL,
    APP_CALCULATOR,
    APP_MUSIC,
    APP_PICTURES,
    BROWSER_SEARCH,
    BROWSER_HOME,
    BROWSER_BACK,
    BROWSER_FORWARD,
    BROWSER_STOP,
    BROWSER_REFRESH,
    BROWSER_FAVORITES,
    KATAKANA,
    UNDERSCORE,
    FURIGANA,
    KANJI,
    HIRAGANA,
    YEN,
    SUN_HELP,
    SUN_STOP,
    SUN_PROPS,
    SUN_FRONT,
    SUN_OPEN,
    SUN_FIND,
    SUN_AGAIN,
    SUN_UNDO,
    SUN_COPY,
    SUN_INSERT,
    SUN_CUT;
   
    public boolean isModifier() {
        switch(this)
        {
            case CONTROL:
                return true;
            case ALT:
                return true;
            case SHIFT:
                return true;
        }

        return false;
    }

    @Override
    public String toString() {
        String value = name();

        value = value.replaceFirst("_", "");

        return value;
    }
}

