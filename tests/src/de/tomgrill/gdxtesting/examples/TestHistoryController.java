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
		return new HistoryController(new GameState(testPlayerList, new Board("cities.txt"), 
				new DestinationTicketList("destinations.txt"), new ArrayList<Turn>()));
	}
	
	public void testTurnIndex() {
		HistoryController hist = initHistoryController();
		assertEquals("turnIndex should be initialized to 0", hist.getTurnIndex(), 0);
		hist.setTurnIndex(8);
		assertEquals("setTurnIndex should change the turn index", hist.getTurnIndex(), 8);
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
		GameState gameState = new GameState(testPlayerList, new Board("cities.txt"), 
				new DestinationTicketList("destinations.txt"), new ArrayList<Turn>());
		assertEquals("history controller should retireve the game state correctly", hist.getGameState().getPlayers(), testPlayerList);
	}
	
	@Test
	public void testPrevTurn() {
		HistoryController hist = initHistoryController();
		assertEquals("Previous Turn should return false if it is the initial turn", hist.previousTurn(), false);
		hist.getGameState().addTurn(new Turn(hist.getGameState().getBoard(), null, null));
		hist.setTurnIndex(1);
		assertEquals("Previous Turn should return true if it is not initial turn", hist.previousTurn(), true);
		assertEquals("Calling Previous Turn should change the turn index", hist.getTurnIndex(), 0);
	}
	
	@Test
	public void testNextTurn() {
		HistoryController hist = initHistoryController();
		assertEquals("Next Turn should return false if it is the current turn", hist.nextTurn(), false);
		hist.getGameState().addTurn(null);
		assertEquals("Next Turn should return true if it is not the current turn", hist.nextTurn(), true);
		assertEquals("Calling Next Turn should change the turn index", hist.getTurnIndex(), 1);
	}
}