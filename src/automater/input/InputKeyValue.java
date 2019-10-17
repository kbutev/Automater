/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a system key value.
 * 
 * Use toString() and fromString() to display an appropriate user readable text
 * value or to serialize the enum.
 * 
 * @author Bytevi
 */
public enum InputKeyValue {
   UNKNOWN,
   
   _MOUSE_LEFT_CLICK,
   _MOUSE_RIGHT_CLICK,
   _MOUSE_MIDDLE_CLICK,
   _MOUSE_4_CLICK,
   _MOUSE_5_CLICK,
   
   _NATIVE_KEY_FIRST,
   _NATIVE_KEY_LAST,
   _NATIVE_KEY_TYPED,
   _NATIVE_KEY_PRESSED,
   _NATIVE_KEY_RELEASED,
   _KEY_LOCATION_UNKNOWN,
   _KEY_LOCATION_STANDARD,
   _KEY_LOCATION_LEFT,
   _KEY_LOCATION_RIGHT,
   _KEY_LOCATION_NUMPAD,
   _ESCAPE,
   _F1,
   _F2,
   _F3,
   _F4,
   _F5,
   _F6,
   _F7,
   _F8,
   _F9,
   _F10,
   _F11,
   _F12,
   _F13,
   _F14,
   _F15,
   _F16,
   _F17,
   _F18,
   _F19,
   _F20,
   _F21,
   _F22,
   _F23,
   _F24,
   _BACKQUOTE,
   _1,
   _2,
   _3,
   _4,
   _5,
   _6,
   _7,
   _8,
   _9,
   _0,
   _MINUS,
   _EQUALS,
   _BACKSPACE,
   _TAB,
   _CAPS_LOCK,
   _A,
   _B,
   _C,
   _D,
   _E,
   _F,
   _G,
   _H,
   _I,
   _J,
   _K,
   _L,
   _M,
   _N,
   _O,
   _P,
   _Q,
   _R,
   _S,
   _T,
   _U,
   _V,
   _W,
   _X,
   _Y,
   _Z,
   _OPEN_BRACKET,
   _CLOSE_BRACKET,
   _BACK_SLASH,
   _SEMICOLON,
   _QUOTE,
   _ENTER,
   _COMMA,
   _PERIOD,
   _SLASH,
   _SPACE,
   _PRINTSCREEN,
   _SCROLL_LOCK,
   _PAUSE,
   _INSERT,
   _DELETE,
   _HOME,
   _END,
   _PAGE_UP,
   _PAGE_DOWN,
   _UP,
   _LEFT,
   _CLEAR,
   _RIGHT,
   _DOWN,
   _NUM_LOCK,
   _SEPARATOR,
   _SHIFT,
   _CONTROL,
   _ALT,
   _META,
   _CONTEXT_MENU,
   _POWER,
   _SLEEP,
   _WAKE,
   _MEDIA_PLAY,
   _MEDIA_STOP,
   _MEDIA_PREVIOUS,
   _MEDIA_NEXT,
   _MEDIA_SELECT,
   _MEDIA_EJECT,
   _VOLUME_MUTE,
   _VOLUME_UP,
   _VOLUME_DOWN,
   _APP_MAIL,
   _APP_CALCULATOR,
   _APP_MUSIC,
   _APP_PICTURES,
   _BROWSER_SEARCH,
   _BROWSER_HOME,
   _BROWSER_BACK,
   _BROWSER_FORWARD,
   _BROWSER_STOP,
   _BROWSER_REFRESH,
   _BROWSER_FAVORITES,
   _KATAKANA,
   _UNDERSCORE,
   _FURIGANA,
   _KANJI,
   _HIRAGANA,
   _YEN,
   _SUN_HELP,
   _SUN_STOP,
   _SUN_PROPS,
   _SUN_FRONT,
   _SUN_OPEN,
   _SUN_FIND,
   _SUN_AGAIN,
   _SUN_UNDO,
   _SUN_COPY,
   _SUN_INSERT,
   _SUN_CUT;
   
   public boolean isModifier()
   {
       switch(this)
       {
           case _CONTROL:
               return true;
           case _ALT:
               return true;
           case _SHIFT:
               return true;
       }
       
       return false;
   }
   
   @Override
   public String toString()
   {
       String value = name();
       
       value = value.replaceFirst("_", "");
       
       return value;
   }
   
   public static InputKeyValue fromString(@NotNull String value)
   {
       value = "_" + value;
       
       return InputKeyValue.valueOf(value);
   }
}
