/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import java.util.ArrayList;

/**
 *
 * @author Bytevi
 */
public class RecorderResult {
    public final ArrayList<RecorderUserInput> userInputs;
    
    public RecorderResult(ArrayList<RecorderUserInput> userInputs)
    {
        this.userInputs = userInputs;
    }
}
