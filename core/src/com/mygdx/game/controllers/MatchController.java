package com.mygdx.game.controllers;

import com.mygdx.game.models.Match;

public class MatchController extends BaseController {

    private Match match;

    public MatchController (Match match){
        super();
        this.match = match;
        System.out.println("Match have started: " + match.toString());
    }



}
