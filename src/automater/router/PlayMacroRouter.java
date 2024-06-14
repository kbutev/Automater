/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.router;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public interface PlayMacroRouter {
    
    interface Protocol {
        
        void start(@NotNull MasterRouter.Protocol router);
        void goBack();
    }
    
    class Impl implements Protocol {
        
        @Nullable MasterRouter.Protocol masterRouter;

        @Override
        public void start(@NotNull MasterRouter.Protocol router) {
            masterRouter = router;
        }
        
        @Override
        public void goBack() {
            assert masterRouter != null;
            
            
        }
    }
}
