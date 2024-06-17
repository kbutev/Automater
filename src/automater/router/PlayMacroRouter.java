/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import automater.model.macro.Macro;
import automater.presenter.PlayMacroPresenter;
import automater.storage.PreferencesStorage;
import automater.ui.view.PlayMacroFrame;
import automater.utilities.Description;
import automater.work.model.ExecutorProgress;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public interface PlayMacroRouter {
    
    interface Protocol {
        
        void start();
    }
    
    class Impl implements Protocol, PlayMacroPresenter.Delegate {
        
        private final @NotNull PlayMacroFrame view;
        private final @NotNull MasterRouter.Protocol masterRouter;
        private @NotNull PlayMacroPresenter.Protocol playMacroPresenter;

        public Impl(@NotNull MasterRouter.Protocol router, @NotNull Macro.Protocol macro) {
            view = new PlayMacroFrame();
            masterRouter = router;
            setup(macro);
        }
        
        private void setup(@NotNull Macro.Protocol macro) {
            playMacroPresenter = new PlayMacroPresenter.Impl(view, macro);
            playMacroPresenter.setDelegate(this);
        }
        
        @Override
        public void start() {
            playMacroPresenter.start();
            view.present();
        }
        
        // # PlayMacroPresenter.Delegate
        
        @Override
        public void onError(Exception e) {
            
        }
        
        @Override
        public void onLoadedPreferencesFromStorage(PreferencesStorage.Values values) {
            
        }
        
        @Override
        public void onLoadedMacroFromStorage(String macroName, String macroDescription, List<Description> macroActions) {
            
        }
        
        @Override
        public void startPlaying() {
            
        }
        
        @Override
        public void stopPlaying(boolean wasCancelled) {
            
        }
        
        @Override
        public void updatePlayStatus(ExecutorProgress progress) {
            
        }
    }
}
