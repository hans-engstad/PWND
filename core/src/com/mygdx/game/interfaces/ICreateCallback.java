package com.mygdx.game.interfaces;

import java.util.Map;

public interface ICreateCallback {

    void onSuccess(String id);

    void onFail(Exception e);

    void onChange(Map data);

    void onDestroy();

}
