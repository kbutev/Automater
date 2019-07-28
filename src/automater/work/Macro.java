/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work;

import automater.recorder.RecorderResult;
import java.io.Serializable;

/**
 *
 * @author Bytevi
 */
public class Macro implements Serializable {
    public String name;
    public transient RecorderResult r;
    
    public Macro(String name, RecorderResult recorderResult)
    {
        this.name = name;
        this.r = recorderResult;
        
        parseRecorderResult(recorderResult);
    }
    
    private void parseRecorderResult(RecorderResult recorderResult)
    {
        
    }
}
