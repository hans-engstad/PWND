package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.interfaces.IFirebase;
import com.mygdx.game.views.MenuView;
import com.mygdx.game.views.ViewManager;


public class PWND extends ApplicationAdapter {

	public static IFirebase firebase;
	public static ViewManager viewManager;


	public PWND(IFirebase firebase){
		this.firebase = firebase;

	}

	@Override
	public void create () {
		viewManager = new ViewManager();
		viewManager.push(new MenuView());
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		viewManager.render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
		viewManager.dispose();
	}
}
