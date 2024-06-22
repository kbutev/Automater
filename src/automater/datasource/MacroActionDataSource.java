/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.datasource;

import automater.model.action.MacroActionDescription;
import automater.utilities.CollectionUtilities;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class MacroActionDataSource implements ListModel<String> {
    
    public final @NotNull List<MacroActionDescription> data;
    
    public MacroActionDataSource() {
        this.data = new ArrayList();
    }

    public MacroActionDataSource(@NotNull List<MacroActionDescription> actions) {
        this.data = CollectionUtilities.copyAsImmutable(actions);
    }
    
    public int getSelectIndexForTime(double time) {
        int index = data.size()-1;
        
        for (int e = 0; e < data.size(); e++) {
            var item = data.get(e);
            
            if (time <= item.timestampAsDouble) {
                index = e;
                break;
            }
        }
        
        return index;
    }
    
    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public String getElementAt(int index) {
        return data.get(index).toString();
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
