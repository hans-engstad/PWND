package com.mygdx.game.models;

import com.mygdx.game.PWND;
import com.mygdx.game.interfaces.ICreateCallback;
import com.mygdx.game.interfaces.IMatchFoundCallback;
import com.mygdx.game.interfaces.IRetrieveCallback;
import com.mygdx.game.interfaces.IUpdateCallback;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class MatchMaker {

    private Match match;

    private Player player;

    private IMatchFoundCallback matchFoundCallback;

    public MatchMaker(){

        this.player = new Player("username2", UUID.randomUUID().toString());

        // Start searching
        // if no open matches, then create new match
        // searchOpenMatches();
    }

    public void createMatch(){
        // Create new match with this player as master
        match = new Match(player);
        PWND.firebase.addMatch(match, new ICreateCallback() {
            @Override
            public void onSuccess(String id) {
                MatchMaker.this.match.setId(id);
            }

            @Override
            public void onFail(Exception e) {
                System.out.println(e);
            }

            @Override
            public void onChange(Map data) {


                match.update(data);

                if (match.getStatus() == Match.Status.OPEN
                        && match.getSlavePlayer() != null
                        && match.getMasterPlayer() == MatchMaker.this.player
                ){
                    MatchMaker.this.startMatch();
                }

            }

            @Override
            public void onDestroy() {

            }
        });
    }

    public void startMatch(){
        match.start();

        System.out.println("MATCHH\n" + match.toString());

        PWND.firebase.updateMatch(match, new IUpdateCallback() {
            @Override
            public void onSuccess() {
                System.out.println("MatchView started!!!");
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    public void searchOpenMatches(IMatchFoundCallback matchFoundCallback){
        this.matchFoundCallback = matchFoundCallback;
        PWND.firebase.retrieveOpenMatches(new IRetrieveCallback() {
            @Override
            public void onSuccess(Map data) {
                if (data.size() > 0){
                    // Join first open match found
                    String key = (String) data.keySet().iterator().next();
                    Map value = (Map) data.get(key);
                    value.put("id", key);

                    MatchMaker.this.match = new Match(value);
                    MatchMaker.this.match.setSlavePlayer(MatchMaker.this.player);

                    MatchMaker.this.matchFoundCallback.onMatchFound(MatchMaker.this.match);


                    MatchMaker.this.startMatch();
                }
                else{
                    // No open matches, create new match
                    createMatch();
                }


                System.out.println("MATCHES\n" + data.toString());
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }



}
