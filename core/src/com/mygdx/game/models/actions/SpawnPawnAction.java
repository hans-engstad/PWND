package com.mygdx.game.models.actions;

import com.mygdx.game.models.Cell;
import com.mygdx.game.models.Match;
import com.mygdx.game.models.Pawn;

import java.util.HashMap;
import java.util.Map;

public class SpawnPawnAction extends BaseAction implements IAction {

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

    @Override
    public Map<String, Object> serialize(){
        Map<String, Object> data = new HashMap<>();
        data.put("pawn", pawn.serialize());
        data.put("laneIndex", laneIndex);
        data.put("cellIndex", cellIndex);
        data.put("type", Type.SPAWN_PAWN.toString());

        return data;
    }

    public static SpawnPawnAction deserialize(Map<String, Object> data){

        Pawn pawn = Pawn.deserialize((Map)data.get("pawn"));
        int laneIndex = Long.valueOf((long) data.get("laneIndex")).intValue();
        int cellIndex = Long.valueOf((long) data.get("cellIndex")).intValue();

        return new SpawnPawnAction(pawn, laneIndex, cellIndex);

    }
}
