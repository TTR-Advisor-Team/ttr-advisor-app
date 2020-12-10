package de.tomgrill.gdxtesting.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.DestinationAction;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.TrainCardAction;
import com.ttradvisor.app.classes.Turn;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class TurnTest {
	private ArrayList<Player> initPlayerArray() {
		//Variables: Board, Action, ArrayList<Player>
		//Board testBoard = new Board(null);
		Player p1 = new Player(null, null, null, 2, 0);
		Player p2 = new Player(null, null, null, 1, 1);
		Player p3 = new Player(null, null, null, 0, 2);
		ArrayList<Player> players = new ArrayList<Player>(); 
		players.add(p1);
		players.add(p2);
		players.add(p3);
		return players;
	}
	
	@Test
	public void testPlayersConstructor1() {
		Turn testTurn = new Turn(null, null, initPlayerArray());
		assertNotNull("Three players should be in the game", testTurn.getPlayerSnapshots().get(2));
	}
	
	@Test
	public void testPlayersConstructor2() {
		Turn testTurn = new Turn(null, null, initPlayerArray());
		assertEquals("Player Scores should be set correctly", 2, testTurn.getPlayerSnapshots().get(0).getScore());
	}
	
	@Test
	public void testBoardConstructor() {
		Turn testTurn = new Turn(new Board("cities.txt"), null, null);
		assertNotNull("Board should be inputted", testTurn.getSnapshot());
	}
	
	@Test
	public void testTCActionConstructor() {
		Turn testTurn = new Turn(null, new TrainCardAction(initPlayerArray().get(1), null), null);
		assertNotNull("TrainCardAction should be registered by turn constructor", testTurn.getAction());
	}
	
	@Test
	public void testDTActionConstructor() {
		Turn testTurn = new Turn(null, new DestinationAction(initPlayerArray().get(1), null), null);
		assertNotNull("DestinationAction should be registered by turn constructor", testTurn.getAction());
	}
	
	@Test
	public void testPlayersSetter() {
		Turn testTurn = new Turn(null, null, null);
		testTurn.setPlayerSnapshots(initPlayerArray());
		assertNotNull("Three players should be in the game", testTurn.getPlayerSnapshots().get(2));
	}
	
	@Test
	public void testBoardSetter() {
		Turn testTurn = new Turn(null, null, null);
		testTurn.setSnapshot(new Board("cities.txt"));
		assertNotNull("Board should be inputted", testTurn.getSnapshot());
	}
	
	@Test
	public void testActionSetter1() {
		Turn testTurn = new Turn(null, null, null);
		testTurn.setAction(new TrainCardAction(initPlayerArray().get(1), null));
		assertNotNull("TrainCardAction should be properly set", testTurn.getAction());
	}
	
	@Test
	public void testActionSetter2() {
		Turn testTurn = new Turn(null, null, null);
		testTurn.setAction(new DestinationAction(initPlayerArray().get(1), null));
		assertNotNull("DestinationAction should be properly set", testTurn.getAction());
	}
	
}
