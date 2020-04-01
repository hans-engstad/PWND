package com.mygdx.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.game.models.Player;

import java.util.UUID;

public abstract class BaseController {

    protected Preferences playerPrefs;

    public BaseController(){
        playerPrefs = Gdx.app.getPreferences("player");
        if (!playerPrefs.contains("username") || playerPrefs.getString("username") == null || playerPrefs.getString("username").trim().equals("")){
            playerPrefs.putString("username", "<username>").flush();
        }

        if (!playerPrefs.contains("id") || playerPrefs.getString("id") == null || playerPrefs.getString("id").trim().equals("")){
            playerPrefs.putString("id", UUID.randomUUID().toString()).flush();
        }
    }

    public Player getThisPlayer(){
        String username = playerPrefs.getString("username");
        String id = playerPrefs.getString("id");

        return new Player(username, id);
    }

}
