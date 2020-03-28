package com.mygdx.game.views.UI;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Button {

    public enum Type {
        BLUE,
        RED,
        YELLOW,
        LIGHT_BLUE
    }

    private TextureAtlas atlas;

    private float size;
    private Type type;
    private String text;

    private TextButton button;
    private TextButton.TextButtonStyle style;
    private BitmapFont font;

    public Button(TextureAtlas atlas){
        this.atlas = atlas;

        // Set default parameters
        this.size = 5f;
        this.type = Type.BLUE;
        this.text = "";
        this.font = new BitmapFont();
        this.style = new TextButton.TextButtonStyle();

        update();
    }

    public TextButton get() {
        return this.button;
    }

    public void update() {

        style.font = this.font;
        style.up = getSkinDrawable();

        button = new TextButton(text, style);
        button.getLabel().setFontScale(size, size);

    }

    public void setSize(float size){
        this.size = size;
        update();
    }

    public void setType(Type type){
        this.type = type;
        update();
    }

    public void setText(String text){
        this.text = text;
        update();
    }

    private Drawable getSkinDrawable(){
        Skin skin = new Skin(atlas);

        if (type == Type.BLUE){
            return skin.getDrawable("blue_big_but_frame");
        }
        else if(type == Type.RED){
            return skin.getDrawable("red_big_but_frame");
        }
        else if(type == Type.YELLOW){
            return skin.getDrawable("yellow_big_but_frame");
        }
        else if(type == Type.LIGHT_BLUE){
            return skin.getDrawable("light_blue_big_but_frame");
        }
        return null;
    }



}
