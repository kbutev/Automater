/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.view;

import automater.Strings;
import automater.utilities.Resources;
import org.jetbrains.annotations.NotNull;
import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author Byti
 */
public class ViewUtilities {

    public static void setAppIconForFrame(@NotNull javax.swing.JFrame frame) {
        Image image = Toolkit.getDefaultToolkit().createImage(Resources.getImagePath(Strings.APP_ICON_FILE));
        frame.setIconImage(image);
    }

    public static void setAppRedIconForFrame(@NotNull javax.swing.JFrame frame) {
        Image image = Toolkit.getDefaultToolkit().createImage(Resources.getImagePath(Strings.APP_ICON_RED_FILE));
        frame.setIconImage(image);
    }
}
