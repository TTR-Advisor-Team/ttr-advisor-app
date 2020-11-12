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

import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

@RunWith(GdxTestRunner.class)
public class TestRecommender {
	private Board board;
	private Player player;
	private Recommender rec;
	
	@Before
	public void setUp() {
		board = new Board("cities.txt");
		player = new Player(Colors.player.BLACK);
		rec = new Recommender(board, player, 20, 4);
	}
	
	@After
	public void tearDown() {
		board = null;
		player = null;
		rec = null;
	}
	@Test
	public void shortesSinglePathTest1() {
		ArrayList<Route> routes = rec.shortestPath("Nashville", "Atlanta");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(1, cost);
	}
	@Test
	public void reverseShortesSinglePathTest1() {
		ArrayList<Route> routes = rec.shortestPath("Atlanta", "Nashville");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(1, cost);
	}
	
	@Test
	public void shortestDoublePathTest() {
		ArrayList<Route> routes = rec.shortestPath("Vancouver", "Portland");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(2, cost);
	}
	
	@Test
	public void reverseShortestDoublePathTest() {
		ArrayList<Route> routes = rec.shortestPath("Portland", "Vancouver");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(2, cost);
	}
	
	@Test
	public void shortDoublePathTest() {
		ArrayList<Route> routes = rec.shortestPath("Miami", "Atlanta");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(5, cost);
	}
	@Test
	public void reverseShortDoublePathTest() {
		ArrayList<Route> routes = rec.shortestPath("Atlanta", "Miami");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(5, cost);
	}
	
	@Test
	public void aLongPathTest1() {
		ArrayList<Route> routes = rec.shortestPath("Seattle", "Toronto");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(17, cost);
	}
	@Test
	public void reverseALongPathTest1() {
		ArrayList<Route> routes = rec.shortestPath("Toronto", "Seattle");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(17, cost);
	}
	@Test
	public void aLongPathTest2() {
		ArrayList<Route> routes = rec.shortestPath("San Francisco", "New York");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(21, cost);
		
	}
	
	@Test
	public void reverseALongPathTest2() {
		ArrayList<Route> routes = rec.shortestPath("New York", "San Francisco");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(21, cost);
		
	}
	@Test
	public void pathTest1() {
		ArrayList<Route> routes = rec.shortestPath("El Paso", "Duluth");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(10, cost);
	}
	@Test
	public void reversePathTest1() {
		ArrayList<Route> routes = rec.shortestPath("Duluth", "El Paso");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(10, cost);
	}
	@Test
	public void pathingAroundOtherPlayers() {
		board.claimRoute("Nashville", "Atlanta", Colors.route.ANY, Colors.player.GREEN);
		ArrayList<Route> routes = rec.shortestPath("Nashville", "Atlanta");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals("Pathing around other players", 5, cost);
		
	}
	@Test
	public void reversePathingAroundOtherPlayers() {
		board.claimRoute("Nashville", "Atlanta", Colors.route.ANY, Colors.player.GREEN);
		ArrayList<Route> routes = rec.shortestPath("Atlanta", "Nashville");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
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
		
		for(int i = 0; i < routes.size(); ++i) {
			for(int j = 0; j < routes.size(); ++j) {
				if (i == j)
					continue;
				assertFalse(routes.get(i).equals(routes.get(j)));
			}
		}
	}
	@Test
	public void ownerTest() {
		board.claimRoute("New Orleans", "Miami", Colors.route.RED, Colors.player.BLACK);
		ArrayList<Route> routes = rec.shortestPath("New Orleans", "Charleston");
		int cost = 0;
		for(Route r: routes) {
			cost += r.getCost();
		}
		assertEquals(10, cost);
	}
	@Test
	public void calculateTest() {
		ArrayList<DestinationTicket> dts = new ArrayList<DestinationTicket>();
		dts.add(new DestinationTicket("New York", "San Francisco"));
		rec.calculate(dts);
	}
}
