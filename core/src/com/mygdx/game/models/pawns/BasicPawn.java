package com.mygdx.game.models.pawns;

import com.mygdx.game.models.Pawn;

import java.util.Map;

public class BasicPawn extends Pawn {

    public BasicPawn(){
        super(Type.PAWN_1);
        name = "Basic Pawn";
    }

    public BasicPawn(Map<String, Object> data){
        this();
    }

}
