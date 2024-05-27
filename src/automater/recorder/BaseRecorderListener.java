/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.recorder;

import automater.recorder.model.RecorderResult;
import automater.recorder.model.RecorderUserInput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A listener for recorded user input actions.
 * 
 * onRecordedUserInput() indicates new input has been recorded.
 * 
 * onRecordedUserInputChanged() indicates that the recorder model has changed,
 * even when no new input object has been added. This may happen due to optimizations,
 * such as grouping multiple input objects into one.
 * 
 * onFailedRecordedUserInput() indicates failure.
 *
 * @author Bytevi
 */
public interface BaseRecorderListener {
    public void onRecordedUserInput(@NotNull RecorderUserInput input);
    public void onRecordedUserInputChanged();
    public void onFailedRecordedUserInput(@NotNull RecorderUserInput input);
    
    public void onFinishedRecording(@Nullable RecorderResult result, boolean success, @Nullable Exception exception);
}
