/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.view;

/**
 *
 * @author Bytevi
 */
public interface FormActionDelegate {
    public void onSwitchWindow();
    
    public void onBeginRecord();
    public void onEndRecord();
    public void onSaveRecording(String name);
    
    public void onPlayMacro(String name);
    public void onStopPlayingMacro();
    public void onDeleteMacro(String name);
    public void onChangePlayMacroParameters(String name);
}
