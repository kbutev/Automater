/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Defines some useful methods for retrieving the device screen.
 * 
 * @author Byti
 */
public class DeviceScreen {
    public static @NotNull Dimension getPrimaryScreenSize()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize;
    }
}
