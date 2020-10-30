package de.tomgrill.gdxtesting.examples;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.DestinationTicketList;
import com.ttradvisor.app.classes.GameState;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.Turn;

import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

@RunWith(GdxTestRunner.class)
public class TestModel {
	
	private GameState initTestGameState() {
		Player testP1 = new Player(Colors.player.RED);
		Player testP2 = new Player(Colors.player.GREEN);
		Player testP3 = new Player(Colors.player.BLUE);
		ArrayList<Player> testPlayerList = new ArrayList<Player>();
		testPlayerList.add(testP1);
		testPlayerList.add(testP2);
		testPlayerList.add(testP3);
		return new GameState(testPlayerList, new Board("cities.txt"), 
				new DestinationTicketList("destinations.txt"), new ArrayList<Turn>());
	}
	
	@Test
	public void stateHasPlayers() {
		GameState testState = initTestGameState();
		assertNotNull("Three players should be added to the state.", testState.getPlayers().get(2));

	}
	
	@Test
	public void playersHaveColors() {
		GameState testState = initTestGameState();
		assertEquals("Player colors should be set.", Colors.player.RED, testState.getPlayers().get(0).getColor());
		
	}
	
	@Test
	public void playersHaveInitiallyEmptyHands() {
		GameState testState = initTestGameState();
		assertEquals("Players start with empty hands.", "[]", testState.getPlayers().get(0).getTCS().toString());

	}

}
