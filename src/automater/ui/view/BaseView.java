/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.view;

/**
 * Defines commonly used methods for a view element.
 * 
 * @author Bytevi
 */
public interface BaseView {
    public void onViewStart();
    public void onViewSuspended();
    public void onViewResume();
    public void onViewTerminate();
    
    public void reloadData();
}
