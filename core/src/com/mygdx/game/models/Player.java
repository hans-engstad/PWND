package com.mygdx.game.models;

import java.util.HashMap;
import java.util.Map;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

public class Player {

    private String username;
    private String id;

    public Player(Map<String, Object> data){
        if (!data.containsKey("username") || !data.containsKey("id")){
            throw new IllegalArgumentException("Data must contain both username and id field");
        }

        if (data.get("username") == null){
            throw new IllegalArgumentException("Username cannot be null");
        }

        if (data.get("id") == null){
            throw new IllegalArgumentException("Id cannot be null");
        }

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
