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
    
    public static Description createFromString(final String value)
    {
        return new DescriptionString(value);
    }
}

class DescriptionString implements Description {
    private final String value;
    
    DescriptionString(String value)
    {
        this.value = value;
    }

    @Override
    public String getStandart() {
        return value;
    }

    @Override
    public String getVerbose() {
        return value;
    }

    @Override
    public String getStandartTooltip() {
        return value;
    }

    @Override
    public String getVerboseTooltip() {
        return value;
    }

    @Override
    public String getName() {
        return value;
    }

    @Override
    public String getDebug() {
        return value;
    }
}
