/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.parser;

import automater.recorder.model.RecorderUserInput;
import automater.work.BaseAction;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Bytevi
 */
public class ActionsParserUtilities {
    public static List<BaseAction> parseUserInputs(Collection<RecorderUserInput> userInputs, BaseActionsParser actionParser) throws Exception
    {
        actionParser.onBeginParsing();
        
        Iterator<RecorderUserInput> it = userInputs.iterator();
        
        while (it.hasNext())
        {
            actionParser.onParseInput(it.next());
        }
        
        return actionParser.onFinishParsingMacro();
    }
}
