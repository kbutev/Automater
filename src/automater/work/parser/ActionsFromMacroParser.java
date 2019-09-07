/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.parser;

import automater.recorder.model.RecorderUserInput;
import automater.utilities.Errors;
import automater.work.Action;
import java.util.ArrayList;
import java.util.List;
import automater.recorder.model.UserInputKeyClick;
import automater.recorder.model.UserInputMouseMotion;
import automater.utilities.Description;
import automater.work.BaseAction;
import automater.recorder.model.UserInputMouseMove;

/**
 *
 * @author Bytevi
 */
public class ActionsFromMacroParser implements BaseActionsParser {
    public final ActionsParserFlags flags;
    
    private ArrayList<BaseAction> _actions = null;
    
    public ActionsFromMacroParser()
    {
        this(ActionsParserFlags.defaults());
    }
    
    public ActionsFromMacroParser(ActionsParserFlags flags)
    {
        this.flags = flags;
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
    public void onParseInput(RecorderUserInput input) throws Exception {
        if (_actions == null)
        {
            Errors.throwIllegalStateError("Parser cannot parse before beginning.");
        }
        
        Action action = null;
        
        if (input instanceof UserInputKeyClick)
        {
            UserInputKeyClick keyClick = (UserInputKeyClick)input;
            Description description = input;
            action = Action.createKeyClick(input.getTimestamp(), keyClick, description);
        }
        
        if (input instanceof UserInputMouseMove)
        {
            UserInputMouseMove mouseMove = (UserInputMouseMove)input;
            Description description = input;
            action = Action.createMouseMovement(mouseMove.getTimestamp(), mouseMove.getX(), mouseMove.getY(), description);
        }
        
        if (input instanceof UserInputMouseMotion)
        {
            UserInputMouseMotion mouseMotion = (UserInputMouseMotion)input;
            ArrayList<UserInputMouseMove> movements = new ArrayList<>();
            
            for (int e = 0; e < mouseMotion.numberOfMovements(); e++)
            {
                movements.add(mouseMotion.getMoveAt(e));
            }
            
            Description description = input;
            action = Action.createMouseMovement(input.getTimestamp(), movements, description);
        }
        
        if (action == null)
        {
            Errors.throwInvalidArgument("Parser cannot parse given user input, cannot recognize type");
            return;
        }
        
        _actions.add(action);
    }

    @Override
    public List<BaseAction> onFinishParsingMacro() throws Exception {
        if (_actions == null)
        {
            Errors.throwIllegalStateError("Parser cannot finish before beginning.");
        }
        
        ArrayList<BaseAction> actions = _actions;
        _actions = null;
        
        return actions;
    }
}
