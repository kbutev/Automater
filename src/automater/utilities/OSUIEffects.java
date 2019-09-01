/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

/**
 * Contains functionality for the operating system specific UI effects.
 * 
 * @author Bytevi
 */
public class OSUIEffects {
    private static OSUIEffects singleton;
    
    private final boolean _active;
    private final SystemTray _tray;
    
    private OSUIEffects()
    {
        _active = SystemTray.isSupported();
        
        if (_active)
        {
            _tray = SystemTray.getSystemTray();
        }
        else
        {
            _tray = null;
        }
    }

    synchronized public static OSUIEffects getShared()
    {
        if (singleton == null)
        {
            singleton = new OSUIEffects();
        }

        return singleton;
    }
    
    public void displayOSNotification(String title, String message, String tooltip)
    {
        displayOSNotification(title, message, tooltip, MessageType.NONE);
    }
    
    public void displayOSNotification(String title, String message, String tooltip, MessageType type)
    {
        if (!_active)
        {
            return;
        }
        
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(tooltip);
        
        try {
            _tray.add(trayIcon);
        } catch (Exception e) {
            return;
        }
        
        trayIcon.displayMessage(title, message, type);
    }
}
