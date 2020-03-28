package com.mygdx.game.interfaces;

import com.mygdx.game.models.Match;

public interface IFirebase {

    void addMatch(Match match, ICreateCallback callback);

    void updateMatch(Match match, IUpdateCallback callback);

    void retrieveOpenMatches(IRetrieveCallback callback);

    void clearListeners();

}
