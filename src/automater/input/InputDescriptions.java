/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.input;

import automater.TextValue;
import automater.utilities.Description;
import automater.utilities.FileSystem;
import automater.utilities.TimeType;
import automater.utilities.TimeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.Serializable;

/**
 * Helper class that holds a collection of input descriptions.
 *
 * @author Bytevi
 */
public class InputDescriptions {
    private static TimeType TIMESTAMP_TIME_TYPE = TimeType.seconds;
    
    public static Description getDoNothingDescription(long timestamp)
    {
        String name = "DoNothing";
        String time = String.format("%s", TIMESTAMP_TIME_TYPE.asStringFromTime(TimeValue.fromMilliseconds(timestamp)));
        String description = TextValue.getText(TextValue.Input_DoNothing);
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getWaitDescription(long timestamp, long wait)
    {
        String name = "Wait";
        String time = String.format("%s", TIMESTAMP_TIME_TYPE.asStringFromTime(TimeValue.fromMilliseconds(timestamp)));
        String waitString = TimeValue.fromMilliseconds(wait).toString();
        String description = TextValue.getText(TextValue.Input_Wait, waitString);
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getKeyboardInputDescription(long timestamp, boolean press, @NotNull InputKey key)
    {
        String time = String.format("%s", TIMESTAMP_TIME_TYPE.asStringFromTime(TimeValue.fromMilliseconds(timestamp)));
        
        if (press)
        {
            String name = "KeyboardPress";
            String description = TextValue.getText(TextValue.Input_KeyboardPress, key.toString());
            String verbose = time + " " + description;
            String tooltip = description;
            
            return new InputDescription(name, description, verbose, tooltip);
        }
        
        String name = "KeyboardRelease";
        String description = TextValue.getText(TextValue.Input_KeyboardRelease, key.toString());
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getMouseInputDescription(long timestamp, boolean press, @NotNull InputKey key)
    {
        String time = String.format("%s", TIMESTAMP_TIME_TYPE.asStringFromTime(TimeValue.fromMilliseconds(timestamp)));
        
        if (press)
        {
            String name = "MousePress";
            String description = TextValue.getText(TextValue.Input_MousePress, key.toString());
            String verbose = time + " " + description;
            String tooltip = description;
            
            return new InputDescription(name, description, verbose, tooltip);
        }
        
        String name = "MouseRelease";
        String description = TextValue.getText(TextValue.Input_MouseRelease, key.toString());
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getMouseMoveDescription(long timestamp, int x, int y)
    {
        String name = "MouseMove";
        String time = String.format("%s", TIMESTAMP_TIME_TYPE.asStringFromTime(TimeValue.fromMilliseconds(timestamp)));
        String description = TextValue.getText(TextValue.Input_MouseMove, String.valueOf(x), String.valueOf(y));
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getMouseMotionDescription(long timestamp, @NotNull InputMouseMove first, @NotNull InputMouseMove last, int numberOfMovements)
    {
        int x = last.getX();
        int y = last.getY();
        
        String name = "MouseMotion";
        String time = String.format("%s", TIMESTAMP_TIME_TYPE.asStringFromTime(TimeValue.fromMilliseconds(timestamp)));
        String description = TextValue.getText(TextValue.Input_MouseMotion, String.valueOf(numberOfMovements), String.valueOf(x), String.valueOf(y));
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getMouseWheelDescription(long timestamp, int value)
    {
        String name = "MouseWheel";
        String time = String.format("%s", TIMESTAMP_TIME_TYPE.asStringFromTime(TimeValue.fromMilliseconds(timestamp)));
        String description = TextValue.getText(TextValue.Input_MouseWheel, String.valueOf(value));
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getSystemCommand(long timestamp, @NotNull String value)
    {
        String name = "SystemCommand";
        String time = String.format("%s", TIMESTAMP_TIME_TYPE.asStringFromTime(TimeValue.fromMilliseconds(timestamp)));
        String description = TextValue.getText(TextValue.Input_SystemCommand, value);
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
    
    public static Description getScreenshot(long timestamp, @NotNull String path)
    {
        String fileName = FileSystem.createFileNameFromFilePath(path);
        
        String name = "Screenshot";
        String time = String.format("%s", TIMESTAMP_TIME_TYPE.asStringFromTime(TimeValue.fromMilliseconds(timestamp)));
        String description = TextValue.getText(TextValue.Input_Screenshot, fileName);
        String verbose = time + " " + description;
        String tooltip = description;
        
        return new InputDescription(name, description, verbose, tooltip);
    }
}

class InputDescription implements Description, Serializable {
    String name;
    String value;
    String verbose;
    String tooltip;
    
    InputDescription(@NotNull String name, @NotNull String value, @NotNull String verbose, @NotNull String tooltip)
    {
        this.name = name;
        this.value = value;
        this.verbose = verbose;
        this.tooltip = tooltip;
    }

    @Override
    public @Nullable String getStandart() {
        return value;
    }

    @Override
    public @Nullable String getVerbose() {
        return verbose;
    }

    @Override
    public @Nullable String getStandartTooltip() {
        return tooltip;
    }

    @Override
    public @Nullable String getVerboseTooltip() {
        return tooltip;
    }

    @Override
    public @Nullable String getName() {
        return name;
    }

    @Override
    public @Nullable String getDebug() {
        return getVerbose();
    }
}
