/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.mvp;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import automater.utilities.Description;

/**
 * Presenter's delegate.
 * 
 * @author Bytevi
 */
public interface BasePresenterDelegate {
    public void onErrorEncountered(@NotNull Exception e);
    
    public interface EditMacroPresenterDelegate extends BasePresenterDelegate {
        public void onLoadedMacroFromStorage(@NotNull String macroName, @NotNull String macroDescription, @NotNull List<Description> macroActions);
        
        // Edit macro action operations
        public void onBeginCreateMacroAction(@NotNull automater.mutableaction.BaseMutableAction action);
        public void onBeginEditMacroAction(@NotNull automater.mutableaction.BaseMutableAction action);
        public void onEditedMacroActions(@NotNull List<Description> newMacroActions);
        public void onClosingMacroWithoutSavingChanges();
    }
}
