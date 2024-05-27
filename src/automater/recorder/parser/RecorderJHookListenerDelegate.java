/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.recorder.parser;

import automater.recorder.model.RecorderUserInput;
import org.jetbrains.annotations.NotNull;

/**
 * Forwards requests for parsed input objects.
 * 
 * Call onParseInput() when new input is forwarded.
 * Call onInputDataChange() when input data has changed without new input being delivered.
 * 
 * @author Bytevi
 */
public interface RecorderJHookListenerDelegate {
    public void onParseInput(@NotNull RecorderUserInput input);
    public void onInputDataChange();
}
