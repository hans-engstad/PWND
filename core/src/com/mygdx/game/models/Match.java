package com.mygdx.game.models;

import com.mygdx.game.models.actions.BaseAction;
import com.mygdx.game.models.actions.IAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Match {

    public enum Status {
        OPEN,       // Open but not yet started
        STARTING,   // Waiting for both players to join
        STARTED,    // Game is started
        MASTER_WON, // Master player won the match
        SLAVE_WON,  // Slave player won the match
        TIE         // Tie between master and slave player
    }

    private Lane[] lanes;

    private String id;


    private Player masterPlayer;
    private Player slavePlayer;

    private Status status;

    private int tick;

    private Stack<IAction> pendingActions;

    public Match(Player masterPlayer){
        this.masterPlayer = masterPlayer;
        this.status = Status.OPEN;
        pendingActions = new Stack<>();
    }

    public Match(Map data){
        this.id = (String) data.get("id");
        this.masterPlayer = new Player((Map) data.get("masterPlayer"));
        pendingActions = new Stack<>();

        this.status = Status.valueOf((String) data.get("status"));

        if (data.containsKey("slavePlayer")){
            this.slavePlayer = new Player((Map) data.get("slavePlayer"));
        }

        if (data.containsKey("masterPlayer")){
            this.slavePlayer = new Player((Map) data.get("masterPlayer"));
        }

        if (data.containsKey("pendingActions")){
            for (Object a : (ArrayList) data.get("pendingActions")){
                pendingActions.push(BaseAction.deserialize((Map) a));
            }
        }

        if (data.containsKey("lanes")){
            this.lanes = new Lane[((Map) data.get("lanes")).size()];

            int i = 0;
            for (Object laneData : ((Map) data.get("lanes")).values()){
                this.lanes[i] = new Lane((Map) laneData);
                i++;
            }
        }

    }

    public void start(){
        lanes = new Lane[]{
                new Lane("lane0"),
                new Lane("lane1"),
                new Lane("lane2"),
                new Lane("lane3")
        };
        status = Status.STARTED;
        tick = 1;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public void setSlavePlayer(Map data){
        String username = (String) data.get("username");
        String id = (String) data.get("id");

        this.slavePlayer = new Player(username, id);
    }

    public void setSlavePlayer(Player player){
        this.slavePlayer = player;
    }

    public Player getSlavePlayer(){
        return this.slavePlayer;
    }

    public Player getMasterPlayer(){
        return this.masterPlayer;
    }

    public Status getStatus(){
        return this.status;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public void setTick(int tick){
        this.tick = tick;
    }

    public int getTick(){
        return tick;
    }

    public void update(Map data){
        // Slave player
        if (slavePlayer == null && data.containsKey("slavePlayer")){
            setSlavePlayer((Map) data.get("slavePlayer"));
        }

        // Status
        if (data.containsKey("status")){
            Status newStatus = Status.valueOf((String) data.get("status"));
            System.out.println("New status: " + newStatus.toString());
            if (newStatus != null && status != newStatus){
                status = newStatus;
            }
        }

    }

    public void performPendingActions(){
        for (int i = 0; i < pendingActions.size(); i++){
            pendingActions.pop().perform(this);
        }
    }

    public void addAction (IAction action){
        if (pendingActions == null){
            pendingActions = new Stack<>();
        }
        pendingActions.push(action);
    }

    public Lane[] getLanes() {
        return lanes;
    }

    public Lane[] getLanesFlipped(){
        Lane[] lanes = new Lane[this.lanes.length];
        for (int i = 0; i < lanes.length; i++){
            // Add flipped cells to lane
            lanes[i] = this.lanes[lanes.length - i - 1];
        }
        return lanes;
    }

    public Map serialize(){
        Map<String, Object> match = new HashMap<>();

        if (status != null){
            match.put("status", status.toString());
        }

        if (id != null){
            match.put("id", id);
        }

        if (masterPlayer != null){
            match.put("masterPlayer", masterPlayer.serialize());
        }

        if (slavePlayer != null){
            match.put("slavePlayer", slavePlayer.serialize());
        }

        List<Object> actions = new ArrayList<>();
        if (pendingActions != null){
            for (int i = 0; i < pendingActions.size(); i++){
                actions.add(pendingActions.get(i).serialize());
            }
        }
        match.put("pendingActions", actions);

        match.put("tick", tick);


        if (lanes != null){
            Map<String, Object> lanesMap = new HashMap<>();
            for (Lane l: lanes) {
                lanesMap.put(l.getKey(), l.serialize());
            }
            match.put("lanes", lanesMap);
        }

        return match;
    }

    public Boolean masterWon (){
        for (Lane lane : lanes){
            Cell lastCell = lane.getCells()[lane.getCells().length - 1];
            if (lastCell.getPawn() != null && lastCell.getPawn().owner == Pawn.PawnOwner.MASTER){
                // Master won!
                return true;
            }
        }
        return false;
    }

    public Boolean slaveWon(){
        for (Lane lane : lanes){
            Cell lastCell = lane.getCells()[0];
            if (lastCell.getPawn() != null && lastCell.getPawn().owner == Pawn.PawnOwner.SLAVE){
                // Slave won!
                return true;
            }
        }
        return false;
    }

    public void movePawnsForward(){
        // Perform fights
        performFights();

        // Move master pawns forward, after fight each master pawn should have an available cell in front
        moveMasterPawnsForward();

        // Perform fights again after master pawns moved forward
        performFights();

        // Move slave pawns forward
        moveSlavePawnsForward();
    }

    private void moveMasterPawnsForward (){
        for (Lane lane : lanes){
            Cell[] cells = lane.getCells();

            // Move master pawns forward
            // Note: Iterating backwards to prevent pawns moving multiple times
            for (int i = cells.length - 1; i >= 0; i--){
                Cell cell = cells[i];
                Pawn pawn = cell.getPawn();
                if (pawn != null && pawn.owner == Pawn.PawnOwner.MASTER){
                    cell.removePawn();
                    cells[i + 1].addPawn(pawn);
                }
            }
        }
    }

    private void moveSlavePawnsForward(){
        for (Lane lane : lanes){
            Cell[] cells = lane.getCells();

            // Move slave pawns forward
            for (int i = 0; i < cells.length; i++){
                Cell cell = cells[i];
                Pawn pawn = cell.getPawn();
                if (pawn != null && pawn.owner == Pawn.PawnOwner.SLAVE){
                    cell.removePawn();
                    cells[i - 1].addPawn(pawn);
                }
            }
        }
    }

    private void performFights(){
        // Perform fights for all master pawns which have slave pawn in front
        for (Lane lane : lanes){
            Cell[] cells = lane.getCells();
            for (int i = 0; i < cells.length; i++){
                Pawn currentPawn = cells[i].getPawn();
                Pawn pawnInFront = i == cells.length - 1 ? null : cells[i+1].getPawn();
                if (currentPawn != null && currentPawn.isMaster() && pawnInFront != null && !pawnInFront.isMaster()){
                    // Master pawn have slave pawn in front. Fight!
                    while(currentPawn.getHealth() > 0 && pawnInFront.getHealth() > 0){
                        currentPawn.decreaseHealth(pawnInFront.getAttack());
                        pawnInFront.decreaseHealth(currentPawn.getAttack());
                    }

                    // Remove pawns with 0 health ("dead pawns")
                    if (currentPawn.getHealth() <= 0){
                        cells[i].removePawn();
                    }
                    if (pawnInFront.getHealth() <= 0){
                        cells[i+1].removePawn();
                    }
                }
            }
        }
    }


    public String toString(){
        String s = "\n-------------------\n";
        s += "\tid:\t\t\t\t\t\t" + id;
        s += "\n\tstatus:\t\t\t\t\t" + status.toString();
        s += "\n\tmasterPlayerID:\t\t\t" + masterPlayer.getId();
        s += "\n\tmasterPlayerUsername:\t" + masterPlayer.getUsername();
        s += "\n\tslavePlayerID:\t\t\t" + slavePlayer.getUsername();
        s += "\n\tslavePlayerUsername:\t" + slavePlayer.getUsername();
        if (lanes != null){
            s += "\n\tmatches:\t" + lanes.toString();
        }
        else{
            s += "\n\tmatches:\tNULL";
        }
        s += "\n--------------------\n";

        return s;
    }

}
