/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.datasource;

import automater.utilities.CollectionUtilities;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * Implements the ListModel with Description objects.
 *
 * @author Bytevi
 */
public class StandardDescriptionDataSource implements ListModel<String> {

    @NotNull public final List<String> data;

    public static @NotNull StandardDescriptionDataSource createGeneric() {
        return new StandardDescriptionDataSource();
    }

    public static @NotNull StandardDescriptionDataSource createDataSource(@NotNull List<String> actions) {
        return new StandardDescriptionDataSource(actions);
    }

    protected StandardDescriptionDataSource() {
        this.data = new ArrayList();
    }

    protected StandardDescriptionDataSource(@NotNull List<String> actions) {
        this.data = CollectionUtilities.copyAsImmutable(actions);
    }
    

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public String getElementAt(int index) {
        return data.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }

    public int indexOf(String str) {
        for (int e = 0; e < data.size(); e++) {
            var standartDescription = data.get(e);

            if (standartDescription != null && standartDescription.equals(str)) {
                return e;
            }
        }

        return -1;
    }
}
