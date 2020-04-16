package com.mygdx.game.models;

import java.util.HashMap;
import java.util.Map;

public class Pawn {

    public enum PawnOwner {
        MASTER,
        SLAVE
    }


    public int health;
    public int attack;
    public PawnOwner owner;


    public Pawn(int health, int attack, PawnOwner owner){
        this.health = health;
        this.attack = attack;
        this.owner = owner;
    }

    public static Pawn deserialize(Map<String, Object> data){
        // Instantiate new pawn
        int health = Integer.parseInt(data.get("health").toString());
        int attack = Integer.parseInt(data.get("attack").toString());
        PawnOwner owner = PawnOwner.valueOf((String) data.get("owner"));
        Pawn p = new Pawn(health, attack, owner);

        return p;
    }

    public String getSpriteName(){
        if (health == 1){
            return "pawn_1";
        }
        else if (health == 2){
            return "pawn_2";
        }
        else if (health == 3){
            return "pawn_3";
        }
        return null;
    }

    public Map<String, Object> serialize (){
        Map<String, Object> data = new HashMap<>();

        data.put("owner", this.owner.toString());
        data.put("health", this.health);
        data.put("attack", this.attack);

        return data;
    }

    public int getHealth () { return health; }
    public int getAttack () { return attack; }

    public void decreaseHealth(){ decreaseHealth(1); }

    public void decreaseHealth(int amount){
        health -= amount;
    }

    public Boolean isMaster(){
        return owner == PawnOwner.MASTER;
    }






}
