/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.model;

import automater.model.event.CapturedEvent;
import automater.model.event.CapturedHardwareEvent;
import automater.utilities.CollectionUtilities;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class EventFilter {

    boolean keyboard = false;
    boolean mouseKeys = false;
    boolean mouseMove = false;
    boolean mouseScroll = false;

    ArrayList<Keystroke> keystrokes = new ArrayList<>();

    public @NotNull
    EventFilter copy() {
        var result = new EventFilter();
        result.keyboard = keyboard;
        result.keystrokes = CollectionUtilities.copy(keystrokes);
        result.mouseKeys = mouseKeys;
        result.mouseMove = mouseMove;
        result.mouseScroll = mouseScroll;
        return result;
    }

    public boolean filtersOut(@NotNull CapturedEvent event) {
        if (event instanceof CapturedHardwareEvent.Click key) {
            if ((!keyboard && key.keystroke.isKeyboard())
                    || (!mouseKeys && key.keystroke.isMouse())) {
                return true;
            }

            for (var keystroke : keystrokes) {
                if (key.keystroke.equals(keystroke)) {
                    return true;
                }
            }
        } else if (event instanceof CapturedHardwareEvent.MouseMove) {
            if (mouseMove) {
                return true;
            }
        } else if (event instanceof CapturedHardwareEvent.MouseScroll) {
            if (mouseScroll) {
                return true;
            }
        }

        return false;
    }
}
