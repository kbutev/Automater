/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder;

import automater.settings.Hotkey;

/**
 * Receives requests about a pressed hotkey.
 *
 * @author Bytevi
 */
public interface RecorderHotkeyListener {
    public Hotkey getHotkey();
    
    public void onHotkeyPressed();
}
