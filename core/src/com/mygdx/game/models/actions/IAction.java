package com.mygdx.game.models.actions;

import com.mygdx.game.models.Match;

import java.util.Map;

public interface IAction {

    void perform(Match match);

    Map<String, Object> serialize();

}
