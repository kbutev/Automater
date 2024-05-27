/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work.parser;

import automater.input.InputDoNothing;
import automater.recorder.model.RecorderUserInput;
import automater.utilities.Errors;
import automater.work.Action;
import java.util.ArrayList;
import java.util.List;
import automater.utilities.Description;
import automater.work.BaseAction;
import automater.input.InputKeyClick;
import automater.input.InputMouseMotion;
import automater.input.InputMouseMove;
import automater.input.InputMouseWheel;
import org.jetbrains.annotations.NotNull;

/**
 * Converts RecorderUserInputs to BaseActions.
 *
 * @author Bytevi
 */
public class ActionsFromMacroInputsParser implements BaseActionsParser {
    private @NotNull ArrayList<BaseAction> _actions = null;
    
    public ActionsFromMacroInputsParser()
    {
        
    }
    
    @Override
    public void onBeginParsing() throws Exception {
        if (_actions != null)
        {
            Errors.throwIllegalStateError("Parser cannot begin, it's already running.");
        }
        
        _actions = new ArrayList<>();
    }

    @Override
    public void onParseInput(@NotNull RecorderUserInput input) throws Exception {
        if (_actions == null)
        {
            Errors.throwIllegalStateError("Parser cannot parse before beginning.");
        }
        
        Action action = null;
        
        if (input instanceof InputKeyClick)
        {
            InputKeyClick keyClick = (InputKeyClick)input;
            Description description = input;
            action = Action.createKeyClick(input.getTimestamp(), keyClick, description);
        }
        
        if (input instanceof InputMouseMove)
        {
            InputMouseMove mouseMove = (InputMouseMove)input;
            Description description = input;
            action = Action.createMouseMovement(mouseMove.getTimestamp(), mouseMove.getX(), mouseMove.getY(), description);
        }
        
        if (input instanceof InputMouseMotion)
        {
            InputMouseMotion mouseMotion = (InputMouseMotion)input;
            ArrayList<InputMouseMove> movements = new ArrayList<>();
            
            for (int e = 0; e < mouseMotion.numberOfMovements(); e++)
            {
                movements.add(mouseMotion.getMoveAt(e));
            }
            
            Description description = input;
            action = Action.createMouseMovement(input.getTimestamp(), movements, description);
        }
        
        if (input instanceof InputMouseWheel)
        {
            InputMouseWheel mouseWheel = (InputMouseWheel)input;
            Description description = input;
            action = Action.createMouseWheel(input.getTimestamp(), mouseWheel.getScrollValue(), description);
        }
        
        if (input instanceof InputDoNothing)
        {
            action = Action.createDoNothing(input.getTimestamp());
        }
        
        if (action == null)
        {
            Errors.throwInvalidArgument("Parser cannot parse given user input, cannot recognize type");
            return;
        }
        
        _actions.add(action);
    }

    @Override
    public @NotNull List<BaseAction> onFinishParsingMacro() throws Exception {
        if (_actions == null)
        {
            Errors.throwIllegalStateError("Parser cannot finish before beginning.");
        }
        
        ArrayList<BaseAction> actions = _actions;
        _actions = null;
        
        return actions;
    }
}
