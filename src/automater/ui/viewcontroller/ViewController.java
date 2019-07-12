/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.ui.view.FormActionDelegate;

/**
 *
 * @author Bytevi
 */
public interface ViewController {
    public void start();
    public void suspend();
    public void terminate();
    
    public void setActionDelegate(FormActionDelegate delegate);
}
