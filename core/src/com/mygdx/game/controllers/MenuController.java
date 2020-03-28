package com.mygdx.game.controllers;

import com.mygdx.game.PWND;
import com.mygdx.game.views.MatchSearchView;

public class MenuController extends BaseController {

    public void onStartClick(){
        // Navigate to match search view
        PWND.viewManager.set(new MatchSearchView());
    }


    public void setUsername (String text){
        playerPrefs.putString("username", text);
        playerPrefs.flush();
    }

    public String getUsername(){
        return playerPrefs.getString("username");
    }

}
