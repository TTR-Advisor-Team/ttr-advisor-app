package de.tomgrill.gdxtesting.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.ttradvisor.app.classes.Board;
import com.ttradvisor.app.classes.CityLocations;
import com.ttradvisor.app.classes.CityLocations.CityLocation;
import com.ttradvisor.app.classes.DestinationAction;
import com.ttradvisor.app.classes.Player;
import com.ttradvisor.app.classes.TrainCardAction;
import com.ttradvisor.app.classes.Turn;

import de.tomgrill.gdxtesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class TestCityLocations {
	
	private CityLocations testLocs;
	
	private void initTestLocations() {
		testLocs = new CityLocations("city_locations.json");
	}
	
	@Test
	public void testCityLocationsLoad() {
		initTestLocations();
		assertNotNull("CityLocations should load from JSON", testLocs);
	}
	
	@Test
	public void testCityLocationsLoadFallback() {
		testLocs = new CityLocations("doesnt_exist.json");
		
		assertTrue("File doesn't exist test", testLocs.getCityLocations().size() == 1);
	}
	
	@Test
	public void testCityLocationsLoadFallback2() {
		testLocs = new CityLocations("invalid.json");
		
		assertTrue("Invalid JSON test", testLocs.getCityLocations().size() == 1);
	}
	
	@Test
	public void testAllLocatedCitiesInBoardFile() {
		initTestLocations();
		Board b = new Board("cities.txt");
		// ensure all cities in cities.txt
		for (CityLocation loc : testLocs.getCityLocations()) {
			assertNotNull("Error, this city wasn't in cities.txt: " + loc.name, b.getCity(loc.name));
		}
	}
	
	@Test
	public void testAllBoardCitiesInLocationsFile() {
		initTestLocations();
		Board b = new Board("cities.txt");
		// ensure all cities in city_locations.json
		for (String city : b.getBoard().keySet()) {
			assertNotNull("Error, this city wasn't in city_locations.json: " + city, testLocs.getCityLocation(city));
		}
	}
	
	@Test
	public void testGetInvalidCity() {
		initTestLocations();
		assertEquals("Honolulu shouldn't be found in the map.", null, testLocs.getCityLocation("Honolulu"));
	}
	
	@Test
	public void testToString() {
		initTestLocations();
		CityLocation test = testLocs.new CityLocation();
		test.name = "Honolulu";
		test.x = -1;
		test.y = -1;
		assertEquals("Test CityLocations toString().", "City Honolulu: (-1, -1)", test.toString());
	}

}
