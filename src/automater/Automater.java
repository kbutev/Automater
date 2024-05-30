/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater;

import automater.di.DI;
import automater.recorder.parser.RecorderSystemKeyboardTranslator;
import automater.recorder.parser.RecorderSystemMouseTranslator;
import automater.ui.viewcontroller.PrimaryViewContoller;
import automater.utilities.DeviceNotifications;
import automater.work.ActionSettingsManager;
import automater.work.Executor;
import automater.work.parser.ActionsFromMacroInputsParser;
import org.jetbrains.annotations.NotNull;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.int4.dirk.api.Injector;
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
        setupDependencyInjection();
        
        // Recorder preload
        DI.get(automater.recorder.Recorder.Protocol.class).preload();
        
        // Show tray icon
        DeviceNotifications.getShared().showTrayIcon();
        DeviceNotifications.getShared().setTrayIconTooltip(TextValue.getText(TextValue.SystemTray_Tooltip));
        
        // Start first screen
        primaryViewContoller.start();
    }
    
    static void setupDependencyInjection() {
        Injector injector = DI.internalInjector;
        
        injector.registerInstance(new automater.work.ActionSettingsManager.Impl());
        
        injector.registerInstance(new automater.storage.GeneralStorage.Impl());
        
        injector.registerInstance(new RecorderSystemKeyboardTranslator.Impl());
        injector.registerInstance(new RecorderSystemMouseTranslator.Impl());
        
        injector.registerInstance(new automater.recorder.parser.RecorderNativeParser.Impl());
        injector.registerInstance(new automater.work.parser.ActionsFromMacroInputsParser.Impl());
        injector.registerInstance(new automater.recorder.parser.RecorderMasterNativeParser.Impl());
        
        injector.registerInstance(new automater.recorder.Recorder.Defaults());
        injector.registerInstance(new automater.recorder.Recorder.Impl());
        
        injector.registerInstance(new automater.work.Executor.Impl());
    }
}
