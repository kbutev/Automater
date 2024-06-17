/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.view;

import java.awt.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Defines commonly used methods for a view element.
 *
 * @author Bytevi
 */
public interface View {
    
    @NotNull Component asComponent();
    
    void reloadData();
}
