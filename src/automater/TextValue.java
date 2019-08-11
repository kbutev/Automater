/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater;

import java.util.HashMap;

/**
 *
 * @author Bytevi
 */
public enum TextValue {
    // Errors
    Error_NameIsEmpty, Error_NameIsTooShort, Error_NameIsTooLong, Error_NameIsTaken,
    
    // Record form
    Record_FormTitle, Record_HeaderText,
    Record_SwitchToOpenButtonTitle, Record_SwitchToOpenButtonTip,
    Record_MacroActionListName,
    Record_IdleStatus,
    Record_MacroNameFieldDefaultText, Record_MacroNameFieldTip,
    Record_BeginRecordingButtonTitle, Record_BeginRecordingButtonTip, Record_StopRecordingButtonTitle, Record_StopRecordingButtonTip,
    Record_SaveButton, Record_SaveButtonEnabledTip, Record_SaveButtonDisabledTip,
    
    // Open form
    Open_FormTitle, Open_HeaderText,
    Open_SwitchToRecordButtonTitle, Open_SwitchToRecordButtonTip,
    Open_DefaultMacroNameText,
    Open_NumActions, Open_CreatedOn, Open_LastPlayedOn, Open_NumTimesPlayed, Open_HasNeverBeenPlayed,
    
    // Play form
    Play_FormTitle,
    Play_BackButtonTitle,
    
    // Dialog
    Dialog_OK,
    Dialog_SavedRecordingTitle, Dialog_SavedRecordingMessage,
    Dialog_SaveRecordingFailedTitle, Dialog_SaveRecordingFailedMessage,
    Dialog_ConfirmDeleteMacroTitle, Dialog_ConfirmDeleteMacroMessage,
    
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
        // Errors
        put(Error_NameIsEmpty, "Name cannot be empty");  
        put(Error_NameIsTooShort, "Name must be longer than %@ characters");  
        put(Error_NameIsTooLong, "Name must be shorter than %@ characters");  
        put(Error_NameIsTaken, "Name already taken.");  
        
        // Record form
        put(Record_FormTitle, "Automater - Record");   
        put(Record_HeaderText, "Record macro");
        put(Record_SwitchToOpenButtonTitle, "MACROS >");
        put(Record_SwitchToOpenButtonTip, "Switch to open macros screen");
        put(Record_MacroActionListName, "Recorded actions; double click to modify");
        put(Record_IdleStatus, "Idle (Press %@ to RECORD/FINISH)");
        put(Record_MacroNameFieldDefaultText, "Untitled macro");
        put(Record_MacroNameFieldTip, "The name of the macro");
        put(Record_BeginRecordingButtonTitle, "Record");
        put(Record_BeginRecordingButtonTip, "Begin recording macro");
        put(Record_StopRecordingButtonTitle, "Stop");
        put(Record_StopRecordingButtonTip, "Stop recording macro");
        put(Record_SaveButton, "Save");
        put(Record_SaveButtonEnabledTip, "Save macro with the recorded actions");
        put(Record_SaveButtonDisabledTip, "Record actions before saving");
        
        // Open form
        put(Open_FormTitle, "Automater - Macros");
        put(Open_HeaderText, "Macros");
        put(Open_SwitchToRecordButtonTitle, "RECORD >");
        put(Open_SwitchToRecordButtonTip, "Switch to record macro screen");
        put(Open_DefaultMacroNameText, "Select macro from the list");
        put(Open_NumActions, "%@ actions");
        put(Open_CreatedOn, "Created on: %@");
        put(Open_LastPlayedOn, "Last played on: %@");
        put(Open_NumTimesPlayed, "Played %@ times");
        put(Open_HasNeverBeenPlayed, "Has never been played");
        
        // Play form
        put(Play_FormTitle, "Automater - Play");
        put(Play_BackButtonTitle, "< BACK");
        
        // Dialog
        put(Dialog_OK, "Ok");
        put(Dialog_SavedRecordingTitle, "Save recording");
        put(Dialog_SavedRecordingMessage, "Successfully saved the recording!");
        put(Dialog_SaveRecordingFailedTitle, "Save recording");
        put(Dialog_SaveRecordingFailedMessage, "Could not save recording: %@");
        put(Dialog_ConfirmDeleteMacroTitle, "Delete macro");
        put(Dialog_ConfirmDeleteMacroMessage, "Are you sure you want to delete '%@'?");
    }};
}
