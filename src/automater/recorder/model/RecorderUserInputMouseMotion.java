/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.recorder.model;

import java.util.ArrayList;
import automater.input.InputMouseMotion;
import automater.input.InputMouseMove;

/**
 * A series of mouse movements.
 *
 * @author Bytevi
 */
public class RecorderUserInputMouseMotion extends RecorderUserInput implements InputMouseMotion {
    private final ArrayList<InputMouseMove> _moves;
    
    public static RecorderUserInputMouseMotion createMouseMovement(InputMouseMove firstMove)
    {
        return new RecorderUserInputMouseMotion(firstMove);
    }
    
    private RecorderUserInputMouseMotion(InputMouseMove firstMove)
    {
        super(firstMove.getTimestamp());
        
        _moves = new ArrayList<>();
        _moves.add(firstMove);
    }
    
    // # Description
    
    @Override
    public String getStandart() {
        return "MouseMotion " + "(" + String.valueOf(numberOfMovements()) + "x)";
    }
    
    // # Public
    
    public void addMovementPoint(InputMouseMove move)
    {
        _moves.add(move);
    }

    @Override
    public int numberOfMovements() {
        return _moves.size();
    }

    @Override
    public InputMouseMove getFirstMove() {
        return _moves.get(0);
    }

    @Override
    public InputMouseMove getLastMove() {
        return _moves.get(_moves.size()-1);
    }

    @Override
    public InputMouseMove getMoveAt(int index) {
        return _moves.get(index);
    }
    
}
