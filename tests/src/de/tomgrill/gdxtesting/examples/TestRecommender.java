package de.tomgrill.gdxtesting.examples;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.Before;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.Colors;
import com.ttradvisor.app.classes.DestinationTicket;
import com.ttradvisor.app.classes.DestinationTicketList;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.Recommender;
import com.ttradvisor.app.classes.TrainCard;

import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

@RunWith(GdxTestRunner.class)
public class TestRecommender {
	private Board board;
	private Player player;
	private Recommender rec;

	@Before
	public void setUp() {
		board = new Board("cities.txt");
		player = new Player(Colors.player.BLACK);
		rec = new Recommender(board, player, 10, 4);
	}

	@After
	public void tearDown() {
		board = null;
		player = null;
		rec = null;
	}

	@Test
	public void shortesSinglePathTest1() {
		DestinationTicket dt = new DestinationTicket("Nashville", "Atlanta");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals(1, cost);
	}

	@Test
	public void reverseShortesSinglePathTest1() {
		DestinationTicket dt = new DestinationTicket("Atlanta", "Nashville");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals(1, cost);
	}

	@Test
	public void shortestDoublePathTest() {
		DestinationTicket dt = new DestinationTicket("Vancouver", "Portland");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}

		assertEquals(2, cost);
	}

	@Test
	public void reverseShortestDoublePathTest() {
		DestinationTicket dt = new DestinationTicket("Portland", "Vancouver");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals(2, cost);
	}

	@Test
	public void shortDoublePathTest() {
		DestinationTicket dt = new DestinationTicket("Miami", "Atlanta");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals(5, cost);
	}

	@Test
	public void reverseShortDoublePathTest() {
		DestinationTicket dt = new DestinationTicket("Atlanta", "Miami");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals(5, cost);
	}

	@Test
	public void aLongPathTest1() {
		DestinationTicket dt = new DestinationTicket("Seattle", "Toronto");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals(17, cost);
	}

	@Test
	public void reverseALongPathTest1() {
		DestinationTicket dt = new DestinationTicket("Toronto", "Seattle");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals(17, cost);
	}

	@Test
	public void aLongPathTest2() {
		DestinationTicket dt = new DestinationTicket("San Francisco", "New York");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals(21, cost);

	}

	@Test
	public void reverseALongPathTest2() {
		DestinationTicket dt = new DestinationTicket("New York", "San Francisco");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals(21, cost);

	}

	@Test
	public void pathTest1() {
		DestinationTicket dt = new DestinationTicket("El Paso", "Duluth");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals(10, cost);
	}

	@Test
	public void reversePathTest1() {
		DestinationTicket dt = new DestinationTicket("Duluth", "El Paso");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals(10, cost);
	}

	@Test
	public void pathingAroundOtherPlayers() {
		board.claimRoute("Nashville", "Atlanta", Colors.route.ANY, Colors.player.GREEN);
		DestinationTicket dt = new DestinationTicket("Nashville", "Atlanta");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals("Pathing around other players", 5, cost);

	}

	@Test
	public void reversePathingAroundOtherPlayers() {
		board.claimRoute("Nashville", "Atlanta", Colors.route.ANY, Colors.player.GREEN);
		DestinationTicket dt = new DestinationTicket("Atlanta", "Nashville");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		Route prev = null;
		for (Route r : routes) {
			if (prev == null) {
				cost += r.getCost();
			} else if (prev.getBegin().equals(r.getBegin()) && prev.getEnd().equals(r.getEnd())) {
				cost += 0;
			} else {
				cost += r.getCost();
			}
			prev = r;
		}
		assertEquals("Reverse pathing around other players", 5, cost);

	}

	@Test
	public void multipleDtTest() {
		ArrayList<DestinationTicket> dts = new ArrayList<DestinationTicket>();
		dts.add(new DestinationTicket("Atlanta", "Nashville"));
		dts.add(new DestinationTicket("Atlanta", "Saint Louis"));
		player = new Player(dts, null, Colors.player.BLACK, 0, 0);
		ArrayList<Route> routes = rec.getRoutes(dts);

		for (int i = 0; i < routes.size(); ++i) {
			for (int j = 0; j < routes.size(); ++j) {
				if (i == j)
					continue;
				assertFalse(routes.get(i).equals(routes.get(j)));
			}
		}
	}

	@Test
	public void ownerTest() {
		board.claimRoute("New Orleans", "Miami", Colors.route.RED, Colors.player.BLACK);
		DestinationTicket dt = new DestinationTicket("New Orleans", "Charleston");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		int cost = 0;
		for (Route r : routes) {
			cost += r.getCost();
		}
		assertEquals(10, cost);
	}

	@Test
	public void emptyPathTest() {
		board.claimRoute("Miami", "Atlanta", Colors.route.BLUE, Colors.player.GREEN);
		board.claimRoute("Miami", "New Orleans", Colors.route.RED, Colors.player.GREEN);
		board.claimRoute("Miami", "Charleston", Colors.route.PINK, Colors.player.GREEN);
		DestinationTicket dt = new DestinationTicket("Atlanta", "Miami");
		ArrayList<Route> routes = rec.shortestPath(dt, player.getColor());
		assertTrue(routes.isEmpty());
	}

	@Test
	public void calculateDrawDestinationTicketTest() {
		// tickets empty
		ArrayList<DestinationTicket> dts = new ArrayList<DestinationTicket>();
		ArrayList<String> recs = rec.calculate(dts);
		for (String s : recs) {
			System.out.println(s);
		}
		assertTrue(recs.contains("Draw destination ticket."));
	}

	@Test
	public void calculateDrawDestinationTicketTest2() {
		// tickets not empty, but impossible to complete route
		ArrayList<DestinationTicket> dts = new ArrayList<DestinationTicket>();
		dts.add(new DestinationTicket("Atlanta", "Miami"));
		board.claimRoute("Miami", "Atlanta", Colors.route.BLUE, Colors.player.GREEN);
		board.claimRoute("Miami", "New Orleans", Colors.route.RED, Colors.player.GREEN);
		board.claimRoute("Miami", "Charleston", Colors.route.PINK, Colors.player.GREEN);
		ArrayList<String> recs = rec.calculate(dts);
		for (String s : recs) {
			System.out.println(s);
		}
		assertTrue(recs.contains("Draw destination ticket."));
	}

	@Test
	public void calculateClaimExpensiveRouteTest() {
		rec = new Recommender(board, player, 100, 4);
		ArrayList<DestinationTicket> dts = new ArrayList<DestinationTicket>();
		while (player.getTCS().size() < 10) {
			player.getTCS().add(new TrainCard(Colors.route.ORANGE));
		}
		ArrayList<String> recs = rec.calculate(dts);
		for (String s : recs) {
			System.out.println(s);
		}
		assertTrue(recs.contains("Claim the most expensive ORANGE or GRAY route."));
	}

	@Test
	public void calculateClaimExpensiveRouteTest2() {
		rec = new Recommender(board, player, 100, 4);
		ArrayList<DestinationTicket> dts = new ArrayList<DestinationTicket>();
		dts.add(new DestinationTicket("Atlanta", "Miami"));
		board.claimRoute("Miami", "Atlanta", Colors.route.BLUE, Colors.player.GREEN);
		board.claimRoute("Miami", "New Orleans", Colors.route.RED, Colors.player.GREEN);
		board.claimRoute("Miami", "Charleston", Colors.route.PINK, Colors.player.GREEN);
		while (player.getTCS().size() < 10) {
			player.getTCS().add(new TrainCard(Colors.route.ORANGE));
		}
		ArrayList<String> recs = rec.calculate(dts);
		for (String s : recs) {
			System.out.println(s);
		}
		assertTrue(recs.contains("Claim the most expensive ORANGE or GRAY route."));
	}

	@Test
	public void calculateClaimRouteOnPathTest() {
		ArrayList<DestinationTicket> dts = new ArrayList<DestinationTicket>();
		player.setNumTrains(46);
		dts.add(new DestinationTicket("San Francisco", "New York"));
		while (player.getTCS().size() < 10) {
			player.getTCS().add(new TrainCard(Colors.route.ORANGE));
		}
		ArrayList<String> recs = rec.calculate(dts);
		for (String s : recs) {
			System.out.println(s);
		}
		assertTrue(recs.contains("Claim ORANGE route from Salt Lake City to San Francisco."));
	}

	@Test
	public void calculateClaimRouteOnPathTest2() {
		ArrayList<DestinationTicket> dts = new ArrayList<DestinationTicket>();
		player.setNumTrains(46);
		dts.add(new DestinationTicket("San Francisco", "New York"));
		dts.add(new DestinationTicket("New York", "San Francisco"));
		while (player.getTCS().size() < 10) {
			player.getTCS().add(new TrainCard(Colors.route.ORANGE));
		}
		ArrayList<String> recs = rec.calculate(dts);
		for (String s : recs) {
			System.out.println(s);
		}
		assertTrue(recs.contains("Claim ORANGE route from Salt Lake City to San Francisco."));
	}

	@Test
	public void graphConsistencyTest() {

		Set<String> keys = board.getBoard().keySet();
		ArrayList<String> list = new ArrayList<String>(keys);
		ArrayList<DestinationTicket> dts = new ArrayList<DestinationTicket>();

		for (int i = 0; i < list.size(); ++i) {
			for (int j = 0; j < list.size(); ++j) {
				if (i == j)
					continue;
				else {
					DestinationTicket dt = new DestinationTicket(list.get(i), list.get(j));
					dts.add(dt);
				}
			}

		}
		for(DestinationTicket d: dts) {
			rec.shortestPath(d, player.getColor());
		}
		assertTrue(true);
	}
	
	@Test
	public void DTListConsistenecyTest() {
		DestinationTicketList dtl = new DestinationTicketList("destinations.txt");
		LinkedList<DestinationTicket> dts = dtl.getList();
		for(DestinationTicket d:dts) {
			rec.shortestPath(d, player.getColor());			
		}
		assertTrue(true);
	}
	
	@Test
	public void LRHelperTest() {
		board.claimRoute("Seattle", "Vancouver", Colors.route.ANY, Colors.player.BLACK);
		board.claimRoute("Vancouver", "Calgary", Colors.route.ANY, Colors.player.BLACK);
		board.claimRoute("Calgary", "Seattle", Colors.route.ANY, Colors.player.BLACK);
		board.claimRoute("Seattle", "Portland", Colors.route.ANY, Colors.player.BLACK);
		board.claimRoute("Calgary", "Helena", Colors.route.ANY, Colors.player.BLACK);

		
		assertEquals(12, rec.longestRoute(Colors.player.BLACK));	
		
	}
	
	@Test
	public void LRHelperTest2() {
		board.claimRoute("Los Angeles", "El Paso", Colors.route.BLACK, Colors.player.RED);
		board.claimRoute("El Paso", "Houston", Colors.route.GREEN, Colors.player.RED);
		board.claimRoute("Houston", "New Orleans", Colors.route.ANY, Colors.player.RED);
		board.claimRoute("New Orleans", "Miami", Colors.route.RED, Colors.player.RED);
		board.claimRoute("New Orleans", "Atlanta", Colors.route.ORANGE, Colors.player.RED);
		board.claimRoute("Atlanta", "Miami", Colors.route.BLUE, Colors.player.RED);
		assertEquals(29, rec.longestRoute(Colors.player.RED));
	}
	
}
