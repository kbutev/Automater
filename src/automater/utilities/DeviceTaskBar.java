/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import com.sun.xml.internal.ws.api.Component;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import javax.swing.JFrame;
import org.bridj.Pointer;
import org.bridj.cpp.com.COMRuntime;
import org.bridj.cpp.com.shell.ITaskbarList3;
import org.bridj.jawt.JAWTUtils;

/**
 *
 * @author Byti
 */
public class DeviceTaskBar {
    public static int MAX_PROGRESS_VALUE = 100;
    
    private static DeviceTaskBar singleton;
    
    private final boolean _active;
    private ITaskbarList3 _item;
    private Pointer<?> _progressPointer;
    
    private DeviceTaskBar()
    {
        try {
            _item = COMRuntime.newInstance(ITaskbarList3.class);
        } catch (Exception e) {
            _item = null;
        }
        
        _active = (_item != null);
    }

    synchronized public static DeviceTaskBar getShared()
    {
        if (singleton == null)
        {
            singleton = new DeviceTaskBar();
        }

        return singleton;
    }
    
    public void setAppTaskBarProgress(JFrame frame, double progress)
    {
        if (!_active)
        {
            return;
        }
        
        int value = (int)progress;
        
        if (value < 0)
        {
            value = 0;
        }
        
        if (value > MAX_PROGRESS_VALUE)
        {
            value = MAX_PROGRESS_VALUE;
        }
        
        long hwndVal = JAWTUtils.getNativePeerHandle(frame);
        _progressPointer = Pointer.pointerToAddress(hwndVal);
        _item.SetProgressValue((Pointer) _progressPointer, value, MAX_PROGRESS_VALUE);
    }
    
    public void resetAppTaskBarProgress(JFrame frame)
    {
        if (!_active)
        {
            return;
        }
        
        long hwndVal = JAWTUtils.getNativePeerHandle(frame);
        _progressPointer = Pointer.pointerToAddress(hwndVal);
        _item.SetProgressValue((Pointer) _progressPointer, 0, MAX_PROGRESS_VALUE);
    }
}
