/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Bytevi
 */
public class AlertWindows {
    public static void showMessage(Component parent, 
            String title, String message, 
            String buttonText)
    {
        showMessage(parent, title, message, buttonText, null);
    }
    
    public static void showMessage(Component parent, 
            String title, String message, 
            String buttonText,
            SimpleCallback okCallback)
    {
        Object[] options = {buttonText};
        
        int result = JOptionPane.showOptionDialog(parent,
                message, title,
                JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE,
                null, 
                options,
                options[0]);
        
        if (okCallback != null)
        {
            if (result == JOptionPane.OK_OPTION)
            {
                okCallback.perform();
            }
        }
    }
    
    public static void showErrorMessage(Component parent, 
            String title, String message, 
            String buttonText)
    {
        showErrorMessage(parent, title, message, buttonText, null);
    }
    
    public static void showErrorMessage(Component parent, 
            String title, String message, 
            String buttonText,
            SimpleCallback okCallback)
    {
        Object[] options = {buttonText};
        
        int result = JOptionPane.showOptionDialog(parent,
                message, title,
                JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE,
                null, 
                options,
                options[0]);
        
        if (okCallback != null)
        {
            if (result == JOptionPane.OK_OPTION)
            {
                okCallback.perform();
            }
        }
    }
}
