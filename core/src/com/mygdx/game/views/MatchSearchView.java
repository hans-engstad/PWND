package com.mygdx.game.views;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.controllers.MatchSearchController;
import com.mygdx.game.models.Match;
import com.mygdx.game.views.UI.Text;

public class MatchSearchView extends BaseView {

    private Text searchingText;
    private Table table;

    private MatchSearchController controller;

    public MatchSearchView(){
        controller = new MatchSearchController(this);
    }

    @Override
    public void show() {
        super.show();

        searchingText = new Text(atlas);
        table = new Table();

        searchingText.setText("Searching...");
        table.add(searchingText.get());
        table.setFillParent(true);

        stage.addActor(table);

        controller.startSearch();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

}
