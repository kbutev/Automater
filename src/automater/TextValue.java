/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater;

import automater.utilities.Logger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;

/**
 * The application text values are here, including the localization values.
 *
 * @author Bytevi
 */
public enum TextValue {
    // Version
    Version,
    
    // Errors
    Error_Generic,
    Error_NameIsEmpty,
    Error_NameIsTooShort, Error_NameIsTooLong, Error_NameIsTaken,
    Error_NameMustBeAlphaNumeric, Error_NameMustBeAlphaNumericWithSpecialSymbols,
    
    // Input descriptions
    Input_DoNothing, Input_Wait,
    Input_KeyboardPress, Input_KeyboardRelease,
    Input_MousePress, Input_MouseRelease,
    Input_MouseMove, Input_MouseMotion, Input_MouseWheel,
    Input_SystemCommand, Input_Screenshot,
    
    // Record form
    Record_FormTitle, Record_HeaderText,
    Record_SwitchToOpenButtonTitle, Record_SwitchToOpenButtonTip,
    Record_MacroActionListName,
    Record_IdleStatus,
    Record_MacroNameFieldDefaultText, Record_MacroNameFieldTip,
    Record_BeginRecordingButtonTitle, Record_BeginRecordingButtonTip, Record_StopRecordingButtonTitle, Record_StopRecordingButtonTip, Record_RedoRecordingButtonTitle,
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
    Play_NotificationStartTitle, Play_NotificationStartMessage,
    Play_NotificationFinishTitle, Play_NotificationFinishMessage,
    Play_NotificationRepeatTitle, Play_NotificationRepeatMessage,
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
    Edit_Delete, Edit_Edit, Edit_Create,
    Edit_CloseWithoutSavingTitle, Edit_CloseWithoutSavingMessage,
    
    // Edit Action dialog
    EditAction_DialogTitle, EditAction_CancelButtonTitle, EditAction_SaveButtonTitle,
    EditAction_StatusDefault, EditAction_StatusListeningToHotkey, EditAction_StatusError,
    EditAction_Time,
    EditAction_Wait, EditAction_WaitTimeType,
    EditAction_Key, EditAction_Press,
    EditAction_X, EditAction_Y, EditAction_FinalX, EditAction_FinalY,
    EditAction_Command,
    EditAction_Path,
    EditAction_ReportsErrors,
    EditAction_ScreenshotTip,
    
    // Edit Action types
    EditAction_TypeDoNothing, EditAction_TypeWait,
    EditAction_TypeKeyboardClick, EditAction_TypeMouseClick, EditAction_TypeMouseMove,
    EditAction_TypeSystemCommand, EditAction_TypeScreenshot,
    
    // Edit Action descriptions
    EditAction_DescriptionDoNothing,
    EditAction_DescriptionWait,
    EditAction_DescriptionKeyboardClick, EditAction_DescriptionMouseClick, EditAction_DescriptionMouseMove,
    EditAction_DescriptionMouseMotion,
    EditAction_DescriptionSystemCommand, EditAction_DescriptionScreenshot,
    
    // Macro parameters
    MacroParameters_Default,
    MacroParameters_Playspeed,
    MacroParameters_Repeat, MacroParameters_RepeatForever, MacroParameters_RepeatNever,
    
    // Commands
    Commands_NotificationErrorTitle, Commands_NotificationErrorMessage,
    
    // Dialog
    Dialog_OK,
    Dialog_SavedRecordingTitle, Dialog_SavedRecordingMessage,
    Dialog_SaveRecordingFailedTitle, Dialog_SaveRecordingFailedMessage,
    Dialog_ConfirmDeleteMacroTitle, Dialog_ConfirmDeleteMacroMessage,
    
    // System tray
    SystemTray_Tooltip,
    
    End;
    
    // Get text
    public static final String placeholderSymbol = "%@";
    
    public static String getText(TextValue value)
    {
        String string = getValues().get(value);
        
        return evaluateTextValue(value, string, null, null, null);
    }
    
