/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater;

import automater.recorder.Recorder;
import automater.ui.viewcontroller.PrimaryViewContoller;
import automater.utilities.DeviceNotifications;
import automater.work.Executor;
import com.sun.istack.internal.NotNull;
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
        // Start first screen
        primaryViewContoller.start();
        
        // Disable jnativehook logging
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        
        // Shutdown cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                automater.utilities.Logger.message("Automater", "Automater shutdown hook");
                
                // Make sure Recorder is no longer operating
                try {
                    Recorder.getDefault().stop();
                } catch (Exception e) {
                    
                }
                
                // Make sure Executor is no longer operating
                Executor.getDefault().stopAll();
            }
        }));
        
        // Recorder preload
        Recorder.getDefault().preload();
        
        // Show tray icon
        DeviceNotifications.getShared().showTrayIcon();
        DeviceNotifications.getShared().setTrayIconTooltip(TextValue.getText(TextValue.SystemTray_Tooltip));
    }
}
