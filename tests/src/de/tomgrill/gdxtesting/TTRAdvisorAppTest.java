// Andrey's note - another attempt at running JUnit with the OpenGL context active for our app.
// It did not work.

package de.tomgrill.gdxtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ttradvisor.app.TTRAdvisorApp;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.DestinationTicketList;
import com.ttradvisor.app.classes.GameState;
import com.ttradvisor.app.classes.HistoryController;
import com.ttradvisor.app.classes.InputTurnController;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.Turn;
import com.ttradvisor.app.screens.TitleScreen;

/**
 * Copied in as example code to modify
 *
 */
public class TTRAdvisorAppTest extends TTRAdvisorApp {
	
	// FOR TESTING
	private GdxTestRunnerTrial testRunner;
	
	static public Skin skin;
	public Colors.player userColor;
	
	public InputTurnController turnInput;
	public HistoryController hist;
	public GameState gameState;
	
	public TTRAdvisorAppTest(GdxTestRunnerTrial testRunner) {
		this.testRunner = testRunner;
	}

	@Override
	public void create () {
		gameState = new GameState(new ArrayList<Player>(), new Board("cities.txt"),
				new DestinationTicketList("destinations.txt"), new ArrayList<Turn>());
		turnInput = new InputTurnController(gameState);
		hist = new HistoryController(gameState);
		skin = new Skin(Gdx.files.internal("ui skin/glassy-ui.json"));
		this.setScreen(new TitleScreen(this));
	}

	@Override
	public void render () {
		// FOR TESTING
		synchronized (testRunner.invokeInRender) {
			for (Map.Entry<FrameworkMethod, RunNotifier> each : testRunner.invokeInRender.entrySet()) {
				testRunner.runChild(each.getKey(), each.getValue());
			}
			testRunner.invokeInRender.clear();
		}
		
		super.render();
	}
	
	@Override
	public void dispose () {
		skin.dispose();
	}

}
