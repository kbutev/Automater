/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.recorder.RecorderResult;

/**
 *
 * @author Bytevi
 */
public class Macro {
    public final String name;
    
    public Macro(String name, RecorderResult recorderResult)
    {
        this.name = name;
        
        parseRecorderResult(recorderResult);
    }
    
    private void parseRecorderResult(RecorderResult recorderResult)
    {
        
    }
}
