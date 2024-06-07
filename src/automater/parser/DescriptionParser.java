/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.model.macro.MacroSummary;
import automater.model.action.MacroAction;
import automater.model.action.MacroActionDescription;
import automater.model.action.MacroHardwareAction;
import automater.model.event.CapturedEvent;
import automater.model.event.CapturedHardwareEvent;
import automater.model.event.EventDescription;
import automater.model.macro.MacroSummaryDescription;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface DescriptionParser {
    
    interface Protocol {
        
        @NotNull EventDescription parseCapturedEvent(@NotNull CapturedEvent event) throws Exception;
        @NotNull MacroActionDescription parseMacroAction(@NotNull MacroAction action) throws Exception;
        @NotNull List<MacroActionDescription> parseMacroActions(@NotNull List<MacroAction> actions) throws Exception;
        @NotNull MacroSummaryDescription parseMacroSummary(@NotNull MacroSummary summary) throws Exception;
    }
    
    class Impl implements Protocol {
        
        @Override
        public @NotNull EventDescription parseCapturedEvent(@NotNull CapturedEvent event) throws Exception {
            if (event instanceof CapturedHardwareEvent.Click click) {
                return new EventDescription("click." + click.kind.name(), click.keystroke.toString());
            } else if (event instanceof CapturedHardwareEvent.MouseMove mmove) {
                return new EventDescription("mouse.move", mmove.point.toString());
            } else if (event instanceof CapturedHardwareEvent.MouseScroll scroll) {
                return new EventDescription("mouse.scroll", scroll.scroll.toString());
            }

            throw new UnsupportedOperationException("Unrecognizable native event");
        }
        
        @Override
        public @NotNull MacroActionDescription parseMacroAction(@NotNull MacroAction action) throws Exception {
            var timestamp = String.format("%.1f", action.getTimestamp());
            
            if (action instanceof MacroHardwareAction.Click click) {
                return new MacroActionDescription(timestamp, "click." + click.kind.name(), click.keystroke.toString());
            } else if (action instanceof MacroHardwareAction.MouseMove mmove) {
                return new MacroActionDescription(timestamp, "mouse.move", mmove.point.toString());
            } else if (action instanceof MacroHardwareAction.MouseScroll scroll) {
                return new MacroActionDescription(timestamp, "mouse.scroll", scroll.scroll.toString());
            }
            
            throw new UnsupportedOperationException("Unrecognizable native event");
        }
        
        @Override
        public @NotNull List<MacroActionDescription> parseMacroActions(@NotNull List<MacroAction> actions) throws Exception {
            var result = new ArrayList<MacroActionDescription>();
            
            for (var action : actions) {
                result.add(parseMacroAction(action));
            }
            
            return result;
        }
        
        @Override
        public @NotNull MacroSummaryDescription parseMacroSummary(@NotNull MacroSummary summary) throws Exception {
            var count = String.format("%d", summary.actionsCount);
            return new MacroSummaryDescription(summary.name, count);
        }
    }
}
