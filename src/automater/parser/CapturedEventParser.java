/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.parser;

import automater.model.event.CapturedEvent;
import automater.model.event.EventDescription;
import org.jetbrains.annotations.NotNull;

/**
 * Parsers captured events and other related data.
 * @author Kristiyan Butev
 */
public interface CapturedEventParser {
    
    interface Protocol {
        @NotNull CapturedEvent parseNativeEvent(@NotNull Object event) throws Exception;
        @NotNull EventDescription parseToDescription(@NotNull CapturedEvent event) throws Exception;
    }
    
    class Impl implements Protocol {
        @Override
        public @NotNull CapturedEvent parseNativeEvent(@NotNull Object event) throws Exception {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
        
        @Override
        public @NotNull EventDescription parseToDescription(@NotNull CapturedEvent event) throws Exception {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }
}
