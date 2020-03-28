package com.mygdx.game.interfaces;

import java.util.Map;

public interface IRetrieveCallback {
    void onSuccess(Map data);

    void onFail(Exception e);
}
