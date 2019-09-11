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
    Error_NameIsEmpty, Error_NameIsTooShort, Error_NameIsTooLong, Error_NameIsTaken, Error_NameMustBeAlphaNumeric, Error_NameMustBeAlphaNumericWithSpecialSymbols,
    
    // Record form
    Record_FormTitle, Record_HeaderText,
    Record_SwitchToOpenButtonTitle, Record_SwitchToOpenButtonTip,
    Record_MacroActionListName,
    Record_IdleStatus,
    Record_MacroNameFieldDefaultText, Record_MacroNameFieldTip,
    Record_BeginRecordingButtonTitle, Record_BeginRecordingButtonTip, Record_StopRecordingButtonTitle, Record_StopRecordingButtonTip,
    Record_SaveButton, Record_SaveButtonEnabledTip, Record_SaveButtonDisabledTip,
    Record_NotificationStartTitle, Record_NotificationStartMessage, Record_NotificationStartTooltip,
    Record_NotificationStopTitle, Record_NotificationStopMessage, Record_NotificationStopTooltip,
    
    // Open form
    Open_FormTitle, Open_HeaderText,
    Open_SwitchToRecordButtonTitle, Open_SwitchToRecordButtonTip,
    Open_DefaultMacroNameText,
    Open_NumActions, Open_CreatedOn, Open_LastPlayedOn, Open_NumTimesPlayed, Open_HasNeverBeenPlayed,
    
    // Play form
    Play_FormTitle,
    Play_BackButtonTitle,
    Play_PlayButtonTitle, Play_StopButtonTitle,
    Play_DialogErrorTitle,
    Play_NotificationStartTitle, Play_NotificationStartMessage, Play_NotificationStartTooltip,
    Play_NotificationFinishTitle, Play_NotificationFinishMessage, Play_NotificationFinishTooltip,
    Play_NotificationRepeatTitle, Play_NotificationRepeatMessage, Play_NotificationRepeatTooltip,
    Play_NotificationRepeatForever,
    Play_StatusIdle, Play_StatusFinished, Play_StatusWaiting, Play_StatusPerformedWaiting,
    Play_StatusPerforming, Play_StatusPerformingRepeat,
    
    // Play options dialog
    PlayOptions_Title, PlayOptions_CancelButtonTitle, PlayOptions_SaveButtonTitle,
    PlayOptions_NotificationPlay, PlayOptions_NotificationStop, PlayOptions_NotificationRepeat,
    PlayOptions_PlaySpeed, PlayOptions_Repeat, PlayOptions_RepeatForever,
    
    // Edit form
    Edit_FormTitle, Edit_BackButtonTitle,
    Edit_Name, Edit_Actions, Edit_Description,
    
    // Edit Action dialog
    EditAction_DialogTitle, EditAction_CancelButtonTitle, EditAction_SaveButtonTitle,
    EditAction_StatusDefault, EditAction_StatusListeningToHotkey, EditAction_StatusError,
    EditAction_Time,
    
    // Edit Action types
    EditAction_TypeKeyboardClick, EditAction_TypeMouseClick, EditAction_TypeMouseMove,
    EditAction_TypeLeftMouseClick, EditAction_TypeRightMouseClick, EditAction_TypeMiddleMouseClick,
    
    // Macro parameters
    MacroParameters_Default,
    MacroParameters_Playspeed,
    MacroParameters_Repeat, MacroParameters_RepeatForever, MacroParameters_RepeatNever,
    
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
        if (string == null)
        {
            return "";
        }
        
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
        put(Error_NameMustBeAlphaNumeric, "Name must contain alpha/numeric chars only.");
        put(Error_NameMustBeAlphaNumericWithSpecialSymbols, "Name must contain alphanumeric chars only and also may contain: %@");
        
        // Record form
        put(Record_FormTitle, "Automater - Record");   
        put(Record_HeaderText, "Record macro");
        put(Record_SwitchToOpenButtonTitle, "MACROS >");
        put(Record_SwitchToOpenButtonTip, "Switch to open macros screen");
        put(Record_MacroActionListName, "Recorded actions");
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
        put(Record_NotificationStartTitle, "Automater");
        put(Record_NotificationStartMessage, "Recording... (Press %@ to stop)");
        put(Record_NotificationStartTooltip, "Recording... (Press %@ to stop)");
        put(Record_NotificationStopTitle, "Automater");
        put(Record_NotificationStopMessage, "Recording finished!");
        put(Record_NotificationStopTooltip, "Recording finished!");
        
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
        put(Play_PlayButtonTitle, "Play (%@)");
        put(Play_StopButtonTitle, "Stop (%@)");
        put(Play_DialogErrorTitle, "Failed to play Macro");
        put(Play_NotificationStartTitle, "Automater");
        put(Play_NotificationStartMessage, "Playing '%@' (Press %@ to stop)");
        put(Play_NotificationStartTooltip, "Playing '%@' (Press %@ to stop)");
        put(Play_NotificationFinishTitle, "Automater");
        put(Play_NotificationFinishMessage, "Finished playing '%@'");
        put(Play_NotificationFinishTooltip, "Finished playing '%@'");
        put(Play_NotificationRepeatTitle, "Automater");
        put(Play_NotificationRepeatMessage, "Repeat playing '%@' (%@)");
        put(Play_NotificationRepeatTooltip, "Repeat playing '%@' (%@)");
        put(Play_NotificationRepeatForever, "Repeat forever");
        put(Play_StatusIdle, "Idle");
        put(Play_StatusFinished, "Finished");
        put(Play_StatusWaiting, "Waiting");
        put(Play_StatusPerformedWaiting, "Finished %@, waiting for next");
        put(Play_StatusPerforming, "Performing %@");
        put(Play_StatusPerformingRepeat, "[%@] Performing %@");
        
        // Play options form
        put(PlayOptions_Title, "Set play options");
        put(PlayOptions_CancelButtonTitle, "Cancel");
        put(PlayOptions_SaveButtonTitle, "Save");
        put(PlayOptions_NotificationPlay, "Play notification");
        put(PlayOptions_NotificationStop, "Stop notification");
        put(PlayOptions_NotificationRepeat, "Repeat notification");
        put(PlayOptions_PlaySpeed, "Play speed");
        put(PlayOptions_Repeat, "Repeat");
        put(PlayOptions_RepeatForever, "Repeat forever");
        
        // Edit form
        put(Edit_FormTitle, "Automater - Edit");
        put(Edit_BackButtonTitle, "< CANCEL");
        put(Edit_Name, "Name");
        put(Edit_Actions, "Actions");
        put(Edit_Description, "Description");
        
        // Edit action dialog
        put(EditAction_DialogTitle, "Automater - Edit Action");
        put(EditAction_CancelButtonTitle, "Cancel");
        put(EditAction_SaveButtonTitle, "Save");
        put(EditAction_StatusDefault, "");
        put(EditAction_StatusListeningToHotkey, "Enter hotkey...");
        put(EditAction_StatusError, "Error: %@");
        put(EditAction_Time, "Time");
        
        // Edit Action types
        put(EditAction_TypeKeyboardClick, "Keyboard click");
        put(EditAction_TypeMouseClick, "Mouse click");
        put(EditAction_TypeMouseMove, "Mouse move");
        put(EditAction_TypeLeftMouseClick, "Left Click");
        put(EditAction_TypeRightMouseClick, "Right Click");
        put(EditAction_TypeMiddleMouseClick, "Middle Click");
        
        // Macro parameters
        put(MacroParameters_Default, "Play once");
        put(MacroParameters_Playspeed, "Playspeed: %@");
        put(MacroParameters_Repeat, "Repeat: %@");
        put(MacroParameters_RepeatForever, "Play forever");
        put(MacroParameters_RepeatNever, "Play once");
        
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
