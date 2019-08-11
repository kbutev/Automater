/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

/**
 * The one and only view controller that is active at all times.
 * 
 * @author Bytevi
 */
public interface RootViewController {
    public void navigateToRecordScreen();
    public void navigateToOpenScreen();
    public void navigateToPlayScreen(automater.work.Macro macro);
}
