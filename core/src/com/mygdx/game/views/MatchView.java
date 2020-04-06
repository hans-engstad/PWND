package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
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
    private final int SPAWN_PAWN_BUTTON_WIDTH = 300;

    private MatchController controller;

    private int screenWidth;
    private int screenHeight;

    private int cellWidth;
    private int cellHeight;

    private Boolean setupComplete;

    private int menuButtonWidth;
    private int menuButtonHeight;
    private int menuButtonx;
    private int menuButtony;



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
        if (!setupComplete && controller.getMatch().getStatus() == Match.Status.DONE) {
            drawMatchDone();
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
        Lane[] lanes = controller.getMatch().getLanes();

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
        Lane[] lanes = controller.getMatch().getLanes();

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
                controller.spawnBasicPawn();
                // controller.setSelectedPawn(new BasicPawn());
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

    void drawMatchDone(){
        //display Winner/Loser
        // if player=loser
        //ImageElement image = new ImageElement("victory.png",screenWidth/2,screenHeight/2,0,0);
        // if player=winner
        ImageElement GameOver = new ImageElement("GameOver.png",screenWidth/2,screenHeight/2,0,0);
        //display back to menu button
        menuButtonWidth = screenWidth/2;
        menuButtonHeight = screenHeight/2;
        menuButtonx = screenWidth/4;
        menuButtony = screenHeight/4;
        ImageElement MenuButton = new ImageElement("ButtonMainmenu.png",menuButtonWidth,menuButtonHeight,menuButtonx,menuButtony);
        //check if button has been pressed
        if (Gdx.input.justTouched()){
            int x = Gdx.input.getX();
            int y = screenHeight - Gdx.input.getY();

            if (x >= menuButtonx+menuButtonWidth && x <= menuButtonx && y >= menuButtony-menuButtonHeight && y <= menuButtonHeight ){
                //back to menu
            }

        }

    }
}
