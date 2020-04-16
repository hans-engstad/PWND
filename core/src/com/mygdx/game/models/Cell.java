package com.mygdx.game.models;

import java.util.HashMap;
import java.util.Map;

public class Cell {


    private Pawn pawn;
    private String key;

    public Cell(String key){
        this.key = key;
    }

    public Cell(Map<String, Object> data){
        this.key = (String) data.get("key");
        if (data.containsKey("pawn")){
            this.pawn = Pawn.deserialize((Map<String, Object>) data.get("pawn"));
        }
    }

    public String getKey() {
        return key;
    }

    public Map serialize(){
        Map<String, Object> cell = new HashMap<>();

        cell.put("key", key);
        if (pawn != null){
            cell.put("pawn", pawn.serialize());
        }

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

    public void removePawn (){
        this.pawn = null;
    }


}
