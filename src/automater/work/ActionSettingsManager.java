/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

/**
 * Holds settings and values for actions.
 * 
 * These values can be customized at any time during runtime.
 * 
 * @author Bytevi
 */
public class ActionSettingsManager {
    private static ActionSettingsManager singleton;
    
    // 1 = no change, 2 = mouse motion looks sluggish
    // 3 and up = looks good, but bad performance
    public static final int MAX_SUBMOVEMENTS = 3;
    
    // Private
    private final Object _lock = new Object();
    private int _maxNumberOfSubmovements = MAX_SUBMOVEMENTS;
    
    private ActionSettingsManager()
    {
        
    }
    
    synchronized public static ActionSettingsManager getDefault()
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
