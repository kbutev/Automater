/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.ui.view;

import java.util.HashMap;

/**
 *
 * @author Bytevi
 */
public enum TextValue {
    // Record form
    RecordFormTitle, RecordHeaderText,
    RecordSwitchToPlayButton, RecordSwitchToPlayButtonTip,
    RecordMacroActionListName,
    RecordIdleStatus,
    RecordMacroNameFieldDefaultText, RecordMacroNameFieldTip,
    RecordBeginRecordingButton, RecordBeginRecordingButtonTip, RecordStopRecordingButton, RecordStopRecordingButtonTip,
    RecordSaveButton, RecordSaveButtonEnabledTip, RecordSaveButtonDisabledTip,
    
    // Play form
    PlayFormTitle,
    
    End;
    
    // Get text
    public static final String placeholderSymbol = "%@";
    
    public static String getText(TextValue value)
    {
        return getValues().get(value);
    }
    
    public static String getText(TextValue value, String arg1)
    {
        return evaluateTextValue(getValues().get(value), arg1, null);
    }
    
    public static String getText(TextValue value, String arg1, String arg2)
    {
        return evaluateTextValue(getValues().get(value), arg1, arg2);
    }
    
    private static String evaluateTextValue(String string, String arg1, String arg2)
    {
        if (arg1 != null)
        {
            string = string.replaceFirst(placeholderSymbol, arg1);
            
            if (arg2 != null)
            {
                string = string.replaceFirst(placeholderSymbol, arg2);
            }
        }
        
        return string;
    }
    
    // Values
    private static HashMap<TextValue, String> getValues()
    {
        // You can return a different map here, depending on the app language
        return englishValues;
    }
    
    private static HashMap<TextValue, String> englishValues = new HashMap<TextValue, String>() {{
        // Record form
        put(RecordFormTitle, "Automater - Record");   
        put(RecordHeaderText, "Record macro");
        put(RecordSwitchToPlayButton, "PLAY >");
        put(RecordSwitchToPlayButtonTip, "Switch to play macros screen");
        put(RecordMacroActionListName, "Recorded actions; double click to modify");
        put(RecordIdleStatus, "Idle (Press %@ to RECORD/FINISH)");
        put(RecordMacroNameFieldDefaultText, "Untitled macro");
        put(RecordMacroNameFieldTip, "The name of the macro");
        put(RecordBeginRecordingButton, "Record");
        put(RecordBeginRecordingButtonTip, "Begin recording macro");
        put(RecordStopRecordingButton, "Stop");
        put(RecordStopRecordingButtonTip, "Stop recording macro");
        put(RecordSaveButton, "Save");
        put(RecordSaveButtonEnabledTip, "Save macro with the recorded actions");
        put(RecordSaveButtonDisabledTip, "Record actions before saving");
        
        // Play form
        put(PlayFormTitle, "Automater - Play");
    }};
}
