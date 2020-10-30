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
import com.ttradvisor.app.classes.GameState;
import com.ttradvisor.app.classes.InputTurnController;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.RouteAction;
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
		return new GameState(testPlayerList, new Board("cities.txt"), new ArrayList<Turn>());
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
	
	@Test
	public void generalTurnDrawTwoTrain() {
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<TrainCard> drawnCards = new ArrayList<TrainCard>();
		drawnCards.add(new TrainCard(Colors.route.BLUE));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.YELLOW));
		
		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards));
		
		testController.newGeralTurn();
		
		ArrayList<TrainCard> drawnCards2 = new ArrayList<TrainCard>();
		drawnCards2.add(new TrainCard(Colors.route.RED));
		drawnCards2.add(new TrainCard(Colors.route.BLUE));

		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards2));
		
		assertTrue("General turn, drawing 2 train cards", testState.getPlayers().get(0).getTCS().size() == 6 && testState.getPlayers().get(0).getTCS().containsAll(drawnCards2));
	}
	
	@Test
	public void generalTurnDrawOneTrain() {
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<TrainCard> drawnCards = new ArrayList<TrainCard>();
		drawnCards.add(new TrainCard(Colors.route.BLUE));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.YELLOW));
		
		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards));
		
		testController.newGeralTurn();
		
		ArrayList<TrainCard> drawnCards2 = new ArrayList<TrainCard>();
		drawnCards2.add(new TrainCard(Colors.route.ANY));

		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards2));
		
		assertTrue("General turn, drawing 2 train cards", testState.getPlayers().get(0).getTCS().size() == 5 && testState.getPlayers().get(0).getTCS().containsAll(drawnCards2));
	}
	
	@Test
	public void generalTurnDrawDestinationOneCard() {
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<DestinationTicket> drawnTickets = new ArrayList<DestinationTicket>();
		drawnTickets.add(new DestinationTicket("Not a real", "city", 50));
		drawnTickets.add(new DestinationTicket("Also a fake", "city", 10));
		drawnTickets.add(new DestinationTicket("city", "seventeen", 20));
		
		testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets));
		
		testController.newGeralTurn();
		
		ArrayList<DestinationTicket> drawnTickets2 = new ArrayList<DestinationTicket>();
		drawnTickets2.add(new DestinationTicket("city2", "city3", 50));
		
		testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets2));
		
		assertTrue("General turn, add 1 destination ticket", testState.getPlayers().get(0).getDTS().size() == 4 && testState.getPlayers().get(0).getDTS().containsAll(drawnTickets2));
	}
	
	@Test
	public void generalTurnDrawDestinationThreeCard() {
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<DestinationTicket> drawnTickets = new ArrayList<DestinationTicket>();
		drawnTickets.add(new DestinationTicket("Not a real", "city", 50));
		drawnTickets.add(new DestinationTicket("Also a fake", "city", 10));
		drawnTickets.add(new DestinationTicket("city", "seventeen", 20));
		
		testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets));
		
		testController.newGeralTurn();
		
		ArrayList<DestinationTicket> drawnTickets2 = new ArrayList<DestinationTicket>();
		drawnTickets2.add(new DestinationTicket("city2", "city3", 50));
		drawnTickets2.add(new DestinationTicket("city2", "city4", 50));
		drawnTickets2.add(new DestinationTicket("city2", "city5", 50));
		
		testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets2));
		
		assertTrue("General turn, add 3 destination tickets", testState.getPlayers().get(0).getDTS().size() == 6 && testState.getPlayers().get(0).getDTS().containsAll(drawnTickets2));
	}
	

	@Test
	public void generalTurnClaimRouteOneColor() {
		
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<TrainCard> drawnCards = new ArrayList<TrainCard>();
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.RED));
	
		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards));
		
		testController.newGeralTurn();
		
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(new TrainCard(Colors.route.RED));
		spentCards.add(new TrainCard(Colors.route.RED));
		spentCards.add(new TrainCard(Colors.route.RED));

		
		Route claimedRoute1 = new Route("city1", "city2", Colors.route.RED, 3);
//		Route claimedRoute2= new Route("city1", "city3", Colors.route.BLUE, 3);
//		Route claimedRoute3 = new Route("city2", "city3", Colors.route.GREEN, 3);
//		testState.addClaimedRoute(claimedRoute2);
//		testState.addClaimedRoute(claimedRoute3);
		
		
		testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute1));
	
		assertTrue("Claim route with train cards of all route color", testState.getPlayers().get(0).getPlayerRoutes().contains(claimedRoute1) && testState.getPlayers().get(0).getTCS().size() == 1);
	}
	@Test
	public void generalTurnClaimRouteANY() {
		
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<TrainCard> drawnCards = new ArrayList<TrainCard>();
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.BLUE));
		drawnCards.add(new TrainCard(Colors.route.ANY));
	
		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards));
		
		testController.newGeralTurn();
		
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(new TrainCard(Colors.route.BLUE));
		spentCards.add(new TrainCard(Colors.route.ANY));

		
		Route claimedRoute2= new Route("city1", "city3", Colors.route.BLUE, 2);
		Route claimedRoute3 = new Route("city2", "city3", Colors.route.GREEN, 4);

		testState.addClaimedRoute(claimedRoute3);
		
		
		testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute2));

	
		assertTrue("Claim route with use of ANY color train card", testState.getPlayers().get(0).getPlayerRoutes().contains(claimedRoute2) && testState.getPlayers().get(0).getTCS().size() == 2);
	}

	@Test
	public void generalTurnClaimRouteNotAble() {
		
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<TrainCard> drawnCards = new ArrayList<TrainCard>();
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.BLUE));
		drawnCards.add(new TrainCard(Colors.route.ANY));
	
		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards));
		
		testController.newGeralTurn();
		
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(new TrainCard(Colors.route.GREEN));
		spentCards.add(new TrainCard(Colors.route.GREEN));
		spentCards.add(new TrainCard(Colors.route.GREEN));
		spentCards.add(new TrainCard(Colors.route.GREEN));

		Route claimedRoute3 = new Route("city2", "city3", Colors.route.GREEN, 4);
		
		testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute3));

		assertTrue("Player does not have correct train cards",testState.getPlayers().get(0).getPlayerRoutes().isEmpty() && testState.getPlayers().get(0).getTCS().size() == 4);
	}
	
//	@Test
//	public void generalTurnClaimRouteParallel() {
//		
//	
//	}
	
	
//	@Test
//	public void initialTurnManyActions() {
//	}
//	
//	@Test
//	public void initialTurnCompletion() {
//	}
	
}
