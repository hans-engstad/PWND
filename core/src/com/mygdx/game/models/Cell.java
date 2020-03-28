package com.mygdx.game.models;

import java.util.HashMap;
import java.util.Map;

public class Cell {



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



}
