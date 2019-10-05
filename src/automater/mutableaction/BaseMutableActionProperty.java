/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.mutableaction;

/**
 * A mutable action property.
 *
 * @author Byti
 */
public interface BaseMutableActionProperty {
    public boolean isValid();
    public String getInvalidError();
    
    public String getValue();
    public void setValue(String value);
    
    public String getName();
    public void setName(String name);
}
