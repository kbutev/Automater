/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.storage;

import automater.work.model.MacroParameters;
import java.io.Serializable;

/**
 * Holds raw storage values.
 *
 * @author Bytevi
 */
public class PreferencesStorageValues implements Serializable {
    public boolean displayStartNotification = true;
    public boolean displayStopNotification = true;
    public boolean displayRepeatNotification = false;
    public MacroParameters macroParameters = MacroParameters.defaultValues();
    
    public static PreferencesStorageValues defaultValues()
    {
        return new PreferencesStorageValues();
    }
    
    private PreferencesStorageValues()
    {
        
    }
    
    public PreferencesStorageValues copy()
    {
        PreferencesStorageValues copied = new PreferencesStorageValues();
        copied.displayStartNotification = displayStartNotification;
        copied.displayStopNotification = displayStopNotification;
        copied.displayRepeatNotification = displayRepeatNotification;
        copied.macroParameters = macroParameters;
        return copied;
    }
}
