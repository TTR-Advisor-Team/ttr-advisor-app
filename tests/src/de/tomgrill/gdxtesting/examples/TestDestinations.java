package de.tomgrill.gdxtesting.examples;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.DestinationTicket;
import com.ttradvisor.app.classes.DestinationTicketList;
import com.ttradvisor.app.classes.GameState;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.Turn;

import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;

@RunWith(GdxTestRunner.class)
public class TestDestinations {
	
	private GameState setupTest() {
		Player testP1 = new Player(Colors.player.RED);
		Player testP2 = new Player(Colors.player.BLUE);
		ArrayList<Player> testPlayerList = new ArrayList<Player>();
		testPlayerList.add(testP1);
		testPlayerList.add(testP2);
		return new GameState(testPlayerList, new Board("cities.txt"), 
				new DestinationTicketList("destinations.txt"), new ArrayList<Turn>());
	}
	
	@Test
	public void ticketListInit() {
		GameState testState = setupTest();
		assertNotNull("DestinationTicketList should not be empty", testState.getDtList());
	}
	
	@Test
	public void testGetList() {
		GameState testState = setupTest();
		assertNotNull("DestinationTicketList getters should work", testState.getDtList().getList());

	}
	
	@Test
	public void DTListParserError() {
		DestinationTicketList failList = new DestinationTicketList("failure.txt");
		assertTrue("DTList made from nonexistent file should be empty", failList.getList().isEmpty());
	}
	
	@Test
	public void ticketGetter() {
		GameState testState = setupTest();
		DestinationTicket testTicket = new DestinationTicket("Boston", "Miami", 12);
		assertTrue("getTicket should return correct ticket", 
				testTicket.toString().equals(testState.getDtList().getTicket(0).toString()));
	}
	
	@Test
	public void ticketValue() {
		GameState testState = setupTest();
		assertEquals("Ticket values should be returned correctly", 12, 
				testState.getDtList().getTicket(0).getValue());
	}
	
	

}
