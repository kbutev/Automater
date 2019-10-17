/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.storage;

import automater.work.model.MacroParameters;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;

/**
 * Holds raw storage values.
 *
 * @author Bytevi
 */
public class PreferencesStorageValues implements Serializable {
    public boolean displayStartNotification = false;
    public boolean displayStopNotification = false;
    public boolean displayRepeatNotification = false;
    public MacroParameters macroParameters = MacroParameters.defaultValues();
    
    public static @NotNull PreferencesStorageValues defaultValues()
    {
        return new PreferencesStorageValues();
    }
    
    private PreferencesStorageValues()
    {
        
    }
    
    public @NotNull PreferencesStorageValues copy()
    {
        PreferencesStorageValues copied = new PreferencesStorageValues();
        copied.displayStartNotification = displayStartNotification;
        copied.displayStopNotification = displayStopNotification;
        copied.displayRepeatNotification = displayRepeatNotification;
        copied.macroParameters = macroParameters;
        return copied;
    }
}
