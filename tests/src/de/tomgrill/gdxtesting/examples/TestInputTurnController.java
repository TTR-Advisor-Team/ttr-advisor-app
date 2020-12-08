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
		GameState initState = new GameState(Colors.player.BLACK, testPlayerList, new Board("cities.txt"),
				new DestinationTicketList("destinations.txt"), new ArrayList<Turn>());
		
		initState.userColor = Colors.player.RED;
		return initState;
	}
	
	private GameState generalTestGameState() {
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<TrainCard> drawnCards = new ArrayList<TrainCard>();
		drawnCards.add(new TrainCard(Colors.route.BLUE));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.YELLOW));
		
		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards));
		
		ArrayList<DestinationTicket> drawnTickets = new ArrayList<DestinationTicket>();
		drawnTickets.add(new DestinationTicket("A", "B", 100));
		drawnTickets.add(new DestinationTicket("C", "D", 10));
		drawnTickets.add(new DestinationTicket("E", "F", 10));
		
		testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets));
		
		return testState;
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
	public void initialTurnRouteActionFails() {
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(new TrainCard(Colors.route.BLUE));
		spentCards.add(new TrainCard(Colors.route.ORANGE));
		
		Route testRoute = testState.getBoard().getRoute("Raleigh", "Charleston");
		
		testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, testRoute));
		
		assertTrue("Initial turn should not complete & route should not get claimed", 
				testState.getBoard().getAllRoutesOfPlayer(testState.getPlayers().get(0).getColor()).isEmpty() 
				&& testController.isInitialTurn());

	}
	
	@Test
	public void initialTurnDuplicateActions() {
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<TrainCard> drawnCards = new ArrayList<TrainCard>();
		drawnCards.add(new TrainCard(Colors.route.BLUE));
		drawnCards.add(new TrainCard(Colors.route.ORANGE));
		drawnCards.add(new TrainCard(Colors.route.RED));
		drawnCards.add(new TrainCard(Colors.route.GREEN));
		
		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards));
		assertFalse("Can't draw cards twice on initial turn!", testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards)));

	}
	
	@Test
	public void initialTurnDuplicateActions2() {
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		
		ArrayList<DestinationTicket> drawnTickets = new ArrayList<DestinationTicket>();
		drawnTickets.add(new DestinationTicket("A", "B", 100));
		drawnTickets.add(new DestinationTicket("C", "D", 10));
		drawnTickets.add(new DestinationTicket("E", "F", 10));
		
		testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets));
		assertFalse("Can't draw cards twice on initial turn!", testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets)));

	}
	
	@Test
	public void initialTurnInvalidActions() {
		GameState testState = initTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		testController.startInitialTurn();
		
		ArrayList<TrainCard> drawnCards = new ArrayList<TrainCard>();
		drawnCards.add(new TrainCard(Colors.route.BLUE));
		drawnCards.add(new TrainCard(Colors.route.ORANGE));
		drawnCards.add(new TrainCard(Colors.route.RED));
		
		assertFalse("Attempt drawing 3 cards on initial turn", testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards)));
		
		ArrayList<DestinationTicket> drawnTickets = new ArrayList<DestinationTicket>();
		drawnTickets.add(new DestinationTicket("A", "B", 100));
		drawnTickets.add(new DestinationTicket("C", "D", 10));
		drawnTickets.add(new DestinationTicket("E", "F", 10));
		drawnTickets.add(new DestinationTicket("F", "G", 10));
		
		testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets));
		assertFalse("Attempt drawing 4 DTs on initial turn", testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards)));

	}
	
	@Test
	public void generalTurnDrawTwoTrain() {
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);

		ArrayList<TrainCard> drawnCards2 = new ArrayList<TrainCard>();
		drawnCards2.add(new TrainCard(Colors.route.RED));
		drawnCards2.add(new TrainCard(Colors.route.BLUE));

		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards2));
		
		assertTrue("General turn, drawing 2 train cards", testState.getPlayers().get(0).getTCS().size() == 6 && testState.getPlayers().get(0).getTCS().containsAll(drawnCards2));
	}
	
	@Test
	public void generalTurnDrawOneTrain() {
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		
		ArrayList<TrainCard> drawnCards2 = new ArrayList<TrainCard>();
		drawnCards2.add(new TrainCard(Colors.route.ANY));

		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards2));
		
		assertTrue("General turn, drawing 2 train cards", testState.getPlayers().get(0).getTCS().size() == 5 && testState.getPlayers().get(0).getTCS().containsAll(drawnCards2));
	}
	
	@Test
	public void generalTurnDrawZeroTrain() {
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);

		ArrayList<TrainCard> drawnCards2 = new ArrayList<TrainCard>();

		assertFalse("General turn, drawing 0 train cards should fail", testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards2)));
	}
	
	@Test
	public void generalTurnDrawThreeTrain() {
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);

		ArrayList<TrainCard> drawnCards2 = new ArrayList<TrainCard>();
		drawnCards2.add(new TrainCard(Colors.route.RED));
		drawnCards2.add(new TrainCard(Colors.route.BLUE));
		drawnCards2.add(new TrainCard(Colors.route.BLUE));

		assertFalse("General turn, drawing 3 train cards should fail", testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards2)));
	}
	
	@Test
	public void generalTurnDrawDestinationOneCard() {
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		
		ArrayList<DestinationTicket> drawnTickets2 = new ArrayList<DestinationTicket>();
		drawnTickets2.add(new DestinationTicket("city2", "city3", 50));
		
		testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets2));

		assertTrue("General turn, add 1 destination ticket", testState.getPlayers().get(0).getDTS().size() == 4 && testState.getPlayers().get(0).getDTS().containsAll(drawnTickets2));
	}
	
	@Test
	public void generalTurnDrawDestinationThreeCard() {
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		
		ArrayList<DestinationTicket> drawnTickets2 = new ArrayList<DestinationTicket>();
		drawnTickets2.add(new DestinationTicket("city2", "city3", 50));
		drawnTickets2.add(new DestinationTicket("city2", "city4", 50));
		drawnTickets2.add(new DestinationTicket("city2", "city5", 50));
		
		testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets2));
		
		assertTrue("General turn, add 3 destination tickets", testState.getPlayers().get(0).getDTS().size() == 6 && testState.getPlayers().get(0).getDTS().containsAll(drawnTickets2));
	}
	
	@Test
	public void generalTurnDrawDestinationZeroCard() {
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
		
		ArrayList<DestinationTicket> drawnTickets2 = new ArrayList<DestinationTicket>();
		
		assertFalse("General turn, shouldn't be possible to draw 0 DTs", testController.takeAction(new DestinationAction(testState.getPlayers().get(0), drawnTickets2)));
	}

	
	@Test
	public void generalTurnClaimRoute() {		
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
						
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(testState.getPlayers().get(0).getTCS().get(1));
		spentCards.add(testState.getPlayers().get(0).getTCS().get(2));
	
		Route claimedRoute1 = new Route("Charleston", "Atlanta", Colors.route.ANY, 2);
		
		testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute1));

		assertTrue("Claim route with train cards of all route color", testState.getBoard().getAllRoutesOfPlayer(testState.getPlayers().get(0).getColor()).size()/2 == 1 && 
					testState.getBoard().getRouteAnyOwner(claimedRoute1.getBegin(), claimedRoute1.getEnd()).getOwner().equals(testState.getPlayers().get(0).getColor()));	
	}
	
	@Test
	public void generalTurnClaimRouteClaimed() {		
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
						
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(testState.getPlayers().get(0).getTCS().get(1));
		spentCards.add(testState.getPlayers().get(0).getTCS().get(2));
		
		Route claimedRoute1 = new Route("Charleston", "Atlanta", Colors.route.ANY, 2);
		
		testState.getBoard().claimRoute(claimedRoute1.getBegin(),claimedRoute1.getEnd(), claimedRoute1.getColor(), testState.getPlayers().get(0).getColor());
	
		assertFalse("Can not claim route that is alreadt claimed", testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute1)));	
	}
	
	
	@Test
	public void generalTurnClaimRouteTwoRoutes3Players() {		
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
						
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(testState.getPlayers().get(0).getTCS().get(1));
		spentCards.add(testState.getPlayers().get(0).getTCS().get(2));
		
		Route claimedRoute2 = new Route("Oklahoma City", "Kansas City", Colors.route.ANY, 2);
		
		Boolean result = testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute2));
		
		assertTrue("3 players, double route claim", result);	
	}
	
	@Test
	public void generalTurnClaimRouteTwoRoutes3PlayersClaimed() {		
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
						
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(testState.getPlayers().get(0).getTCS().get(1));
		spentCards.add(testState.getPlayers().get(0).getTCS().get(2));
		
		Route claimedRoute2 = new Route("Oklahoma City", "Kansas City", Colors.route.ANY, 2);
		
		testState.getBoard().claimRoute(claimedRoute2.getBegin(),claimedRoute2.getEnd(), claimedRoute2.getColor(), testState.getPlayers().get(1).getColor());
		
		Boolean result = testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute2));
		
		assertFalse("3 players, double route claim", result);	
	}
	
	@Test
	public void generalTurnClaimRouteTwoRoutes4Players() {		
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
						
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(testState.getPlayers().get(0).getTCS().get(1));
		spentCards.add(testState.getPlayers().get(0).getTCS().get(2));
		
		Route claimedRoute2 = new Route("Oklahoma City", "Kansas City", Colors.route.ANY, 2);
		
		Player testP4 = new Player(Colors.player.YELLOW);
		testState.getPlayers().add(testP4);
		
		testState.getBoard().claimRoute(claimedRoute2.getBegin(),claimedRoute2.getEnd(), claimedRoute2.getColor(), testState.getPlayers().get(1).getColor());
		
		Boolean result = testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute2));
		
		assertTrue("4 players, double route claim", result);	
	}
	
	@Test
	public void generalTurnClaimRouteTwoRoutes4PlayersOwned() {		
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
						
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(testState.getPlayers().get(0).getTCS().get(1));
		spentCards.add(testState.getPlayers().get(0).getTCS().get(2));
		
		Route claimedRoute2 = new Route("Oklahoma City", "Kansas City", Colors.route.ANY, 2);
		
		Player testP4 = new Player(Colors.player.YELLOW);
		testState.getPlayers().add(testP4);
		
		testState.getBoard().claimRoute(claimedRoute2.getBegin(),claimedRoute2.getEnd(), claimedRoute2.getColor(), testState.getPlayers().get(1).getColor());
		testState.getBoard().claimRoute(claimedRoute2.getBegin(),claimedRoute2.getEnd(), claimedRoute2.getColor(), testState.getPlayers().get(1).getColor());
		
		Boolean result = testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute2));
		
		assertFalse("4 players, double route claim fails, no route available", result);	
	}
	
	@Test
	public void generalTurnClaimRouteTwoRoutes4PlayersOtherOwns() {		
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
						
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(testState.getPlayers().get(0).getTCS().get(1));
		spentCards.add(testState.getPlayers().get(0).getTCS().get(2));
		
		Route claimedRoute2 = new Route("Oklahoma City", "Kansas City", Colors.route.ANY, 2);
		
		Player testP4 = new Player(Colors.player.YELLOW);
		testState.getPlayers().add(testP4);
		
		testState.getBoard().claimRoute(claimedRoute2.getBegin(),claimedRoute2.getEnd(), claimedRoute2.getColor(), testState.getPlayers().get(0).getColor());
		
		Boolean result = testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute2));
		
		assertFalse("4 players, double route claim fails, already claimed parallel route", result);	
	}
	
	@Test
	public void generalTurnClaimRouteSpent() {		
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
						
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(testState.getPlayers().get(0).getTCS().get(1));
		
		Route claimedRoute2 = new Route("Oklahoma City", "Kansas City", Colors.route.ANY, 2);
		
		testState.getBoard().claimRoute(claimedRoute2.getBegin(),claimedRoute2.getEnd(), claimedRoute2.getColor(), testState.getPlayers().get(0).getColor());
		
		Boolean result = testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute2));
		
		assertFalse("Cards spent are less than cost", result);	
	}
	
	@Test
	public void generalTurnClaimRouteSpentWrong() {		
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
						
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(testState.getPlayers().get(0).getTCS().get(1));
		spentCards.add(testState.getPlayers().get(0).getTCS().get(2));
		
		Route claimedRoute2 = new Route("Oklahoma City", "Kansas City", Colors.route.ANY, 2);
		
		testState.getBoard().claimRoute(claimedRoute2.getBegin(),claimedRoute2.getEnd(), claimedRoute2.getColor(), testState.getPlayers().get(0).getColor());
		
		Boolean result = testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute2));
		
		assertFalse("Cards spent are less than cost", result);	
	}

	@Test
	public void generalTurnClaimRouteSpentBlue() {		
		GameState testState = generalTestGameState();
		InputTurnController testController = new InputTurnController(testState);
						
		ArrayList<TrainCard> drawnCards2 = new ArrayList<TrainCard>();
		drawnCards2.add(new TrainCard(Colors.route.BLUE));
		drawnCards2.add(new TrainCard(Colors.route.BLUE));
		
		testController.takeAction(new TrainCardAction(testState.getPlayers().get(0), drawnCards2));
		
		ArrayList<TrainCard> spentCards = new ArrayList<TrainCard>();
		spentCards.add(testState.getPlayers().get(0).getTCS().get(4));
		spentCards.add(testState.getPlayers().get(0).getTCS().get(5));
		spentCards.add(testState.getPlayers().get(0).getTCS().get(0));
		
		Route claimedRoute3 = new Route("Oklahoma City", "Santa Fe", Colors.route.BLUE, 3);
		
		Boolean result = testController.takeAction(new RouteAction(testState.getPlayers().get(0), spentCards, claimedRoute3));
		
		assertTrue("Cards spent are less than cost", result);	
	}
	
	
	@Test
	public void generalTurnClaimRouteParallel() {
		
	
	}
	
	
	@Test
	public void initialTurnManyActions() {
	}
	
	@Test
	public void initialTurnCompletion() {
	}
	
	@Test
	public void severalconsecutiveTurns() {
		initTestGameState();
	}
	
}
