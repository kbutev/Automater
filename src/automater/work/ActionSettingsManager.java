/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work;

import org.jetbrains.annotations.NotNull;

/**
 * Holds global values for actions, that can be used to customize their behavior.
 * 
 * These values can be changed at any time during runtime.
 * 
 * @author Bytevi
 */
public class ActionSettingsManager {
    private static ActionSettingsManager singleton;
    
    // 1 = no change, 2 = mouse motion looks sluggish
    // 3 and up = looks good, but bad performance
    public static final int MAX_SUBMOVEMENTS = 3;
    
    // Private
    @NotNull private final Object _lock = new Object();
    private int _maxNumberOfSubmovements = MAX_SUBMOVEMENTS;
    
    private ActionSettingsManager()
    {
        
    }
    
    synchronized public static @NotNull ActionSettingsManager getDefault()
    {
        if (singleton == null)
        {
            singleton = new ActionSettingsManager();
        }
        
        return singleton;
    }
    
    public int getMaxNumberOfSubmovements()
    {
        synchronized (_lock)
        {
            return _maxNumberOfSubmovements;
        }
    }
    
    public void setMaxNumberOfSubmovements(int value)
    {
        if (value < 1)
        {
            value = 1;
        }
        
        synchronized (_lock)
        {
            _maxNumberOfSubmovements = value;
        }
    }
}
