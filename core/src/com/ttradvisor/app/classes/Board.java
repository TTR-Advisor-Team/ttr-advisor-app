package com.ttradvisor.app.classes;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ttradvisor.app.classes.Board.Route;

public class Board {
	private HashMap<String, LinkedList<Route>> board;
	
	public Board(String path) {
		board = new HashMap<String, LinkedList<Route>>();
		try {
			FileHandle handle = Gdx.files.internal(path);
			String allCities = handle.readString();
			String[] cities = allCities.split(System.lineSeparator());
			for (String s : cities) {
				String[] data = s.split(",");
				LinkedList<Route> routes = new LinkedList<Route>();
				for (int i = 0; i < data.length / 4; ++i) {
					routes.add(new Route(data[0], data[1 + (i * 4)], Colors.route.valueOf(data[2 + (i * 4)]),
							Integer.parseInt(data[3 + (i * 4)])));
				}
				board.put(data[0], routes);
			}
		} catch (Exception e) {
			Gdx.app.error("Board Parser", e.getClass() + e.getMessage());
			board = null;
		}

	}
	private Board(HashMap<String, LinkedList<Route>> board) {
		this.board = board;
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
		for (Route r : begin) {
			if ((r.end.equals(cityEnd)) && (r.color.equals(color)) && (r.owner.equals(Colors.player.NONE))) {
				r.owner = player;
				break;
			}
		}

		LinkedList<Route> end = board.get(cityEnd);
		for (Route r : end) {
			if ((r.end.equals(cityBegin)) && (r.color.equals(color)) && (r.owner.equals(Colors.player.NONE))) {
				r.owner = player;
				break;
			}
		}

	}

	public Board snapshotBoard() {
		HashMap<String, LinkedList<Route>> copyMap = new HashMap<String, LinkedList<Route>>();
		Set<String> s = board.keySet();
		for(String city: s) {
			LinkedList<Route> routes = board.get(city);
			LinkedList<Route> copyRoutes = new LinkedList<Route>();
			for(Route r: routes) {
				copyRoutes.add(new Route(r.begin, r.end, r.color, r.owner, r.cost));
			}
			copyMap.put(city, copyRoutes);
		}
		Board copyBoard = new Board(copyMap);
		return copyBoard;
	}

	// returns a list of routes from a city to all neighboring cities
	public LinkedList<Route> getAllRoutes(String city) {
		return board.get(city);
	}

	// return a specific route from start city to end city
	public Route getRoute(String start, String end, Colors.route color, Colors.player owner) {
		LinkedList<Route> routes = board.get(start);
		for (Route r : routes) {
			if (r.end.equals(end) && r.color.equals(color) && r.owner.equals(owner))
				return r;
			continue;
		}
		return null;
	}
	/**
	 * Returns the first route from start city to end city
	 * @param start 
	 * @param end
	 * @return a route from begin to end
	 */
	public Route getRoute(String start, String end) {
		//needs to be redone, functionality checked
		LinkedList<Route> routes = board.get(start);
		for (Route r : routes) {
			if (r.end.equals(end) /*&& r.owner.equals(Colors.player.NONE)*/)
				return r;
			continue;
		}
		return null;
	}

	
	
	
	
	public Route getRouteAnyOwner(String start, String end) {
		LinkedList<Route> routes = board.get(start);
		for (Route r: routes) {
			if (r.end.equals(end))
				return r;
		}
		return null;
	}
	
	public LinkedList<Route> getAllRoutesOfPlayer(Colors.player player) {
		LinkedList<Route> list = new LinkedList<Route>();
		Set<String> keys = board.keySet();
		for (String key: keys) {
			LinkedList<Route> routes = board.get(key);
			for (Route r: routes) {
				if (r.owner.equals(player))
					list.add(r);
			}
		}
		return list;
	}
	
	public int getNumberRoutes(String start, String end) {
		int count = 0;
		LinkedList<Route> routes = board.get(start);
		for (Route r: routes) {
			if (r.end.equals(end))
				count += 1;
		}
		if (count == 0) {
			Gdx.app.error("City Conections", "City start and end is not signle route.");
			return count;
		}
		else {
			return count;
		}
	}
	
	public LinkedList<Route> getAllRoutes(String start, String end) {
		LinkedList<Route> list = new LinkedList<Route>();
		LinkedList<Route> routes = board.get(start);
		for (Route r: routes) {
			if (r.end.equals(end))
				list.add(r);
		}
		return list;
	}
	
	public int getIndexeNotOwned(LinkedList<Route> routes) {
		int index = 0;
		for (Route r : routes) {
			if (r.owner.equals(Colors.player.NONE)) {
				 return index;
			}
			index++;
		}
		return index;
	}
	public int getNumberNotOwned(LinkedList<Route> routes) {
		int count = 0;
		for (Route r : routes) {
			if (r.owner.equals(Colors.player.NONE)) {
				 count++;
			}
		}
		return count;
	}
	
	public int getCountSpendableCards(Route route, ArrayList<TrainCard> spent) {
		int count  = 0;
		for (TrainCard card: spent) {
			if (card.getColor().equals(route.getColor()) || route.getColor().equals(Colors.route.ANY)) {
				count++;
			}
		}
		return count;
	}
	
	public Boolean hasUnOwnedRoute(LinkedList<Route> routes) {
		for (Route r : routes) {
			if (r.owner.equals(Colors.player.NONE)) {
				 return true;
			}
		}
		return false;
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
		
		private Route(String begin, String end, Colors.route color, Colors.player owner, int cost) {
			this.begin = begin;
			this.end = end;
			this.color = color;
			this.cost = cost;
			this.owner = owner;
		}

		@Override
		public String toString() {
			return color + ": " + begin + " ====> " + end;
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