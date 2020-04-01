package com.mygdx.game.controllers;

import com.mygdx.game.PWND;
import com.mygdx.game.interfaces.IMatchChange;
import com.mygdx.game.interfaces.IRetrieveCallback;
import com.mygdx.game.models.Lane;
import com.mygdx.game.models.Match;
import com.mygdx.game.models.Pawn;
import com.mygdx.game.models.actions.SpawnPawnAction;
import com.mygdx.game.models.pawns.BasicPawn;
import com.mygdx.game.views.MatchView;

import java.util.Map;

public class MatchController extends BaseController {

    private final float TICK_INTERVAL = 1000f;   // Time between ticks in ms

    private MatchView view;
    private Boolean isMaster;   // Is this user the master user?
    private Lane selectedLane;
    private Pawn selectedPawn;


    private float timeSinceLastTick;    // Time in ms since last tick

    private Match match;
    private String matchKey;

    public MatchController (MatchView view, String matchKey){
        super();
        this.view = view;
        this.matchKey = matchKey;
        this.timeSinceLastTick = 0f;

        fetchMatch();
    }

    void fetchMatch(){

        PWND.firebase.retrieveMatch(matchKey, new IRetrieveCallback() {
            @Override
            public void onSuccess(Map data) {
                match = new Match(data);
                isMaster = match.getMasterPlayer().getId().equals(getThisPlayer().getId());

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
                    match.getLanes()[0].getCell(1).addPawn(new BasicPawn(Pawn.PawnOwner.MASTER));

                    // This is the master user, start match
                    PWND.firebase.updateMatch(match);
                }
            }

            @Override
            public void onFail(Exception e) {}
        });
    }

    public void onMatchUpdate(Match newMatch){
        // Check if tick incremented in new match
        match = newMatch;
    }

    public Match getMatch(){
        return match;
    }

    public void spawnBasicPawn(){
        // TODO: Set lane selection in another way?

        if (selectedLane == null){
            System.out.println("Can't spawn pawn, selected lane can't be null. ");
            return;
        }

        if (match.getLanes() == null){
            System.out.println("Can't spawn pawn, match do not have any lanes!");
            System.out.println("Match" + match.toString());
            return;
        }


        int cellIndex = isMaster ? 0 : selectedLane.getCells().length - 1;
        int laneIndex = -1;
        for (Lane l : match.getLanes()){
            laneIndex++;
            if (l.getKey() == selectedLane.getKey()){
                break;
            }
        }

        Pawn pawn = new BasicPawn(isMaster ? Pawn.PawnOwner.MASTER : Pawn.PawnOwner.SLAVE);
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
            match.performPendingActions();
            timeSinceLastTick = 0f;
            PWND.firebase.updateMatch(match);
        }


    }



}
