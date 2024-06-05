/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.di;

import org.int4.dirk.api.Injector;

/**
 *
 * @author Kristiyan Butev
 */
public class DISetup {
    
    private static boolean initialized = false;
    
    public static void setup() {
        if (initialized) {
            return;
        }
        
        initialized = true;
        
        Injector injector = DI.internalInjector;
        
        injector.registerInstance(new com.google.gson.Gson());
        
        injector.registerInstance(new automater.work.ActionSettingsManager.Impl());
        
        injector.registerInstance(new automater.storage.GeneralStorage.Impl());
        
        injector.registerInstance(new automater.parser.ScriptActionParser.Impl());
        injector.registerInstance(new automater.parser.ScriptActionsParser.Impl());
        
        injector.registerInstance(new automater.work.Executor.Impl());
    }
}
