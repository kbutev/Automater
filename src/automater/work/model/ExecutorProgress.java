/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work.model;

import org.jetbrains.annotations.NotNull;

/**
 * Holds information for how far an execution has made it.
 * 
 * This is for the UI mostly.
 *
 * @author Bytevi
 */
public interface ExecutorProgress {
    public @NotNull String getCurrentStatus();
    
    public double getPercentageDone();
    public int getCurrentActionIndex();
}
