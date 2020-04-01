package com.mygdx.game.models.actions;

import com.mygdx.game.models.Cell;
import com.mygdx.game.models.Lane;
import com.mygdx.game.models.Match;
import com.mygdx.game.models.Pawn;

public class SpawnPawnAction implements IAction {

    private Pawn pawn;
    private int laneIndex;
    private int cellIndex;

    public SpawnPawnAction(Pawn pawn, int laneIndex, int cellIndex){
        this.pawn = pawn;
        this.laneIndex = laneIndex;
        this.cellIndex = cellIndex;

    }

    @Override
    public void perform(Match match) {
        // Find correct cell
        Cell cell =  match.getLanes()[laneIndex].getCell(cellIndex);

        // Add pawn to this cell
        cell.addPawn(pawn);
    }
}
