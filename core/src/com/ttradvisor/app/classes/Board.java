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
			if ((r.begin.compareTo(cityBegin) == 0) && (r.color==color)) {
				r.owner = player;
			}
		}
		
		LinkedList<Route> end = board.get(cityEnd);
		for(Route r: end) {
			if ((r.end.compareTo(cityEnd) == 0)  && (r.color==color)) {
				r.owner = player;
			}
		}
		
	}
	
	public HashMap<String, LinkedList<Route>> snapshotBoard () {	
		return null;
	}
	//returns a list of all routes from a city to all neighboring cities
	public LinkedList<Route> getRoutes(String city) {
		return board.get(city);
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
		
	}
}