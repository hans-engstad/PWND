package com.mygdx.game.views.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.PWND;

public class TextElement extends BaseElement {

    private float size;
    private String text;
    private Label label;


    public TextElement(String text){
        // Set default parameters
        this.size = 5f;

        // Set other variables
        this.text = text;

        // Instantiate label
        label = new Label(text, PWND.skin);

        // Set actor variable in super-class
        setActor(label);

        // Refresh in order to update actor
        refresh();
    }

    public void setSize(float size){
        this.size = size;
        refresh();
    }

    public void setText(String text){
        this.text = text;
        refresh();
    }

    @Override
    public void refresh(){
        label.clear();
        label.setText(text);
        label.setFontScale(size);
    }

}
