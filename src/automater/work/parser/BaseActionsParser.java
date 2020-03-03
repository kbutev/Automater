/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.work.parser;

import automater.recorder.model.RecorderUserInput;
import automater.work.BaseAction;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Represents a parser that takes RecorderUserInputs and converts them to
 * BaseActions.
 *
 * @author Bytevi
 */
public interface BaseActionsParser {
    public void onBeginParsing() throws Exception;
    public void onParseInput(@NotNull RecorderUserInput input) throws Exception;
    public @NotNull List<BaseAction> onFinishParsingMacro() throws Exception;
}
