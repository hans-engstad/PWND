package com.mygdx.game.views.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.PWND;

public class ImageElement extends BaseElement {

    private String spriteName;

    private int width;
    private int height;

    private int xPos;
    private int yPos;

    private Image image;

    public ImageElement(String spriteName, int width, int height, int xPos, int yPos){
        this.spriteName = spriteName;
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;

        // Instantiate button
        image = new Image();

        // Add button as actor
        setActor(image);

        // Refresh in order to update actor
        refresh();
    }

    @Override
    public void refresh(){
        image.clear();
        image.setDrawable(new Skin(PWND.atlas).getDrawable(spriteName));
        image.setSize(width, height);
        image.setPosition(xPos, yPos);
    }
}

