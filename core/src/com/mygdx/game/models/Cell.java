package com.mygdx.game.models;

import java.util.HashMap;
import java.util.Map;

public class Cell {


    private Pawn pawn;
    private String key;

    public Cell(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Map serialize(){
        Map<String, Object> cell = new HashMap<>();

        cell.put("key", key);

        return cell;
    }

    public void addPawn(Pawn pawn){
        if (pawn != null){
            // TODO: Throw excpection here.
        }
        this.pawn = pawn;
    }

    public Pawn getPawn(){
        return this.pawn;
    }



}
