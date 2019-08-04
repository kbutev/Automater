/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater;

import automater.ui.viewcontroller.PrimaryViewContoller;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;

/**
 *
 * @author Bytevi
 */
public class Automater {
    public static final PrimaryViewContoller primaryViewContoller = new PrimaryViewContoller();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        primaryViewContoller.start();
        
        // Disable jnativehook logging
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
    }
}
