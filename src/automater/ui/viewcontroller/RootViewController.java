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
    public @Nullable BaseViewController getCurrentViewController();
    public void navigateToRecordScreen();
    public void navigateToOpenScreen();
    public void navigateToPlayScreen(@NotNull automater.work.model.Macro macro);
    public void navigateToEditScreen(@NotNull automater.work.model.Macro macro);
}
