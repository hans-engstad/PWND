package com.mygdx.game.controllers;

import com.mygdx.game.PWND;
import com.mygdx.game.interfaces.ICreateCallback;
import com.mygdx.game.interfaces.IRetrieveCallback;
import com.mygdx.game.interfaces.IUpdateCallback;
import com.mygdx.game.models.Match;
import com.mygdx.game.models.Player;
import com.mygdx.game.views.MatchSearchView;
import com.mygdx.game.views.MatchView;
import com.mygdx.game.views.MenuView;

import java.util.Map;

public class MatchSearchController extends BaseController {


    Player thisPlayer;
    Match match;

    MatchSearchView view;

    Boolean joinMatch;

    public MatchSearchController(MatchSearchView view) {
        this.view = view;

        String username = playerPrefs.getString("username");
        String id = playerPrefs.getString("id");
        thisPlayer = new Player(username, id);

        joinMatch = false;
    }


    public void createMatch(){
        // Create new match with this player as master
        match = new Match(thisPlayer);
        PWND.firebase.addMatch(match, new ICreateCallback() {
            @Override
            public void onSuccess(String id) {
                MatchSearchController.this.match.setId(id);
            }

            @Override
            public void onFail(Exception e) {
                System.out.println(e);
            }

            @Override
            public void onChange(Map data) {

                match.update(data);

                if (match.getStatus() == Match.Status.STARTING){
                    // Join match by navigating to match view
                    joinMatch = true;
                }
            }

            @Override
            public void onDestroy() {

            }
        });
    }

    public void startSearch(){

        PWND.firebase.retrieveOpenMatches(new IRetrieveCallback() {
            @Override
            public void onSuccess(Map data) {
                if (data.size() > 0){
                    // Join first open match found
                    String key = (String) data.keySet().iterator().next();
                    Map value = (Map) data.get(key);
                    value.put("id", key);

                    MatchSearchController.this.match = new Match(value);
                    MatchSearchController.this.match.setSlavePlayer(MatchSearchController.this.thisPlayer);
                    MatchSearchController.this.match.setStatus(Match.Status.STARTING);

                    // Update match in firebase database
                    PWND.firebase.updateMatch(MatchSearchController.this.match, new IUpdateCallback() {
                        @Override
                        public void onSuccess() {
                            // Join match by setting join flag to true
                            joinMatch = true;
                        }

                        @Override
                        public void onFail(Exception e) {

                        }
                    });
                }
                else{
                    // No open matches, create new match
                    createMatch();
                }
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    public void update(float delta){
        if (joinMatch){
            PWND.viewManager.set(new MatchView(match.getId()));
        }
    }
}
