/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.recorder.model.RecorderUserInput;
import automater.utilities.CollectionUtilities;
import automater.utilities.Description;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Bytevi
 */
public class RecorderResult implements Serializable {
    public final Collection<RecorderUserInput> userInputs;
    
    public RecorderResult(ArrayList<RecorderUserInput> userInputs)
    {
        this.userInputs = CollectionUtilities.copyAsImmutable(userInputs);
    }
    
    public Iterator<RecorderUserInput> getIteratorForUserInputs()
    {
        return userInputs.iterator();
    }
    
    public List<Description> getUserInputAsDescriptions()
    {
        ArrayList<Description> descriptions = new ArrayList<>();
        descriptions.addAll(userInputs);
        return descriptions;
    }
}
