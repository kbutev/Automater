/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

/**
 * Describes an object.
 * 
 * @author Bytevi
 */
public interface Description {
    public String getStandart();
    public String getVerbose();
    public String getStandartTooltip();
    public String getVerboseTooltip();
    public String getName();
    public String getDebug();
}
