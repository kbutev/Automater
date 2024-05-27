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
 * Describes a recorded user input model.
 * 
 * The receiver is to process and store given objects so
 * they can later be retrieved by retrieveRecordedData().
 * 
 * The method addInput() takes an input object and adds it to the data.
 * Method is capable of throwing exceptions indicating that parsing should stop.
 * 
 * The method retrieveRecordedData() gets the processed data by the parser.
 * 
 * @author Bytevi
 */
public interface BaseRecorderModel {
    public int getSize();
    
    public @Nullable RecorderUserInput getFirstAddedInput();
    public @Nullable RecorderUserInput getLastAddedInput();
    
    public void addInput(@NotNull RecorderUserInput input) throws Exception;
    
    public @Nullable RecorderResult retrieveRecordedData();
}
