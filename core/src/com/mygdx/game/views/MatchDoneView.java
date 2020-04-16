package com.mygdx.game.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.PWND;
import com.mygdx.game.controllers.MatchDoneController;
import com.mygdx.game.controllers.MenuController;
import com.mygdx.game.views.UI.ButtonElement;
import com.mygdx.game.views.UI.TextElement;

public class MatchDoneView extends BaseView {

    private MatchDoneController controller;

    private TextElement titleText;
    private ButtonElement menuButton;
    private Table table;


    public MatchDoneView(MatchDoneController.Status status){
        controller = new MatchDoneController(status);
    }

    @Override
    public void show() {
        super.show();

        // Create table that will hold our elements
        table = new Table();
        table.setFillParent(true);

        // Create title element
        titleText = new TextElement(controller.getTitle());
        table.add(titleText.getActor()).row();

        // Create menu button element
        menuButton = new ButtonElement("MENU", new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Navigate to menu view
                PWND.viewManager.set(new MenuView());
            }
        });
        table.add(menuButton.getActor());

        // Add table actor to stage
        PWND.viewManager.peek().getStage().addActor(table);
    }

}
