/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.ui.view;

import automater.model.event.EventDescription;
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
public class StandardEventDescriptionDataSource implements ListModel<String> {

    @NotNull public final List<EventDescription> data;

    public static @NotNull StandardEventDescriptionDataSource createGeneric() {
        return new StandardEventDescriptionDataSource();
    }

    public static @NotNull StandardEventDescriptionDataSource createDataSource(@NotNull List<EventDescription> actions) {
        return new StandardEventDescriptionDataSource(actions);
    }

    protected StandardEventDescriptionDataSource() {
        this.data = new ArrayList();
    }

    protected StandardEventDescriptionDataSource(@NotNull List<EventDescription> actions) {
        this.data = CollectionUtilities.copyAsImmutable(actions);
    }
    
    public EventDescription get(int index) {
        return data.get(index);
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
