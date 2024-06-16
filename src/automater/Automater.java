/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater;

import automater.di.DI;
import automater.di.DISetup;
import automater.router.MasterRouter;
import automater.service.NativeEventMonitor;
import automater.storage.PreferencesStorage;
import automater.utilities.DeviceNotifications;
import automater.utilities.FileSystem;
import automater.utilities.Path;
import com.google.gson.Gson;
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
        var gson = new Gson();
        var path = Path.getLocalDirectory().withSubpath("preferences.json");
        var json = gson.toJson(path);
        var result = gson.fromJson(json, Path.class);
        var file = result.getFile();
        var extension = path.extension();
        var lastComponent = path.lastComponent();
        var parent = path.parent();
        
        try {
            var contents = FileSystem.readFromFile(file);
            automater.utilities.Logger.message("Automater", "test: " + result.toString());
        } catch (Exception e) {
            automater.utilities.Logger.message("Automater", "error: " + e);
        }
        
        DI.get(PreferencesStorage.Protocol.class).load();
        DI.get(MasterRouter.Protocol.class).start();
    }
}
