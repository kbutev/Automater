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
public interface GeneralStorage {
    interface Protocol {
        public @NotNull MacroStorage getMacrosStorage();
        public @NotNull PreferencesStorage getPreferencesStorage();
    }
    
    class Impl implements Protocol {
        private final MacroStorage _macrosStorage = new MacroStorage();
        private final PreferencesStorage _preferencesStorage = new PreferencesStorage();

        @Override
        public @NotNull MacroStorage getMacrosStorage()
        {
            return _macrosStorage;
        }

        @Override
        public @NotNull PreferencesStorage getPreferencesStorage()
        {
            return _preferencesStorage;
        }
   }
}
