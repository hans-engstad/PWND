package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.PWND;
import com.mygdx.game.controllers.MenuController;
import com.mygdx.game.views.UI.ButtonElement;
import com.mygdx.game.views.UI.TextElement;


public class MenuView extends BaseView {

    private MenuController controller;

    private TextElement usernameText;
    private ButtonElement startButton;
    private ButtonElement editUsernameButton;
    private Table table;

    public MenuView(){
        controller = new MenuController();
    }

    @Override
    public void show() {
        super.show();

        usernameText = new TextElement(controller.getUsername());

        startButton = new ButtonElement("START", new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("CLICKED***");
                controller.onStartClick();
            }
        });

        editUsernameButton = new ButtonElement("Edit username", new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.input.getTextInput(new Input.TextInputListener() {
                    @Override
                    public void input (String text) {
                        // Save new username
                        controller.setUsername(text);

                        // Update UI
                        usernameText.setText(text);
                        usernameText.refresh();
                    }

                    @Override
                    public void canceled () {}

                }, "Set username", controller.getUsername(), "username...");
            }
        });

        table = new Table();
        table.setFillParent(true);
        table.add(usernameText.getActor()).row();
        table.add(editUsernameButton.getActor()).row();
        table.add(startButton.getActor());

        PWND.viewManager.peek().getStage().addActor(table);

    }


}