    public static String getText(TextValue value, String arg1)
    {
        String string = getValues().get(value);
        
        return evaluateTextValue(value, string, arg1, null, null);
    }
    
    public static String getText(TextValue value, String arg1, String arg2)
    {
        String string = getValues().get(value);
        
        return evaluateTextValue(value, string, arg1, arg2, null);
    }
    
    public static String getText(TextValue value, String arg1, String arg2, String arg3)
    {
        String string = getValues().get(value);
        
        return evaluateTextValue(value, string, arg1, arg2, arg3);
    }
    
    public static String evaluateTextValue(TextValue value, String textValueString, String arg1, String arg2, String arg3)
    {
        if (textValueString == null)
        {
            recordMissingValue(value);
            return "";
        }
        
        if (arg1 != null)
        {
            arg1 = preprocessTextArgument(arg1);
            textValueString = textValueString.replaceFirst(placeholderSymbol, arg1);
            
            if (arg2 != null)
            {
                arg2 = preprocessTextArgument(arg2);
                textValueString = textValueString.replaceFirst(placeholderSymbol, arg2);
                
                if (arg3 != null)
                {
                    arg3 = preprocessTextArgument(arg3);
                    textValueString = textValueString.replaceFirst(placeholderSymbol, arg3);
                }
            }
        }
        
        return textValueString;
    }
    
    private static String preprocessTextArgument(String arg)
    {
        // Backslashes and dollar signs will not work when replacing a target string with the String.replaceX methods.
        // Use Matcher to get a proper string literal
        return Matcher.quoteReplacement(arg);
    }
    
    private static void recordMissingValue(TextValue value)
    {
        String key = value.name();
        
        boolean alreadyRecorded = !missingValues.add(key);
        
        if (!alreadyRecorded)
        {
            Logger.warning(TextValue.class.getCanonicalName(), "Could not find string for text value " + key + "!");
        }  
    }
    
    // Values
    private static HashMap<TextValue, String> getValues()
    {
        // You can return a different map here, depending on the app currently selected language
        return englishValues;
    }
    
    // Any invalid request values will be added to this set.
    // This is done to prevent warning log spam.
    private static final HashSet<String> missingValues = new HashSet<>();
    
