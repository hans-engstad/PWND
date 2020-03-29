package com.mygdx.game.views;

import com.badlogic.gdx.scenes.scene2d.Stage;
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
        views.pop().dispose();
        views.push(view);
        view.show();
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

    public BaseView peek(){
        return views.peek();
    }

}
