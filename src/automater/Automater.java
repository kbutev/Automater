/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater;

import automater.utilities.Logger;
import automater.ui.view.RecordForm;
import automater.ui.viewcontroller.PrimaryViewContoller;

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
    }
}
