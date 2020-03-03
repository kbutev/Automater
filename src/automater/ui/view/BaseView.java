/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.view;

/**
 * Defines commonly used methods for a view element.
 * 
 * @author Bytevi
 */
public interface BaseView {
    public void onViewStart();
    public void onViewSuspended();
    public void onViewResume();
    public void onViewTerminate();
    
    public void reloadData();
}
