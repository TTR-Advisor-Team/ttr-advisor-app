package com.ttradvisor.app.classes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;

/**
 * Wrapper to interpret a correctly formatted JSON file of city locations
 * (pixel coordinates relative to the map image)
 */
public class CityLocations {
	
	/**
	 * Objects for JSON
	 */
	public class CityLocation {
		public String name;
		public int x;
		public int y;
		
		public String toString() {
			return "City " + name + ": (" + x + ", " + y + ")";
		}
	}
	
	private JsonValue cityLocs;
	private ArrayList<CityLocation> parsedLocs;
	
	/**
	 * Expects a correctly formatted JSON file
	 */
	public CityLocations(String filename) {
		
		JsonReader reader = new JsonReader();
		
		try {
			FileHandle handle = Gdx.files.internal(filename);
			cityLocs = reader.parse(handle);
			// Gdx.app.log("JSON", cityLocs.prettyPrint(JsonWriter.OutputType.json, 1));
			
			try {
				// test format
				getAllCityLocations();
			}
			catch (Exception e) {
				Gdx.app.error("City Locations Parser", "Parsed JSON file has unexpected format.");
				Gdx.app.error("City Locations Parser", e.getClass().toString() + " : " + e.getMessage());
			}
			
		}
		catch (Exception e) {
			Gdx.app.error("City Locations Parser", "Could not read JsonValue from given filename.");
			Gdx.app.error("City Locations Parser", e.getClass().toString() + " : " + e.getMessage());
			
			// initialize Vancouver as a fallback
			JsonValue mockCity = new JsonValue(ValueType.object);
			mockCity.addChild("name", new JsonValue("Vancouver"));
			mockCity.addChild("x", new JsonValue(163));
			mockCity.addChild("y", new JsonValue(151));
			cityLocs = new JsonValue(ValueType.array);
			cityLocs.addChild(mockCity);
		}
	}
	
	/**
	 * Returns the pixel location of the given city
	 */
	public Vector2 getCityLocation(String cityName) {
		JsonValue cityInfo = cityLocs.get(cityName);
		if (cityInfo == null) {
			Gdx.app.error("City Locations Parser", "The city " + cityName + " does not exist in the locations list.");
			return null;
		}
		return new Vector2(cityInfo.getInt("x"), cityInfo.getInt("y"));
	}
	
	/**
	 * Doubles as a test for correct expected JSON format
	 * @return a full list of cities with locations
	 */
	private ArrayList<CityLocation> getAllCityLocations() {
		parsedLocs = new ArrayList<CityLocation>();
		for (JsonValue val : cityLocs) {
			CityLocation loc = new CityLocation();
			loc.name = val.name;
			loc.x = val.getInt("x");
			loc.y = val.getInt("y");
			parsedLocs.add(loc);
		}
		return parsedLocs;
	}
	
	/**
	 * @return a full list of cities with locations
	 */
	public ArrayList<CityLocation> getCityLocations() {
		return parsedLocs;
	}

}


