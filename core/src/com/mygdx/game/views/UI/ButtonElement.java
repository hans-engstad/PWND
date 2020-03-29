package com.mygdx.game.views.UI;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.game.PWND;

public class ButtonElement extends BaseElement {

    public enum Type {
        BLUE,
        RED,
        YELLOW,
        LIGHT_BLUE
    }

    private float size;
    private Type type;
    private String text;

    private TextButton button;
    private TextButton.TextButtonStyle style;
    private ChangeListener onChange;


    public ButtonElement(String text, ChangeListener onChange){
        // Set default parameters
        size = 5f;
        type = Type.BLUE;
        style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont();
        style.up = getSkinDrawable();

        // Set other variables
        this.text = text;
        this.onChange = onChange;

        // Instantiate button
        button = new TextButton(text, style);

        // Add button as actor
        setActor(button);

        // Refresh in order to update actor
        refresh();
    }

    @Override
    public void refresh(){
        button.setText(text);
        button.getLabel().setFontScale(size, size);
        button.setStyle(style);

        if (onChange != null){
            button.addListener(onChange);
        }
    }

    public void setChangeListener(ChangeListener onChange){
        this.onChange = onChange;
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

    private Drawable getSkinDrawable(){
        if (type == Type.BLUE){
            return PWND.skin.getDrawable("blue_big_but_frame");
        }
        else if(type == Type.RED){
            return PWND.skin.getDrawable("red_big_but_frame");
        }
        else if(type == Type.YELLOW){
            return PWND.skin.getDrawable("yellow_big_but_frame");
        }
        else if(type == Type.LIGHT_BLUE){
            return PWND.skin.getDrawable("light_blue_big_but_frame");
        }
        return null;
    }
}
