package com.mygdx.game.interfaces;

import com.mygdx.game.models.Match;

import java.util.EventListener;

public interface IFirebase {

    void addMatch(Match match, ICreateCallback callback);

    void updateMatch(Match match, IUpdateCallback callback);
    void updateMatch(Match match);

    void retrieveOpenMatches(IRetrieveCallback callback);

    void addMatchChangeListener(Match match, IMatchChange callback);


}
