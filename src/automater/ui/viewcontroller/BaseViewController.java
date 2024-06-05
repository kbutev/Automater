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
    void start();
    void resume();
    void suspend();
    void terminate();
}
