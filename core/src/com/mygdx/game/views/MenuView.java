package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.controllers.MenuController;
import com.mygdx.game.views.UI.Button;
import com.mygdx.game.views.UI.Text;


public class MenuView extends BaseView {

    private MenuController controller;

    public MenuView(){
        controller = new MenuController();
    }

    @Override
    public void render(float delta) {

        // Username text
        Text usernameText = new Text(atlas);
        usernameText.setText(controller.getUsername());

        // Start button
        Button startButton = new Button(atlas);
        startButton.setText("START");
        startButton.get().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onStartClick();
            }
        });

        // Edit username button
        Button editUsernameButton = new Button(atlas);
        editUsernameButton.setText("Edit username");
        editUsernameButton.get().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.getTextInput(new Input.TextInputListener() {
                    @Override
                    public void input (String text) {
                        MenuView.this.controller.setUsername(text);
                        MenuView.this.stage.clear();
                    }

                    @Override
                    public void canceled () {}

                }, "Set username", controller.getUsername(), "username...");
            }
        });

        Table table = new Table();

        table.add(usernameText.get());
        table.row();
        table.add(editUsernameButton.get());
        table.row();
        table.add(startButton.get());
        table.setFillParent(true);

        stage.addActor(table);
        super.render(delta);
    }


}
