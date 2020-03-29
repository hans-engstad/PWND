package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.UUID;

public abstract class BaseView implements Screen {

    protected Stage stage;
    protected Preferences playerPrefs;

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        playerPrefs = Gdx.app.getPreferences("player");

        // Set username and id if not already set
        if (!playerPrefs.contains("username")){
            playerPrefs.putString("username", "<username>");
        }
        if (!playerPrefs.contains("id")){
            playerPrefs.putString("id", UUID.randomUUID().toString());
        }

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Act
        stage.act(delta);

        // Draw stage
        stage.draw();
    }

    public Stage getStage(){
        return this.stage;
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
