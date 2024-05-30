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
public interface ActionSettingsManager {
    
    interface Protocol {
        int getMaxNumberOfSubmovements();
        void setMaxNumberOfSubmovements(int value);
    }
    
    class Impl implements Protocol {
        // 1 = no change, 2 = mouse motion looks sluggish
        // 3 and up = looks good, but bad performance
        public static final int MAX_SUBMOVEMENTS = 3;

        // Private
        @NotNull private final Object _lock = new Object();
        private int _maxNumberOfSubmovements = MAX_SUBMOVEMENTS;

        @Override
        public int getMaxNumberOfSubmovements()
        {
            synchronized (_lock)
            {
                return _maxNumberOfSubmovements;
            }
        }

        @Override
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
}
