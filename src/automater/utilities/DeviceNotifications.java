/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

/**
 * Contains functionality for the operating system notifications system.
 * 
 * OS have different types of notifications, but in this case the notifications
 * the "global" ones, the ones the are displayed usually in a form of a bubble
 * and almost always have some sort of history.
 * 
 * Should NOT be confused with temporary notifications that are visible for a
 * few seconds and are always "attached" to a specific app.
 * 
 * @author Bytevi
 */
public class DeviceNotifications {
    public static final String NOTIFICATION_IMAGE_FILE = "notification_icon";
    
    private static DeviceNotifications _singleton;
    
    private final boolean _active;
    private final SystemTray _tray;
    private TrayIcon _icon;
    
    private DeviceNotifications()
    {
        _active = SystemTray.isSupported();
        
        if (_active)
        {
            _tray = SystemTray.getSystemTray();
            createIconIfNecessary();
        }
        else
        {
            _tray = null;
        }
    }

    synchronized public static DeviceNotifications getShared()
    {
        if (_singleton == null)
        {
            _singleton = new DeviceNotifications();
        }

        return _singleton;
    }
    
    public void showTrayIcon()
    {
        if (!_active)
        {
            return;
        }
        
        createIconIfNecessary();
    }
    
    public void setTrayIconTooltip(String tooltip)
    {
        if (!_active)
        {
            return;
        }
        
        _icon.setToolTip(tooltip);
    }
    
    public void displayGlobalNotification(String title, String message)
    {
        displayGlobalNotification(title, message, MessageType.NONE);
    }
    
    public void displayGlobalNotification(String title, String message, MessageType type)
    {
        if (!_active)
        {
            return;
        }
        
        _icon.displayMessage(title, message, type);
    }
    
    private void createIconIfNecessary()
    {
        if (_icon != null)
        {
            return;
        }
        
        Image image = Toolkit.getDefaultToolkit().createImage(Resources.getImagePath(NOTIFICATION_IMAGE_FILE));

        _icon = new TrayIcon(image, "Automater");
        _icon.setImageAutoSize(true);
        
        try {
            _tray.add(_icon);
        } catch (Exception e) {
            
        }
    }
}
