package com.mygdx.game.views;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.PWND;
import com.mygdx.game.controllers.MatchSearchController;
import com.mygdx.game.views.UI.TextElement;

public class MatchSearchView extends BaseView {

    private TextElement searchingText;
    private Table table;

    private MatchSearchController controller;

    public MatchSearchView(){
        controller = new MatchSearchController(this);
    }

    @Override
    public void show() {
        super.show();

        // Searching text
        searchingText = new TextElement("Searching...");

        // Table
        table = new Table();
        table.setFillParent(true);

        // Add searching text to table
        table.add(searchingText.getActor());

        // Add table to actors
        PWND.viewManager.peek().getStage().addActor(table);

        // Start searching
        controller.startSearch();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // Pass render call to controller
        controller.update(delta);
    }

}
