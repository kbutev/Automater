/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2024 Kristiyan Butev. All rights reserved.
 */
package automater.datasource;

import automater.presenter.MutableEntryPresenter;
import automater.utilities.CollectionUtilities;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Kristiyan Butev
 */
public class MutableEntryDataSource implements ListModel<String> {
    
    private final @NotNull List<MutableEntryPresenter.Protocol> data;
    
    public MutableEntryDataSource(@NotNull List<MutableEntryPresenter.Protocol> data) {
        this.data = CollectionUtilities.copy(data);
    }
    
    public @NotNull List<MutableEntryPresenter.Protocol> getData() {
        return data;
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
