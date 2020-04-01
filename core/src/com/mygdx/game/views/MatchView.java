package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
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
    private final int SPAWN_PAWN_BUTTON_WIDTH = 300;

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

        updateLaneSelection();

        drawBoard();
        drawPawns();
        drawBottomBar();

        super.render(delta);

        controller.render(delta);
    }

    void drawBoard(){
        Lane[] lanes = match.getLanes();

        for (int i = 0; i < lanes.length; i++){
            Lane lane = lanes[i];
            Boolean currentLaneSelected = lane == controller.getSelectedLane();
            for (int j = 0; j < lane.getCells().length; j++){

                int w = cellWidth;          // Width
                int h = cellHeight;         // Height
                int x = cellWidth * i;      // X-position
                int y = (cellHeight * j) + BOTTOM_BAR_HEIGHT;     // Y-position

                if (currentLaneSelected){
                    ImageElement image = new ImageElement("cell_selected", w, h, x, y);
                    stage.addActor(image.getActor());
                }
                else{
                    ImageElement image = new ImageElement("cell", w, h, x, y);
                    stage.addActor(image.getActor());
                }


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
        // TODO: Figure out why this is not working
        /*

        Table table = new Table();
        table.setPosition(0f, 0f);
        table.setSize(screenWidth, BOTTOM_BAR_HEIGHT);
        table.align(Align.left);

        ButtonElement spawnPawnButton = new ButtonElement("Basic pawn", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Pawn p = new BasicPawn();
                Lane l = match.getLanes()[0];
                controller.spawnPawn(p, l);
            }
        });

        table.add(spawnPawnButton.getActor()).size(SPAWN_PAWN_BUTTON_WIDTH, BOTTOM_BAR_HEIGHT);

        stage.addActor(table);

        */

        // NOTE: Temporary solution
        Table table = new Table();
        table.setPosition(0f, 0f);
        table.setSize(screenWidth, BOTTOM_BAR_HEIGHT);
        table.align(Align.left);

        ImageElement spawnPawnButtonFrame = new ImageElement("square_red_but_frame", BOTTOM_BAR_HEIGHT, BOTTOM_BAR_HEIGHT, 0, 0);
        ImageElement spawnPawnButtonPawn = new ImageElement("pawn_1", BOTTOM_BAR_HEIGHT - 40, BOTTOM_BAR_HEIGHT - 40, 20, 20);

        Group group = new Group();
        group.addActor(spawnPawnButtonFrame.getActor());
        group.addActor(spawnPawnButtonPawn.getActor());

        table.add(group).size(SPAWN_PAWN_BUTTON_WIDTH, BOTTOM_BAR_HEIGHT);

        stage.addActor(table);


        // Check if "button" was pressed
        if (Gdx.input.justTouched()){
            int x = Gdx.input.getX();
            int y = screenHeight - Gdx.input.getY();

            if (x >= 0 && x <= BOTTOM_BAR_HEIGHT && y >= 0 && y <= BOTTOM_BAR_HEIGHT ){
                controller.setSelectedPawn(new BasicPawn());
            }

        }



    }

    void updateLaneSelection (){
        if (Gdx.input.justTouched()){
            int x = Gdx.input.getX();
            int y = screenHeight - Gdx.input.getY();
            // Check if click is above bottom bar
            if (y > BOTTOM_BAR_HEIGHT){
                // Check which lane was clicked and select that lane
                int laneIndex = x / cellWidth;
                Lane laneClicked = match.getLanes()[laneIndex];
                if (controller.getSelectedLane() == laneClicked){
                    // Lane already selected clicked, deselect lane
                    controller.setSelectedLane(null);
                }
                else{
                    // Select lane clicked
                    controller.setSelectedLane(laneClicked);
                }
            }
        }
    }
}
