package de.tomgrill.gdxtesting.examples;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.Colors;

import de.tomgrill.gdxtesting.GdxTestRunner;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;

@RunWith(GdxTestRunner.class)
public class TestBoard {
	
	@Test
	public void boardNotNullTest() {
		Board b = new Board("cities.txt");
		assertNotNull(b);
	}
	@Test
	public void getBoardNotNullTest() {
		Board b = new Board("cities.txt");
		assertNotNull(b.getBoard());
	}
	@Test
	public void handleNotNullTest() {
		FileHandle handle = Gdx.files.internal("cities.txt");
		assertNotNull(handle);
	}

	@Test
	public void fileNotNullTest() {
		FileHandle handle = Gdx.files.internal("cities.txt");
		File file = handle.file();
		assertNotNull(file);
	}
	
	@Test
	public void notHereTest() {
		Board b = new Board("NotHere.txt");
		assertNull(b.getBoard());
	}
	
	@Test
	public void goodRouteTest() {
		Route r = new Route("a","b",Colors.route.ANY,2);
		assertNotNull(r);
	}
	
	@Test
	public void goodCityTest() {
		Board b = new Board("cities.txt");
		assertEquals("Pittsburg", b.getCity("Pittsburg"));
	}
	
	@Test
	public void badCityTest() {
		Board b = new Board("cities.txt");
		assertNull(b.getCity("Madison"));
	}
	
	@Test
	public void getAllRoutesTest() {
		Board b = new Board("cities.txt");
		LinkedList<Route> routes1 = new LinkedList<Route>();
		//Boston,Montreal,ANY,2,,Montreal,ANY,2,,New York,RED,2,,New York,YELLOW,2
		routes1.add(new Route("Boston", "Montreal", Colors.route.ANY, 2));
		routes1.add(new Route("Boston", "Montreal", Colors.route.ANY, 2));
		routes1.add(new Route("Boston", "New York", Colors.route.RED, 2));
		routes1.add(new Route("Boston", "New York", Colors.route.YELLOW, 2));
		
		LinkedList<Route> routes2 = b.getAllRoutes("Boston");
		while((routes1.peek() != null) && (routes2.peek() != null)) {
			Route r1 = routes1.removeFirst();
			Route r2 = routes2.removeFirst();
			
			assertEquals(r1.getBegin(), r2.getBegin());
			assertEquals(r1.getEnd(), r2.getEnd());
			assertEquals(r1.getColor(), r2.getColor());
			assertEquals(r1.getOwner(), r2.getOwner());
			assertEquals(r1.getCost(), r2.getCost());
		}
	}
	
	@Test
	public void getRouteTest() {
		Board b = new Board("cities.txt");
		//Boston,Montreal,ANY,2,,Montreal,ANY,2,,New York,RED,2,,New York,YELLOW,2
		Route r1 = new Route("Boston", "Montreal", Colors.route.ANY, 2);
		Route r2 = b.getRoute("Boston", "Montreal", Colors.route.ANY, Colors.player.NONE);
		assertEquals(r1.getBegin(), r2.getBegin());
		assertEquals(r1.getEnd(), r2.getEnd());
		assertEquals(r1.getColor(), r2.getColor());
		assertEquals(r1.getOwner(), r2.getOwner());
	}
	
	@Test
	public void getBadRouteTest() {
		Board b = new Board("cities.txt");
		//Boston,Montreal,ANY,2,,Montreal,ANY,2,,New York,RED,2,,New York,YELLOW,2
		Route r1 = b.getRoute("Boston", "Madison", Colors.route.ANY, Colors.player.NONE);
		assertNull(r1);
	}
	
	@Test
	public void claimRouteTest() {
		Board b = new Board("cities.txt");
		//Boston,Montreal,ANY,2,,Montreal,ANY,2,,New York,RED,2,,New York,YELLOW,2
		//Montreal,Boston,ANY,2,,Boston,ANY,2,,Sault St. Marie,BLACK,5,,Toronto,ANY,3
		b.claimRoute("Boston", "Montreal", Colors.route.ANY, Colors.player.BLUE);
		Route r1 = b.getRoute("Boston", "Montreal", Colors.route.ANY, Colors.player.BLUE);
		Route r2 = b.getRoute("Montreal", "Boston", Colors.route.ANY, Colors.player.BLUE);
		assertEquals(Colors.player.BLUE, r1.getOwner());
		assertEquals(Colors.player.BLUE, r2.getOwner());
	}
}
