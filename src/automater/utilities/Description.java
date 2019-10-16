/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import com.sun.istack.internal.NotNull;

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
    
    public static @NotNull Description createFromString(@NotNull final String value)
    {
        return new DescriptionString(value);
    }
}

class DescriptionString implements Description {
    @NotNull private final String value;
    
    DescriptionString(@NotNull String value)
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
