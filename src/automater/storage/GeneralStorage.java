/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.storage;

import org.jetbrains.annotations.NotNull;

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
