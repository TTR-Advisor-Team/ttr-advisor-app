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

	/**
	 * Private constructor to only be used by snapShotBoard method
	 * 
	 * @param board
	 */
	private Board(HashMap<String, LinkedList<Route>> board) {
		this.board = board;
	}

	/**
	 * 
	 * @return The HashMap for the game TicketToRide
	 */
	public HashMap<String, LinkedList<Route>> getBoard() {
		return board;
	}

	/**
	 * Debugging method. Confirms a city key exists in the Board's HashMap.
	 * 
	 * @param city
	 * @return
	 */
	public String getCity(String city) {
		if (board.containsKey(city))
			return city;
		return null;
	}

	/**
	 * Claims a route for a particular player. Sets the route's owner field to that
	 * of player. This is a pair-wise operation, as the graph is undirected, so all
	 * routes are double. Can only claim route owned by Colors.player.NONE.
	 * 
	 * @param cityBegin
	 * @param cityEnd
	 * @param color
	 * @param player
	 */
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

	/**
	 * Makes a copy of the board object.
	 * 
	 * @return A deep copy of the current board.
	 */
	public Board snapshotBoard() {
		HashMap<String, LinkedList<Route>> copyMap = new HashMap<String, LinkedList<Route>>();
		Set<String> s = board.keySet();
		for (String city : s) {
			LinkedList<Route> routes = board.get(city);
			LinkedList<Route> copyRoutes = new LinkedList<Route>();
			for (Route r : routes) {
				copyRoutes.add(new Route(r.begin, r.end, r.color, r.owner, r.cost));
			}
			copyMap.put(city, copyRoutes);
		}
		Board copyBoard = new Board(copyMap);
		return copyBoard;
	}

	/**
	 * returns a list of routes from a city to all neighboring cities
	 * 
	 * @param city
	 * @return A linked list of all routes whose origin is the city
	 */
	public LinkedList<Route> getAllRoutes(String city) {
		return board.get(city);
	}

	/**
	 * return a specific route from start city to end city
	 * 
	 * @param start
	 * @param end
	 * @param color
	 * @param owner
	 * @return Returns a route that satisfies all parameters. Can return null.
	 */
	public Route getRoute(String start, String end, Colors.route color, Colors.player owner) {
		LinkedList<Route> routes = board.get(start);
		for (Route r : routes) {
			if (r.end.equals(end) && r.color.equals(color) && r.owner.equals(owner))
				return r;
		}
		return null;
	}

	/**
	 * Returns a single route from start city to end city whose owner is specified.
	 * If none are found, returns the first unowned route. If none found, returns
	 * null.
	 * 
	 * @param start
	 * @param end
	 * @param owner
	 * @return Route from start to end or null.
	 */
	public Route getRoute(String start, String end, Colors.player owner) {
		LinkedList<Route> routes = board.get(start);
		for (Route r : routes) {
			if (r.end.equals(end) && r.owner.equals(owner)) {
				return r;
			}
		}
		for (Route r : routes) {
			if (r.end.equals(end) && r.owner.equals(Colors.player.NONE))
				return r;
		}
		return null;
	}

	/**
	 * Returns the first route from start city to end city
	 * 
	 * @param start
	 * @param end
	 * @return a route from begin to end
	 */
	public Route getRoute(String start, String end) {
		LinkedList<Route> routes = board.get(start);
		for (Route r : routes) {
			if (r.end.equals(end))
				return r;
			continue;
		}
		return null;
	}

	/**
	 * Returns all routes from start city to adjacent end city that is owned by the
	 * player, or that is not owned by anyone
	 * 
	 * @param start
	 * @param end
	 * @param player
	 * @return
	 */
	public ArrayList<Route> getAllRoutesBetween(String start, String end, Colors.player player) {
		ArrayList<Route> routes = new ArrayList<Route>();
		LinkedList<Route> all = board.get(start);
		for (Route r : all) {
			if (r.getEnd().equals(end) && (r.getOwner().equals(player) || r.getOwner().equals(Colors.player.NONE))) {
				routes.add(r);
			}
		}
		return routes;
	}

	/**
	 * A convenience method for returning a single route between two neighboring
	 * cities.
	 * 
	 * @param start
	 * @param end
	 * @return Route from start to end. Can return null.
	 */
	public Route getRouteAnyOwner(String start, String end) {
		LinkedList<Route> routes = board.get(start);
		for (Route r : routes) {
			if (r.end.equals(end))
				return r;
		}
		return null;
	}

	public LinkedList<Route> getAllRoutesOfPlayer(Colors.player player) {
		LinkedList<Route> list = new LinkedList<Route>();
		Set<String> keys = board.keySet();
		for (String key : keys) {
			LinkedList<Route> routes = board.get(key);
			for (Route r : routes) {
				if (r.owner.equals(player))
					list.add(r);
			}
		}
		return list;
	}

	public int getNumberRoutes(String start, String end) {
		int count = 0;
		LinkedList<Route> routes = board.get(start);
		for (Route r : routes) {
			if (r.end.equals(end))
				count += 1;
		}
		if (count == 0) {
			Gdx.app.error("City Conections", "City start and end is not signle route.");
			return count;
		} else {
			return count;
		}
	}

	public LinkedList<Route> getAllRoutes(String start, String end) {
		LinkedList<Route> list = new LinkedList<Route>();
		LinkedList<Route> routes = board.get(start);
		for (Route r : routes) {
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
			if (route.getColor().equals(Colors.route.ANY) || card.getColor().equals(route.getColor()) || card.getColor().equals(Colors.route.ANY)) {	
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
	
	public ArrayList<Route> getAllRoutesBetweenOfPlayer(String start, String end, Colors.player player) {
		ArrayList<Route> routes = new ArrayList<Route>();
		LinkedList<Route> all = board.get(start);
		for (Route r : all) {
			if (r.getEnd().equals(end) && r.getOwner().equals(player)) {
				routes.add(r);
			}
		}
		return routes;
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