/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import automater.presenter.BasePresenterDelegate;

/**
 * Base implementation for view controllers.
 * 
 * @author Bytevi
 */
public interface BaseViewController {
    public void start();
    public void resume();
    public void suspend();
    public void terminate();
}
