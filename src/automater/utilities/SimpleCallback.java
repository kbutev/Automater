/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

/**
 * A function callback that takes no parameters and return no value.
 * 
 * @author Bytevi
 */
public abstract class SimpleCallback {
    abstract public void perform();
    
    public static SimpleCallback createDoNothing()
    {
        return new SimpleCallback() {
            @Override
            public void perform() {
                // Do nothing
            }
        };
    }
}
