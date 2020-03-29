package com.mygdx.game.views.UI;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.PWND;

public abstract class BaseElement {

    private Actor actor;

    public Actor getActor(){
        return actor;
    }

    public void setActor(Actor actor){
        this.actor = actor;
    }

    public void refresh() {}
}
