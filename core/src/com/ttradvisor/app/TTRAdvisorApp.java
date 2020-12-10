package com.ttradvisor.app;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.DestinationTicketList;
import com.ttradvisor.app.classes.GameState;
import com.ttradvisor.app.classes.HistoryController;
import com.ttradvisor.app.classes.InputTurnController;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.Recommender;
import com.ttradvisor.app.classes.Turn;
import com.ttradvisor.app.screens.TitleScreen;

/**
 * Copied in as example code to modify
 *
 */
public class TTRAdvisorApp extends Game {
	static public Skin skin;
	public Colors.player userColor;
	
	public Recommender rec;
	public InputTurnController turnInput;
	public HistoryController hist;
	public GameState gameState;
	
	@Override
	public void create () {
		gameState = new GameState(userColor, new ArrayList<Player>(), new Board("cities.txt"),
				new DestinationTicketList("destinations.txt"), new ArrayList<Turn>());
		turnInput = new InputTurnController(gameState);
		hist = new HistoryController(gameState);
//		skin = new Skin(Gdx.files.internal("ui skin/glassy-ui.json"));
		Skin tempSkin = new Skin(Gdx.files.internal("ttr_ui_skin_plus/ttr_skin.json"));
//		skin = new Skin(Gdx.files.internal("ttr_ui_skin2/train_cards_included.json"));
		tempSkin.getFont("font").getData().setScale(Gdx.graphics.getHeight()/720, Gdx.graphics.getHeight()/720);
		tempSkin.getFont("font-big").getData().setScale(Gdx.graphics.getHeight()/720, Gdx.graphics.getHeight()/720);
		skin = tempSkin;
		this.setScreen(new TitleScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		skin.dispose();
	}
}
