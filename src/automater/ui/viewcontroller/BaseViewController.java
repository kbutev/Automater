/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.viewcontroller;

/**
 * Base implementation for view controllers.
 * 
 * @author Bytevi
 */
public interface BaseViewController {
    public void start();
    public void resume();
    public void suspend();
    public void terminate();
}
