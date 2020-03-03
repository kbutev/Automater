/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
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
