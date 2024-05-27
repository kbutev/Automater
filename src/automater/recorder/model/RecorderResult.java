/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.recorder.model;

import automater.utilities.CollectionUtilities;
import automater.utilities.Description;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Holds parsed recorded user input.
 * 
 * @author Bytevi
 */
public class RecorderResult implements Serializable {
    @NotNull public final Collection<RecorderUserInput> userInputs;
    
    public RecorderResult(@NotNull ArrayList<RecorderUserInput> userInputs)
    {
        this.userInputs = CollectionUtilities.copyAsImmutable(userInputs);
    }
    
    public @NotNull Iterator<RecorderUserInput> getIteratorForUserInputs()
    {
        return userInputs.iterator();
    }
    
    public @NotNull List<Description> getUserInputAsDescriptions()
    {
        ArrayList<Description> descriptions = new ArrayList<>();
        descriptions.addAll(userInputs);
        return descriptions;
    }
}
