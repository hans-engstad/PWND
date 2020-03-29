package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.PWND;
import com.mygdx.game.controllers.MatchController;
import com.mygdx.game.models.Cell;
import com.mygdx.game.models.Lane;
import com.mygdx.game.models.Match;
import com.mygdx.game.models.Pawn;
import com.mygdx.game.models.pawns.BasicPawn;
import com.mygdx.game.views.UI.ButtonElement;
import com.mygdx.game.views.UI.ImageElement;

import java.util.Map;

public class MatchView extends BaseView {

    private final int BOTTOM_BAR_HEIGHT = 200;  // Height in pixels of bottom UI bar

    private MatchController controller;
    private Match match;

    private int screenWidth;
    private int screenHeight;

    private int cellWidth;
    private int cellHeight;


    public MatchView (Match match){
        this.match = match;
    }

    @Override
    public void show(){
        super.show();
        controller = new MatchController(this, match);

        // Screen size
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        updateCellSize();
    }

    void updateCellSize(){
        cellWidth = screenWidth / match.getLanes().length;
        cellHeight = (screenHeight - BOTTOM_BAR_HEIGHT) / match.getLanes()[0].getCells().length;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        screenWidth = width;
        screenHeight = height;
        updateCellSize();
    }

    @Override
    public void render(float delta) {
        // Clear previous elements
        stage.clear();

        drawBoard();
        drawPawns();

        drawBottomBar();

        super.render(delta);
    }

    void drawBoard(){
        Lane[] lanes = match.getLanes();

        for (int i = 0; i < lanes.length; i++){
            Lane lane = lanes[i];
            for (int j = 0; j < lane.getCells().length; j++){

                int w = cellWidth;          // Width
                int h = cellHeight;         // Height
                int x = cellWidth * i;      // X-position
                int y = (cellHeight * j) + BOTTOM_BAR_HEIGHT;     // Y-position

                ImageElement image = new ImageElement("cell", w, h, x, y);

                stage.addActor(image.getActor());
            }
        }
    }

    void drawPawns(){
        Lane[] lanes = match.getLanes();

        for (int i = 0; i < lanes.length; i++){
            Lane lane = lanes[i];
            for (int j = 0; j < lane.getCells().length; j++){
                Cell cell = lane.getCell(j);
                Pawn pawn = cell.getPawn();
                if (pawn != null){
                    // Draw pawn image
                    int w = cellWidth;          // Width
                    int h = cellWidth;          // Height
                    int x = cellWidth * i;      // X-position
                    int y = (cellHeight * j) + BOTTOM_BAR_HEIGHT;     // Y-position

                    ImageElement image = new ImageElement(pawn.getSpriteName(), w, h, x, y);

                    stage.addActor(image.getActor());
                }
            }
        }
    }

    void drawBottomBar(){
        ButtonElement spawnPawnButton = new ButtonElement("SPAWN", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Pawn p = new BasicPawn();
                Lane l = match.getLanes()[0];
                controller.spawnPawn(p, l);
            }
        });

        stage.addActor(spawnPawnButton.getActor());
    }
}
