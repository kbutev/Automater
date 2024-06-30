/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.datasource;

import automater.builder.MacroActionBuilder;
import automater.utilities.CollectionUtilities;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Kristiyan Butev
 */
public class MacroActionKindsDataSource implements ComboBoxModel<String> {
    
    private final @NotNull List<MacroActionBuilder.Kind> data;
    private @Nullable String selectedItem;
    
    public MacroActionKindsDataSource(@NotNull List<MacroActionBuilder.Kind> data) {
        this.data = CollectionUtilities.copy(data);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (anItem instanceof String string) {
            selectedItem = string;
        }
    }
    
    public int getSelectedIndex() {
        if (selectedItem == null) {
            return -1;
        }
        
        return MacroActionBuilder.Kind.allValuesAsStrings.indexOf(selectedItem);
    }
    
    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }
    
    public String getSelectedStringItem() {
        return selectedItem;
    }
    
    @Override
    public int getSize() {
        return data.size();
    }
    
    @Override
    public String getElementAt(int index) {
        return data.get(index).value;
    }
    
    @Override
    public void addListDataListener(ListDataListener l) {
        
    }
    
    @Override
    public void removeListDataListener(ListDataListener l) {
        
    }
}
