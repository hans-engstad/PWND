package com.mygdx.game.models;

import com.mygdx.game.models.pawns.BasicPawn;

import java.util.HashMap;
import java.util.Map;

public abstract class Pawn {

    public enum PawnOwner {
        MASTER,
        SLAVE
    }

    public enum Type {
        PAWN_1
    }

    public int health;
    public String name;
    public PawnOwner owner;
    private Type type;


    public Pawn(Type type){
        this.type = type;
    }

    public static Pawn deserialize(Map<String, Object> data){
        // Instantiate correct pawn type
        Type type = Type.valueOf((String)data.get("type"));
        if (type == Type.PAWN_1){
            return new BasicPawn(data);
        }

        return null;
    }

    public String getSpriteName(){
        if (type == Type.PAWN_1){
            return "pawn_1";
        }
        return null;
    }

    public Map<String, Object> serialize (){
        Map<String, Object> data = new HashMap<>();

        data.put("type", this.type.toString());
        data.put("owner", this.owner.toString());

        return data;
    }

    public String toString(){
        return name;
    }






}
