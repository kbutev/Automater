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

    void onViewStart();
    void onViewSuspended();
    void onViewResume();
    void onViewTerminate();

    void reloadData();
}
