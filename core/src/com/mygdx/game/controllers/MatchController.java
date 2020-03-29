package com.mygdx.game.controllers;

import com.mygdx.game.PWND;
import com.mygdx.game.interfaces.IMatchChange;
import com.mygdx.game.interfaces.IUpdateCallback;
import com.mygdx.game.models.Match;
import com.mygdx.game.views.MatchView;

public class MatchController extends BaseController {

    private MatchView view;
    private Match match;
    private Boolean isMaster;   // Is this user the master user?

    public MatchController (MatchView view, Match match){
        super();
        this.view = view;
        this.match = match;
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
            // This is the master user, start match
            PWND.firebase.updateMatch(match);
        }
    }

    public void onMatchUpdate(Match newMatch){
        match = newMatch;
        System.out.println("New match: " + match.toString());
    }



}
