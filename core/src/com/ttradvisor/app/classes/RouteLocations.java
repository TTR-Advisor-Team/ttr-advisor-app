package com.ttradvisor.app.classes;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.ttradvisor.app.classes.CityLocations.CityLocation;

/**
 * Wrapper to interpret a correctly formatted JSON file of city locations
 * (pixel coordinates relative to the map image)
 */
public class RouteLocations {
	
	/**
	 * Objects for JSON
	 */
	public class RouteLocation {
		public String cityStart;
		public String cityEnd;
		public String color;
		public ArrayList<TrainLocation> trains;
		// public Boolean isClaimedRoute = false;
		
		public String toString() {
			return "Start" + cityStart;
		}
		
		RouteLocation(){
			this.trains = new ArrayList<TrainLocation>();
		}
	}
	
	/**
	 * Objects for JSON
	 */
	public class TrainLocation {
		public int x;
		public int y;
		public int r;
		
		public String toString() {
			return "Train: (" + x + ", " + y + ")";
		}
	}
	
	public JsonValue routeLocs;
	public ArrayList<RouteLocation> routesList;
	
	/**
	 * Expects a correctly formatted JSON file
	 */
	public RouteLocations(String filename) {
		
		JsonReader reader = new JsonReader();
		
		try {
			FileHandle handle = Gdx.files.internal(filename);
			routeLocs = reader.parse(handle);
//			System.out.println("routeLocs:  "+routeLocs);
			// Gdx.app.log("JSON", cityLocs.prettyPrint(JsonWriter.OutputType.json, 1));
			
			try {
				// test format
				setList();
//				list = getList();
			}
			catch (Exception e) {
				Gdx.app.error("Route Locations Parser 1", "Parsed JSON file has unexpected format.");
				Gdx.app.error("Route Locations Parser 1", e.getClass().toString() + " : " + e.getMessage());
				
//				initFallback();
			}
			
		}
		catch (Exception e) {
			Gdx.app.error("Route Locations Parser 2", "Could not read JsonValue from given filename.");
			Gdx.app.error("Route Locations Parser 2", e.getClass().toString() + " : " + e.getMessage());
			
//			initFallback();
		}
	}
	
//	public void initFallback() {
//		// initialize Vancouver as a fallback
//		JsonValue mockCity = new JsonValue(ValueType.object);
//		mockCity.addChild("name", new JsonValue("Vancouver"));
//		mockCity.addChild("x", new JsonValue(163));
//		mockCity.addChild("y", new JsonValue(151));
//		routeLocs = new JsonValue(ValueType.array);
//		routeLocs.addChild(mockCity);
//		// refresh the list to have only this, instead
//		getAllRouteLocations();
//	}

	public void setList() {
		routesList = new ArrayList<RouteLocation>();

		for (JsonValue val : routeLocs) {	
			for(JsonValue val2: val.get("ends")) {
				for(JsonValue val3: val2.get("routes")) {
					RouteLocation loc = new RouteLocation();
					
					loc.cityStart = val.getString("start");
					loc.cityEnd = val2.getString("end");
					loc.color = val3.getString("color");
//					if(val3.size == 3) {
//						loc.isClaimedRoute = val3.getBoolean("isClaimed");
//					}
//					loc.isClaimedRoute = val3.getBoolean("isClaimed");
					for(JsonValue val4: val3.get("trains")) {
						TrainLocation loc2 =  new TrainLocation();
						loc2.x = val4.getInt("x");
						loc2.y = val4.getInt("y");
						loc2.r = val4.getInt("r");
						loc.trains.add(loc2);
					}
					routesList.add(loc);
				}
			}
			
		}
	}
	
	public RouteLocation getRouteLocation(String start, String end, String color) {
		for (RouteLocation r: routesList) {
			if (r.cityStart.equalsIgnoreCase(start)) {
				if (r.cityEnd.equalsIgnoreCase(end)) {
					if(r.color.equalsIgnoreCase(color))
					return r;
				}
			}
		}
		return null;
		
	}
	
	public ArrayList<RouteLocation> getList() {
		return routesList;
	}

}