/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.viewcontroller;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

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
