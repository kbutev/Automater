/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

/**
 *
 * @author Kristiyan Butev
 */
public class RunState {
    
    private boolean started = false;
    
    public boolean isStarted() {
        return started;
    }
    
    public void start() throws Exception {
        if (started) {
            throw Errors.alreadyStarted();
        }
        
        started = true;
    }
    
    public void stop() throws Exception {
        if (started) {
            throw Errors.alreadyStopped();
        }
        
        started = false;
    }
}