    private static final HashMap<TextValue, String> englishValues = new HashMap<TextValue, String>() {{
        put(Version, "0.1");
        
        // Errors
        put(Error_Generic, "Error");
        put(Error_NameIsEmpty, "Name cannot be empty");
        put(Error_NameIsTooShort, "Name must be longer than %@ characters");
        put(Error_NameIsTooLong, "Name must be shorter than %@ characters");
        put(Error_NameIsTaken, "Name already taken.");
        put(Error_NameMustBeAlphaNumeric, "Name must contain alpha/numeric chars only.");
        put(Error_NameMustBeAlphaNumericWithSpecialSymbols, "Name must contain alphanumeric chars only and also may contain: %@");
        
        // Input descriptions
        put(Input_DoNothing, "DoNothing");   
        put(Input_Wait, "Wait for %@");   
        put(Input_KeyboardPress, "KeyboardPress '%@'");   
        put(Input_KeyboardRelease, "KeyboardRelease '%@'");   
        put(Input_MousePress, "MousePress '%@'");   
        put(Input_MouseRelease, "MouseRelease '%@'");   
        put(Input_MouseMove, "MouseMove %@,%@");
        put(Input_MouseMotion, "MouseMotion %@x moves, ends at %@,%@");
        put(Input_MouseWheel, "MouseWheel %@");
        put(Input_SystemCommand, "SystemCommand %@");
        put(Input_Screenshot, "Screenshot '%@'");
        
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
        put(Record_RedoRecordingButtonTitle, "Re-Record");
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
        put(Play_NotificationFinishTitle, "Automater");
        put(Play_NotificationFinishMessage, "Finished playing '%@'");
        put(Play_NotificationRepeatTitle, "Automater");
        put(Play_NotificationRepeatMessage, "Repeat playing '%@' (%@)");
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
        put(Edit_Delete, "Delete");
        put(Edit_Edit, "Edit");
        put(Edit_Create, "Create");
        put(Edit_CloseWithoutSavingTitle, "Cancel edit macro");
        put(Edit_CloseWithoutSavingMessage, "Are you sure you want to close without saving the changes?");
        
        // Edit action dialog
        put(EditAction_DialogTitle, "Automater - Edit Action");
        put(EditAction_CancelButtonTitle, "Cancel");
        put(EditAction_SaveButtonTitle, "Save");
        put(EditAction_StatusDefault, "");
        put(EditAction_StatusListeningToHotkey, "Enter hotkey...");
        put(EditAction_StatusError, "Error: %@");
        put(EditAction_Time, "Time position");
        put(EditAction_Wait, "Wait interval");
        put(EditAction_WaitTimeType, "Time type");
        put(EditAction_Key, "Key");
        put(EditAction_Press, "Press");
        put(EditAction_X, "X");
        put(EditAction_Y, "Y");
        put(EditAction_FinalX, "Final X");
        put(EditAction_FinalY, "Final Y");
        put(EditAction_Command, "Command");
        put(EditAction_Path, "Path");
        put(EditAction_ReportsErrors, "Reports errors");
        put(EditAction_ScreenshotTip, "<html>Placeholders:<br>%y year %mt month %d day<br>" +
                "%h hour %min minute %s second %ms millisec<br>%t timestamp");
        
        // Edit Action types
        put(EditAction_TypeDoNothing, "Do Nothing");
        put(EditAction_TypeWait, "Wait");
        put(EditAction_TypeKeyboardClick, "Keyboard click");
        put(EditAction_TypeMouseClick, "Mouse click");
        put(EditAction_TypeMouseMove, "Mouse move");
        put(EditAction_TypeSystemCommand, "System command");
        put(EditAction_TypeScreenshot, "Screenshot");
        
        // Edit Action descriptions
        put(EditAction_DescriptionDoNothing, "Do nothing");
        put(EditAction_DescriptionWait, "Waits for a specific amount of time");
        put(EditAction_DescriptionKeyboardClick, "Simulates keyboard key press or release");
        put(EditAction_DescriptionMouseClick, "Simulates mouse key press or release");
        put(EditAction_DescriptionMouseMove, "Simulates one mouse movement, choose x and y");
        put(EditAction_DescriptionMouseMotion, "Simulates %@x mouse movements, choose the final x and y");
        put(EditAction_DescriptionSystemCommand, "Execute system command line");
        put(EditAction_DescriptionScreenshot, "Take a screenshot of the device screen");
        
        // Macro parameters
        put(MacroParameters_Default, "Play once");
        put(MacroParameters_Playspeed, "Playspeed: %@");
        put(MacroParameters_Repeat, "Repeat: %@");
        put(MacroParameters_RepeatForever, "Play forever");
        put(MacroParameters_RepeatNever, "Play once");
        
        // Commands
        put(Commands_NotificationErrorTitle, "Automater failed command");
        put(Commands_NotificationErrorMessage, "Error: %@");
        
        // Dialog
        put(Dialog_OK, "Ok");
        put(Dialog_SavedRecordingTitle, "Save recording");
        put(Dialog_SavedRecordingMessage, "Successfully saved the recording!");
        put(Dialog_SaveRecordingFailedTitle, "Save recording");
        put(Dialog_SaveRecordingFailedMessage, "Could not save recording: %@");
        put(Dialog_ConfirmDeleteMacroTitle, "Delete macro");
        put(Dialog_ConfirmDeleteMacroMessage, "Are you sure you want to delete '%@'?");
        
        // System tray
        put(SystemTray_Tooltip, "Automater");
    }};
}
