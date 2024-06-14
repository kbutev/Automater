/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.presenter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface PresenterWithDelegate<Delegate> extends Presenter {
    
    @Nullable Delegate getDelegate();
    void setDelegate(@NotNull Delegate delegate);
}
