package com.mygdx.game.models;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private String username;
    private String id;

    public Player(Map<String, Object> data){
        this.username = (String) data.get("username");
        this.id = (String) data.get("id");
    }

    public Player (String username, String id){
        this.username = username;
        this.id = id;
    }

    public Map serialize(){
        Map<String, Object> player = new HashMap<>();

        player.put("username", username);
        player.put("id", id);

        return player;
    }

    public String getId(){
        return this.id;
    }

    public String getUsername(){
        return this.username;
    }


}
