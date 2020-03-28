package com.mygdx.game.views;

import com.mygdx.game.PWND;
import com.mygdx.game.views.BaseView;

import java.util.Stack;

public class ViewManager {

    private Stack<BaseView> views;

    public ViewManager(){
        views = new Stack<BaseView>();
    }

    public void push(BaseView view){
        views.push(view);
        view.show();
    }

    public void pop(){
        views.pop().dispose();
    }

    public void set(BaseView view){
        // Clear event listeners
        PWND.firebase.clearListeners();
        System.out.println("*******SETTING NEW VIEW----");
        views.pop().dispose();
        System.out.println("*1");
        views.push(view);
        System.out.println("*2");
        view.show();
        System.out.println("*3");
    }

    public void render(float dt){
        views.peek().render(dt);
    }

    public void dispose () {
        // Dispose all views in stack
        for (BaseView v : views){
            v.dispose();
        }
    }

}
