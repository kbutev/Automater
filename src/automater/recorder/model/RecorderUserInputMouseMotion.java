/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.recorder.model;

import automater.input.InputDescriptions;
import java.util.ArrayList;
import automater.input.InputMouseMotion;
import automater.input.InputMouseMove;
import automater.utilities.CollectionUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * A series of mouse movements.
 *
 * @author Bytevi
 */
public class RecorderUserInputMouseMotion extends RecorderUserInput implements InputMouseMotion {
    @NotNull private final ArrayList<InputMouseMove> _moves;
    
    public static RecorderUserInputMouseMotion create(@NotNull InputMouseMove firstMove)
    {
        return new RecorderUserInputMouseMotion(firstMove);
    }
    
    private RecorderUserInputMouseMotion(@NotNull InputMouseMove firstMove)
    {
        super(firstMove.getTimestamp());
        
        _moves = new ArrayList<>();
        _moves.add(firstMove);
    }
    
    // # Description
    
    @Override
    public @Nullable String getStandart() {
        InputMouseMove firstMove = getFirstMove();
        InputMouseMove lastMove = getLastMove();
        
        return InputDescriptions.getMouseMotionDescription(getTimestamp(), firstMove, lastMove, numberOfMovements()).getStandart();
    }
    
    @Override
    public @Nullable String getVerbose() {
        InputMouseMove firstMove = getFirstMove();
        InputMouseMove lastMove = getLastMove();
        
        return InputDescriptions.getMouseMotionDescription(getTimestamp(), firstMove, lastMove, numberOfMovements()).getVerbose();
    }
    
    // # InputMouseMotion
    
    @Override
    public int numberOfMovements() {
        return _moves.size();
    }
    
    @Override
    public @NotNull List<InputMouseMove> getMoves() {
        return CollectionUtilities.copyAsImmutable(_moves);
    }

    @Override
    public @NotNull InputMouseMove getFirstMove() {
        return _moves.get(0);
    }

    @Override
    public @NotNull InputMouseMove getLastMove() {
        return _moves.get(_moves.size()-1);
    }

    @Override
    public @NotNull InputMouseMove getMoveAt(int index) {
        return _moves.get(index);
    }
    
    // # Public
    
    public void addMovementPoint(@NotNull InputMouseMove move)
    {
        _moves.add(move);
    }
}
