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
 * Implements the ListModel with Description objects.
 * 
 * @author Bytevi
 */
public class StandardDescriptionsDataSource implements ListModel<String> {
    public final List<Description> data;
    
    public static StandardDescriptionsDataSource createGeneric()
    {
        return new StandardDescriptionsDataSource();
    }
    
    public static StandardDescriptionsDataSource createDataSourceForStandartText(List<Description> actions)
    {
        return new StandardDescriptionsDataSource(actions);
    }
    
    public static StandardDescriptionsDataSource createDataSourceForVerboseText(List<Description> actions)
    {
        return new StandartDescriptionsDataSourceVerbose(actions);
    }
    
    protected StandardDescriptionsDataSource()
    {
        this.data = new ArrayList();
    }
    
    protected StandardDescriptionsDataSource(List<Description> actions)
    {
        this.data = CollectionUtilities.copyAsImmutable(actions);
    }
    
    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public String getElementAt(int index) {
        return data.get(index).getStandart();
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        
    }
    
    public int indexOf(String str) {
        for (int e = 0; e < data.size(); e++)
        {
            if (data.get(e).getStandart().equals(str))
            {
                return e;
            }
        }
        
        return -1;
    }
}

class StandartDescriptionsDataSourceVerbose extends StandardDescriptionsDataSource {
    StandartDescriptionsDataSourceVerbose(List<Description> actions)
    {
        super(actions);
    }
    
    @Override
    public String getElementAt(int index) {
        return data.get(index).getVerbose();
    }
}