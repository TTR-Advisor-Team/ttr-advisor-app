package de.tomgrill.gdxtesting.examples;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.DestinationTicketList;
import com.ttradvisor.app.classes.GameState;
import com.ttradvisor.app.classes.HistoryController;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.Turn;

import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.*;

import java.util.ArrayList;

@RunWith(GdxTestRunner.class)
public class TestHistoryController {
	
	private HistoryController initHistoryController() {
		Player testP1 = new Player(Colors.player.RED);
		Player testP2 = new Player(Colors.player.GREEN);
		Player testP3 = new Player(Colors.player.BLUE);
		ArrayList<Player> testPlayerList = new ArrayList<Player>();
		testPlayerList.add(testP1);
		testPlayerList.add(testP2);
		testPlayerList.add(testP3);
		return new HistoryController(new GameState(Colors.player.BLACK, testPlayerList, new Board("cities.txt"), 
				new DestinationTicketList("destinations.txt"), new ArrayList<Turn>()));
	}
	
	@Test
	public void testTurnIndex() {
		HistoryController hist = initHistoryController();
		assertEquals("turnIndex should be initialized to 0", 0, hist.getTurnIndex());
		assertEquals("turnIndexView should return the viewed players position in the player array (0)", 0, hist.getTurnIndexView());
		hist.setTurnIndex(8);
		assertEquals("setTurnIndex should change the turn index", 8, hist.getTurnIndex());
		assertEquals("turnIndexView should return the viewed players position in the player array (non 0)", 1, hist.getTurnIndexView());
		
	}
	
	@Test
	public void testGameState() {
		HistoryController hist = initHistoryController();
		Player testP1 = new Player(Colors.player.RED);
		Player testP2 = new Player(Colors.player.GREEN);
		Player testP3 = new Player(Colors.player.BLUE);
		ArrayList<Player> testPlayerList = new ArrayList<Player>();
		testPlayerList.add(testP1);
		testPlayerList.add(testP2);
		testPlayerList.add(testP3);
		GameState gameState = new GameState(Colors.player.BLACK, testPlayerList, new Board("cities.txt"), 
				new DestinationTicketList("destinations.txt"), new ArrayList<Turn>());
		assertEquals("history controller should retireve the game state correctly", testPlayerList, hist.getGameState().getPlayers());
	}
	
	@Test
	public void testPrevTurn() {
		HistoryController hist = initHistoryController();
		assertFalse("Previous Turn should return false if it is the initial turn", hist.previousTurn());
		hist.getGameState().addTurn(new Turn(hist.getGameState().getBoard(), null, null));
		hist.setTurnIndex(1);
		assertTrue("Previous Turn should return true if it is not initial turn", hist.previousTurn());
		assertEquals("Calling Previous Turn should change the turn index", 0, hist.getTurnIndex());
	}
	
	@Test
	public void testNextTurn() {
		HistoryController hist = initHistoryController();
		assertFalse("Next Turn should return false if it is the current turn", hist.nextTurn());
		hist.getGameState().addTurn(new Turn(hist.getGameState().getBoard(), null, null));
		hist.getGameState().addTurn(new Turn(hist.getGameState().getBoard(), null, null));
		assertTrue("Next Turn should return true if it is not the current turn", hist.nextTurn());
		assertEquals("Calling Next Turn should change the turn index", hist.getTurnIndex(), 1);
		assertTrue("Next Turn should return true if it is not the current turn (second call)", hist.nextTurn());
		assertEquals("Calling Next Turn should change the turn index (two calls)", 2, hist.getTurnIndex());
	}
}