/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Describes any dependency that should be initialized each time it is resolved.
 * These dependencies should have a default constructor that take no parameters.
 * @author Kristiyan Butev
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueDependency {
    
}
