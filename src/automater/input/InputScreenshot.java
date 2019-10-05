/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.input;

import java.awt.Rectangle;

/**
 * Represents an action, that takes a snapshot of the user screen.
 * 
 * @author Byti
 */
public interface InputScreenshot {
    public String getPath();
    
    public boolean isFullScreen();
    
    public Rectangle getArea();
}
