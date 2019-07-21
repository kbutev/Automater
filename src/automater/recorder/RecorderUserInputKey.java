/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

/**
 * A key value and a key mask.
 * 
 * @author Bytevi
 */
public class RecorderUserInputKey {
    public final RecorderUserInputKeyValue value;
    public final RecorderUserInputKeyMask mask;
    
    public RecorderUserInputKey(RecorderUserInputKeyValue value, RecorderUserInputKeyMask mask)
    {
        this.value = value;
        this.mask = mask;
    }
}
