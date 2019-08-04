/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.view;

import automater.utilities.CollectionUtilities;
import automater.utilities.Description;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Bytevi
 */
public class RecordFormActionsDataSource implements ListModel<String> {
    public final List<Description> actions;
    
    public RecordFormActionsDataSource()
    {
        this.actions = new ArrayList();
    }
    
    public RecordFormActionsDataSource(List<Description> actions)
    {
        this.actions = CollectionUtilities.copyAsImmutable(actions);
    }
    
    @Override
    public int getSize() {
        return actions.size();
    }

    @Override
    public String getElementAt(int index) {
        return actions.get(index).getVerbose();
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        
    }
    
}
