package de.tomgrill.gdxtesting.examples;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.GameState;
import com.ttradvisor.app.classes.InputTurnController;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.Turn;

import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

@RunWith(GdxTestRunner.class)
public class TestInputTurnController {
	
	private GameState initTestGameState() {
		
		Player testP1 = new Player(Colors.player.RED);
		Player testP2 = new Player(Colors.player.BLUE);
		Player testP3 = new Player(Colors.player.GREEN);
		ArrayList<Player> testPlayerList = new ArrayList<Player>();
		testPlayerList.add(testP1);
		testPlayerList.add(testP2);
		testPlayerList.add(testP3);
		return new GameState(testPlayerList, new Board("cities.txt"), new ArrayList<Turn>());
	}
	
	
	@Test
	public void initialTurnTwoActions() {
	}
	
	@Test
	public void initialTurnManyActions() {
	}
	
	@Test
	public void initialTurnCompletion() {
	}
	
}
