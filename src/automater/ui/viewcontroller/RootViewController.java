/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.viewcontroller;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The one and only view controller that is active at all times.
 *
 * @author Bytevi
 */
public interface RootViewController {

    @Nullable BaseViewController getCurrentViewController();
    void navigateToRecordScreen();
    void navigateToOpenMacrosScreen();
    void navigateToPlayScreen(@NotNull automater.work.model.Macro macro);
    void navigateToEditScreen(@NotNull automater.work.model.Macro macro);
}
