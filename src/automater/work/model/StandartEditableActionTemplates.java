/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.model;

import automater.input.InputKey;
import automater.input.InputKeyClick;
import automater.input.InputKeyValue;
import automater.utilities.Description;
import automater.work.Action;
import automater.work.BaseAction;

/**
 *
 * @author Byti
 */
public class StandartEditableActionTemplates {
    public static BaseEditableAction buildTemplateFromTypeIndex(int typeIndex, long timestamp)
    {
        switch (typeIndex)
        {
            case 0:
                return buildTemplateKeyboardClick(timestamp);
            case 1:
                return buildTemplateMouseClick(timestamp);
            case 2:
                return buildTemplateMouseMove(timestamp);
            default:
                break;
        }
        
        return null;
    }
    
    public static BaseEditableAction buildTemplateKeyboardClick(long timestamp)
    {
        final InputKey key = new InputKey(InputKeyValue._A);
        
        InputKeyClick keyClick = new InputKeyClick() {
            @Override
            public InputKey getKeyValue() {
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
            action = Action.createKeyClick(timestamp, keyClick, Description.createFromString("KeyboardClick"));
        } catch (Exception e) {
            return null;
        }
        
        return StandartEditableAction.create(action);
    }
    
    public static BaseEditableAction buildTemplateMouseClick(long timestamp)
    {
        final InputKey key = new InputKey(InputKeyValue._MOUSE_LEFT_CLICK);
        
        InputKeyClick keyClick = new InputKeyClick() {
            @Override
            public InputKey getKeyValue() {
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
            action = Action.createKeyClick(timestamp, keyClick, Description.createFromString("MouseClick"));
        } catch (Exception e) {
            return null;
        }
        
        return StandartEditableAction.create(action);
    }
    
    public static BaseEditableAction buildTemplateMouseMove(long timestamp)
    {
        BaseAction action;
        
        try {
            action = Action.createMouseMovement(timestamp, 0, 0, Description.createFromString("MouseMove"));
        } catch (Exception e) {
            return null;
        }
        
        return StandartEditableAction.create(action);
    }
}
