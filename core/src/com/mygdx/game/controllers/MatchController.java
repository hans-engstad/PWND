package com.mygdx.game.controllers;

import com.mygdx.game.PWND;
import com.mygdx.game.interfaces.IMatchChange;
import com.mygdx.game.interfaces.IUpdateCallback;
import com.mygdx.game.models.Cell;
import com.mygdx.game.models.Lane;
import com.mygdx.game.models.Match;
import com.mygdx.game.models.Pawn;
import com.mygdx.game.models.actions.IAction;
import com.mygdx.game.models.actions.SpawnPawnAction;
import com.mygdx.game.models.pawns.BasicPawn;
import com.mygdx.game.views.MatchView;

public class MatchController extends BaseController {

    private final float TICK_INTERVAL = 1000f;   // Time between ticks in ms

    private MatchView view;
    private Boolean isMaster;   // Is this user the master user?
    private Lane selectedLane;
    private Pawn selectedPawn;


    private float timeSinceLastTick;    // Time in ms since last tick

    private Match match;

    public MatchController (MatchView view, Match match){
        super();
        this.view = view;
        this.match = match;
        this.timeSinceLastTick = 0f;
        this.isMaster = match.getMasterPlayer().getId().equals(getThisPlayer().getId());

        PWND.firebase.addMatchChangeListener(match, new IMatchChange(){
            @Override
            public void onChange(Match newMatch){
                onMatchUpdate(newMatch);
            }
        });

        // Start match if this is master player
        if (isMaster){
            match.start();

            // [TEST] - Add pawn to test rendering
            match.getLanes()[0].getCell(1).addPawn(new BasicPawn());

            // This is the master user, start match
            PWND.firebase.updateMatch(match);
        }
    }

    public void onMatchUpdate(Match newMatch){
        // Check if tick incremented in new match
        if (newMatch.getTick() > match.getTick()){
            // New tick started
            match.performPendingActions();
        }
        match = newMatch;
        System.out.println("New match: " + match.toString());
    }

    public void spawnPawn(Pawn pawn, Lane lane){

        if (pawn == null || lane == null){
            System.out.println("Can't spawn pawn, given pawn and lane can't be null. ");
        }

        // Find first cell in lane
        int cellIndex = isMaster ? 0 : lane.getCells().length - 1;
        int laneIndex = -1;
        for (Lane l : match.getLanes()){
            laneIndex++;
            if (l == lane){
                break;
            }
        }

        SpawnPawnAction action = new SpawnPawnAction(pawn, laneIndex, cellIndex);
        match.addAction(action);

        PWND.firebase.updateMatch(match);
    }

    public void setSelectedLane(Lane lane){
        selectedLane = lane;
    }

    public Lane getSelectedLane(){
        return selectedLane;
    }

    public void setSelectedPawn(Pawn pawn){
        selectedPawn = pawn;
    }

    public Pawn getSelectedPawn(){
        return selectedPawn;
    }

    public void render(float delta){
        timeSinceLastTick += delta;
        if(isMaster && timeSinceLastTick > TICK_INTERVAL){
            match.setTick(match.getTick() + 1);
            timeSinceLastTick = 0f;
            PWND.firebase.updateMatch(match);
        }


    }



}
