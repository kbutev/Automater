/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.storage;

/**
 *
 * @author Bytevi
 */
public class GeneralStorage {
    private static GeneralStorage singleton;
    
    private final Object _lock = new Object();
    
    private final MacrosStorage _macrosStorage = new MacrosStorage();
    
    synchronized public static GeneralStorage getDefault()
    {
        if (singleton == null)
        {
            singleton = new GeneralStorage();
        }
        
        return singleton;
    }
    
    public MacrosStorage getMacrosStorage()
    {
        return _macrosStorage;
    }
}
