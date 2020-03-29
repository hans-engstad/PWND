package com.mygdx.game.views;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.PWND;
import com.mygdx.game.controllers.MatchController;
import com.mygdx.game.models.Lane;
import com.mygdx.game.models.Match;

public class MatchView extends BaseView {

    private MatchController controller;
    private Match match;

    private float screenWidth;
    private float screenHeight;


    public MatchView (Match match){
        this.match = match;
    }

    @Override
    public void show(){
        super.show();
        controller = new MatchController(this, match);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        screenWidth = (float) width;
        screenHeight = (float) height;
    }

    @Override
    public void render(float delta) {
        // Clear previous elements
        stage.clear();

        drawBoard();

        super.render(delta);
    }

    void drawBoard(){
        Lane[] lanes = match.getLanes();
        float cellWidth = screenWidth / (float) lanes.length;
        float cellHeight = screenHeight / (float) lanes[0].getCells().length;

        for (int i = 0; i < lanes.length; i++){
            Lane lane = lanes[i];
            for (int j = 0; j < lane.getCells().length; j++){
                Image img = new Image();
                img.setDrawable(new Skin(PWND.atlas).getDrawable("cell"));

                img.setWidth(cellWidth);
                img.setHeight(cellHeight);
                img.setPosition(cellWidth * i, cellHeight * j);

                stage.addActor(img);
            }
        }
    }

    void drawPawns(){

    }
}
