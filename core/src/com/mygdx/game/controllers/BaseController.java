package com.mygdx.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.game.models.Player;

public abstract class BaseController {

    protected Preferences playerPrefs;

    public BaseController(){
        playerPrefs = Gdx.app.getPreferences("player");
    }

    public Player getThisPlayer(){
        String username = playerPrefs.getString("username");
        String id = playerPrefs.getString("id");
        return new Player(username, id);
    }

}
