/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

/**
 * Holds information for how far an execution has made it.
 * 
 * This is for the UI mostly.
 *
 * @author Bytevi
 */
public interface ExecutorProgress {
    public String getCurrentStatus();
    
    public double getPercentageDone();
    public int getCurrentActionIndex();
}
