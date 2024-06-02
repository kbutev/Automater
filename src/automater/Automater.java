/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater;

import automater.di.DI;
import automater.di.DISetup;
import automater.model.KeyEventKind;
import automater.model.KeyValue;
import automater.model.action.ScriptHardwareAction;
import automater.model.event.CapturedHardwareEvent;
import automater.ui.viewcontroller.PrimaryViewContoller;
import automater.utilities.DeviceNotifications;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;

/**
 * Automater entry point.
 * 
 * @author Bytevi
 */
public class Automater {
    @NotNull public static final PrimaryViewContoller primaryViewContoller = new PrimaryViewContoller();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Disable jnativehook logging
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        
        // Shutdown cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            automater.utilities.Logger.message("Automater", "Automater shutdown hook");
            
            // Make sure Recorder is no longer operating
            try {
                DI.get(automater.recorder.Recorder.Protocol.class).stop();
            } catch (Exception e) {
                
            }
            
            // Make sure Executor is no longer operating
            DI.get(automater.work.Executor.Protocol.class).stopAll();
        }));
        
        // Dependency injection
        DISetup.setup();
        
        // Recorder preload
        DI.get(automater.recorder.Recorder.Protocol.class).preload();
        
        // Show tray icon
        DeviceNotifications.getShared().showTrayIcon();
        DeviceNotifications.getShared().setTrayIconTooltip(TextValue.getText(TextValue.SystemTray_Tooltip));
        
        // Start first screen
        primaryViewContoller.start();
    }
}
