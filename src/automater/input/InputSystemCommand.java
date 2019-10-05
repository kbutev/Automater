/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

/**
 * Represents a system command, like a cmd in Windows OS.
 * 
 * @author Byti
 */
public interface InputSystemCommand {
    public String getValue();
    
    public boolean reportsErrors();
}
