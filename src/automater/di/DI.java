/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.di;

import org.int4.dirk.api.Injector;
import org.int4.dirk.api.instantiation.*;
import org.int4.dirk.api.scope.ScopeNotActiveException;
import org.int4.dirk.di.Injectors;

/**
 * Dependency injector service.
 *
 * @author Kristiyan Butev
 */
public class DI {

    public static DI injector = new DI();
    public static Injector internalInjector = Injectors.autoDiscovering();

    public static <T extends Object> T get(Class<T> cls) throws UnsatisfiedResolutionException, AmbiguousResolutionException, CreationException, ScopeNotActiveException {
        var result = internalInjector.getInstance(cls);
        
        if (cls.isAnnotationPresent(UniqueDependency.class)) {
            try {
                return (T) result.getClass().getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        return result;
    }
}
