package com.mygdx.game.views.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Text {

    private TextureAtlas atlas;

    private float size;
    private String text;

    private Label label;

    private BitmapFont font;

    public Text(TextureAtlas atlas){
        this.atlas = atlas;

        // Set default parameters
        this.size = 5f;
        this.text = "";
        this.font = new BitmapFont();

        update();
    }

    public Label get() {
        return this.label;
    }

    public void update() {
        if (label == null){
            label = new com.badlogic.gdx.scenes.scene2d.ui.Label(text, new Skin(Gdx.files.internal("uiskin.json")));
        }
        label.setFontScale(size);
        label.setText(text);

    }

    public void setSize(float size){
        this.size = size;
        update();
    }

    public void setText(String text){
        this.text = text;
        update();
    }

}
