/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.storage;

import com.sun.istack.internal.NotNull;

/**
 * Holds and maintains the storage of the application.
 * 
 * @author Bytevi
 */
public class GeneralStorage {
    private static GeneralStorage singleton;
    
    @NotNull private final MacroStorage _macrosStorage = new MacroStorage();
    @NotNull private final PreferencesStorage _preferencesStorage = new PreferencesStorage();
    
    synchronized public static @NotNull GeneralStorage getDefault()
    {
        if (singleton == null)
        {
            singleton = new GeneralStorage();
        }
        
        return singleton;
    }
    
    public @NotNull MacroStorage getMacrosStorage()
    {
        return _macrosStorage;
    }
    
    public @NotNull PreferencesStorage getPreferencesStorage()
    {
        return _preferencesStorage;
    }
}
