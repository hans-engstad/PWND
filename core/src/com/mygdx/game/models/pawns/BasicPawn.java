package com.mygdx.game.models.pawns;

import com.mygdx.game.models.Pawn;

import java.util.Map;

public class BasicPawn extends Pawn {

    public BasicPawn(PawnOwner owner){
        super(Type.PAWN_1);
        this.owner = owner;
        name = "Basic Pawn";
    }

    public BasicPawn(Map<String, Object> data){
        this(PawnOwner.valueOf((String) data.get("owner")));
    }



}
