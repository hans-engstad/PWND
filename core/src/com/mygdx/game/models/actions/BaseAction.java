package com.mygdx.game.models.actions;

import com.mygdx.game.models.Match;

import java.util.Map;

public abstract class BaseAction {

    public enum Type {
        SPAWN_PAWN
    }

    public static IAction deserialize(Map<String, Object> data){
        // Find action type
        if (Type.valueOf(data.get("type").toString()) == Type.SPAWN_PAWN){
            return SpawnPawnAction.deserialize(data);
        }

        // Type not found, return null
        return null;
    }

}
