/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.view;

import automater.Strings;
import automater.utilities.Resources;
import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author Byti
 */
public class ViewUtilities {
    public static void setAppIconForFrame(javax.swing.JFrame frame)
    {
        Image image = Toolkit.getDefaultToolkit().createImage(Resources.getImagePath(Strings.APP_ICON_FILE));
        frame.setIconImage(image);
    }
    
    public static void setAppRedIconForFrame(javax.swing.JFrame frame)
    {
        Image image = Toolkit.getDefaultToolkit().createImage(Resources.getImagePath(Strings.APP_ICON_RED_FILE));
        frame.setIconImage(image);
    }
}
