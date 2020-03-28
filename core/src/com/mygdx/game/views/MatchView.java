package com.mygdx.game.views;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.controllers.MatchController;
import com.mygdx.game.models.Match;
import com.mygdx.game.views.UI.Text;

public class MatchView extends BaseView {

    private MatchController controller;

    private Match match;

    public MatchView (Match match){
        this.match = match;
    }

    @Override
    public void show(){
        // super.show();
        //controller = new MatchController(match);

        System.out.println("MATCH STARTED:" + match.toString());
    }


}
