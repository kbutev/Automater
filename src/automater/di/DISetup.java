/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.di;

import org.int4.dirk.api.Injector;
import com.google.gson.GsonBuilder;

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

        var gson = new GsonBuilder().setPrettyPrinting().create();
        injector.registerInstance(gson);

        injector.registerInstance(new automater.work.ActionSettingsManager.Impl());

        injector.registerInstance(new automater.parser.MacroActionParser.Impl());
        injector.registerInstance(new automater.parser.MacroActionsParser.Impl());
        injector.registerInstance(new automater.parser.DescriptionParser.Impl());
        injector.registerInstance(new automater.parser.CapturedEventParser.Impl());
        injector.registerInstance(new automater.parser.MacroParser.Impl());

        injector.registerInstance(new automater.storage.MacroStorage.Impl());
        injector.registerInstance(new automater.storage.PreferencesStorage.Impl());
        
        injector.registerInstance(new automater.work.Executor.Impl());
        
        injector.registerInstance(new automater.service.NativeEventMonitor.Impl());
        
        injector.registerInstance(new automater.router.MasterRouter.Impl());
    }
}
