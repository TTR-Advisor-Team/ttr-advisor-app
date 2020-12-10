package de.tomgrill.gdxtesting.examples;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.CityLocations;
import com.ttradvisor.app.classes.Board.Route;
import com.ttradvisor.app.classes.RouteLocations;
import com.ttradvisor.app.classes.RouteLocations.RouteLocation;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class TestRouteLocations {
	
	private RouteLocations testLocs;
	
	private void initTestLocations() {
		testLocs = new RouteLocations("routes.json");
	}
	
	@Test
	public void testRouteLocationsLoad() {
		initTestLocations();
		assertNotNull("RouteLocations should load from JSON", testLocs);
	}
	
	@Test
	public void testCityLocationsLoadFails1() {
		testLocs = new RouteLocations("doesnt_exist.json");
		
		assertTrue("File doesn't exist test", testLocs.getList().size() == 0);
	}
	
	@Test
	public void testCityLocationsLoadFails2() {
		testLocs = new RouteLocations("invalid.json");
		
		assertTrue("Invalid JSON test", testLocs.getList().size() == 0);
	}
	
	
	@Test
	public void testAllLocatedRoutesInBoardFile() {
		initTestLocations();
		Board b = new Board("cities.txt");
		// ensure all cities in cities.txt
		for (RouteLocation loc : testLocs.getList()) {
			Route locRoute = b.getRoute(loc.cityStart, loc.cityEnd);
			assertNotNull("Error, this route wasn't in cities.txt: " + locRoute, locRoute);
		}
	}
	
	@Test
	public void testAllBoardRoutesInLocationsFile() {
		initTestLocations();
		Board b = new Board("cities.txt");
		// ensure all cities in city_locations.json
		for (String city : b.getBoard().keySet()) {
			for (Route route : b.getAllRoutes(city)) {
				assertNotNull("Error, this route wasn't in routes.json: " + route, testLocs.getRouteLocation(route.getBegin(), route.getEnd(), route.getColor().toString()));
			}
		}
	}
	
	@Test
	public void testGetInvalidCity() {
		initTestLocations();
		assertNull("There shouldn't be a route between Vancouver and Miami.", testLocs.getRouteLocation("Vancouver", "Miami", "ANY"));
	}

}
