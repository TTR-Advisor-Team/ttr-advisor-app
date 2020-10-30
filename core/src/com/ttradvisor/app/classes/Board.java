package com.ttradvisor.app.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Board {
	private HashMap<String, LinkedList<Route>> board;

	public Board(String path) {
		board = new HashMap<String, LinkedList<Route>>();
		FileHandle handle = Gdx.files.internal(path);
		File initBoard = handle.file();
		Scanner reader;
		try {
			reader = new Scanner(initBoard);
			while (reader.hasNextLine()) {
				String[] data = reader.nextLine().split(",");
				LinkedList<Route> routes = new LinkedList<Route>();
				for (int i = 0; i < data.length / 4; ++i) {
					routes.add(new Route(data[0], data[1 + (i * 4)], Colors.route.valueOf(data[2 + (i * 4)]),
							Integer.parseInt(data[3 + (i * 4)])));
				}
				board.put(data[0], routes);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.print(e.getMessage());
			board = null;
		}

	}
	
	public HashMap<String, LinkedList<Route>> getBoard() {
		return board;
	}
	
	public String getCity(String city) {
		if (board.containsKey(city))
			return city;
		return null;
	}
	
	public void claimRoute(String cityBegin, String cityEnd, Colors.route color, Colors.player player) {
		LinkedList<Route> begin = board.get(cityBegin);
		for(Route r: begin) {
			if ((r.end.equals(cityEnd)) && (r.color.equals(color))) {
				r.owner = player;
				break;
			}
		}
		
		LinkedList<Route> end = board.get(cityEnd);
		for(Route r: end) {
			if ((r.end.equals(cityBegin)) && (r.color.equals(color))) {
				r.owner = player;
				break;
			}
		}
		
	}
	
	public HashMap<String, LinkedList<Route>> snapshotBoard () {	
		return null;
	}
	//returns a list of routes from a city to all neighboring cities
	public LinkedList<Route> getAllRoutes(String city) {
		return board.get(city);
	}
	
	//return a specific route from start city to end city
	public Route getRoute(String start, String end, Colors.route color, Colors.player owner) {
		LinkedList<Route> routes = board.get(start);
		for (Route r: routes) {
			if (r.end.equals(end) && r.color.equals(color) && r.owner.equals(owner))
				return r;
			continue;
		}
		return null;
	}
	public Route getRoute(String start, String end) {
		LinkedList<Route> routes = board.get(start);
		for (Route r: routes) {
			if (r.end.equals(end) && r.owner.equals(Colors.player.NONE))
				return r;
			continue;
		}
		return null;
	}
	public static class Route {
		String begin;
		String end;
		Colors.route color;
		Colors.player owner;
		int cost;

		public Route(String begin, String end, Colors.route color, int cost) {
			this.begin = begin;
			this.end = end;
			this.color = color;
			this.cost = cost;
			this.owner = Colors.player.NONE;
		}
		@Override
		public String toString() {
			return begin + " ====> " + end; 
		}
		public String getBegin() {
			return this.begin;
		}
		
		public String getEnd() {
			return this.end;
		}
		
		public Colors.route getColor() {
			return this.color;
		}
		
		public Colors.player getOwner() {
			return this.owner;
		}
		
		public int getCost() {
			return this.cost;
		}
	}
}