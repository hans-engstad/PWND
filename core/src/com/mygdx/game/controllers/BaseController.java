package com.mygdx.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public abstract class BaseController {

    protected Preferences playerPrefs;

    public BaseController(){
        playerPrefs = Gdx.app.getPreferences("player");
    }


}
