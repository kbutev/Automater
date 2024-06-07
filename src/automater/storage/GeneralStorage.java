/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.storage;

import automater.di.DI;
import org.jetbrains.annotations.NotNull;

/**
 * Holds and maintains the storage of the application.
 *
 * @author Bytevi
 */
public interface GeneralStorage {

    interface Protocol {

        @NotNull MacroStorage.Protocol getMacrosStorage();
        @NotNull PreferencesStorage getPreferencesStorage();
    }

    class Impl implements Protocol {

        private final MacroStorage.Protocol storage = DI.get(MacroStorage.Protocol.class);
        private final PreferencesStorage _preferencesStorage = new PreferencesStorage();

        @Override
        public @NotNull MacroStorage.Protocol getMacrosStorage() {
            return storage;
        }

        @Override
        public @NotNull PreferencesStorage getPreferencesStorage() {
            return _preferencesStorage;
        }
    }
}
