/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.recorder.model.RecorderUserInput;
import automater.utilities.CollectionUtilities;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Bytevi
 */
public class RecorderResult {
    public final Collection<RecorderUserInput> userInputs;
    
    public RecorderResult(ArrayList<RecorderUserInput> userInputs)
    {
        this.userInputs = CollectionUtilities.copyAsImmutable(userInputs);
    }
}
