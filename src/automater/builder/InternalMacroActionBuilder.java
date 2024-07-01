/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.builder;

import automater.di.DI;
import automater.model.InputKeystroke;
import automater.model.KeyEventKind;
import automater.model.MouseKey;
import automater.storage.StorageValue;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import automater.model.MutableStorageValue;
import automater.model.action.MacroAction;
import automater.model.action.MacroHardwareAction;
import automater.parser.InputKeyValueParser;
import automater.utilities.Logger;
import automater.utilities.Point;

/**
 *
 * @author Kristiyan Butev
 */
public interface InternalMacroActionBuilder {
    
    interface Protocol {
        
        @NotNull String getActionName();
        
        @NotNull List<MutableStorageValue.Protocol> buildValues();
        
        @NotNull MacroAction build(double timestamp) throws Exception;
    }
    
    class DoNothing implements Protocol {
        
        @Override
        public @NotNull String getActionName() {
            return "Do nothing";
        }
        
        @Override
        public @NotNull List<MutableStorageValue.Protocol> buildValues() {
            return new ArrayList<>();
        }
        
        @Override
        public @NotNull MacroAction build(double timestamp) throws Exception {
            Logger.messageVerbose(this, "Build " + getActionName() + " time = " + timestamp);
            
            return new automater.model.action.DoNothing(timestamp);
        }
    }
    
    class MacroHardwareActionKeyboardClick implements Protocol {
        
        @NotNull InputKeystroke.AWT keystroke = InputKeystroke.AWT.anyKey();
        @NotNull KeyEventKind eventKind = KeyEventKind.press;
        
        final @NotNull StorageValue<InputKeystroke.AWT> keystrokeStorage =
                StorageValue.build(() -> { return keystroke; }, (var value) -> { keystroke = value; });
        
        final @NotNull StorageValue<String> eventKindStorage =
                StorageValue.build(() -> { return eventKind.value; }, (var value) -> { setEventKind(value); });
        
        @Override
        public @NotNull String getActionName() {
            return "Keyboard Click";
        }
        
        @Override
        public @NotNull List<MutableStorageValue.Protocol> buildValues() {
            var values = new ArrayList<MutableStorageValue.Protocol>();
            
            values.add(new MutableStorageValue.Keystroke("keystroke", keystrokeStorage));
            values.add(new MutableStorageValue.EnumList("type", eventKindStorage, KeyEventKind.allValues));
            
            return values;
        }
        
        @Override
        public @NotNull MacroAction build(double timestamp) throws Exception {
            Logger.messageVerbose(this, "Build " + getActionName() + " time = " + timestamp + " kind = " + eventKind.value + " key = " + keystroke.toString());
            
            return new MacroHardwareAction.AWTClick(timestamp, eventKind, keystroke);
        }
        
        public void setEventKind(@NotNull String value) {
            var result = KeyEventKind.named(value);
            
            if (result != null) {
                eventKind = result;
            } else {
                Logger.error(this, "Invalid KeyEventKind input string");
            }
        }
    }
    
    class MacroHardwareActionMouseClick implements Protocol {
        
        private final InputKeyValueParser.AWTProtocol parser = DI.get(InputKeyValueParser.AWTProtocol.class);
        
        @NotNull MouseKey key = MouseKey.LEFT;
        @NotNull KeyEventKind eventKind = KeyEventKind.tap;
        
        final @NotNull StorageValue<String> keyStorage =
                StorageValue.build(() -> { return key.value; }, (var value) -> { setKey(value); });
        
        final @NotNull StorageValue<String> eventKindStorage =
                StorageValue.build(() -> { return eventKind.value; }, (var value) -> { setEventKind(value); });
        
        @Override
        public @NotNull String getActionName() {
            return "Mouse Click";
        }
        
        @Override
        public @NotNull List<MutableStorageValue.Protocol> buildValues() {
            var values = new ArrayList<MutableStorageValue.Protocol>();
            
            values.add(new MutableStorageValue.EnumList("key", keyStorage, MouseKey.allValuesAsStrings));
            values.add(new MutableStorageValue.EnumList("type", eventKindStorage, KeyEventKind.allValues));
            
            return values;
        }
        
        @Override
        public @NotNull MacroAction build(double timestamp) throws Exception {
            var mouseKey = parser.parseFromMouseKey(key);
            
            Logger.messageVerbose(this, "Build " + getActionName() + " time = " + timestamp + " kind = " + eventKind.value + " key = " + mouseKey.toString());
            
            return new MacroHardwareAction.AWTClick(timestamp, eventKind, new InputKeystroke.AWT(mouseKey));
        }
        
        public void setKey(@NotNull String value) {
            var result = MouseKey.named(value);
            
            if (result != null) {
                key = result;
            } else {
                Logger.error(this, "Invalid MouseKey input string");
            }
        }
        
        public void setEventKind(@NotNull String value) {
            var result = KeyEventKind.named(value);
            
            if (result != null) {
                eventKind = result;
            } else {
                Logger.error(this, "Invalid KeyEventKind input string");
            }
        }
    }
    
    class MacroHardwareActionMouseMove implements Protocol {
        
        @NotNull Point point = Point.zero();
        
        final @NotNull StorageValue<Point> pointStorage =
                StorageValue.build(() -> { return point; }, (var value) -> { point = value; });
        
        @Override
        public @NotNull String getActionName() {
            return "Mouse Move";
        }
        
        @Override
        public @NotNull List<MutableStorageValue.Protocol> buildValues() {
            var values = new ArrayList<MutableStorageValue.Protocol>();
            
            values.add(new MutableStorageValue.PointXY("point", pointStorage, null));
            
            return values;
        }
        
        @Override
        public @NotNull MacroAction build(double timestamp) throws Exception {
            Logger.messageVerbose(this, "Build " + getActionName() + " time = " + timestamp + " point = " + point.toString());
            
            return new MacroHardwareAction.MouseMove(timestamp, point);
        }
    }
    
    class MacroHardwareActionMouseScroll implements Protocol {
        
        double scrollValue;
        
        final @NotNull StorageValue<Double> scrollStorage =
                StorageValue.build(() -> { return scrollValue; }, (var value) -> { scrollValue = value; });
        
        @Override
        public @NotNull String getActionName() {
            return "Mouse Scroll";
        }
        
        @Override
        public @NotNull List<MutableStorageValue.Protocol> buildValues() {
            var values = new ArrayList<MutableStorageValue.Protocol>();
            
            values.add(new MutableStorageValue.SimpleNumber("scroll", scrollStorage, null));
            
            return values;
        }
        
        @Override
        public @NotNull MacroAction build(double timestamp) throws Exception {
            Logger.messageVerbose(this, "Build " + getActionName() + " time = " + timestamp + " scroll = " + scrollValue);
            
            return new MacroHardwareAction.MouseScroll(timestamp, Point.zero(), Point.make(0, scrollValue));
        }
    }
}
