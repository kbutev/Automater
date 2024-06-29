/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.ui.view;

import javax.swing.DefaultListSelectionModel;

/**
 *
 * @author Kristiyan Butev
 */
class DisabledItemSelectionModel extends DefaultListSelectionModel {

    @Override
    public void setSelectionInterval(int index0, int index1) {
        super.setSelectionInterval(-1, -1);
    }

    public void manualySelectIndex(int index) {
        super.setSelectionInterval(index, index);
    }
}
