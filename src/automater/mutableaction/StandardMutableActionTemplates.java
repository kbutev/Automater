/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.mutableaction;

import automater.input.InputDescriptions;
import automater.input.InputKey;
import automater.input.InputKeyClick;
import automater.input.InputKeyValue;
import automater.utilities.Description;
import automater.utilities.FileSystem;
import automater.utilities.Path;
import automater.utilities.TimeValue;
import automater.work.Action;
import automater.work.BaseAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Builder for BaseMutableActions.
 *
 * @author Byti
 */
public class StandardMutableActionTemplates {
    public static @Nullable StandardMutableAction buildTemplateFromTypeIndex(int typeIndex, long timestamp)
    {
        switch (typeIndex)
        {
            case 0:
                return buildTemplateDoNothing(timestamp);
            case 1:
                return buildTemplateWait(timestamp);
            case 2:
                return buildTemplateKeyboardClick(timestamp);
            case 3:
                return buildTemplateMouseClick(timestamp);
            case 4:
                return buildTemplateMouseMove(timestamp);
            case 5:
                return buildTemplateSystemCommand(timestamp);
            case 6:
                return buildTemplateScreenshot(timestamp);
            default:
                break;
        }
        
        return null;
    }
    
    public static @Nullable StandardMutableAction buildTemplateDoNothing(long timestamp)
    {
        return new StandartMutableActionDoNothing(timestamp);
    }
    
    public static @Nullable StandardMutableAction buildTemplateWait(long timestamp)
    {
        return new StandartMutableActionWait(timestamp, TimeValue.zero());
    }
    
    public static @Nullable StandardMutableAction buildTemplateKeyboardClick(long timestamp)
    {
        final InputKey key = new InputKey(InputKeyValue._A);
        
        InputKeyClick keyClick = new InputKeyClick() {
            @Override
            public @NotNull InputKey getKeyValue() {
                return key;
            }

            @Override
            public boolean isPress() {
                return false;
            }

            @Override
            public long getTimestamp() {
                return timestamp;
            }
        };
        
        BaseAction action;
        
        try {
            Description description = InputDescriptions.getKeyboardInputDescription(timestamp, keyClick.isPress(), key);
            action = Action.createKeyClick(timestamp, keyClick, description);
        } catch (Exception e) {
            return null;
        }
        
        return StandardMutableAction.createFromAction(action);
    }
    
    public static @Nullable StandardMutableAction buildTemplateMouseClick(long timestamp)
    {
        final InputKey key = new InputKey(InputKeyValue._MOUSE_LEFT_CLICK);
        
        InputKeyClick keyClick = new InputKeyClick() {
            @Override
            public @NotNull InputKey getKeyValue() {
                return key;
            }

            @Override
            public boolean isPress() {
                return false;
            }

            @Override
            public long getTimestamp() {
                return timestamp;
            }
        };
        
        BaseAction action;
        
        try {
            Description description = InputDescriptions.getKeyboardInputDescription(timestamp, keyClick.isPress(), key);
            action = Action.createKeyClick(timestamp, keyClick, description);
        } catch (Exception e) {
            return null;
        }
        
        return StandardMutableAction.createFromAction(action);
    }
    
    public static @Nullable StandardMutableAction buildTemplateMouseMove(long timestamp)
    {
        BaseAction action;
        
        try {
            Description description = InputDescriptions.getMouseMoveDescription(timestamp, 0, 0);
            action = Action.createMouseMovement(timestamp, 0, 0, description);
        } catch (Exception e) {
            return null;
        }
        
        return StandardMutableAction.createFromAction(action);
    }
    
    public static @Nullable StandardMutableAction buildTemplateSystemCommand(long timestamp)
    {
        BaseAction action;
        
        try {
            Description description = InputDescriptions.getSystemCommand(timestamp, "");
            action = Action.createSystemCommand(timestamp, "", true, description);
        } catch (Exception e) {
            return null;
        }
        
        return StandardMutableAction.createFromAction(action);
    }
    
    public static @Nullable StandardMutableAction buildTemplateScreenshot(long timestamp)
    {
        BaseAction action;
        
        var path = Path.getLocalDirectory().withSubpath("screenshot.jpg").toString();
        
        try {
            Description description = InputDescriptions.getScreenshot(timestamp, path);
            action = Action.createScreenshot(timestamp, path, description);
        } catch (Exception e) {
            return null;
        }
        
        return StandardMutableAction.createFromAction(action);
    }
}
