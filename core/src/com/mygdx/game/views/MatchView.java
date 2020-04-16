package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.controllers.MatchController;
import com.mygdx.game.models.Cell;
import com.mygdx.game.models.Lane;
import com.mygdx.game.models.Match;
import com.mygdx.game.models.Pawn;
import com.mygdx.game.views.UI.ImageElement;

public class MatchView extends BaseView {

    private final int BOTTOM_BAR_HEIGHT = 200;  // Height in pixels of bottom UI bar
    private final int SPAWN_PAWN_BUTTON_WIDTH = 200;

    private MatchController controller;

    private int screenWidth;
    private int screenHeight;

    private int cellWidth;
    private int cellHeight;

    private Boolean setupComplete;


    public MatchView (String matchKey){
        controller = new MatchController(this, matchKey);
        setupComplete = false;
    }

    void setup(){
        // Screen size
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        updateCellSize();

        setupComplete = true;
    }

    void updateCellSize(){
        cellWidth = screenWidth / controller.getMatch().getLanes().length;
        cellHeight = (screenHeight - BOTTOM_BAR_HEIGHT) / controller.getMatch().getLanes()[0].getCells().length;
    }

    @Override
    public void resize(int width, int height) {
        if (setupComplete){
            super.resize(width, height);
            screenWidth = width;
            screenHeight = height;
            updateCellSize();
        }
    }

    @Override
    public void render(float delta) {
        if (controller.getMatch() == null) {
            return;
        }
        if (!setupComplete && controller.getMatch().getStatus() == Match.Status.STARTED){
            setup();
        }

        if (controller.getMatch().getLanes() == null || controller.getMatch().getLanes().length == 0){
            return;
        }

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
        Lane[] lanes = controller.getIsMaster() ? controller.getMatch().getLanes() : controller.getMatch().getLanesFlipped();

        for (int i = 0; i < lanes.length; i++){
            Lane lane = lanes[i];
            Boolean currentLaneSelected = lane == controller.getSelectedLane();
            Cell[] cells = controller.getIsMaster() ? lane.getCells() : lane.getCellsFlipped();
            for (int j = 0; j < cells.length; j++){

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
        Lane[] lanes = controller.getIsMaster() ? controller.getMatch().getLanes() : controller.getMatch().getLanesFlipped();

        for (int i = 0; i < lanes.length; i++){
            Lane lane = lanes[i];
            Cell[] cells = controller.getIsMaster() ? lane.getCells() : lane.getCellsFlipped();
            for (int j = 0; j < cells.length; j++){
                Cell cell = cells[j];
                Pawn pawn = cell.getPawn();
                if (pawn != null){
                    // Draw pawn image
                    int w = cellWidth;          // Width
                    int h = cellWidth;          // Height
                    int x = cellWidth * i;      // X-position
                    int y = (cellHeight * j) + BOTTOM_BAR_HEIGHT;     // Y-position

                    ImageElement image = new ImageElement(pawn.getSpriteName(), w, h, x, y);

                    // Flip pawn image if pawn belongs to other player
                    if (pawn.isMaster() != controller.getIsMaster()){
                        Actor imageActor = image.getActor();
                        imageActor.setOrigin(cellWidth / 2, cellHeight / 2);
                        imageActor.rotateBy(180f);
                    }

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

        String[] pawnSprites = new String[] { "pawn_1", "pawn_2", "pawn_3" };
        int[] pawnHealths = new int[]{ 1, 2, 3 };
        int[] pawnAttacks = new int[]{ 1, 2, 3 };

        for (int i = 0; i < pawnSprites.length; i++){
            ImageElement spawnPawnButtonFrame = new ImageElement("square_red_but_frame", SPAWN_PAWN_BUTTON_WIDTH, BOTTOM_BAR_HEIGHT, 0, 0);
            ImageElement spawnPawnButtonPawn = new ImageElement(pawnSprites[i], SPAWN_PAWN_BUTTON_WIDTH - 40, BOTTOM_BAR_HEIGHT - 40, 20, 20);

            Group group = new Group();
            group.addActor(spawnPawnButtonFrame.getActor());
            group.addActor(spawnPawnButtonPawn.getActor());

            table.add(group).size(SPAWN_PAWN_BUTTON_WIDTH, BOTTOM_BAR_HEIGHT);
        }


        stage.addActor(table);


        // Check if "button" was pressed
        if (Gdx.input.justTouched()){
            int x = Gdx.input.getX();
            int y = screenHeight - Gdx.input.getY();

            int index = x / BOTTOM_BAR_HEIGHT;
            if (index >= 0 && index < pawnHealths.length && y >= 0 && y <= BOTTOM_BAR_HEIGHT ){
                Pawn pawn = new Pawn(pawnHealths[index], pawnAttacks[index], controller.getIsMaster() ? Pawn.PawnOwner.MASTER : Pawn.PawnOwner.SLAVE);
                controller.spawnPawn(pawn);
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
                if (!controller.getIsMaster()){
                    laneIndex = controller.getMatch().getLanes().length - laneIndex - 1;
                }
                Lane laneClicked = controller.getMatch().getLanes()[laneIndex];
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
