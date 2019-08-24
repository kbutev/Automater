/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.work.parser;

import automater.recorder.model.RecorderUserInput;
import automater.work.BaseAction;
import java.util.List;

/**
 *
 * @author Bytevi
 */
public interface BaseActionsParser {
    public void onBeginParsing() throws Exception;
    public void onParseInput(RecorderUserInput input) throws Exception;
    public List<BaseAction> onFinishParsingMacro() throws Exception;
}
