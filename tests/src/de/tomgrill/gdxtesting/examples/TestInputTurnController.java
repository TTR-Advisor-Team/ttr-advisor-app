package de.tomgrill.gdxtesting.examples;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ttradvisor.app.classes.Action;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.DestinationAction;
import com.ttradvisor.app.classes.DestinationTicket;
import com.ttradvisor.app.classes.DestinationTicketList;
import com.ttradvisor.app.classes.GameState;
import com.ttradvisor.app.classes.InputTurnController;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.TrainCard;
import com.ttradvisor.app.classes.TrainCardAction;
import com.ttradvisor.app.classes.Turn;

import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

@RunWith(GdxTestRunner.class)
public class TestInputTurnController {
	
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
	public void initialTurnTrainCardAction() {
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<TrainCard> drawnCards = new ArrayList<TrainCard>();
		drawnCards.add(new TrainCard(Colors.route.BLUE));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.YELLOW));
		
		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards));

		assertTrue("Initial turn, draw 4 train cards", testState.getPlayers().get(0).getTCS().containsAll(drawnCards));
	}
	
	@Test
	public void initialTurnDestinationAction() {
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<DestinationTicket> drawnTickets = new ArrayList<DestinationTicket>();
		drawnTickets.add(new DestinationTicket("Not a real", "city", 50));
		drawnTickets.add(new DestinationTicket("Also a fake", "city", 10));
		drawnTickets.add(new DestinationTicket("city", "seventeen", 20));
		
		testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets));
		
		assertTrue("Initial turn, draw 1-3 tickets", testState.getPlayers().get(0).getDTS().containsAll(drawnTickets));
	}
	
	@Test
	public void initialTurnTwoActions() {
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<TrainCard> drawnCards = new ArrayList<TrainCard>();
		drawnCards.add(new TrainCard(Colors.route.BLUE));
		drawnCards.add(new TrainCard(Colors.route.ORANGE));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.GREEN));
		
		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards));
		
		ArrayList<DestinationTicket> drawnTickets = new ArrayList<DestinationTicket>();
		drawnTickets.add(new DestinationTicket("A", "B", 100));
		drawnTickets.add(new DestinationTicket("C", "D", 10));
		drawnTickets.add(new DestinationTicket("E", "F", 10));
		
		testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets));
		
		assertTrue("Initial turn should complete", testState.getPlayers().get(0).getTCS().containsAll(drawnCards)
				&& testState.getPlayers().get(0).getDTS().containsAll(drawnTickets)
				&& !testController.isInitialTurn());

	}
	
//	@Test
//	public void initialTurnManyActions() {
//	}
//	
//	@Test
//	public void initialTurnCompletion() {
//	}
	
}
