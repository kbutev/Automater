/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.presenter;

/**
 * Base implementation for presenters.
 * 
 * @author Bytevi
 */
public interface BasePresenter {
    void start();
    void setDelegate(BasePresenterDelegate delegate);
}
