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
import com.ttradvisor.app.classes.GameState;
import com.ttradvisor.app.classes.InputTurnController;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.Turn;
import com.ttradvisor.app.screens.TitleScreen;

/**
 * Copied in as example code to modify
 *
 */
public class TTRAdvisorApp extends Game {
	static public Skin skin;
	public int numPlayers;
	public ArrayList<Colors.player> turnOrder;
	public Colors.player userColor;
	
	public InputTurnController turnInput;
	public GameState gameState;
	
	@Override
	public void create () {
		turnInput = new InputTurnController(gameState);
		gameState = new GameState(new ArrayList<Player>(), new Board("cities.txt"), new ArrayList<Turn>());
		skin = new Skin(Gdx.files.internal("ui skin/glassy-ui.json"));
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
