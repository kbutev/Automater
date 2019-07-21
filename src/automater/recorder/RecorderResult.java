/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Bytevi
 */
public class RecorderResult {
    public final Collection<RecorderUserInput> userInputs;
    
    public RecorderResult(ArrayList<RecorderUserInput> userInputs)
    {
        this.userInputs = Collections.unmodifiableCollection(new ArrayList<>(userInputs));
    }
}
