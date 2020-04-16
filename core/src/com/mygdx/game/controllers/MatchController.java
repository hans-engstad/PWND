package com.mygdx.game.controllers;

import com.mygdx.game.PWND;
import com.mygdx.game.interfaces.IMatchChange;
import com.mygdx.game.interfaces.IRetrieveCallback;
import com.mygdx.game.models.Lane;
import com.mygdx.game.models.Match;
import com.mygdx.game.models.Pawn;
import com.mygdx.game.models.actions.SpawnPawnAction;
import com.mygdx.game.views.MatchDoneView;
import com.mygdx.game.views.MatchView;

import java.util.Map;

public class MatchController extends BaseController {

    private final float TICK_INTERVAL = 5f;   // Time between ticks in seconds

    private MatchView view;
    private Boolean isMaster;   // Is this user the master user?
    private Lane selectedLane;


    private float timeSinceLastTick;    // Time in seconds since last tick

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
                    // match.getLanes()[0].getCell(1).addPawn(new BasicPawn(Pawn.PawnOwner.MASTER));

                    // This is the master user, start match
                    PWND.firebase.updateMatch(match);
                }
            }

            @Override
            public void onFail(Exception e) {}
        });
    }

    public void onMatchUpdate(Match newMatch){
        match = newMatch;
    }

    public Match getMatch(){
        return match;
    }

    public void spawnPawn(Pawn pawn){
        if (selectedLane == null){
            System.out.println("Can't spawn pawn, selected lane can't be null. ");
            return;
        }

        if (match.getLanes() == null){
            System.out.println("Can't spawn pawn, match do not have any lanes!");
            System.out.println("Match" + match.toString());
            return;
        }


        int cellIndex = isMaster ? 0 : match.getLanes()[0].getCells().length - 1;
        int laneIndex = -1;
        for (Lane l : match.getLanes()){
            laneIndex++;
            if (l.getKey().equals(selectedLane.getKey())){
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

    public Boolean getIsMaster(){ return this.isMaster; }

    public void render(float delta){
        updateMatchDone();

        timeSinceLastTick += delta;
        if(isMaster && timeSinceLastTick > TICK_INTERVAL && match.getStatus() == Match.Status.STARTED){
            incrementTick();
        }
    }

    private void updateMatchDone (){
        Match.Status status = match.getStatus();

        if ((status == Match.Status.MASTER_WON && isMaster) || (status == Match.Status.SLAVE_WON && !isMaster)) {
            // This player won
            PWND.viewManager.set(new MatchDoneView(MatchDoneController.Status.WON));
            return;
        }
        else if ((status == Match.Status.MASTER_WON && !isMaster) || (status == Match.Status.SLAVE_WON && isMaster)){
            // This player lost
            PWND.viewManager.set(new MatchDoneView(MatchDoneController.Status.LOST));
            return;
        }
        else if (status == Match.Status.TIE) {
            // Tie
            PWND.viewManager.set(new MatchDoneView(MatchDoneController.Status.TIE));
            return;
        }
    }

    private void incrementTick(){
        // Increment tick
        match.setTick(match.getTick() + 1);

        // Move pawns one step forward
        match.movePawnsForward();

        // Perform actions (Spawning of pawns)
        match.performPendingActions();

        // Check if master/slave won the match
        if (match.masterWon() && match.slaveWon()){
            // TIE
            match.setStatus(Match.Status.TIE);
        }
        else if (match.masterWon()){
            // MASTER WON
            match.setStatus(Match.Status.MASTER_WON);
        }
        else if (match.slaveWon()) {
            // SLAVE WON
            match.setStatus(Match.Status.SLAVE_WON);
        }

        // Reset tick timer
        timeSinceLastTick = 0f;

        // Update match in firebase database
        PWND.firebase.updateMatch(match);
    }



}
