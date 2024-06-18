/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater;

import automater.ui.text.TextValue;
import automater.di.DI;
import automater.di.DISetup;
import automater.router.MasterRouter;
import automater.service.NativeEventMonitor;
import automater.storage.PreferencesStorage;
import automater.utilities.DeviceNotifications;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Disable jnativehook logging
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        // Dependency injection
        DISetup.setup();
        
        // Recorder setup
        var recorder = DI.get(NativeEventMonitor.Protocol.class);
        
        try {
            recorder.start();
        } catch(Exception e) {
            return;
        }
        
        // Shutdown cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            automater.utilities.Logger.message("Automater", "Automater shutdown hook");
            
            try {
                recorder.stop();
            } catch(Exception e) {}
        }));

        // Show tray icon
        DeviceNotifications.getShared().showTrayIcon();
        DeviceNotifications.getShared().setTrayIconTooltip(TextValue.getText(TextValue.SystemTray_Tooltip));

        // Start
        DI.get(automater.utilities.Looper.Main.class).start();
        DI.get(automater.utilities.Looper.Background.class).start();
        DI.get(PreferencesStorage.Protocol.class).load();
        DI.get(MasterRouter.Protocol.class).start();
    }
}
